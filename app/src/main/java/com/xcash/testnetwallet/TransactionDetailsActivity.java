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


import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.xcash.models.local.KeyValueItem;
import com.xcash.utils.ClipboardTool;
import com.xcash.utils.StringTool;
import com.xcash.utils.database.entity.TransactionInfo;
import com.xcash.utils.database.entity.Wallet;
import com.xcash.wallet.aidl.manager.XManager;
import com.xcash.wallet.uihelp.ActivityHelp;

import java.util.ArrayList;
import java.util.List;

public class TransactionDetailsActivity extends NewBaseActivity {

    private Wallet wallet;
    private TransactionInfo transactionInfo;

    private int mainColorText;
    private int layout_transaction_item_pay_in;
    private int layout_transaction_item_pay_out;
    private int editText_normal_hint;

    private ImageView imageViewBack;
    private TextView textViewTitle;
    private TextView textViewStatus;
    private TextView textViewAmount;
    private TextView textViewHash;
    private TextView textViewTime;
    private LinearLayout linearLayoutDetails;

    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_transaction_details);
        Intent intent = getIntent();
        wallet = (Wallet) intent.getSerializableExtra(ActivityHelp.WALLET_KEY);
        transactionInfo = (TransactionInfo) intent.getSerializableExtra(ActivityHelp.TRANSACTION_INFO_KEY);
        initAll();
    }

    @SuppressWarnings("ResourceType")
    private void getTypeArrayColor() {
        TypedArray typedArray = getTheme().obtainStyledAttributes(new int[]{R.attr.mainColorText});
        try {
            mainColorText = typedArray.getColor(0, 0xffffffff);
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
        layout_transaction_item_pay_in = ContextCompat.getColor(TransactionDetailsActivity.this, R.color.layout_transaction_item_pay_in);
        layout_transaction_item_pay_out = ContextCompat.getColor(TransactionDetailsActivity.this, R.color.layout_transaction_item_pay_out);
        editText_normal_hint = ContextCompat.getColor(TransactionDetailsActivity.this, R.color.editText_normal_hint);
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        textViewStatus = (TextView) findViewById(R.id.textViewStatus);
        textViewAmount = (TextView) findViewById(R.id.textViewAmount);
        textViewHash = (TextView) findViewById(R.id.textViewHash);
        textViewTime = (TextView) findViewById(R.id.textViewTime);
        linearLayoutDetails = (LinearLayout) findViewById(R.id.linearLayoutDetails);
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewTitle.setText(R.string.activity_transaction_details_textViewTitle_text);
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
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
    }

    private void showTransactionDetails(LinearLayout linearLayout) {
        if (wallet == null || transactionInfo == null) {
            return;
        }
        if (transactionInfo.isPending() || transactionInfo.getConfirmations() < XManager.TRANSACTION_MIN_CONFIRMATION) {
            textViewStatus.setText(getString(R.string.layout_transaction_item_status_tips) + "(" + getString(R.string.layout_transaction_item_status_pending_tips) + " " + transactionInfo.getConfirmations() + ")");
            textViewStatus.setTextColor(editText_normal_hint);
        } else {
            textViewStatus.setText(getString(R.string.layout_transaction_item_status_tips) + "(" + getString(R.string.layout_transaction_item_status_over_tips) + ")");
            textViewStatus.setTextColor(mainColorText);
        }
        String amount = transactionInfo.getAmount();
        if (transactionInfo.getDirection() == 1) {
            textViewAmount.setText("-" + amount);
            textViewAmount.setTextColor(layout_transaction_item_pay_out);
        } else {
            textViewAmount.setText("+" + amount);
            textViewAmount.setTextColor(layout_transaction_item_pay_in);
        }
        textViewHash.setText(getString(R.string.layout_transaction_item_hash_tips) + "    " + transactionInfo.getHash());
        textViewTime.setText(getString(R.string.layout_transaction_item_date_tips) + "    " + StringTool.getDateTime(transactionInfo.getTimestamp()));
        textViewHash.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardTool.copyToClipboard(TransactionDetailsActivity.this, transactionInfo.getHash());
            }
        });
        List<KeyValueItem> keyValueItems = new ArrayList<>();
        keyValueItems.add(new KeyValueItem(getString(R.string.activity_transaction_details_fee_tips), transactionInfo.getFee() + " " + wallet.getSymbol()));
        keyValueItems.add(new KeyValueItem(getString(R.string.activity_transaction_details_blockHeight_tips), String.valueOf(transactionInfo.getBlockHeight())));
        keyValueItems.add(new KeyValueItem(getString(R.string.activity_transaction_details_confirmations_tips), String.valueOf(transactionInfo.getConfirmations())));
        keyValueItems.add(new KeyValueItem(getString(R.string.activity_transaction_details_paymentId_tips), transactionInfo.getPaymentId()));
        linearLayout.removeAllViews();
        LayoutInflater layoutInflater = LayoutInflater.from(TransactionDetailsActivity.this);
        for (int i = 0; i < keyValueItems.size(); i++) {
            KeyValueItem keyValueItem = keyValueItems.get(i);
            View view = layoutInflater.inflate(R.layout.activity_payment_confirm_item, null);
            TextView textViewContent = (TextView) view.findViewById(R.id.textViewContent);
            final String value = (String) keyValueItem.getValue();
            textViewContent.setText(keyValueItem.getKey() + "  " + value);
            linearLayout.addView(view);
        }
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

}
