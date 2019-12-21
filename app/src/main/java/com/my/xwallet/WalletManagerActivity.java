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
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.my.adapters.recyclerviewadapter.WalletManagerActivity_RecyclerViewAdapter;
import com.my.base.BaseActivity;
import com.my.base.recyclerviewlibrary.models.ViewItem;
import com.my.base.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
import com.my.utils.CoroutineHelper;
import com.my.utils.database.AppDatabase;
import com.my.utils.database.entity.Wallet;
import com.my.xwallet.aidl.OnNormalListener;
import com.my.xwallet.aidl.WalletOperateManager;
import com.my.xwallet.uihelp.ActivityHelp;
import com.my.xwallet.uihelp.ProgressDialogHelp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WalletManagerActivity extends NewBaseActivity {

    private ImageView imageViewBack;
    private TextView textViewTitle;
    private BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout;
    private Button buttonCreateWallet;
    private Button buttonImportWallet;
    private View.OnClickListener onClickListener;
    private CoroutineHelper coroutineHelper = new CoroutineHelper();
    private WalletManagerActivity_RecyclerViewAdapter walletManagerActivity_recyclerViewAdapter;

    private boolean doCloseActiveWallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wallet_manager);
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
        baseRecyclerViewFromFrameLayout = (BaseRecyclerViewFromFrameLayout) findViewById(R.id.baseRecyclerViewFromFrameLayout);
        buttonCreateWallet = (Button) findViewById(R.id.buttonCreateWallet);
        buttonImportWallet = (Button) findViewById(R.id.buttonImportWallet);
        initBaseRecyclerViewFromFrameLayout();

        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewTitle.setText(R.string.activity_wallet_manager_textViewTitle_text);
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {

    }

    private void initBaseRecyclerViewFromFrameLayout() {
        TheApplication.setColorSchemeColors(WalletManagerActivity.this, baseRecyclerViewFromFrameLayout.getSwipeRefreshLayout());
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                showWallets();
            }
        };
        baseRecyclerViewFromFrameLayout.setOnRefreshListener(onRefreshListener);
        baseRecyclerViewFromFrameLayout.autoRefresh(handler, TheApplication.AUTOREFRESHDELAY);
    }

    private void onClickListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageViewBack:
                        doBack();
                        break;
                    case R.id.buttonCreateWallet:
                        Intent intent1 = new Intent(WalletManagerActivity.this,
                                SetWalletActivity.class);
                        intent1.putExtra(ActivityHelp.CHOOSE_WALLET_TYPE_KEY, ActivityHelp.CHOOSE_WALLET_TYPE_CREATE);
                        startActivity(intent1);
                        break;
                    case R.id.buttonImportWallet:
                        Intent intent2 = new Intent(WalletManagerActivity.this,
                                SetWalletActivity.class);
                        intent2.putExtra(ActivityHelp.CHOOSE_WALLET_TYPE_KEY, ActivityHelp.CHOOSE_WALLET_TYPE_IMPORT);
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        buttonCreateWallet.setOnClickListener(onClickListener);
        buttonImportWallet.setOnClickListener(onClickListener);
    }

    private void initOrRefreshAdapter(List<ViewItem> viewItems) {
        if (viewItems == null) {
            viewItems = new ArrayList<ViewItem>();
        }
        if (walletManagerActivity_recyclerViewAdapter == null) {
            walletManagerActivity_recyclerViewAdapter = new WalletManagerActivity_RecyclerViewAdapter(WalletManagerActivity.this, baseRecyclerViewFromFrameLayout.getRecyclerView(), viewItems);
            walletManagerActivity_recyclerViewAdapter.setOnWalletManagerListener(new WalletManagerActivity_RecyclerViewAdapter.OnWalletManagerListener() {
                @Override
                public void onItemSelect(Wallet wallet) {
                    activeWallet(wallet);
                }
            });
            baseRecyclerViewFromFrameLayout.setAdapter(walletManagerActivity_recyclerViewAdapter);
        } else {
            TheApplication.replaceAllFormBaseRecyclerViewAdapter(walletManagerActivity_recyclerViewAdapter, viewItems, baseRecyclerViewFromFrameLayout.getRecyclerView());
        }
    }


    private void showWallets() {
        coroutineHelper.launch(new CoroutineHelper.OnCoroutineListener<List<Wallet>>() {
            @Override
            public List<Wallet> runOnIo() {
                List<Wallet> wallets = null;
                try {
                    wallets = AppDatabase.getInstance().walletDao().loadWallets();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return wallets;
            }

            @Override
            public void overRunOnMain(List<Wallet> wallets) {
                List<ViewItem> viewItems = new ArrayList<ViewItem>();
                if (wallets != null) {
                    int activeIndex = 0;
                    ViewItem activeViewItem = null;
                    Collections.reverse(wallets);
                    for (int i = 0; i < wallets.size(); i++) {
                        Wallet wallet = wallets.get(i);
                        ViewItem viewItem = new ViewItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1, wallet);
                        if (wallet.isActive()) {
                            activeIndex = i;
                            activeViewItem = viewItem;
                        }
                        viewItems.add(viewItem);
                    }
                    if (activeViewItem != null) {
                        viewItems.remove(activeIndex);
                        viewItems.add(0, activeViewItem);
                    }
                }
                initOrRefreshAdapter(viewItems);
            }
        });
    }


    private void activeWallet(final Wallet wallet) {
        if (wallet == null) {
            return;
        }
        closeActiveWallet();
        Object[] objects = ProgressDialogHelp.unEnabledView(WalletManagerActivity.this, null);
        final ProgressDialog progressDialog = (ProgressDialog) objects[0];
        final String progressDialogKey = (String) objects[1];
        coroutineHelper.launch(new CoroutineHelper.OnCoroutineListener<Boolean>() {
            @Override
            public Boolean runOnIo() {
                boolean result = false;
                try {
                    List<Wallet> wallets = AppDatabase.getInstance().walletDao().loadWallets();
                    if (wallets != null) {
                        Wallet[] walletsArray = wallets.toArray(new Wallet[]{});
                        for (int i = 0; i < walletsArray.length; i++) {
                            Wallet theWallet = walletsArray[i];
                            if (wallet.getId() == theWallet.getId()) {
                                theWallet.setActive(true);
                            } else {
                                theWallet.setActive(false);
                            }
                        }
                        AppDatabase.getInstance().walletDao().updateWallets(walletsArray);
                    }
                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            public void overRunOnMain(Boolean result) {
                if (result == false) {
                    BaseActivity.showShortToast(WalletManagerActivity.this, getString(R.string.activity_wallet_manager_activeWallet_error_tips));
                } else {
                    doRefresh();
                    MainActivity.doRefreshIfActivityExist();
                }
                ProgressDialogHelp.enabledView(WalletManagerActivity.this, progressDialog, progressDialogKey, null);
            }
        });
    }

    private void closeActiveWallet() {
        if (doCloseActiveWallet) {
            return;
        }
        WalletOperateManager walletOperateManager = TheApplication.getTheApplication().getWalletServiceHelper().getWalletOperateManager();
        if (walletOperateManager == null) {
            return;
        }
        try {
            walletOperateManager.closeActiveWallet(new OnNormalListener.Stub() {
                @Override
                public void onSuccess(final String tips) throws RemoteException {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {

                        }
                    });
                }

                @Override
                public void onError(final String error) throws RemoteException {
                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            BaseActivity.showShortToast(WalletManagerActivity.this, error);
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
        doCloseActiveWallet = true;
    }


    public void doRefresh() {
        baseRecyclerViewFromFrameLayout.autoRefresh(handler, TheApplication.AUTOREFRESHDELAY);
    }


    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        coroutineHelper.onDestroy();
    }

    public static void doRefreshIfActivityExist() {
        WalletManagerActivity walletManagerActivity = (WalletManagerActivity) TheApplication.getActivityFromActivityManager(WalletManagerActivity.class.getName());
        if (walletManagerActivity != null) {
            walletManagerActivity.doRefresh();
        }
    }

}
