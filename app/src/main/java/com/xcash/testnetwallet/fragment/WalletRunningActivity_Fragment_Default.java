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

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xcash.adapters.recyclerviewadapter.Transaction_Default_RecyclerViewAdapter;
import com.xcash.base.recyclerviewlibrary.models.ViewItem;
import com.xcash.base.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
import com.xcash.utils.CoroutineHelper;
import com.xcash.utils.database.AppDatabase;
import com.xcash.utils.database.entity.TransactionInfo;
import com.xcash.utils.database.entity.Wallet;
import com.xcash.wallet.R;
import com.xcash.wallet.TheApplication;
import com.xcash.wallet.TransactionDetailsActivity;
import com.xcash.wallet.uihelp.ActivityHelp;
import com.xcash.wallet.uihelp.BaseRecyclerViewFromFrameLayoutHelp;

import java.util.ArrayList;
import java.util.List;

public class WalletRunningActivity_Fragment_Default extends BaseFragment {

    private Wallet wallet;
    private String transaction_type;
    private boolean need_auto_refresh;

    private View view;
    private BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout;

    private boolean alreadyInitUi = true;
    private CoroutineHelper coroutineHelper = new CoroutineHelper();
    private Transaction_Default_RecyclerViewAdapter transaction_Default_RecyclerViewAdapter;
    public int selectPosition = -1;
    public boolean getInitSuccess = false;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_wallet_running_fragment_default, container, false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            wallet = (Wallet) bundle.getSerializable(ActivityHelp.WALLET_KEY);
            transaction_type = bundle.getString(ActivityHelp.TRANSACTION_TYPE_KEY);
            need_auto_refresh = bundle.getBoolean(ActivityHelp.NEED_AUTO_REFRESH_KEY_KEY, false);
        }
        initAll();
        return view;
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    @Override
    protected void initUi() {
        baseRecyclerViewFromFrameLayout = (BaseRecyclerViewFromFrameLayout) view.findViewById(R.id.baseRecyclerViewFromFrameLayout);
        initBaseRecyclerViewFromFrameLayout();
        alreadyInitUi = true;
    }

    @Override
    protected void initConfigUi() {
        BaseRecyclerViewFromFrameLayoutHelp.setEmptyTransactionTips(getBaseActivity(), baseRecyclerViewFromFrameLayout);
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
                loadTransactionInfos();
            }
        };
        baseRecyclerViewFromFrameLayout.setOnRefreshListener(onRefreshListener);
        if (need_auto_refresh) {
            baseRecyclerViewFromFrameLayout.autoRefresh(handler, TheApplication.AUTOREFRESHDELAY);
        }
    }

    private void initOrRefreshAdapter(List<ViewItem> viewItems) {
        if (viewItems == null) {
            viewItems = new ArrayList<ViewItem>();
        }
        if (transaction_Default_RecyclerViewAdapter == null) {
            transaction_Default_RecyclerViewAdapter = new Transaction_Default_RecyclerViewAdapter(getBaseActivity(), baseRecyclerViewFromFrameLayout.getRecyclerView(), viewItems);
            transaction_Default_RecyclerViewAdapter.setOnTransactionDefaultListener(new Transaction_Default_RecyclerViewAdapter.OnTransactionDefaultListener() {
                @Override
                public void onItemSelect(TransactionInfo transactionInfo) {
                    Intent intent = new Intent(getBaseActivity(),
                            TransactionDetailsActivity.class);
                    intent.putExtra(ActivityHelp.WALLET_KEY, wallet);
                    intent.putExtra(ActivityHelp.TRANSACTION_INFO_KEY, transactionInfo);
                    getBaseActivity().startActivity(intent);
                }
            });
            baseRecyclerViewFromFrameLayout.setAdapter(transaction_Default_RecyclerViewAdapter);
        } else {
            TheApplication.replaceAllFormBaseRecyclerViewAdapter(transaction_Default_RecyclerViewAdapter, viewItems, baseRecyclerViewFromFrameLayout.getRecyclerView());
        }
    }

    private void loadTransactionInfos() {
        if (wallet == null) {
            return;
        }
        coroutineHelper.launch(new CoroutineHelper.OnCoroutineListener<List<TransactionInfo>>() {
            @Override
            public List<TransactionInfo> runOnIo() {
                List<TransactionInfo> transactionInfos = null;
                try {
                    if (transaction_type == null || transaction_type.equals(ActivityHelp.TRANSACTION_TYPE_ALL)) {
                        transactionInfos = AppDatabase.getInstance().transactionInfoDao().loadTransactionInfosByWalletId(wallet.getSymbol(), wallet.getId());
                    } else if (transaction_type.equals(ActivityHelp.TRANSACTION_TYPE_RECEIVE)) {
                        transactionInfos = AppDatabase.getInstance().transactionInfoDao().loadTransactionInfosByWalletIdAndDirection(wallet.getSymbol(), wallet.getId(), 0);
                    } else if (transaction_type.equals(ActivityHelp.TRANSACTION_TYPE_SEND)) {
                        transactionInfos = AppDatabase.getInstance().transactionInfoDao().loadTransactionInfosByWalletIdAndDirection(wallet.getSymbol(), wallet.getId(), 1);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return transactionInfos;
            }

            @Override
            public void overRunOnMain(List<TransactionInfo> transactionInfos) {
                List<ViewItem> viewItems = new ArrayList<ViewItem>();
                if (transactionInfos != null) {
                    for (int i = 0; i < transactionInfos.size(); i++) {
                        TransactionInfo transactionInfo = transactionInfos.get(i);
                        ViewItem viewItem = new ViewItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1, transactionInfo);
                        viewItems.add(viewItem);
                    }
                }
                getInitSuccess = true;
                initOrRefreshAdapter(viewItems);
            }
        });
    }

    public int getSelectPosition() {
        return selectPosition;
    }

    public void setSelectPosition(int selectPosition) {
        this.selectPosition = selectPosition;
    }

    public void doRefresh() {
        if (alreadyInitUi) {
            baseRecyclerViewFromFrameLayout.autoRefresh(handler, TheApplication.AUTOREFRESHDELAY);
        }
    }

    public void doReset() {
        if (alreadyInitUi) {
            getInitSuccess = false;
            baseRecyclerViewFromFrameLayout.reset();
        }
    }

    public void doRefreshWhenViewPagerSelect() {
        if (alreadyInitUi && !getInitSuccess) {
            baseRecyclerViewFromFrameLayout.autoRefresh(handler, TheApplication.AUTOREFRESHDELAY);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        coroutineHelper.onDestroy();
    }

}
