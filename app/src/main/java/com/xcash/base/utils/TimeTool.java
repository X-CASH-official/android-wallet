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
package com.xcash.base.utils;

 import java.text.SimpleDateFormat;
 import java.util.Date;
 import java.util.TimeZone;

 public class TimeTool {

    private static long oldTime = -1;
    private static int count = 0;

    public static synchronized long getOnlyTime() {
        long time = System.currentTimeMillis();
        if (time == oldTime) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            time = System.currentTimeMillis();
        }
        oldTime = time;
        return time;
    }

    public static synchronized long getOnlyTimeWithoutSleep() {
        int ratio = 100000;
        if (count < ratio - 1) {
            count = count + 1;
        } else {
            count = 0;
            try {
                Thread.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        long time = System.currentTimeMillis();
        time = time * ratio + count;
        return time;
    }

    public static String getUtcTime() {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm");
        simpleDateFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        return simpleDateFormat.format(new Date());
    }


}
