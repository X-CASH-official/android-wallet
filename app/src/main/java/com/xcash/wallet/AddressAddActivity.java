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

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.xcash.base.BaseActivity;
import com.xcash.utils.StringTool;
import com.xcash.wallet.aidl.manager.XManager;
import com.xcash.wallet.uihelp.ActivityHelp;

public class AddressAddActivity extends NewBaseActivity {

    private int colorPrimary;
    private int mainColorText;

    private ImageView imageViewBack;
    private TextView textViewTitle;
    private ImageView imageViewRight;
    private EditText editTextAddress;
    private FrameLayout frameLayoutAddress;
    private EditText editTextNotes;
    private FrameLayout frameLayoutNotes;
    private Button buttonNext;

    private View.OnFocusChangeListener onFocusChangeListener;
    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_add);
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
        imageViewRight = (ImageView) findViewById(R.id.imageViewRight);
        editTextAddress = (EditText) findViewById(R.id.editTextAddress);
        frameLayoutAddress = (FrameLayout) findViewById(R.id.frameLayoutAddress);
        editTextNotes = (EditText) findViewById(R.id.editTextNotes);
        frameLayoutNotes = (FrameLayout) findViewById(R.id.frameLayoutNotes);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        onFocusChangeListener();
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewTitle.setText(R.string.activity_address_add_textViewTitle_text);
        imageViewRight.setVisibility(View.VISIBLE);
        imageViewRight.setImageResource(R.mipmap.activity_payment_qr_code);
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
                    case R.id.editTextAddress:
                        if (hasFocus) {
                            frameLayoutAddress.setBackgroundColor(colorPrimary);
                        } else {
                            frameLayoutAddress.setBackgroundColor(mainColorText);
                        }
                        break;
                    case R.id.editTextNotes:
                        if (hasFocus) {
                            frameLayoutNotes.setBackgroundColor(colorPrimary);
                        } else {
                            frameLayoutNotes.setBackgroundColor(mainColorText);
                        }
                        break;
                    default:
                        break;
                }

            }
        };
        editTextAddress.setOnFocusChangeListener(onFocusChangeListener);
        editTextNotes.setOnFocusChangeListener(onFocusChangeListener);
    }

    private void onClickListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageViewBack:
                        doBack();
                        break;
                    case R.id.imageViewRight:
                        QRCodeActivity.openQRCodeActivity(AddressAddActivity.this);
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
        imageViewRight.setOnClickListener(onClickListener);
        buttonNext.setOnClickListener(onClickListener);
    }

    private void doNext() {
        String address = editTextAddress.getText().toString();
        String notes = editTextNotes.getText().toString();
        if (address.equals("") || notes.equals("")) {
            BaseActivity.showShortToast(AddressAddActivity.this, getString(R.string.activity_address_add_confirmEmpty_tips));
            return;
        }
        if (!StringTool.checkWalletAddress(address)) {
            BaseActivity.showShortToast(AddressAddActivity.this, getString(R.string.address_error_tips));
            return;
        }
        Intent intent = new Intent();
        intent.putExtra(ActivityHelp.REQUEST_ADDRESS_KEY, address);
        intent.putExtra(ActivityHelp.REQUEST_NOTES_KEY, notes);
        intent.putExtra(ActivityHelp.REQUEST_SYMBOL_KEY, XManager.SYMBOL);
        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                IntentResult intentResult = IntentIntegrator.parseActivityResult(resultCode, data);
                editTextAddress.setText(intentResult.getContents());
            } else {
                BaseActivity.showShortToast(AddressAddActivity.this, getString(R.string.qrcode_cancel_tips));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void doBack() {
        super.doBack();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

}
