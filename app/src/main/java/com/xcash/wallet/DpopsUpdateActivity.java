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
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.xcash.base.BaseActivity;
import com.xcash.models.local.KeyValueItem;
import com.xcash.utils.WalletServiceHelper;
import com.xcash.utils.database.entity.Wallet;
import com.xcash.wallet.aidl.OnNormalListener;
import com.xcash.wallet.aidl.WalletOperateManager;
import com.xcash.wallet.uihelp.ActivityHelp;
import com.xcash.wallet.uihelp.PopupWindowHelp;
import com.xcash.wallet.uihelp.ProgressDialogHelp;

import java.util.ArrayList;
import java.util.List;


public class DpopsUpdateActivity extends NewBaseActivity {

    private Wallet wallet;

    private int colorPrimary;
    private int mainColorText;
    private int textView_error;
    private int view_normal_margin_broad;

    private ImageView imageViewBack;
    private TextView textViewTitle;
    private RelativeLayout relativeLayoutItem;
    private EditText editTextItem;
    private FrameLayout frameLayoutItemType;
    private FrameLayout frameLayoutItem;
    private EditText editTextValue;
    private FrameLayout frameLayoutValue;
    private TextView textViewTips;
    private Button buttonNext;

    private View.OnFocusChangeListener onFocusChangeListener;
    private View.OnClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dpops_update);
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
        textView_error = ContextCompat.getColor(DpopsUpdateActivity.this, R.color.textView_error);
        view_normal_margin_broad = getResources().getDimensionPixelSize(R.dimen.view_normal_margin_broad);
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        relativeLayoutItem = (RelativeLayout) findViewById(R.id.relativeLayoutItem);
        editTextItem = (EditText) findViewById(R.id.editTextItem);
        frameLayoutItemType = (FrameLayout) findViewById(R.id.frameLayoutItemType);
        frameLayoutItem = (FrameLayout) findViewById(R.id.frameLayoutItem);
        editTextValue = (EditText) findViewById(R.id.editTextValue);
        frameLayoutValue = (FrameLayout) findViewById(R.id.frameLayoutValue);
        textViewTips = (TextView) findViewById(R.id.textViewTips);
        buttonNext = (Button) findViewById(R.id.buttonNext);
        onFocusChangeListener();
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewTitle.setText(R.string.activity_dpops_update_textViewTitle_text);
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
                    case R.id.editTextItem:
                        if (hasFocus) {
                            frameLayoutItem.setBackgroundColor(colorPrimary);
                        } else {
                            frameLayoutItem.setBackgroundColor(mainColorText);
                        }
                        break;
                    case R.id.editTextValue:
                        if (hasFocus) {
                            frameLayoutValue.setBackgroundColor(colorPrimary);
                        } else {
                            frameLayoutValue.setBackgroundColor(mainColorText);
                        }
                        break;
                    default:
                        break;
                }

            }
        };
        editTextItem.setOnFocusChangeListener(onFocusChangeListener);
        editTextValue.setOnFocusChangeListener(onFocusChangeListener);
    }

    private void onClickListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageViewBack:
                        doBack();
                        break;
                    case R.id.frameLayoutItemType:
                        showItemType(frameLayoutItemType);
                        break;
                    case R.id.buttonNext:
                        doUpdate(buttonNext);
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        frameLayoutItemType.setOnClickListener(onClickListener);
        buttonNext.setOnClickListener(onClickListener);
    }

    private void showItemType(View view) {
        final int[] locations = new int[2];
        editTextItem.getLocationOnScreen(locations);
        final List<KeyValueItem> keyValueItems = new ArrayList<>();
        keyValueItems.add(new KeyValueItem("IP_address", "IP_address"));
        keyValueItems.add(new KeyValueItem("about", "about"));
        keyValueItems.add(new KeyValueItem("website", "website"));
        keyValueItems.add(new KeyValueItem("team", "team"));
        keyValueItems.add(new KeyValueItem("pool_mode", "pool_mode"));
        keyValueItems.add(new KeyValueItem("fee_structure", "fee_structure"));
        keyValueItems.add(new KeyValueItem("server_settings", "server_settings"));
        PopupWindowHelp.showPopupWindowListViewNormal(DpopsUpdateActivity.this, view.getRootView(), view, relativeLayoutItem.getWidth(), view_normal_margin_broad, locations[1] + editTextItem.getHeight(), keyValueItems, 5, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                editTextItem.setText(keyValueItems.get(position).getKey());
                TheApplication.setCursorToLast(editTextItem);
            }
        });
    }

    private void doUpdate(final View view) {
        if (wallet == null) {
            return;
        }
        String item = editTextItem.getText().toString();
        if (item.equals("")) {
            BaseActivity.showShortToast(DpopsUpdateActivity.this, getString(R.string.activity_dpops_update_confirmItem_tips));
            return;
        }
        String value = editTextValue.getText().toString();
        if (value.equals("")) {
            BaseActivity.showShortToast(DpopsUpdateActivity.this, getString(R.string.activity_dpops_update_confirmValue_tips));
            return;
        }
        WalletOperateManager walletOperateManager = TheApplication.getTheApplication().getWalletServiceHelper().getWalletOperateManager();
        if (walletOperateManager == null) {
            return;
        }
        Object[] objects = ProgressDialogHelp.unEnabledView(DpopsUpdateActivity.this, view);
        final ProgressDialog progressDialog = (ProgressDialog) objects[0];
        final String progressDialogKey = (String) objects[1];
        try {
            final String content = "{\"item\":" + item + ",\"value\":" + value + "}\n\nResult=> ";
            walletOperateManager.delegateUpdate(item, value, new OnNormalListener.Stub() {
                @Override
                public void onSuccess(final String tips) throws RemoteException {
                    WalletServiceHelper.addOperationHistory(wallet.getId(), "Delegate Update", true, content + tips);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textViewTips.setText(tips);
                            textViewTips.setTextColor(colorPrimary);
                            ProgressDialogHelp.enabledView(DpopsUpdateActivity.this, progressDialog, progressDialogKey, view);
                        }
                    });
                }

                @Override
                public void onError(final String error) throws RemoteException {
                    WalletServiceHelper.addOperationHistory(wallet.getId(), "Delegate Update", false, content + error);
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            textViewTips.setText(error);
                            textViewTips.setTextColor(textView_error);
                            ProgressDialogHelp.enabledView(DpopsUpdateActivity.this, progressDialog, progressDialogKey, view);
                        }
                    });
                }

            });
        } catch (RemoteException e) {
            e.printStackTrace();
            ProgressDialogHelp.enabledView(DpopsUpdateActivity.this, progressDialog, progressDialogKey, view);
        }
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

}
