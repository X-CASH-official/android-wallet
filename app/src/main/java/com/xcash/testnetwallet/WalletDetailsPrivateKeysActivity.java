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


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.xcash.models.local.KeyValueItem;
import com.xcash.utils.ClipboardTool;
import com.xcash.utils.database.entity.Wallet;
import com.xcash.testnetwallet.aidl.OnWalletDataListener;
import com.xcash.testnetwallet.aidl.WalletOperateManager;
import com.xcash.testnetwallet.uihelp.ActivityHelp;
import com.xcash.testnetwallet.uihelp.ProgressDialogHelp;

import java.util.ArrayList;
import java.util.List;

public class WalletDetailsPrivateKeysActivity extends NewBaseActivity {

    private Wallet wallet;
    private String set_wallet_password;

    private ImageView imageViewBack;
    private TextView textViewWordTips;
    private LinearLayout linearLayoutPrivateKeys;

    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_details_private_keys);
        Intent intent = getIntent();
        wallet = (Wallet) intent.getSerializableExtra(ActivityHelp.WALLET_KEY);
        set_wallet_password = intent.getStringExtra(ActivityHelp.SET_WALLET_PASSWORD_KEY);
        initAll();
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    @Override
    protected void initUi() {
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewWordTips = (TextView) findViewById(R.id.textViewWordTips);
        linearLayoutPrivateKeys = (LinearLayout) findViewById(R.id.linearLayoutPrivateKeys);
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewWordTips.setText(getString(R.string.activity_wallet_details_private_keys_waitingLoadingWalletTips_text));
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {
        recoverPrivateKeys();
    }

    private void onClickListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageViewBack:
                        doBack();
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
    }

    private void recoverPrivateKeys() {
        if (wallet == null || set_wallet_password == null) {
            return;
        }
        WalletOperateManager walletOperateManager = TheApplication.getTheApplication().getWalletServiceHelper().getWalletOperateManager();
        if (walletOperateManager == null) {
            return;
        }
        Object[] objects = ProgressDialogHelp.unEnabledView(WalletDetailsPrivateKeysActivity.this, null);
        final ProgressDialog progressDialog = (ProgressDialog) objects[0];
        final String progressDialogKey = (String) objects[1];
        try {
            walletOperateManager.getWalletData(wallet.getId(), wallet.getName(), set_wallet_password, new OnWalletDataListener.Stub() {
                @Override
                public void onSuccess(final com.xcash.testnetwallet.aidl.Wallet wallet) throws RemoteException {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textViewWordTips.setText(getString(R.string.activity_wallet_details_private_keys_writeDownTips_text));
                            List<KeyValueItem> keyValueItems = new ArrayList<>();
                            keyValueItems.add(new KeyValueItem(getString(R.string.activity_wallet_details_private_keys_addressKey_tips), wallet.getAddress()));
                            keyValueItems.add(new KeyValueItem(getString(R.string.activity_wallet_details_private_keys_privateViewKey_tips), wallet.getSecretViewKey()));
                            keyValueItems.add(new KeyValueItem(getString(R.string.activity_wallet_details_private_keys_privateSpendKey_tips), wallet.getSecretSpendKey()));
                            keyValueItems.add(new KeyValueItem(getString(R.string.activity_wallet_details_private_keys_publicViewKey_tips), wallet.getPublicViewKey()));
                            keyValueItems.add(new KeyValueItem(getString(R.string.activity_wallet_details_private_keys_publicSpendKey_tips), wallet.getPublicSpendKey()));
                            showPrivateKeys(linearLayoutPrivateKeys, keyValueItems);
                            ProgressDialogHelp.enabledView(WalletDetailsPrivateKeysActivity.this, progressDialog, progressDialogKey, null);
                        }
                    });
                }

                @Override
                public void onError(final String error) throws RemoteException {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (error != null && error.contains(ActivityHelp.SPECIFIED_FILE_EXISTS)) {
                                textViewWordTips.setText(getString(R.string.activity_create_wallet_walletExist_tips));
                            } else {
                                if (error == null) {
                                    textViewWordTips.setText(getString(R.string.get_wallet_data_error_tips));
                                } else {
                                    textViewWordTips.setText(error);
                                }
                            }
                            textViewWordTips.setTextColor(ContextCompat.getColor(WalletDetailsPrivateKeysActivity.this, R.color.textView_error));
                            ProgressDialogHelp.enabledView(WalletDetailsPrivateKeysActivity.this, progressDialog, progressDialogKey, null);
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            ProgressDialogHelp.enabledView(WalletDetailsPrivateKeysActivity.this, progressDialog, progressDialogKey, null);
        }
    }

    private void showPrivateKeys(LinearLayout linearLayout, List<KeyValueItem> keyValueItems) {
        if (keyValueItems == null) {
            return;
        }
        linearLayout.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(WalletDetailsPrivateKeysActivity.this);
        for (int i = 0; i < keyValueItems.size(); i++) {
            KeyValueItem keyValueItem = keyValueItems.get(i);
            View view = layoutInflater.inflate(R.layout.activity_wallet_details_private_keys_item, null);
            RelativeLayout relativeLayoutContent = (RelativeLayout) view.findViewById(R.id.relativeLayoutContent);
            TextView textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
            TextView textViewContent = (TextView) view.findViewById(R.id.textViewContent);

            final String value = (String) keyValueItem.getValue();
            textViewTitle.setText(keyValueItem.getKey());
            textViewContent.setText(value);
            relativeLayoutContent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ClipboardTool.copyToClipboard(WalletDetailsPrivateKeysActivity.this, value);
                }
            });
            linearLayout.addView(view);
        }
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

}
