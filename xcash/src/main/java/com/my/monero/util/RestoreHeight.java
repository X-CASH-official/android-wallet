/*
 * Copyright (c) 2019-2020 The X-Cash Foundation
 * Copyright (c) 2017 m2049r
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
        blockHeight.put("2020-01-01", 509640L);
        blockHeight.put("2020-02-01", 532380L);
        blockHeight.put("2020-03-01", 553000L);
        blockHeight.put("2020-04-01", 575000L);
        blockHeight.put("2020-05-01", 597000L);
        blockHeight.put("2020-06-01", 619000L);
        blockHeight.put("2020-07-01", 641000L);
        blockHeight.put("2020-08-01", 663000L);
        blockHeight.put("2020-09-01", 685000L);
        blockHeight.put("2020-10-01", 707200L);
        blockHeight.put("2020-11-01", 728500L);
        blockHeight.put("2020-12-01", 750000L);
        blockHeight.put("2021-01-01", 773000L);
        blockHeight.put("2021-02-01", 795000L);
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
        if (calendar.get(Calendar.YEAR) > 2021||(calendar.get(Calendar.YEAR)==2021&&calendar.get(Calendar.MONTH)>=1))
            return 795000;
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
                    return 0;
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
