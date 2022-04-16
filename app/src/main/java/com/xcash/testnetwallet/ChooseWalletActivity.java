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
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.RelativeLayout;

import com.xcash.testnetwallet.uihelp.ActivityHelp;

public class ChooseWalletActivity extends NewBaseActivity {

    private RelativeLayout relativeLayoutContent;
    private Button buttonCreateWallet;
    private Button buttonImportWallet;

    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window statusBar = getWindow();
            statusBar.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_choose_wallet);
        setWindowType(1);
        initAll();
    }

    @Override
    protected void initHandler() {

    }

    @Override
    protected void initUi() {
        relativeLayoutContent = (RelativeLayout) findViewById(R.id.relativeLayoutContent);
        buttonCreateWallet = (Button) findViewById(R.id.buttonCreateWallet);
        buttonImportWallet = (Button) findViewById(R.id.buttonImportWallet);
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        adaptationStatusBar(relativeLayoutContent);
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {

    }

    private void onClickListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.buttonCreateWallet:
                        Intent intent1 = new Intent(ChooseWalletActivity.this,
                                SetWalletActivity.class);
                        intent1.putExtra(ActivityHelp.CHOOSE_WALLET_TYPE_KEY, ActivityHelp.CHOOSE_WALLET_TYPE_CREATE);
                        startActivity(intent1);
                        break;
                    case R.id.buttonImportWallet:
                        Intent intent2 = new Intent(ChooseWalletActivity.this,
                                SetWalletActivity.class);
                        intent2.putExtra(ActivityHelp.CHOOSE_WALLET_TYPE_KEY, ActivityHelp.CHOOSE_WALLET_TYPE_IMPORT);
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }
        };
        buttonCreateWallet.setOnClickListener(onClickListener);
        buttonImportWallet.setOnClickListener(onClickListener);
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

}
