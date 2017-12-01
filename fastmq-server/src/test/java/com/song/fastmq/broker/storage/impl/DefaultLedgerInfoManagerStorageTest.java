package com.song.fastmq.broker.storage.impl;

import com.jayway.jsonassert.JsonAssert;
import com.song.fastmq.broker.storage.LedgerInfo;
import com.song.fastmq.broker.storage.LedgerInfoManager;
import com.song.fastmq.broker.storage.LedgerManagerStorage;
import com.song.fastmq.broker.storage.LedgerStorageException;
import com.song.fastmq.broker.storage.Version;
import com.song.fastmq.broker.storage.concurrent.AsyncCallback;
import com.song.fastmq.common.utils.JsonUtils;
import java.util.Collections;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import org.apache.logging.log4j.core.config.Configurator;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * Created by song on 2017/11/5.
 */
public class DefaultLedgerInfoManagerStorageTest {

    private ZooKeeper zookeeper;

    private LedgerManagerStorage ledgerManagerStorage;

    @Before
    public void setUp() throws Exception {
        Configurator.initialize("CocaRMQBenchmark", Thread.currentThread().getContextClassLoader(), "log4j2.xml");
        CountDownLatch latch = new CountDownLatch(1);
        zookeeper = new ZooKeeper("127.0.0.1:2181", 10000, event -> {
            if (event.getState() == Watcher.Event.KeeperState.SyncConnected) {
                System.out.println("Zookeeper connected.");
            } else {
                throw new RuntimeException("Error connecting to zookeeper");
            }
            latch.countDown();
        });
        latch.await();
        ledgerManagerStorage = new LedgerManagerStorageImpl(zookeeper);
    }

    @Test
    public void getLedgerStream() throws Exception {
        String ledgerName = "HelloWorldTest1";
        LedgerInfoManager ledgerInfoManager = ledgerManagerStorage.getLedgerManager(ledgerName);
        String json = JsonUtils.get().writeValueAsString(ledgerInfoManager);
        JsonAssert.with(json).assertEquals("$.name", ledgerName);
    }

    @Test
    public void asyncUpdateLedgerStream() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger counter = new AtomicInteger();
        String ledgerName = "HelloWorldTest1";
        ledgerManagerStorage.asyncGetLedgerManager(ledgerName, new AsyncCallback<LedgerInfoManager, LedgerStorageException>() {

            @Override public void onCompleted(LedgerInfoManager result, Version version) {
                result.setLedgers(Collections.singletonList(new LedgerInfo()));
                ledgerManagerStorage.asyncUpdateLedgerManager(ledgerName, result, version, new AsyncCallback<Void, LedgerStorageException>() {

                    @Override public void onCompleted(Void result, Version version) {
                        counter.incrementAndGet();
                        latch.countDown();
                    }

                    @Override public void onThrowable(LedgerStorageException throwable) {
                        throwable.printStackTrace();
                        latch.countDown();
                    }
                });
            }

            @Override public void onThrowable(LedgerStorageException throwable) {
                throwable.printStackTrace();
                latch.countDown();
            }
        });
        latch.await();
        Assert.assertEquals(1, counter.get());
    }

    @Test
    public void asyncRemoveLedger() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        AtomicInteger counter = new AtomicInteger();
        ledgerManagerStorage.asyncRemoveLedger("HelloWorldTest1", new AsyncCallback<Void, LedgerStorageException>() {
            @Override public void onCompleted(Void result, Version version) {
                counter.incrementAndGet();
                latch.countDown();
            }

            @Override public void onThrowable(LedgerStorageException throwable) {
                throwable.printStackTrace();
                latch.countDown();
            }
        });
        latch.await();
        Assert.assertEquals(1, counter.get());
    }

    @After
    public void tearDown() throws Exception {
        zookeeper.close();
    }
}