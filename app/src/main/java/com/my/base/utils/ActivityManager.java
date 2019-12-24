/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.base.utils;


import com.my.base.BaseActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

public class ActivityManager {

    public static final String ACTIVITYKEYSEPARATOR = "&&";

    private static ActivityManager activityManager;

    private final HashMap<String, BaseActivity> hashMapBaseActivitys = new HashMap<String, BaseActivity>();

    private ActivityManager() {
    }

    public static synchronized ActivityManager getInstance() {
        if (activityManager == null) {
            activityManager = new ActivityManager();
        }
        return activityManager;
    }

    public void addBaseActivity(String activityKey, BaseActivity baseActivity) {
        if (activityKey == null || baseActivity == null) {
            return;
        }
        synchronized (this) {
            hashMapBaseActivitys.put(activityKey, baseActivity);
        }
    }

    public void removeBaseActivity(String activityKey) {
        if (activityKey == null) {
            return;
        }
        synchronized (this) {
            if (hashMapBaseActivitys.containsKey(activityKey)) {
                hashMapBaseActivitys.remove(activityKey);
            }
        }
    }

    public BaseActivity getBaseActivity(String activityKey) {
        if (activityKey == null) {
            return null;
        }
        synchronized (this) {
            if (hashMapBaseActivitys.containsKey(activityKey)) {
                return hashMapBaseActivitys.get(activityKey);
            } else {
                return null;
            }
        }
    }

    public ArrayList<BaseActivity> getAllBaseActivitys() {
        synchronized (this) {
            ArrayList<BaseActivity> arrayListBaseActivitys = new ArrayList<BaseActivity>();
            Iterator<Map.Entry<String, BaseActivity>> iterator = hashMapBaseActivitys.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, BaseActivity> entry = iterator.next();
                BaseActivity baseActivity = entry.getValue();
                arrayListBaseActivitys.add(baseActivity);
            }
            return arrayListBaseActivitys;
        }
    }

    public ArrayList<Object[]> getTheActivitysByClassName(String className) {
        if (className == null) {
            return null;
        }
        synchronized (this) {
            ArrayList<Object[]> arrayListBaseActivitys = new ArrayList<Object[]>();
            Iterator<Map.Entry<String, BaseActivity>> iterator = hashMapBaseActivitys.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, BaseActivity> entry = iterator.next();
                String key = entry.getKey();
                BaseActivity baseActivity = entry.getValue();
                String[] ss = key.split(ACTIVITYKEYSEPARATOR);
                if (className.equals(ss[0])) {
                    Object[] object = new Object[3];
                    object[0] = key;
                    object[1] = baseActivity;
                    if (ss.length >= 2) {
                        object[2] = ss[1];
                    } else {
                        object[2] = "-1";
                    }
                    arrayListBaseActivitys.add(object);
                }
            }
            return arrayListBaseActivitys;
        }
    }

    public void killAllActivity() {
        synchronized (this) {
            Iterator<Map.Entry<String, BaseActivity>> iterator = hashMapBaseActivitys.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, BaseActivity> entry = iterator.next();
                BaseActivity baseActivity = entry.getValue();
                baseActivity.finish();
            }
        }
    }

    public void killAllActivityWithOutMe(String activityKey) {
        if (activityKey == null) {
            return;
        }
        synchronized (this) {
            Iterator<Map.Entry<String, BaseActivity>> iterator = hashMapBaseActivitys.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, BaseActivity> entry = iterator.next();
                String key = entry.getKey();
                BaseActivity baseActivity = entry.getValue();
                if (!activityKey.equals(key)) {
                    baseActivity.finish();
                }
            }
        }
    }

    public void killAllActivityWithOutWe(String[] activityKeys) {
        if (activityKeys == null) {
            return;
        }
        synchronized (this) {
            Iterator<Map.Entry<String, BaseActivity>> iterator = hashMapBaseActivitys.entrySet().iterator();
            while (iterator.hasNext()) {
                Map.Entry<String, BaseActivity> entry = iterator.next();
                String key = entry.getKey();
                BaseActivity baseActivity = entry.getValue();
                boolean notFinish = false;
                for (int i = 0; i < activityKeys.length; i++) {
                    if (activityKeys[i] != null) {
                        if (activityKeys[i].equals(key)) {
                            notFinish = true;
                            break;
                        }
                    }
                }
                if (!notFinish) {
                    baseActivity.finish();
                }
            }
        }
    }

}
