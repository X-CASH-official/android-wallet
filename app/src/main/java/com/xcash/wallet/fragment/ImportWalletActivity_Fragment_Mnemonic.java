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
package com.xcash.wallet.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xcash.base.BaseActivity;
import com.my.monero.util.RestoreHeight;
import com.xcash.utils.StringTool;
import com.xcash.wallet.MainActivity;
import com.xcash.wallet.R;
import com.xcash.wallet.TheApplication;
import com.xcash.wallet.aidl.OnWalletDataListener;
import com.xcash.wallet.aidl.WalletOperateManager;
import com.xcash.wallet.uihelp.ActivityHelp;
import com.xcash.wallet.uihelp.PopupWindowHelp;
import com.xcash.wallet.uihelp.ProgressDialogHelp;

public class ImportWalletActivity_Fragment_Mnemonic extends BaseFragment {

    private String set_wallet_name;
    private String set_wallet_password;
    private String set_wallet_description;

    private View view;
    private int mainColorText;
    private EditText editTextMnemonic;
    private RelativeLayout relativeLayoutDate;
    private TextView textViewDate;
    private EditText editTextBlockHeight;
    private Button buttonNext;

    private View.OnClickListener onClickListener;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_import_wallet_fragment_mnemonic, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            set_wallet_name = bundle.getString(ActivityHelp.SET_WALLET_NAME_KEY);
            set_wallet_password = bundle.getString(ActivityHelp.SET_WALLET_PASSWORD_KEY);
            set_wallet_description = bundle.getString(ActivityHelp.SET_WALLET_DESCRIPTION_KEY);
        }
        initAll();
        return view;
    }

    @SuppressWarnings("ResourceType")
    private void getTypeArrayColor() {
        TypedArray typedArray = getBaseActivity().getTheme().obtainStyledAttributes(new int[]{R.attr.mainColorText});
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
        editTextMnemonic = (EditText) view.findViewById(R.id.editTextMnemonic);
        relativeLayoutDate = (RelativeLayout) view.findViewById(R.id.relativeLayoutDate);
        textViewDate = (TextView) view.findViewById(R.id.textViewDate);
        editTextBlockHeight = (EditText) view.findViewById(R.id.editTextBlockHeight);
        buttonNext = (Button) view.findViewById(R.id.buttonNext);
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
                    case R.id.relativeLayoutDate:
                        chooseDate(relativeLayoutDate);
                        break;
                    case R.id.buttonNext:
                        restoreWallet();
                        break;
                    default:
                        break;
                }
            }
        };
        relativeLayoutDate.setOnClickListener(onClickListener);
        buttonNext.setOnClickListener(onClickListener);
    }

    private void chooseDate(View view) {
        PopupWindowHelp.showPopupWindowChooseDate(getBaseActivity(), view.getRootView(), view, null, new PopupWindowHelp.OnShowPopupWindowChooseDateListener() {
            @Override
            public void OKClick(DatePicker datePicker, PopupWindow popupWindow, View view) {
                textViewDate.setText(PopupWindowHelp.convertDate(datePicker));
                textViewDate.setTextColor(mainColorText);
                popupWindow.dismiss();
            }
        });
    }

    private void restoreWallet() {
        if (set_wallet_name == null || set_wallet_password == null) {
            return;
        }
        final String mnemonic = editTextMnemonic.getText().toString();
        if (mnemonic.equals("")) {
            BaseActivity.showShortToast(getBaseActivity(), getString(R.string.activity_import_wallet_fragment_mnemonic_mnemonicEmpty_tips));
            return;
        }
        long blockHeight = 0;
        final String blockHeightString = editTextBlockHeight.getText().toString();
        if (!blockHeightString.equals("")) {
            blockHeight = StringTool.convertStringToLong(blockHeightString);
        } else {
            final String date = textViewDate.getText().toString();
            if (!date.equals("")) {
                blockHeight = RestoreHeight.getInstance().getHeight(date);
            }
        }
        // BaseActivity.showShortToast(getBaseActivity(),getString(R.string.activity_import_wallet_fragment_quick_restore_height_tips)+blockHeight);
        WalletOperateManager walletOperateManager = TheApplication.getTheApplication().getWalletServiceHelper().getWalletOperateManager();
        if (walletOperateManager == null) {
            return;
        }
        Object[] objects = ProgressDialogHelp.unEnabledView(getBaseActivity(), null);
        final ProgressDialog progressDialog = (ProgressDialog) objects[0];
        final String progressDialogKey = (String) objects[1];
        try {
            walletOperateManager.importWalletMnemonic(set_wallet_name, set_wallet_password, set_wallet_description, mnemonic, blockHeight, new OnWalletDataListener.Stub() {
                @Override
                public void onSuccess(final com.xcash.wallet.aidl.Wallet wallet) throws RemoteException {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (TheApplication.getActivityFromActivityManager(MainActivity.class.getName()) == null) {
                                Intent intent = new Intent(getBaseActivity(),
                                        MainActivity.class);
                                getBaseActivity().startActivity(intent);
                            } else {
                                MainActivity.enterAndRefreshIfActivityExist();
                            }
                            ProgressDialogHelp.enabledView(getBaseActivity(), progressDialog, progressDialogKey, null);
                        }
                    });
                }

                @Override
                public void onError(final String error) throws RemoteException {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            if (error != null && error.contains(ActivityHelp.FILE_ALREADY_EXISTS)) {
                                BaseActivity.showShortToast(getBaseActivity(), getString(R.string.activity_create_wallet_walletExist_tips));
                            } else {
                                if (error == null) {
                                    BaseActivity.showShortToast(getBaseActivity(), getString(R.string.activity_create_wallet_createWalletError_tips));
                                } else {
                                    BaseActivity.showShortToast(getBaseActivity(), error);
                                }
                            }
                            ProgressDialogHelp.enabledView(getBaseActivity(), progressDialog, progressDialogKey, null);
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
            ProgressDialogHelp.enabledView(getBaseActivity(), progressDialog, progressDialogKey, null);
        }
    }

}
