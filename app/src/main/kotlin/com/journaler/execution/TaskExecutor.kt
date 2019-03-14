package com.journaler.execution

import java.sql.Time
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.ThreadPoolExecutor
import java.util.concurrent.TimeUnit

class TaskExecutor private constructor(
corePoolSize: Int,
maximumPoolSize: Int,
worqQueue: BlockingQueue<Runnable>?
):ThreadPoolExecutor(
    corePoolSize,
    maximumPoolSize,
    0L,
    TimeUnit.MILLISECONDS,
    worqQueue
){
    companion object {
        fun getInstance(capacity: Int): TaskExecutor{
            return TaskExecutor(
                capacity,
                capacity * 2,
                LinkedBlockingQueue<Runnable>()
            )
        }
    }
}