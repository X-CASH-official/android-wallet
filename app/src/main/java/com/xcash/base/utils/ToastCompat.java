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

import android.content.Context;
import android.content.res.Resources;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.xcash.base.BaseActivity;

import java.lang.reflect.Field;
import java.lang.reflect.Modifier;


/**
 * Fix android 7.1.1 bug
 */
public class ToastCompat extends Toast {

    public ToastCompat(Context context) {
        super(context);
    }

    public static Toast makeText(Context context, CharSequence text, int duration) {
        ToastCompat toastCompat = new ToastCompat(context);

        LayoutInflater layoutInflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        Resources resources = context.getResources();
        View view = layoutInflater.inflate(resources.getIdentifier("transient_notification", "layout", "android"), null);
        TextView textView = (TextView) view.findViewById(resources.getIdentifier("message", "id", "android"));
        textView.setText(text);
        toastCompat.setView(view);
        toastCompat.setDuration(duration);
        return toastCompat;
    }

    public static Toast makeText(Context context, int resId, int duration)
            throws Resources.NotFoundException {
        return makeText(context, context.getResources().getText(resId), duration);
    }

    @Override
    public void show() {
        if (checkIfNeedToHack()) {
            tryToHack();
        }
        super.show();
    }

    protected boolean checkIfNeedToHack() {
        return Build.VERSION.SDK_INT == Build.VERSION_CODES.N_MR1;
    }

    private void tryToHack() {
        try {
            Object mTN = getFieldValue(this, "mTN");
            if (mTN != null) {
                boolean isSuccess = false;

                Object rawShowRunnable = getFieldValue(mTN, "mShow");
                if (rawShowRunnable != null && rawShowRunnable instanceof Runnable) {
                    isSuccess = setFieldValue(mTN, "mShow", new InternalRunnable((Runnable) rawShowRunnable));
                }
                if (!isSuccess) {
                    Object rawHandler = getFieldValue(mTN, "mHandler");
                    if (rawHandler != null && rawHandler instanceof Handler) {
                        isSuccess = setFieldValue(rawHandler, "mCallback", new InternalHandlerCallback((Handler) rawHandler));
                    }
                }
                if (!isSuccess) {
                    BaseActivity.showSystemErrorLog("TryToHack error.");
                }
            }
        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private static boolean setFieldValue(Object object, String fieldName, Object newFieldValue) {
        Field field = getDeclaredField(object, fieldName);
        if (field != null) {
            try {
                int accessFlags = field.getModifiers();
                if (Modifier.isFinal(accessFlags)) {
                    Field modifiersField = Field.class.getDeclaredField("accessFlags");
                    modifiersField.setAccessible(true);
                    modifiersField.setInt(field, field.getModifiers() & ~Modifier.FINAL);
                }
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                field.set(object, newFieldValue);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    private static Object getFieldValue(Object object, final String fieldName) {
        Field field = getDeclaredField(object, fieldName);
        return getFieldValue(object, field);
    }

    private static Object getFieldValue(Object obj, Field field) {
        if (field != null) {
            try {
                if (!field.isAccessible()) {
                    field.setAccessible(true);
                }
                return field.get(obj);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private static Field getDeclaredField(final Object object, final String fieldName) {
        for (Class<?> superClass = object.getClass(); superClass != Object.class; superClass = superClass.getSuperclass()) {
            try {
                Field field = superClass.getDeclaredField(fieldName);
                return field;
            } catch (NoSuchFieldException e) {
                continue;
            }
        }
        return null;
    }

    private class InternalRunnable implements Runnable {

        private final Runnable runnable;

        public InternalRunnable(Runnable mRunnable) {
            this.runnable = mRunnable;
        }

        @Override
        public void run() {
            try {
                this.runnable.run();
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
    }

    private class InternalHandlerCallback implements Handler.Callback {

        private final Handler handler;

        public InternalHandlerCallback(Handler handler) {
            this.handler = handler;
        }

        @Override
        public boolean handleMessage(Message msg) {
            try {
                handler.handleMessage(msg);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            return true;
        }
    }

}