/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.xwallet.fragment;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.my.adapters.recyclerviewadapter.WalletManagerActivity_RecyclerViewAdapter;
import com.my.base.BaseActivity;
import com.my.base.recyclerviewlibrary.models.ViewItem;
import com.my.base.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
import com.my.models.local.KeyValueItem;
import com.my.utils.CoroutineHelper;
import com.my.utils.database.AppDatabase;
import com.my.utils.database.entity.Wallet;
import com.my.xwallet.MainActivity;
import com.my.xwallet.R;
import com.my.xwallet.TheApplication;
import com.my.xwallet.WalletManagerActivity;
import com.my.xwallet.uihelp.PopupWindowHelp;
import com.my.xwallet.uihelp.ProgressDialogHelp;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MainActivity_Fragment_Wallet extends BaseFragment {

    private View view;
    private RelativeLayout relativeLayoutContent;
    private ImageView imageViewMenu;
    private ImageView imageViewMore;
    private BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout;

    private boolean alreadyInitUi = true;
    private View.OnClickListener onClickListener;
    private CoroutineHelper coroutineHelper = new CoroutineHelper();
    private WalletManagerActivity_RecyclerViewAdapter walletManagerActivity_recyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_content_fragment_wallet, container, false);
        initAll();
        return view;
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    @Override
    protected void initUi() {
        relativeLayoutContent = (RelativeLayout) view.findViewById(R.id.relativeLayoutContent);
        imageViewMenu = (ImageView) view.findViewById(R.id.imageViewMenu);
        imageViewMore = (ImageView) view.findViewById(R.id.imageViewMore);
        baseRecyclerViewFromFrameLayout = (BaseRecyclerViewFromFrameLayout) view.findViewById(R.id.baseRecyclerViewFromFrameLayout);
        initBaseRecyclerViewFromFrameLayout();
        alreadyInitUi = true;
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        adaptationStatusBar(relativeLayoutContent);
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {

    }

    private void initBaseRecyclerViewFromFrameLayout() {
        TheApplication.setColorSchemeColors(getBaseActivity(), baseRecyclerViewFromFrameLayout.getSwipeRefreshLayout());
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
                    case R.id.imageViewMenu:
                        ((MainActivity) getBaseActivity()).openDrawer();
                        break;
                    case R.id.imageViewMore:
                        showMore();
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewMenu.setOnClickListener(onClickListener);
        imageViewMore.setOnClickListener(onClickListener);
    }

    private void initOrRefreshAdapter(List<ViewItem> viewItems) {
        if (viewItems == null) {
            viewItems = new ArrayList<ViewItem>();
        }
        if (walletManagerActivity_recyclerViewAdapter == null) {
            walletManagerActivity_recyclerViewAdapter = new WalletManagerActivity_RecyclerViewAdapter(getBaseActivity(), baseRecyclerViewFromFrameLayout.getRecyclerView(), viewItems);
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

    private void showMore() {
        final int[] locations = new int[2];
        imageViewMore.getLocationOnScreen(locations);
        List<KeyValueItem> keyValueItems = new ArrayList<KeyValueItem>();
        String show_more_tips = getString(R.string.show_more_tips);
        keyValueItems.add(new KeyValueItem(show_more_tips, show_more_tips));
        PopupWindowHelp.showPopupWindowMenuListViewMore(getBaseActivity(), imageViewMore.getRootView(), imageViewMore, 0, locations[1] + imageViewMore.getHeight(), keyValueItems, 2, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(getBaseActivity(),
                            WalletManagerActivity.class);
                    getBaseActivity().startActivity(intent);
                }
            }
        });
    }

    private void activeWallet(final Wallet wallet) {
        if (wallet == null) {
            return;
        }
        Object[] objects = ProgressDialogHelp.unEnabledView(getBaseActivity(), null);
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
                    BaseActivity.showShortToast(getBaseActivity(), getString(R.string.activity_wallet_manager_activeWallet_error_tips));
                } else {
                    MainActivity.doRefreshIfActivityExist();
                }
                ProgressDialogHelp.enabledView(getBaseActivity(), progressDialog, progressDialogKey, null);
            }
        });
    }

    public void doRefresh() {
        if (alreadyInitUi) {
            baseRecyclerViewFromFrameLayout.autoRefresh(handler, TheApplication.AUTOREFRESHDELAY);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        coroutineHelper.onDestroy();
    }

}
