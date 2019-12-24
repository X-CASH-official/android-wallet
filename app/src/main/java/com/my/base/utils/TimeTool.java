/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.base.utils;

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

}
