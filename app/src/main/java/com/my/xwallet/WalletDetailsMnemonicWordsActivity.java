/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.xwallet;


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

import com.my.base.BaseActivity;
import com.my.utils.ClipboardTool;
import com.my.utils.database.entity.Wallet;
import com.my.xwallet.aidl.OnWalletDataListener;
import com.my.xwallet.aidl.WalletOperateManager;
import com.my.xwallet.uihelp.ActivityHelp;
import com.my.xwallet.uihelp.ProgressDialogHelp;


public class WalletDetailsMnemonicWordsActivity extends NewBaseActivity {

    private Wallet wallet;
    private String set_wallet_password;

    private int view_normal_margin_default;
    private int view_normal_margin_narrow;
    private int mnemonicWordViewWidth;

    private ImageView imageViewBack;
    private ImageView imageViewCopy;
    private TextView textViewWordTips;
    private LinearLayout linearLayoutWord;

    private View.OnClickListener onClickListener;
    private String seed;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_details_mnemonic_words);
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
        view_normal_margin_default = getResources().getDimensionPixelSize(R.dimen.view_normal_margin_default);
        view_normal_margin_narrow = getResources().getDimensionPixelSize(R.dimen.view_normal_margin_narrow);
        mnemonicWordViewWidth = (BaseActivity.getScreenWidth(WalletDetailsMnemonicWordsActivity.this) - 2 * view_normal_margin_default - 6 * view_normal_margin_narrow) / 3;
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        imageViewCopy = (ImageView) findViewById(R.id.imageViewCopy);
        textViewWordTips = (TextView) findViewById(R.id.textViewWordTips);
        linearLayoutWord = (LinearLayout) findViewById(R.id.linearLayoutWord);
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewWordTips.setText(getString(R.string.activity_wallet_details_mnemonic_words_waitingLoadingWalletTips_text));
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {
        recoverMnemonicWords();
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
                        ClipboardTool.copyToClipboard(WalletDetailsMnemonicWordsActivity.this, seed);
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        imageViewCopy.setOnClickListener(onClickListener);
    }

    private void recoverMnemonicWords() {
        if (wallet == null || set_wallet_password == null) {
            return;
        }
        WalletOperateManager walletOperateManager = TheApplication.getTheApplication().getWalletServiceHelper().getWalletOperateManager();
        if (walletOperateManager == null) {
            return;
        }
        Object[] objects = ProgressDialogHelp.unEnabledView(WalletDetailsMnemonicWordsActivity.this, null);
        final ProgressDialog progressDialog = (ProgressDialog) objects[0];
        final String progressDialogKey = (String) objects[1];
        try {
            walletOperateManager.getWalletData(wallet.getId(), wallet.getName(), set_wallet_password, new OnWalletDataListener.Stub() {
                @Override
                public void onSuccess(final com.my.xwallet.aidl.Wallet wallet) throws RemoteException {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            seed = wallet.getSeed();
                            if (seed == null || seed.equals("")) {
                                textViewWordTips.setText(getString(R.string.activity_wallet_details_mnemonic_words_mnemonicEmpty_tips));
                                textViewWordTips.setTextColor(ContextCompat.getColor(WalletDetailsMnemonicWordsActivity.this, R.color.textView_error));
                            } else {
                                textViewWordTips.setText(getString(R.string.activity_wallet_details_mnemonic_words_writeDownTips_text));
                                showMnemonicWord(linearLayoutWord, seed);
                            }
                            ProgressDialogHelp.enabledView(WalletDetailsMnemonicWordsActivity.this, progressDialog, progressDialogKey, null);
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
                            textViewWordTips.setTextColor(ContextCompat.getColor(WalletDetailsMnemonicWordsActivity.this, R.color.textView_error));
                            ProgressDialogHelp.enabledView(WalletDetailsMnemonicWordsActivity.this, progressDialog, progressDialogKey, null);
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            ProgressDialogHelp.enabledView(WalletDetailsMnemonicWordsActivity.this, progressDialog, progressDialogKey, null);
        }
    }

    private void showMnemonicWord(LinearLayout linearLayout, String seed) {
        if (seed == null || seed.equals("")) {
            return;
        }
        String[] words = seed.split(" ");
        int wordsLength = words.length;
        linearLayout.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(WalletDetailsMnemonicWordsActivity.this);
        LinearLayout linearLayoutHorizontal = null;
        for (int i = 0; i < wordsLength; i++) {
            if (linearLayoutHorizontal == null) {
                linearLayoutHorizontal = new LinearLayout(WalletDetailsMnemonicWordsActivity.this);
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
