/**
 * Copyright (c) 2017-2018 m2049r
 * <p>
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */

package com.my.monero.util;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TimeZone;
import java.util.concurrent.TimeUnit;

public class RestoreHeight {

    private static RestoreHeight restoreHeight;

    private Map<String, Long> blockHeight = new HashMap<>();

    public static synchronized RestoreHeight getInstance() {
        if (restoreHeight == null) {
            restoreHeight = new RestoreHeight();
        }
        return restoreHeight;
    }

    public RestoreHeight() {
        blockHeight.put("2018-08-01", 0L);
        blockHeight.put("2018-09-01", 49000L);
        blockHeight.put("2018-10-01", 89900L);
        blockHeight.put("2018-11-01", 127000L);
        blockHeight.put("2018-12-01", 170000L);
        blockHeight.put("2019-01-01", 215000L);
        blockHeight.put("2019-02-01", 257000L);
        blockHeight.put("2019-03-01", 289000L);
        blockHeight.put("2019-04-01", 311000L);
        blockHeight.put("2019-05-01", 333000L);
        blockHeight.put("2019-06-01", 355000L);
        blockHeight.put("2019-07-01", 377000L);
        blockHeight.put("2019-08-01", 399000L);
        blockHeight.put("2019-09-01", 421000L);
        blockHeight.put("2019-10-01", 443660L);
        blockHeight.put("2019-11-01", 465560L);
        blockHeight.put("2019-12-01", 487600L);
    }

    public long getHeight(String date) {
        SimpleDateFormat parser = new SimpleDateFormat("yyyy-MM-dd");
        parser.setTimeZone(TimeZone.getTimeZone("UTC"));
        parser.setLenient(false);
        try {
            return getHeight(parser.parse(date));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0;
    }

    public long getHeight(final Date date) {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.set(Calendar.DST_OFFSET, 0);
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, -4);
        if (calendar.get(Calendar.YEAR) < 2018)
            return 0;
        Calendar query = (Calendar) calendar.clone();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        formatter.setTimeZone(TimeZone.getTimeZone("UTC"));
        String queryDate = formatter.format(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        long prevTime = calendar.getTimeInMillis();
        String prevDate = formatter.format(prevTime);
        Long prevBc = blockHeight.get(prevDate);
        if (prevBc == null) {
            while (prevBc == null) {
                calendar.add(Calendar.MONTH, -1);
                if (calendar.get(Calendar.YEAR) < 2018) {
                    throw new IllegalStateException("endless loop looking for blockheight");
                }
                prevTime = calendar.getTimeInMillis();
                prevDate = formatter.format(prevTime);
                prevBc = blockHeight.get(prevDate);
            }
        }
        long height = prevBc;
        if (queryDate.equals(prevDate)) return height;
        calendar.add(Calendar.MONTH, 1);
        long nextTime = calendar.getTimeInMillis();
        String nextDate = formatter.format(nextTime);
        Long nextBc = blockHeight.get(nextDate);
        if (nextBc != null) {
            long diff = nextBc - prevBc;
            long diffDays = TimeUnit.DAYS.convert(nextTime - prevTime, TimeUnit.MILLISECONDS);
            long days = TimeUnit.DAYS.convert(query.getTimeInMillis() - prevTime,
                    TimeUnit.MILLISECONDS);
            height = Math.round(prevBc + diff * (1.0 * days / diffDays));
        } else {
            long days = TimeUnit.DAYS.convert(query.getTimeInMillis() - prevTime,
                    TimeUnit.MILLISECONDS);
            height = Math.round(prevBc + 1.0 * days * (24 * 60 / 2));
        }
        return height;
    }

}
