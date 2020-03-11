/**
 * Copyright (c) 2019 by snakeway
 *
 * All rights reserved.
 */
package com.xcash.utils


import kotlinx.coroutines.*

class CoroutineHelper {

    private val job = Job()
    private val uiScope = CoroutineScope(Dispatchers.Main + job)

    fun <T> launch(onCoroutineListener: OnCoroutineListener<T>) {
        uiScope.launch {
            val result = withContext(Dispatchers.IO) {
                onCoroutineListener.runOnIo()
            }
            onCoroutineListener.overRunOnMain(result)
        }
    }

    fun onDestroy() {
        job.cancel()
    }

    interface OnCoroutineListener<T> {

        fun runOnIo(): T

        fun overRunOnMain(t: T)

    }

}
