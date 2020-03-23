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

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.xcash.wallet.R;

import java.util.ArrayList;
import java.util.List;

public class PermissionHelper {

    public static final int REQUEST_PERMISSION_REQUEST = 12305;
    public static final int REQUEST_OPEN_APPLICATION_SETTINGS_CODE = 12306;

    private String[] permissions;
    private List<String> permissionList = new ArrayList<>();
    private Activity activity;
    private AlertDialog permissionDialog;
    private OnPermissionListener onPermissionListener;

    public PermissionHelper(Activity activity, String[] permissions) {
        this.activity = activity;
        this.permissions = permissions;
    }

    public void checkPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            permissionList.clear();
            for (int i = 0; i < permissions.length; i++) {
                if (ContextCompat.checkSelfPermission(activity, permissions[i]) != PackageManager.PERMISSION_GRANTED) {
                    permissionList.add(permissions[i]);
                }
            }
            if (permissionList.size() > 0) {
                ActivityCompat.requestPermissions(activity, permissions, REQUEST_PERMISSION_REQUEST);
            } else {
                if (onPermissionListener != null) {
                    onPermissionListener.onSuccess();
                }
            }
        } else {
            if (onPermissionListener != null) {
                onPermissionListener.onSuccess();
            }
        }
    }

    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        boolean hasPermissionDismiss = false;
        if (REQUEST_PERMISSION_REQUEST == requestCode) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    hasPermissionDismiss = true;
                }
            }
            if (hasPermissionDismiss) {
                showPermissionDialog();
            } else {
                if (onPermissionListener != null) {
                    onPermissionListener.onSuccess();
                }
            }
        }
    }

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (onPermissionListener != null) {
            onPermissionListener.onActivityResult(requestCode, resultCode, data);
        }
        switch (requestCode) {
            case REQUEST_OPEN_APPLICATION_SETTINGS_CODE:
                checkPermission();
                break;
            default:
                break;
        }
    }

    private void showPermissionDialog() {
        if (permissionDialog == null) {
            permissionDialog = new AlertDialog.Builder(activity)
                    .setMessage(R.string.permission_setting_tips)
                    .setPositiveButton(R.string.permission_setting, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            permissionDialog.cancel();
                            try {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("package:" + activity.getPackageName()));
                                intent.addCategory(Intent.CATEGORY_DEFAULT);
                                activity.startActivityForResult(intent, REQUEST_OPEN_APPLICATION_SETTINGS_CODE);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    })
                    .setNegativeButton(R.string.permission_cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            permissionDialog.cancel();
                            activity.finish();
                        }
                    })
                    .create();
        }
        permissionDialog.show();
    }

    public String[] getPermissions() {
        return permissions;
    }

    public void setPermissions(String[] permissions) {
        this.permissions = permissions;
    }

    public OnPermissionListener getOnPermissionListener() {
        return onPermissionListener;
    }

    public void setOnPermissionListener(OnPermissionListener onPermissionListener) {
        this.onPermissionListener = onPermissionListener;
    }

    public interface OnPermissionListener {

        void onSuccess();

        void onActivityResult(int requestCode, int resultCode, Intent data);
    }

}
