package com.song.fastmq.broker.storage.impl;

import com.song.fastmq.broker.storage.AsyncCallback;
import com.song.fastmq.broker.storage.LedgerStorageException;
import com.song.fastmq.broker.storage.Version;
import com.song.fastmq.broker.storage.config.BookKeeperConfig;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.bookkeeper.client.BookKeeper;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by song on 下午10:02.
 */
public class DefaultLedgerManagerTest {

    private DefaultLedgerManager ledgerManager;

    @Before
    public void setUp() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        ZooKeeper zookeeper = new ZooKeeper("127.0.0.1:2181", 10000, event -> {
            if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                System.out.println("Zookeeper connected.");
            } else {
                throw new RuntimeException("Error connecting to zookeeper");
            }
            latch.countDown();
        });
        latch.await();
        CountDownLatch initLatch = new CountDownLatch(1);
        ledgerManager = new DefaultLedgerManager("JustATest", new BookKeeperConfig(), new BookKeeper("127.0.0.1:2181"), new DefaultLedgerStreamStorage(zookeeper));
        ledgerManager.init(new AsyncCallback<Void, LedgerStorageException>() {
            @Override public void onCompleted(Void result, Version version) {
                System.out.println("Done");
                initLatch.countDown();
            }

            @Override public void onThrowable(LedgerStorageException throwable) {
                throwable.printStackTrace();
                initLatch.countDown();
            }
        });
        initLatch.await();
    }

    @Test
    public void getName() throws Exception {
        Assert.assertEquals("JustATest", ledgerManager.getName());
    }

    @Test(timeout = 3000)
    public void addEntry() throws Exception {
        int count = 100;
        AtomicInteger atomicInteger = new AtomicInteger();
        final CountDownLatch downLatch = new CountDownLatch(100);
        for (int i = 0; i < count; i++) {
            ledgerManager.asyncAddEntry("Hello World".getBytes(), new AsyncCallback<Void, LedgerStorageException>() {
                @Override public void onCompleted(Void result, Version version) {
                    downLatch.countDown();
                    atomicInteger.incrementAndGet();
                }

                @Override public void onThrowable(LedgerStorageException throwable) {
                    throwable.printStackTrace();
                }
            });
        }
        downLatch.await();
        Assert.assertEquals(100, atomicInteger.get());
    }

    @Test
    public void asyncAddEntry() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }
}