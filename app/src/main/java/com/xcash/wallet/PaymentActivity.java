/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.wallet;


import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RadioButton;
import android.widget.TextView;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.xcash.base.BaseActivity;
import com.xcash.utils.StringTool;
import com.xcash.utils.database.entity.Wallet;
import com.xcash.wallet.aidl.OnCreateTransactionListener;
import com.xcash.wallet.aidl.Transaction;
import com.xcash.wallet.aidl.WalletOperateManager;
import com.xcash.wallet.uihelp.ActivityHelp;
import com.xcash.wallet.uihelp.ColorHelp;
import com.xcash.wallet.uihelp.PopupWindowHelp;
import com.xcash.wallet.uihelp.ProgressDialogHelp;

public class PaymentActivity extends NewBaseActivity {

    private Wallet wallet;

    private int colorPrimary;
    private int mainColorText;

    private ImageView imageViewBack;
    private TextView textViewTitle;
    private ImageView imageViewRight;
    private ImageView imageViewIcon;
    private TextView textViewWalletName;
    private TextView textViewAmount;
    private ImageView imageViewWalletAddress;
    private EditText editTextWalletAddress;
    private FrameLayout frameLayoutWalletAddress;
    private TextView textViewAllAmount;
    private EditText editTextAmount;
    private FrameLayout frameLayoutAmount;
    private EditText editTextRingSize;
    private FrameLayout frameLayoutRingSize;
    private EditText editTextPaymentId;
    private FrameLayout frameLayoutPaymentId;
    private EditText editTextDescription;
    private FrameLayout frameLayoutDescription;
    private LinearLayout linearLayoutLow;
    private RadioButton radioButtonLow;
    private LinearLayout linearLayoutNormal;
    private RadioButton radioButtonNormal;
    private LinearLayout linearLayoutHigh;
    private RadioButton radioButtonHigh;
    private CheckBox checkBox;
    private Button buttonNext;

    private Drawable drawableWalletAddress;
    private View.OnFocusChangeListener onFocusChangeListener;
    private View.OnClickListener onClickListener;
    private int priority = 1;
    private String unlockedBalance;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment);
        Intent intent = getIntent();
        wallet = (Wallet) intent.getSerializableExtra(ActivityHelp.WALLET_KEY);
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
        handler = new Handler();
    }

    @Override
    protected void initUi() {
        getTypeArrayColor();
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        imageViewRight = (ImageView) findViewById(R.id.imageViewRight);
        imageViewIcon = (ImageView) findViewById(R.id.imageViewIcon);
        textViewWalletName = (TextView) findViewById(R.id.textViewWalletName);
        textViewAmount = (TextView) findViewById(R.id.textViewAmount);
        imageViewWalletAddress = (ImageView) findViewById(R.id.imageViewWalletAddress);
        editTextWalletAddress = (EditText) findViewById(R.id.editTextWalletAddress);
        frameLayoutWalletAddress = (FrameLayout) findViewById(R.id.frameLayoutWalletAddress);
        textViewAllAmount = (TextView) findViewById(R.id.textViewAllAmount);
        editTextAmount = (EditText) findViewById(R.id.editTextAmount);
        frameLayoutAmount = (FrameLayout) findViewById(R.id.frameLayoutAmount);
        editTextRingSize = (EditText) findViewById(R.id.editTextRingSize);
        frameLayoutRingSize = (FrameLayout) findViewById(R.id.frameLayoutRingSize);
        editTextPaymentId = (EditText) findViewById(R.id.editTextPaymentId);
        frameLayoutPaymentId = (FrameLayout) findViewById(R.id.frameLayoutPaymentId);
        editTextDescription = (EditText) findViewById(R.id.editTextDescription);
        frameLayoutDescription = (FrameLayout) findViewById(R.id.frameLayoutDescription);
        linearLayoutLow = (LinearLayout) findViewById(R.id.linearLayoutLow);
        radioButtonLow = (RadioButton) findViewById(R.id.radioButtonLow);
        linearLayoutNormal = (LinearLayout) findViewById(R.id.linearLayoutNormal);
        radioButtonNormal = (RadioButton) findViewById(R.id.radioButtonNormal);
        linearLayoutHigh = (LinearLayout) findViewById(R.id.linearLayoutHigh);
        radioButtonHigh = (RadioButton) findViewById(R.id.radioButtonHigh);
        checkBox = (CheckBox) findViewById(R.id.checkBox);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        onFocusChangeListener();
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewTitle.setText(R.string.activity_payment_textViewTitle_text);
        imageViewRight.setVisibility(View.VISIBLE);
        imageViewRight.setImageResource(R.mipmap.activity_payment_qr_code);
        drawableWalletAddress = getResources().getDrawable(R.mipmap.activity_main_navigationview_address);
        ColorHelp.setImageViewDrawableTint(imageViewWalletAddress, drawableWalletAddress, mainColorText);
        if (wallet != null) {
            textViewWalletName.setText(wallet.getName());
            unlockedBalance = wallet.getUnlockedBalance();
            textViewAmount.setText(unlockedBalance + " " + wallet.getSymbol());
        }
        radioButtonLow.setActivated(true);
        setPriority(1);
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
                    case R.id.editTextWalletAddress:
                        if (hasFocus) {
                            ColorHelp.setImageViewDrawableTint(imageViewWalletAddress, drawableWalletAddress, colorPrimary);
                            frameLayoutWalletAddress.setBackgroundColor(colorPrimary);
                        } else {
                            ColorHelp.setImageViewDrawableTint(imageViewWalletAddress, drawableWalletAddress, mainColorText);
                            frameLayoutWalletAddress.setBackgroundColor(mainColorText);
                        }
                        break;
                    case R.id.editTextAmount:
                        if (hasFocus) {
                            textViewAllAmount.setTextColor(colorPrimary);
                            frameLayoutAmount.setBackgroundColor(colorPrimary);
                        } else {
                            textViewAllAmount.setTextColor(mainColorText);
                            frameLayoutAmount.setBackgroundColor(mainColorText);
                        }
                        break;
                    case R.id.editTextRingSize:
                        if (hasFocus) {
                            frameLayoutRingSize.setBackgroundColor(colorPrimary);
                        } else {
                            frameLayoutRingSize.setBackgroundColor(mainColorText);
                        }
                        break;
                    case R.id.editTextPaymentId:
                        if (hasFocus) {
                            frameLayoutPaymentId.setBackgroundColor(colorPrimary);
                        } else {
                            frameLayoutPaymentId.setBackgroundColor(mainColorText);
                        }
                        break;
                    case R.id.editTextDescription:
                        if (hasFocus) {
                            frameLayoutDescription.setBackgroundColor(colorPrimary);
                        } else {
                            frameLayoutDescription.setBackgroundColor(mainColorText);
                        }
                        break;
                    default:
                        break;
                }

            }
        };
        editTextWalletAddress.setOnFocusChangeListener(onFocusChangeListener);
        editTextAmount.setOnFocusChangeListener(onFocusChangeListener);
        editTextRingSize.setOnFocusChangeListener(onFocusChangeListener);
        editTextPaymentId.setOnFocusChangeListener(onFocusChangeListener);
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
                    case R.id.imageViewRight:
                        QRCodeActivity.openQRCodeActivity(PaymentActivity.this);
                        break;
                    case R.id.imageViewWalletAddress:
                        chooseAddress();
                        break;
                    case R.id.textViewAllAmount:
                        setAllAmount();
                        break;
                    case R.id.linearLayoutLow:
                        setPriority(1);
                        break;
                    case R.id.linearLayoutNormal:
                        setPriority(2);
                        break;
                    case R.id.linearLayoutHigh:
                        setPriority(3);
                        break;
                    case R.id.buttonNext:
                        doNext(buttonNext);
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        imageViewRight.setOnClickListener(onClickListener);
        imageViewWalletAddress.setOnClickListener(onClickListener);
        textViewAllAmount.setOnClickListener(onClickListener);
        linearLayoutLow.setOnClickListener(onClickListener);
        linearLayoutNormal.setOnClickListener(onClickListener);
        linearLayoutHigh.setOnClickListener(onClickListener);
        buttonNext.setOnClickListener(onClickListener);
    }

    private void chooseAddress() {
        Intent intent = new Intent(PaymentActivity.this,
                AddressManagerActivity.class);
        intent.putExtra(ActivityHelp.CHOOSE_ADDRESS_KEY, true);
        startActivityForResult(intent, ActivityHelp.REQUEST_CODE_CHOOSE_ADDRESS);
    }

    private void setAllAmount() {
        editTextAmount.setText(unlockedBalance);
        TheApplication.setCursorToLast(editTextAmount);
    }

    private void setPriority(int priority) {
        radioButtonLow.setChecked(false);
        radioButtonNormal.setChecked(false);
        radioButtonHigh.setChecked(false);
        switch (priority) {
            case 1:
                radioButtonLow.setChecked(true);
                break;
            case 2:
                radioButtonNormal.setChecked(true);
                break;
            case 3:
                radioButtonHigh.setChecked(true);
                break;
            default:
                break;
        }
        this.priority = priority;
    }

    private void doNext(View view) {
        if (wallet == null) {
            return;
        }
        final String walletAddress = editTextWalletAddress.getText().toString();
        final String amount = editTextAmount.getText().toString();
        final String ringSize = editTextRingSize.getText().toString();
        final String paymentId = editTextPaymentId.getText().toString();
        final String description = editTextDescription.getText().toString();
        if (walletAddress.equals("") || amount.equals("")) {
            BaseActivity.showShortToast(PaymentActivity.this, getString(R.string.activity_payment_confirmEmpty_tips));
            return;
        }
        if (!StringTool.checkWalletAddress(walletAddress)) {
            BaseActivity.showShortToast(PaymentActivity.this, getString(R.string.address_error_tips));
            return;
        }
        boolean amountCheck = false;
        boolean all = false;
        try {
            float theAmount = Float.parseFloat(amount);
            if (theAmount > 0) {
                amountCheck = true;
            }
            if (unlockedBalance != null) {
                float theAllAmount = Float.parseFloat(unlockedBalance);
                if (theAmount == theAllAmount) {
                    all = true;
                }
            }
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        if (!amountCheck) {
            BaseActivity.showShortToast(PaymentActivity.this, getString(R.string.activity_payment_payAmountCheckError_tips));
            return;
        }
        if (all) {
            PopupWindowHelp.showPopupWindowNormalTips(PaymentActivity.this, view.getRootView(), view, getString(R.string.activity_payment_payAllAmount_tips), new PopupWindowHelp.OnShowPopupWindowNormalTipsListener() {
                @Override
                public void okClick(PopupWindow popupWindow, View view) {
                    popupWindow.dismiss();
                    createTransaction(walletAddress, "-1", ringSize, paymentId, description, priority, checkBox.isChecked());
                }
            });
        } else {
            createTransaction(walletAddress, amount, ringSize, paymentId, description, priority, checkBox.isChecked());
        }
    }

    private void createTransaction(final String walletAddress, String amount, String ringSize, String paymentId, String description, int priority, boolean publicTransaction) {
        WalletOperateManager walletOperateManager = TheApplication.getTheApplication().getWalletServiceHelper().getWalletOperateManager();
        if (walletOperateManager == null) {
            return;
        }
        Object[] objects = ProgressDialogHelp.unEnabledView(PaymentActivity.this, null);
        final ProgressDialog progressDialog = (ProgressDialog) objects[0];
        final String progressDialogKey = (String) objects[1];
        try {
            walletOperateManager.createTransaction(walletAddress, amount, ringSize, paymentId, description, priority, publicTransaction, new OnCreateTransactionListener.Stub() {
                @Override
                public void onSuccess(final Transaction transaction) throws RemoteException {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            ProgressDialogHelp.enabledView(PaymentActivity.this, progressDialog, progressDialogKey, null);
                            Intent intent = new Intent(PaymentActivity.this,
                                    PaymentConfirmActivity.class);
                            intent.putExtra(ActivityHelp.WALLET_KEY, wallet);
                            intent.putExtra(ActivityHelp.ADDRESS_KEY, walletAddress);
                            intent.putExtra(ActivityHelp.TRANSACTION_KEY, transaction);
                            startActivity(intent);
                            finish();
                        }
                    });
                }

                @Override
                public void onError(final String error) throws RemoteException {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            BaseActivity.showShortToast(PaymentActivity.this, error);
                            ProgressDialogHelp.enabledView(PaymentActivity.this, progressDialog, progressDialogKey, null);
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            ProgressDialogHelp.enabledView(PaymentActivity.this, progressDialog, progressDialogKey, null);
        }
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ActivityHelp.REQUEST_CODE_CHOOSE_ADDRESS) {
            if (resultCode == RESULT_OK && data != null) {
                String address = data.getStringExtra(ActivityHelp.REQUEST_ADDRESS_KEY);
                editTextWalletAddress.setText(address);
            }
        } else if (requestCode == IntentIntegrator.REQUEST_CODE) {
            if (resultCode == RESULT_OK && data != null) {
                IntentResult intentResult = IntentIntegrator.parseActivityResult(resultCode, data);
                editTextWalletAddress.setText(intentResult.getContents());
            } else {
                BaseActivity.showShortToast(PaymentActivity.this, getString(R.string.qrcode_cancel_tips));
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}
