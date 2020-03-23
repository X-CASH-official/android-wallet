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
package com.xcash.wallet;


import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.xcash.base.BaseActivity;
import com.xcash.utils.ClipboardTool;
import com.xcash.wallet.aidl.OnWalletDataListener;
import com.xcash.wallet.aidl.WalletOperateManager;
import com.xcash.wallet.uihelp.ActivityHelp;
import com.xcash.wallet.uihelp.ProgressDialogHelp;

public class CreateWalletActivity extends NewBaseActivity {

    private String set_wallet_name;
    private String set_wallet_password;
    private String set_wallet_description;

    private int view_normal_margin_default;
    private int view_normal_margin_narrow;
    private int mnemonicWordViewWidth;
    private ImageView imageViewBack;
    private ImageView imageViewCopy;
    private TextView textViewWordTips;
    private LinearLayout linearLayoutWord;
    private Button buttonNext;

    private View.OnClickListener onClickListener;
    private String seed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet);
        Intent intent = getIntent();
        set_wallet_name = intent.getStringExtra(ActivityHelp.SET_WALLET_NAME_KEY);
        set_wallet_password = intent.getStringExtra(ActivityHelp.SET_WALLET_PASSWORD_KEY);
        set_wallet_description = intent.getStringExtra(ActivityHelp.SET_WALLET_DESCRIPTION_KEY);
        initAll();
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    @Override
    protected void initUi() {
        view_normal_margin_default = getResources().getDimensionPixelSize(R.dimen.view_normal_margin_default);
        view_normal_margin_narrow = getResources().getDimensionPixelSize(R.dimen.view_normal_margin_narrow);

        mnemonicWordViewWidth = (BaseActivity.getScreenWidth(CreateWalletActivity.this) - 2 * view_normal_margin_default - 6 * view_normal_margin_narrow) / 3;
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        imageViewCopy = (ImageView) findViewById(R.id.imageViewCopy);
        textViewWordTips = (TextView) findViewById(R.id.textViewWordTips);
        linearLayoutWord = (LinearLayout) findViewById(R.id.linearLayoutWord);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewWordTips.setText(getString(R.string.activity_create_wallet_waitingCreateWalletTips_text));
        buttonNext.setVisibility(View.GONE);
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {
        createWallet();
    }

    private void onClickListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageViewBack:
                        doBack();
                        break;
                    case R.id.imageViewCopy:
                        ClipboardTool.copyToClipboard(CreateWalletActivity.this, seed);
                        break;
                    case R.id.buttonNext:
                        if (seed != null) {
                            Intent intent = new Intent(CreateWalletActivity.this,
                                    CreateWalletConfirmActivity.class);
                            intent.putExtra(ActivityHelp.CREATE_WALLET_CONFIRM_SEED_KEY, seed);
                            startActivity(intent);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        imageViewCopy.setOnClickListener(onClickListener);
        buttonNext.setOnClickListener(onClickListener);
    }

    private void createWallet() {
        if (set_wallet_name == null || set_wallet_password == null) {
            return;
        }
        WalletOperateManager walletOperateManager = TheApplication.getTheApplication().getWalletServiceHelper().getWalletOperateManager();
        if (walletOperateManager == null) {
            return;
        }
        Object[] objects = ProgressDialogHelp.unEnabledView(CreateWalletActivity.this, null);
        final ProgressDialog progressDialog = (ProgressDialog) objects[0];
        final String progressDialogKey = (String) objects[1];
        try {
            walletOperateManager.createWallet(set_wallet_name, set_wallet_password, set_wallet_description, new OnWalletDataListener.Stub() {
                @Override
                public void onSuccess(final com.xcash.wallet.aidl.Wallet wallet) throws RemoteException {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            seed = wallet.getSeed();
                            textViewWordTips.setText(getString(R.string.activity_create_wallet_writeDownTips_text));
                            buttonNext.setVisibility(View.VISIBLE);
                            showMnemonicWord(linearLayoutWord, seed);
                            WalletManagerActivity.doRefreshIfActivityExist();
                            MainActivity.doRefreshIfActivityExist();
                            ProgressDialogHelp.enabledView(CreateWalletActivity.this, progressDialog, progressDialogKey, null);
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
                                    textViewWordTips.setText(getString(R.string.activity_create_wallet_createWalletError_tips));
                                } else {
                                    textViewWordTips.setText(error);
                                }
                            }
                            textViewWordTips.setTextColor(ContextCompat.getColor(CreateWalletActivity.this, R.color.textView_error));
                            ProgressDialogHelp.enabledView(CreateWalletActivity.this, progressDialog, progressDialogKey, null);
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            ProgressDialogHelp.enabledView(CreateWalletActivity.this, progressDialog, progressDialogKey, null);
        }
    }

    private void showMnemonicWord(LinearLayout linearLayout, String seed) {
        if (seed == null || seed.equals("")) {
            return;
        }
        String[] words = seed.split(" ");
        int wordsLength = words.length;
        linearLayout.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(CreateWalletActivity.this);
        LinearLayout linearLayoutHorizontal = null;
        for (int i = 0; i < wordsLength; i++) {
            if (linearLayoutHorizontal == null) {
                linearLayoutHorizontal = new LinearLayout(CreateWalletActivity.this);
                linearLayoutHorizontal.setOrientation(LinearLayout.HORIZONTAL);
            }

            String word = words[i];
            View view = layoutInflater.inflate(R.layout.activity_create_wallet_mnemonic_word_item, null);

            RelativeLayout relativeLayoutWord = (RelativeLayout) view.findViewById(R.id.relativeLayoutWord);
            TextView textViewSerialNumber = (TextView) view.findViewById(R.id.textViewSerialNumber);
            TextView textViewContent = (TextView) view.findViewById(R.id.textViewContent);

            TheApplication.setLayoutParams(relativeLayoutWord, mnemonicWordViewWidth, -1);
            TheApplication.setLayoutMargins(relativeLayoutWord, view_normal_margin_narrow, view_normal_margin_narrow, view_normal_margin_narrow, view_normal_margin_narrow);

            textViewSerialNumber.setText(String.valueOf(i + 1));
            textViewContent.setText(word);

            linearLayoutHorizontal.addView(view);
            if ((i + 1) % 3 == 0 || i == wordsLength - 1) {
                linearLayout.addView(linearLayoutHorizontal);
                linearLayoutHorizontal = null;
            }
        }
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

}
