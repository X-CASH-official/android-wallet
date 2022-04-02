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
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xcash.base.BaseActivity;
import com.xcash.models.local.KeyValueItem;
import com.xcash.utils.database.entity.Wallet;
import com.xcash.testnetwallet.aidl.OnNormalListener;
import com.xcash.testnetwallet.aidl.Transaction;
import com.xcash.testnetwallet.aidl.WalletOperateManager;
import com.xcash.testnetwallet.uihelp.ActivityHelp;
import com.xcash.testnetwallet.uihelp.ProgressDialogHelp;

import java.util.ArrayList;
import java.util.List;

public class PaymentConfirmActivity extends NewBaseActivity {

    private Wallet wallet;
    private String address;
    private Transaction transaction;

    private ImageView imageViewBack;
    private TextView textViewTitle;
    private LinearLayout linearLayoutDetails;
    private Button buttonNext;

    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_payment_confirm);
        Intent intent = getIntent();
        wallet = (Wallet) intent.getSerializableExtra(ActivityHelp.WALLET_KEY);
        address = (String) intent.getSerializableExtra(ActivityHelp.ADDRESS_KEY);
        transaction = (Transaction) intent.getParcelableExtra(ActivityHelp.TRANSACTION_KEY);
        initAll();
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    @Override
    protected void initUi() {
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        linearLayoutDetails = (LinearLayout) findViewById(R.id.linearLayoutDetails);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewTitle.setText(R.string.activity_payment_confirm_textViewTitle_text);
        showTransactionDetails(linearLayoutDetails);
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
                        sendTransaction();
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        buttonNext.setOnClickListener(onClickListener);
    }

    private void showTransactionDetails(LinearLayout linearLayout) {
        if (wallet == null || transaction == null) {
            return;
        }
        List<KeyValueItem> keyValueItems = new ArrayList<>();
        keyValueItems.add(new KeyValueItem(getString(R.string.activity_payment_confirm_address_tips), address));
        keyValueItems.add(new KeyValueItem(getString(R.string.activity_payment_confirm_amount_tips), String.valueOf(transaction.getAmount() / 1000000.0f) + " " + wallet.getSymbol()));
        keyValueItems.add(new KeyValueItem(getString(R.string.activity_payment_confirm_fee_tips), String.valueOf(transaction.getFee() / 1000000.0f) + " " + wallet.getSymbol()));
        keyValueItems.add(new KeyValueItem(getString(R.string.activity_payment_confirm_firstTxId_tips), String.valueOf(transaction.getFirstTxId())));
        keyValueItems.add(new KeyValueItem(getString(R.string.activity_payment_confirm_txCount_tips), String.valueOf(transaction.getTxCount())));
        linearLayout.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(PaymentConfirmActivity.this);
        for (int i = 0; i < keyValueItems.size(); i++) {
            KeyValueItem keyValueItem = keyValueItems.get(i);
            View view = layoutInflater.inflate(R.layout.activity_payment_confirm_item, null);
            TextView textViewContent = (TextView) view.findViewById(R.id.textViewContent);
            final String value = (String) keyValueItem.getValue();
            textViewContent.setText(keyValueItem.getKey() + "  " + value);
            linearLayout.addView(view);
        }
    }

    private void sendTransaction() {
        WalletOperateManager walletOperateManager = TheApplication.getTheApplication().getWalletServiceHelper().getWalletOperateManager();
        if (walletOperateManager == null) {
            return;
        }
        Object[] objects = ProgressDialogHelp.unEnabledView(PaymentConfirmActivity.this, null);
        final ProgressDialog progressDialog = (ProgressDialog) objects[0];
        final String progressDialogKey = (String) objects[1];
        try {
            walletOperateManager.sendTransaction(new OnNormalListener.Stub() {
                @Override
                public void onSuccess(final String transaction) throws RemoteException {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            BaseActivity.showShortToast(PaymentConfirmActivity.this, getString(R.string.activity_payment_confirm_paySuccess_tips));
                            ProgressDialogHelp.enabledView(PaymentConfirmActivity.this, progressDialog, progressDialogKey, null);
                            finish();
                        }
                    });
                }

                @Override
                public void onError(final String error) throws RemoteException {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            BaseActivity.showShortToast(PaymentConfirmActivity.this, error);
                            ProgressDialogHelp.enabledView(PaymentConfirmActivity.this, progressDialog, progressDialogKey, null);
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            ProgressDialogHelp.enabledView(PaymentConfirmActivity.this, progressDialog, progressDialogKey, null);
        }
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

}
