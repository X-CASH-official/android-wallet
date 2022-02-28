 /*
 * Copyright (c) 2019-2020 The X-Cash Foundation
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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
