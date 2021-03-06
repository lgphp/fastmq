package com.song.fastmq.storage.storage.concurrent

import com.song.fastmq.common.concurrent.SafeRunnable
import com.song.fastmq.common.concurrent.SimpleThreadFactory
import com.song.fastmq.common.utils.Utils
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

/**
 * Created by song on 2017/11/5.
 */
object CommonPool {

    private val COMMON_POOL = ThreadPoolExecutor(
            Utils.AVAILABLE_PROCESSORS, Utils.AVAILABLE_PROCESSORS * 4,
            120L, TimeUnit.SECONDS, LinkedBlockingQueue(),
            SimpleThreadFactory("common-pool"))

    fun executeBlocking(runnable: Runnable) = COMMON_POOL.execute(object : SafeRunnable() {

        override fun safeRun() {
            runnable.run()
        }
    })

}
