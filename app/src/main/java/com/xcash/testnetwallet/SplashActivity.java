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
package com.xcash.testnetwallet;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;

import com.xcash.utils.CoroutineHelper;
import com.xcash.utils.database.AppDatabase;
import com.xcash.utils.database.entity.Wallet;
import com.xcash.testnetwallet.aidl.manager.XManager;

public class SplashActivity extends NewBaseActivity {

//    private String[] permissions = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};
//    private PermissionHelper permissionHelper = new PermissionHelper(SplashActivity.this, permissions);
    private CoroutineHelper coroutineHelper = new CoroutineHelper();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window statusBar = getWindow();
            statusBar.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);//Need above M,If LOLLIPOP some models will have a black screen, such as Coolpad
        }
        if (!isTaskRoot()) {
            finish();
        } else {
            setContentView(R.layout.activity_splash);
            setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            setWindowType(1);
            TheApplication.getTheApplication().getWalletServiceHelper().bindService();
            initAll();
        }
    }

    @Override
    protected void initHandler() {

    }

    @Override
    protected void initUi() {

    }

    @Override
    protected void initConfigUi() {
//        permissionHelper.setOnPermissionListener(new PermissionHelper.OnPermissionListener() {
//            @Override
//            public void onSuccess() {
//                goToJump();
//            }
//
//            @Override
//            public void onActivityResult(int requestCode, int resultCode, Intent data) {
//
//            }
//        });
//        permissionHelper.checkPermission();
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {
        goToJump();
    }

    private void goToJump() {
        coroutineHelper.launch(new CoroutineHelper.OnCoroutineListener<Wallet>() {

            @Override
            public Wallet runOnIo() {
                try {
                    Thread.sleep(1000);
                    Wallet wallet = AppDatabase.getInstance().walletDao().loadActiveWalletBySymbol(XManager.SYMBOL);
                    return wallet;
                } catch (Exception e) {
                    e.printStackTrace();
                    return null;
                }
            }

            @Override
            public void overRunOnMain(Wallet wallet) {
                if (wallet == null) {
                    Intent intent = new Intent(SplashActivity.this,
                            ChooseWalletActivity.class);
                    startActivity(intent);
                } else {
                    Intent intent = new Intent(SplashActivity.this,
                            MainActivity.class);
                    startActivity(intent);
                }
                finish();
            }
        });
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        permissionHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
//    }

//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        permissionHelper.onActivityResult(requestCode, resultCode, data);
//    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        coroutineHelper.onDestroy();
    }

}
