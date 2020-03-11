/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.wallet;


import android.app.Activity;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.xcash.base.BaseActivity;
import com.xcash.wallet.aidl.manager.XManager;
import com.xcash.wallet.uihelp.ActivityHelp;

public class NodeAddActivity extends NewBaseActivity {

    private int colorPrimary;
    private int mainColorText;

    private ImageView imageViewBack;
    private TextView textViewTitle;
    private EditText editTextNodeIp;
    private FrameLayout frameLayoutNodeIp;
    private EditText editTextNodePort;
    private FrameLayout frameLayoutNodePort;
    private EditText editTextUsername;
    private FrameLayout frameLayoutUsername;
    private EditText editTextPassword;
    private FrameLayout frameLayoutPassword;
    private Button buttonNext;

    private View.OnFocusChangeListener onFocusChangeListener;
    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_add);
        initAll();
    }

    @SuppressWarnings("ResourceType")
    private void getTypeArrayColor() {
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
        getTypeArrayColor();
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        editTextNodeIp = (EditText) findViewById(R.id.editTextNodeIp);
        frameLayoutNodeIp = (FrameLayout) findViewById(R.id.frameLayoutNodeIp);
        editTextNodePort = (EditText) findViewById(R.id.editTextNodePort);
        frameLayoutNodePort = (FrameLayout) findViewById(R.id.frameLayoutNodePort);
        editTextUsername = (EditText) findViewById(R.id.editTextUsername);
        frameLayoutUsername = (FrameLayout) findViewById(R.id.frameLayoutUsername);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        frameLayoutPassword = (FrameLayout) findViewById(R.id.frameLayoutPassword);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        onFocusChangeListener();
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewTitle.setText(R.string.activity_node_add_textViewTitle_text);
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
                    case R.id.editTextNodeIp:
                        if (hasFocus) {
                            frameLayoutNodeIp.setBackgroundColor(colorPrimary);
                        } else {
                            frameLayoutNodeIp.setBackgroundColor(mainColorText);
                        }
                        break;
                    case R.id.editTextNodePort:
                        if (hasFocus) {
                            frameLayoutNodePort.setBackgroundColor(colorPrimary);
                        } else {
                            frameLayoutNodePort.setBackgroundColor(mainColorText);
                        }
                        break;
                    case R.id.editTextUsername:
                        if (hasFocus) {
                            frameLayoutUsername.setBackgroundColor(colorPrimary);
                        } else {
                            frameLayoutUsername.setBackgroundColor(mainColorText);
                        }
                        break;
                    case R.id.editTextPassword:
                        if (hasFocus) {
                            frameLayoutPassword.setBackgroundColor(colorPrimary);
                        } else {
                            frameLayoutPassword.setBackgroundColor(mainColorText);
                        }
                        break;
                    default:
                        break;
                }

            }
        };
        editTextNodeIp.setOnFocusChangeListener(onFocusChangeListener);
        editTextNodePort.setOnFocusChangeListener(onFocusChangeListener);
        editTextUsername.setOnFocusChangeListener(onFocusChangeListener);
        editTextPassword.setOnFocusChangeListener(onFocusChangeListener);
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
        String nodeIp = editTextNodeIp.getText().toString();
        String nodePort = editTextNodePort.getText().toString();
        String username = editTextUsername.getText().toString();
        String password = editTextPassword.getText().toString();
        if (nodeIp.equals("") || nodePort.equals("")) {
            BaseActivity.showShortToast(NodeAddActivity.this, getString(R.string.activity_node_add_confirmEmpty_tips));
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(ActivityHelp.REQUEST_NODE_IP_KEY, nodeIp);
        intent.putExtra(ActivityHelp.REQUEST_NODE_PORT_KEY, nodePort);
        intent.putExtra(ActivityHelp.REQUEST_NODE_USERNAME_KEY, username);
        intent.putExtra(ActivityHelp.REQUEST_NODE_PASSWORD_KEY, password);
        intent.putExtra(ActivityHelp.REQUEST_SYMBOL_KEY, XManager.SYMBOL);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void doBack() {
        super.doBack();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

}
