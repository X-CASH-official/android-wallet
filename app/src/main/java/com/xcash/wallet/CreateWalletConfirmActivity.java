/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.wallet;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.xcash.base.BaseActivity;
import com.xcash.wallet.uihelp.ActivityHelp;

public class CreateWalletConfirmActivity extends NewBaseActivity {

    private String create_wallet_confirm_seed;

    private ImageView imageViewBack;
    private EditText editTextMnemonic;
    private Button buttonNext;

    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_wallet_confirm);
        Intent intent = getIntent();
        create_wallet_confirm_seed = intent.getStringExtra(ActivityHelp.CREATE_WALLET_CONFIRM_SEED_KEY);
        initAll();
    }

    @Override
    protected void initHandler() {

    }

    @Override
    protected void initUi() {
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        editTextMnemonic = (EditText) findViewById(R.id.editTextMnemonic);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        onClickListener();
    }

    @Override
    protected void initConfigUi() {

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
                    case R.id.imageViewBack:
                        doBack();
                        break;
                    case R.id.buttonNext:
                        confirm();
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        buttonNext.setOnClickListener(onClickListener);
    }

    private void confirm() {
        String mnemonic = editTextMnemonic.getText().toString();
        if (mnemonic.equals("")) {
            BaseActivity.showShortToast(CreateWalletConfirmActivity.this, getString(R.string.activity_create_wallet_confirm_confirmMnemonicEmpty_tips));
            return;
        }
        if (create_wallet_confirm_seed == null || !create_wallet_confirm_seed.equals(mnemonic)) {
            BaseActivity.showShortToast(CreateWalletConfirmActivity.this, getString(R.string.activity_create_wallet_confirm_confirmMnemonicUnEquals_tips));
            return;
        }
        if (TheApplication.getActivityFromActivityManager(MainActivity.class.getName()) == null) {
            Intent intent = new Intent(CreateWalletConfirmActivity.this,
                    MainActivity.class);
            startActivity(intent);
        } else {
            MainActivity.enterAndRefreshIfActivityExist();
        }
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

}
