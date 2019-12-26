/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.xwallet;


import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.my.base.BaseActivity;
import com.my.utils.AppSignTool;
import com.my.xwallet.uihelp.ActivityHelp;
import com.my.xwallet.uihelp.ColorHelp;

public class SetWalletActivity extends NewBaseActivity {

    private String choose_wallet_type;

    private int colorPrimary;
    private int mainColorText;

    private ImageView imageViewBack;
    private TextView textViewTitle;
    private ImageView imageViewName;
    private EditText editTextName;
    private FrameLayout frameLayoutName;
    private ImageView imageViewPassword;
    private EditText editTextPassword;
    private FrameLayout frameLayoutPassword;
    private ImageView imageViewConfirmPassword;
    private EditText editTextConfirmPassword;
    private FrameLayout frameLayoutConfirmPassword;
    private ImageView imageViewDescription;
    private EditText editTextDescription;
    private FrameLayout frameLayoutDescription;
    private TextView textViewSha256Tips;
    private CheckBox checkBox;
    private Button buttonNext;

    private View.OnFocusChangeListener onFocusChangeListener;
    private View.OnClickListener onClickListener;
    private Drawable drawableName;
    private Drawable drawablePassword;
    private Drawable drawableConfirmPassword;
    private Drawable drawableDescription;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_wallet);
        Intent intent = getIntent();
        choose_wallet_type = intent.getStringExtra(ActivityHelp.CHOOSE_WALLET_TYPE_KEY);
        initAll();
    }

    @SuppressWarnings("ResourceType")
    private void getTypeArray() {
        TypedArray typedArray = getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimary, R.attr.mainColorText});
        try {
            colorPrimary = typedArray.getColor(0, 0xffffffff);
            mainColorText = typedArray.getColor(1, 0xffffffff);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void initHandler() {

    }

    @Override
    protected void initUi() {
        getTypeArray();
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        imageViewName = (ImageView) findViewById(R.id.imageViewName);
        editTextName = (EditText) findViewById(R.id.editTextName);
        frameLayoutName = (FrameLayout) findViewById(R.id.frameLayoutName);
        imageViewPassword = (ImageView) findViewById(R.id.imageViewPassword);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        frameLayoutPassword = (FrameLayout) findViewById(R.id.frameLayoutPassword);
        imageViewConfirmPassword = (ImageView) findViewById(R.id.imageViewConfirmPassword);
        editTextConfirmPassword = (EditText) findViewById(R.id.editTextConfirmPassword);
        frameLayoutConfirmPassword = (FrameLayout) findViewById(R.id.frameLayoutConfirmPassword);
        imageViewDescription = (ImageView) findViewById(R.id.imageViewDescription);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        frameLayoutDescription = (FrameLayout) findViewById(R.id.frameLayoutDescription);
        textViewSha256Tips = (TextView) findViewById(R.id.textViewSha256Tips);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        onFocusChangeListener();
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        drawableName = getResources().getDrawable(R.mipmap.activity_set_wallet_name);
        drawablePassword = getResources().getDrawable(R.mipmap.activity_set_wallet_password);
        drawableConfirmPassword = getResources().getDrawable(R.mipmap.activity_set_wallet_password);
        drawableDescription = getResources().getDrawable(R.mipmap.activity_set_wallet_description);

        ColorHelp.setImageViewDrawableTint(imageViewName, drawableName, mainColorText);
        ColorHelp.setImageViewDrawableTint(imageViewPassword, drawablePassword, mainColorText);
        ColorHelp.setImageViewDrawableTint(imageViewConfirmPassword, drawableConfirmPassword, mainColorText);
        ColorHelp.setImageViewDrawableTint(imageViewDescription, drawableDescription, mainColorText);

        if (choose_wallet_type != null && choose_wallet_type.equals(ActivityHelp.CHOOSE_WALLET_TYPE_IMPORT)) {
            textViewTitle.setText(R.string.activity_import_wallet_textViewTitle_text);
        } else {
            textViewTitle.setText(R.string.activity_create_wallet_textViewTitle_text);
        }
        textViewSha256Tips.setText(getString(R.string.activity_set_wallet_SHA256_tips) + AppSignTool.getSingInfo(SetWalletActivity.this, getPackageName(), AppSignTool.SHA256));
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {

    }

    private void onFocusChangeListener() {
        onFocusChangeListener = new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                switch (view.getId()) {
                    case R.id.editTextName:
                        if (hasFocus) {
                            ColorHelp.setImageViewDrawableTint(imageViewName, drawableName, colorPrimary);
                            frameLayoutName.setBackgroundColor(colorPrimary);
                        } else {
                            ColorHelp.setImageViewDrawableTint(imageViewName, drawableName, mainColorText);
                            frameLayoutName.setBackgroundColor(mainColorText);
                        }
                        break;
                    case R.id.editTextPassword:
                        if (hasFocus) {
                            ColorHelp.setImageViewDrawableTint(imageViewPassword, drawablePassword, colorPrimary);
                            frameLayoutPassword.setBackgroundColor(colorPrimary);
                        } else {
                            ColorHelp.setImageViewDrawableTint(imageViewPassword, drawablePassword, mainColorText);
                            frameLayoutPassword.setBackgroundColor(mainColorText);
                        }
                        break;
                    case R.id.editTextConfirmPassword:
                        if (hasFocus) {
                            ColorHelp.setImageViewDrawableTint(imageViewConfirmPassword, drawableConfirmPassword, colorPrimary);
                            frameLayoutConfirmPassword.setBackgroundColor(colorPrimary);
                        } else {
                            ColorHelp.setImageViewDrawableTint(imageViewConfirmPassword, drawableConfirmPassword, mainColorText);
                            frameLayoutConfirmPassword.setBackgroundColor(mainColorText);
                        }
                        break;
                    case R.id.editTextDescription:
                        if (hasFocus) {
                            ColorHelp.setImageViewDrawableTint(imageViewDescription, drawableDescription, colorPrimary);
                            frameLayoutDescription.setBackgroundColor(colorPrimary);
                        } else {
                            ColorHelp.setImageViewDrawableTint(imageViewDescription, drawableDescription, mainColorText);
                            frameLayoutDescription.setBackgroundColor(mainColorText);
                        }
                        break;
                    default:
                        break;
                }

            }
        };
        editTextName.setOnFocusChangeListener(onFocusChangeListener);
        editTextPassword.setOnFocusChangeListener(onFocusChangeListener);
        editTextConfirmPassword.setOnFocusChangeListener(onFocusChangeListener);
        editTextDescription.setOnFocusChangeListener(onFocusChangeListener);
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
                        doNext();
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        buttonNext.setOnClickListener(onClickListener);
    }

    private void doNext() {
        String name = editTextName.getText().toString();
        String password = editTextPassword.getText().toString();
        String confirmPassword = editTextConfirmPassword.getText().toString();
        String description = editTextDescription.getText().toString();
        if (name.equals("") || password.equals("") || confirmPassword.equals("")) {
            BaseActivity.showShortToast(SetWalletActivity.this, getString(R.string.activity_set_wallet_confirmEmpty_tips));
            return;
        }
        if (!password.equals(confirmPassword)) {
            BaseActivity.showShortToast(SetWalletActivity.this, getString(R.string.activity_set_wallet_passwordConfirm_tips));
            return;
        }
        if (!checkBox.isChecked()) {
            BaseActivity.showShortToast(SetWalletActivity.this, getString(R.string.activity_set_wallet_unCheck_tips));
            return;
        }

        if (choose_wallet_type != null && choose_wallet_type.equals(ActivityHelp.CHOOSE_WALLET_TYPE_IMPORT)) {
            Intent intent = new Intent(SetWalletActivity.this,
                    ImportWalletActivity.class);
            intent.putExtra(ActivityHelp.SET_WALLET_NAME_KEY, name);
            intent.putExtra(ActivityHelp.SET_WALLET_PASSWORD_KEY, password);
            intent.putExtra(ActivityHelp.SET_WALLET_DESCRIPTION_KEY, description);
            startActivity(intent);
        } else {
            Intent intent = new Intent(SetWalletActivity.this,
                    CreateWalletActivity.class);
            intent.putExtra(ActivityHelp.SET_WALLET_NAME_KEY, name);
            intent.putExtra(ActivityHelp.SET_WALLET_PASSWORD_KEY, password);
            intent.putExtra(ActivityHelp.SET_WALLET_DESCRIPTION_KEY, description);
            startActivity(intent);
        }
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

}
