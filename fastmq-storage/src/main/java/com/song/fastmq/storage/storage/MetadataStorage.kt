package com.song.fastmq.storage.storage

import com.song.fastmq.storage.storage.metadata.Log
import io.reactivex.Observable

/**
 * Store the metadata of log
 *
 * Created by song on 2017/11/5.
 */
interface MetadataStorage {

    /**
     * Get ledgerStream by name
     *
     * @param name name of the topic
     * @return the log with given name
     */
    fun getLogInfo(name: String): Observable<Log>

    fun updateLogInfo(name: String, log: Log): Observable<Void>

    fun removeLogInfo(name: String): Observable<Void>
}
