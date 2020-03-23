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
package com.xcash.utils;

import android.annotation.TargetApi;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.xcash.wallet.R;


public class NotificationHelper extends ContextWrapper {

    private String channel_id = "channel_id";
    private String channel_name = "channel_name";

    private NotificationManager notificationManager;

    public NotificationHelper(Context context, String channel_id, String channel_name) {
        super(context);
        this.channel_id = channel_id;
        this.channel_name = channel_name;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            createNotificationChannel();
        }
    }

    @TargetApi(Build.VERSION_CODES.O)
    private void createNotificationChannel() {
        NotificationChannel channel = new NotificationChannel(channel_id, channel_name, NotificationManager.IMPORTANCE_DEFAULT);
        channel.canBypassDnd();
        channel.enableLights(true);
        channel.setLockscreenVisibility(NotificationCompat.VISIBILITY_SECRET);
        channel.setLightColor(Color.RED);
        channel.canShowBadge();
        channel.enableVibration(true);
        channel.getAudioAttributes();
        channel.getGroup();
        channel.setBypassDnd(true);
        channel.setVibrationPattern(new long[]{100, 100, 200});
        channel.shouldShowLights();
        getNotificationManager().createNotificationChannel(channel);
    }

    private NotificationManager getNotificationManager() {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
        }
        return notificationManager;
    }

    private NotificationCompat.Builder getNotification(String title, String content, PendingIntent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id);
        ;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setAutoCancel(true);
        if (intent != null) {
            builder.setContentIntent(intent);
        }
        return builder;
    }

    private NotificationCompat.Builder getNotificationProgress(String title, String content,
                                                               int progress, PendingIntent intent) {
        NotificationCompat.Builder builder = new NotificationCompat.Builder(getApplicationContext(), channel_id);
        ;
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) {
            builder.setPriority(NotificationCompat.PRIORITY_DEFAULT);
        }
        builder.setContentTitle(title);
        builder.setContentText(content);
        builder.setSmallIcon(R.mipmap.ic_launcher);
        builder.setLargeIcon(BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher));
        if (progress > 0 && progress < 100) {
            builder.setProgress(100, progress, false);
        } else {
            builder.setProgress(0, 0, false);
            builder.setContentText("Download Success");
        }
        builder.setAutoCancel(true);
        builder.setWhen(System.currentTimeMillis());
        if (intent != null) {
            builder.setContentIntent(intent);
        }
        return builder;
    }

    public void sendNotification(int notifyId, String title, String content, PendingIntent intent) {
        NotificationCompat.Builder builder = getNotification(title, content, intent);
        getNotificationManager().notify(notifyId, builder.build());
    }

    public void sendNotificationProgress(int notifyId, String title, String content, int progress, PendingIntent intent) {
        NotificationCompat.Builder builder = getNotificationProgress(title, content, progress, intent);
        getNotificationManager().notify(notifyId, builder.build());
    }

}
