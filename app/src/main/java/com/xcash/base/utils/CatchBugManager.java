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
 */you may not use this file except in compliance with the License.
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
package com.xcash.base.utils;

import android.app.Application;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.Thread.UncaughtExceptionHandler;

public class CatchBugManager implements UncaughtExceptionHandler {

    private static CatchBugManager catchBugManager;
    private final Application application;
    private final UncaughtExceptionHandler defaultUncaughtExceptionHandler;

    private CatchBugManager(Application application) {
        this.application = application;
        defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(this);
    }

    public static synchronized CatchBugManager getInstance(Application application) {
        if (catchBugManager == null) {
            catchBugManager = new CatchBugManager(application);
        }
        return catchBugManager;
    }

    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        if (throwable == null) {
            writeBugThenDoDefault("throwable is null", thread, throwable);
            return;
        }
        String bugContent = null;
        StringWriter stringWriter = null;
        PrintWriter printWriter = null;
        try {
            stringWriter = new StringWriter();
            printWriter = new PrintWriter(stringWriter);
            throwable.printStackTrace(printWriter);
            Throwable throwableCause = throwable.getCause();
            while (throwableCause != null) {
                throwableCause.printStackTrace(printWriter);
                throwableCause = throwableCause.getCause();
            }
            bugContent = stringWriter.toString();
        } finally {
            try {
                if (printWriter != null) {
                    printWriter.close();
                }
                if (stringWriter != null) {
                    stringWriter.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        writeBugThenDoDefault(bugContent, thread, throwable);
    }

    public Application getApplication() {
        return application;
    }

    public UncaughtExceptionHandler getDefaultUncaughtExceptionHandler() {
        return defaultUncaughtExceptionHandler;
    }

    private void writeBugThenDoDefault(String bugContent, Thread thread, Throwable throwable) {
        String bug = "My device:" + android.os.Build.MODEL + "in android version:" + android.os.Build.VERSION.RELEASE + "get a bug:\n" + bugContent;
        FileTool.needShowAndWriteLogToSdcard(true, FileTool.getDefaultRootPath() + File.separator + "CrashLog.txt", bug, null, 1);
        defaultUncaughtExceptionHandler.uncaughtException(thread, throwable);
    }

}
