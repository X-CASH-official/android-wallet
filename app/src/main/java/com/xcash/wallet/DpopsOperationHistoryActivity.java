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
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xcash.adapters.recyclerviewadapter.DpopsOperationHistoryActivity_RecyclerViewAdapter;
import com.xcash.base.recyclerviewlibrary.models.ViewItem;
import com.xcash.base.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
import com.xcash.utils.CoroutineHelper;
import com.xcash.utils.database.AppDatabase;
import com.xcash.utils.database.entity.OperationHistory;
import com.xcash.utils.database.entity.Wallet;
import com.xcash.wallet.uihelp.ActivityHelp;

import java.util.ArrayList;
import java.util.List;

public class DpopsOperationHistoryActivity extends NewBaseActivity {

    private Wallet wallet;

    private RelativeLayout relativeLayoutRoot;
    private ImageView imageViewBack;
    private TextView textViewTitle;
    private ImageView imageViewRight;
    private BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout;

    private View.OnClickListener onClickListener;
    private CoroutineHelper coroutineHelper = new CoroutineHelper();
    private DpopsOperationHistoryActivity_RecyclerViewAdapter dpopsOperationHistoryActivity_RecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dpops_operation_history);
        Intent intent = getIntent();
        wallet = (Wallet) intent.getSerializableExtra(ActivityHelp.WALLET_KEY);
        initAll();
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    @Override
    protected void initUi() {
        relativeLayoutRoot = (RelativeLayout) findViewById(R.id.relativeLayoutRoot);
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        baseRecyclerViewFromFrameLayout = (BaseRecyclerViewFromFrameLayout) findViewById(R.id.baseRecyclerViewFromFrameLayout);
        initBaseRecyclerViewFromFrameLayout();
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewTitle.setText(R.string.activity_dpops_operation_history_textViewTitle_text);
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {

    }

    private void initBaseRecyclerViewFromFrameLayout() {
        TheApplication.setColorSchemeColors(DpopsOperationHistoryActivity.this, baseRecyclerViewFromFrameLayout.getSwipeRefreshLayout());
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadOperationHistorys();
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
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
    }

    private void initOrRefreshAdapter(List<ViewItem> viewItems) {
        if (viewItems == null) {
            viewItems = new ArrayList<ViewItem>();
        }
        if (dpopsOperationHistoryActivity_RecyclerViewAdapter == null) {
            dpopsOperationHistoryActivity_RecyclerViewAdapter = new DpopsOperationHistoryActivity_RecyclerViewAdapter(DpopsOperationHistoryActivity.this, baseRecyclerViewFromFrameLayout.getRecyclerView(), viewItems);
            dpopsOperationHistoryActivity_RecyclerViewAdapter.setOnDpopsOperationHistory(new DpopsOperationHistoryActivity_RecyclerViewAdapter.OnDpopsOperationHistory() {
                @Override
                public void onItemSelect(OperationHistory operationHistory) {

                }
            });
            baseRecyclerViewFromFrameLayout.setAdapter(dpopsOperationHistoryActivity_RecyclerViewAdapter);
        } else {
            TheApplication.replaceAllFormBaseRecyclerViewAdapter(dpopsOperationHistoryActivity_RecyclerViewAdapter, viewItems, baseRecyclerViewFromFrameLayout.getRecyclerView());
        }
    }

    private void loadOperationHistorys() {
        if (wallet == null) {
            return;
        }
        coroutineHelper.launch(new CoroutineHelper.OnCoroutineListener<List<OperationHistory>>() {
            @Override
            public List<OperationHistory> runOnIo() {
                List<OperationHistory> operationHistorys = null;
                try {
                    operationHistorys = AppDatabase.getInstance().operationHistoryDao().loadOperationHistorysByWalletId(wallet.getId());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return operationHistorys;
            }

            @Override
            public void overRunOnMain(List<OperationHistory> operationHistorys) {
                List<ViewItem> viewItems = new ArrayList<ViewItem>();
                if (operationHistorys != null) {
                    for (int i = 0; i < operationHistorys.size(); i++) {
                        OperationHistory operationHistory = operationHistorys.get(i);
                        ViewItem viewItem = new ViewItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1, operationHistory);
                        viewItems.add(viewItem);
                    }
                }
                initOrRefreshAdapter(viewItems);
            }
        });
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

}
