package com.song.fastmq.broker.storage.concurrent;

import com.song.fastmq.broker.storage.LedgerEntryWrapper;
import java.util.List;

/**
 * @author song
 */
public class AsyncCallbacks {

    public interface ReadEntryCallback {
        void readEntryComplete(List<LedgerEntryWrapper> entries);

        void readEntryFailed(Throwable throwable);
    }

    public interface CloseLedgerCursorCallback {
        void onComplete();

        void onThrowable(Throwable throwable);
    }
}