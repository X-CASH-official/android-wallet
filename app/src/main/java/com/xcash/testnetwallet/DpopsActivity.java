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
 import android.content.pm.ActivityInfo;
 import android.os.Bundle;
 import android.os.Handler;
 import android.os.Message;
 import android.view.View;
 import android.widget.AdapterView;
 import android.widget.BaseAdapter;
 import android.widget.Button;
 import android.widget.EditText;
 import android.widget.FrameLayout;
 import android.widget.ImageView;
 import android.widget.PopupWindow;
 import android.widget.RelativeLayout;
 import android.widget.TextView;

 import androidx.annotation.NonNull;
 import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

 import com.xcash.adapters.listviewadapter.Search_History_ListViewAdapter;
 import com.xcash.adapters.recyclerviewadapter.DpopsActivity_RecyclerViewAdapter;
 import com.xcash.base.BaseActivity;
 import com.xcash.base.recyclerviewlibrary.models.ViewItem;
 import com.xcash.base.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
 import com.xcash.base.utils.TimeTool;
 import com.xcash.models.Delegate;
 import com.xcash.models.local.KeyValueItem;
 import com.xcash.models.local.Setting;
 import com.xcash.models.net.GetDelegates_GsonModel;
 import com.xcash.utils.CoroutineHelper;
 import com.xcash.utils.HttpURLConnectionTool;
 import com.xcash.utils.WalletServiceHelper;
 import com.xcash.utils.database.entity.Wallet;
 import com.xcash.testnetwallet.uihelp.ActivityHelp;
 import com.xcash.testnetwallet.uihelp.PopupWindowHelp;
 import com.xcash.testnetwallet.uihelp.ProgressDialogHelp;

 import java.util.ArrayList;
 import java.util.Calendar;
 import java.util.List;
 import java.util.TimeZone;


 public class DpopsActivity extends NewBaseActivity {

    private Wallet wallet;
    private int view_normal_margin_default;

    private ImageView imageViewBack;
    private TextView textViewTitle;
    private ImageView imageViewRight;
    private RelativeLayout relativeLayoutTarget;
    private EditText editTextTarget;
    private FrameLayout frameLayoutHistory;
    private EditText editTextVoteValue;
    private Button buttonSearch;
    private Button buttonVote;
    private Button buttonRegister;
    private Button buttonUpdate;
    private Button buttonMore;
    private BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout;
    private TextView textViewUtcTime;

    private View.OnClickListener onClickListener;
    private CoroutineHelper coroutineHelper = new CoroutineHelper();
    private DpopsActivity_RecyclerViewAdapter dpopsActivity_RecyclerViewAdapter;
    private String selectSearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dpops);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Intent intent = getIntent();
        wallet = (Wallet) intent.getSerializableExtra(ActivityHelp.WALLET_KEY);
        initAll();
    }

    @Override
    protected void initHandler() {
        handler = new Handler() {
            @Override
            public void handleMessage(@NonNull Message msg) {
                switch (msg.what) {
                    case 0:
                        textViewUtcTime.setText(getString(R.string.activity_dpops_utcTime_tips)+ TimeTool.getUtcTime());
                        handler.sendEmptyMessageDelayed(0, 30000);
                        break;
                    default:
                        break;
                }
                super.handleMessage(msg);
            }
        };
    }


    @Override
    protected void initUi() {
        view_normal_margin_default = getResources().getDimensionPixelSize(R.dimen.view_normal_margin_default);

        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        imageViewRight = (ImageView) findViewById(R.id.imageViewRight);
        relativeLayoutTarget = (RelativeLayout) findViewById(R.id.relativeLayoutTarget);
        editTextTarget = (EditText) findViewById(R.id.editTextTarget);
        frameLayoutHistory = (FrameLayout) findViewById(R.id.frameLayoutHistory);
        editTextVoteValue = (EditText) findViewById(R.id.editTextVoteValue);
        buttonSearch = (Button) findViewById(R.id.buttonSearch);
        buttonVote = (Button) findViewById(R.id.buttonVote);
        buttonRegister = (Button) findViewById(R.id.buttonRegister);
        buttonUpdate = (Button) findViewById(R.id.buttonUpdate);
        buttonMore = (Button) findViewById(R.id.buttonMore);
        baseRecyclerViewFromFrameLayout = (BaseRecyclerViewFromFrameLayout) findViewById(R.id.baseRecyclerViewFromFrameLayout);
        textViewUtcTime = (TextView) findViewById(R.id.textViewUtcTime);

        initBaseRecyclerViewFromFrameLayout();
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewTitle.setText(R.string.activity_dpops_textViewTitle_text);
        selectSearch = getSearchHistorys().get(0);
        editTextTarget.setText(selectSearch);
        TheApplication.setCursorToLast(editTextTarget);
        imageViewRight.setVisibility(View.VISIBLE);
        imageViewRight.setImageResource(R.mipmap.activity_main_home_menu_more);
        handler.sendEmptyMessage(0);
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {

    }

    private void initBaseRecyclerViewFromFrameLayout() {
        TheApplication.setColorSchemeColors(DpopsActivity.this, baseRecyclerViewFromFrameLayout.getSwipeRefreshLayout());
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getDelegates(selectSearch);
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
                    case R.id.imageViewRight:
                        showMore(imageViewRight);
                        break;
                    case R.id.frameLayoutHistory:
                        showHistory(frameLayoutHistory);
                        break;
                    case R.id.buttonSearch:
                        doSearch(buttonSearch);
                        break;
                    case R.id.buttonVote:
                        checkVote(buttonVote);
                        break;
                    case R.id.buttonRegister:
                        Intent intent = new Intent(DpopsActivity.this,
                                DpopsRegisterActivity.class);
                        intent.putExtra(ActivityHelp.WALLET_KEY, wallet);
                        startActivity(intent);
                        break;
                    case R.id.buttonUpdate:
                        Intent intent1 = new Intent(DpopsActivity.this,
                                DpopsUpdateActivity.class);
                        intent1.putExtra(ActivityHelp.WALLET_KEY, wallet);
                        startActivity(intent1);
                        break;
                    case R.id.buttonMore:
                        Intent intent2 = new Intent(DpopsActivity.this,
                                WebViewActivity.class);
                        intent2.putExtra(ActivityHelp.REQUEST_TITLE_KEY, selectSearch);
                        intent2.putExtra(ActivityHelp.REQUEST_URL_KEY, selectSearch);
                        startActivity(intent2);
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        imageViewRight.setOnClickListener(onClickListener);
        frameLayoutHistory.setOnClickListener(onClickListener);
        buttonSearch.setOnClickListener(onClickListener);
        buttonVote.setOnClickListener(onClickListener);
        buttonRegister.setOnClickListener(onClickListener);
        buttonUpdate.setOnClickListener(onClickListener);
        buttonMore.setOnClickListener(onClickListener);
    }

    private void initOrRefreshAdapter(List<ViewItem> viewItems) {
        if (viewItems == null) {
            viewItems = new ArrayList<ViewItem>();
        }
        if (dpopsActivity_RecyclerViewAdapter == null) {
            dpopsActivity_RecyclerViewAdapter = new DpopsActivity_RecyclerViewAdapter(DpopsActivity.this, baseRecyclerViewFromFrameLayout.getRecyclerView(), viewItems);
            dpopsActivity_RecyclerViewAdapter.setOnDpopsListener(new DpopsActivity_RecyclerViewAdapter.OnDpopsListener() {
                @Override
                public void onItemSelect(Delegate delegate) {
                    editTextVoteValue.setText(delegate.getDelegate_name());
                    TheApplication.setCursorToLast(editTextVoteValue);
                }
            });
            baseRecyclerViewFromFrameLayout.setAdapter(dpopsActivity_RecyclerViewAdapter);
        } else {
            TheApplication.replaceAllFormBaseRecyclerViewAdapter(dpopsActivity_RecyclerViewAdapter, viewItems, baseRecyclerViewFromFrameLayout.getRecyclerView());
        }
    }

    private void getDelegates(final String address) {
        coroutineHelper.launch(new CoroutineHelper.OnCoroutineListener<List<Delegate>>() {
            @Override
            public List<Delegate> runOnIo() {
                String result = HttpURLConnectionTool.sendGet(address + "/getdelegates");
                List<Delegate> delegates = null;
                if (result != null) {
                    delegates = GetDelegates_GsonModel.getData(result);
                }
                return delegates;
            }

            @Override
            public void overRunOnMain(List<Delegate> delegates) {
                List<ViewItem> viewItems = new ArrayList<ViewItem>();
                if (delegates != null) {
                    for (int i = 0; i < delegates.size(); i++) {
                        Delegate delegate = delegates.get(i);
                        ViewItem viewItem = new ViewItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1, delegate);
                        viewItems.add(viewItem);
                    }
                }
                initOrRefreshAdapter(viewItems);
            }
        });
    }

    private void showMore(View view) {
        final int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        List<KeyValueItem> keyValueItems = new ArrayList<KeyValueItem>();
        String show_history_tips = getString(R.string.activity_dpops_show_history_tips);
        keyValueItems.add(new KeyValueItem(show_history_tips, show_history_tips));
        PopupWindowHelp.showPopupWindowMenuListViewMore(DpopsActivity.this, view.getRootView(), view, 0, locations[1] + view.getHeight(), keyValueItems, 2, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(DpopsActivity.this,
                            DpopsOperationHistoryActivity.class);
                    intent.putExtra(ActivityHelp.WALLET_KEY, wallet);
                    startActivity(intent);
                }
            }
        });
    }

    private List<String> getSearchHistorys() {
        Setting setting = TheApplication.getSetting();
        List<String> searchHistorys = setting.getSearchHistorys();
        if (searchHistorys == null) {
            searchHistorys = new ArrayList<>();
        }
        if (searchHistorys.size() == 0) {
            searchHistorys.add("dpops-test-delegates.xcash.foundation");
            searchHistorys.add("dpops-test-1.xcash.foundation");
            searchHistorys.add("dpops-test-2.xcash.foundation");
            searchHistorys.add("dpops-test-3.xcash.foundation");
            searchHistorys.add("dpops-test-4.xcash.foundation");
            setting.setSearchHistorys(searchHistorys);
            TheApplication.setAndWriteSetting(setting);
        }
        return searchHistorys;
    }

    private void setSearchHistorys(List<String> searchHistorys) {
        Setting setting = TheApplication.getSetting();
        setting.setSearchHistorys(searchHistorys);
        TheApplication.setAndWriteSetting(setting);
    }

    private void showHistory(View view) {
        final int[] locations = new int[2];
        editTextTarget.getLocationOnScreen(locations);
        final List<String> searchHistorys = getSearchHistorys();
        PopupWindowHelp.showPopupWindowSearchListViewHistory(DpopsActivity.this, view.getRootView(), view, relativeLayoutTarget.getWidth(), view_normal_margin_default, locations[1] + editTextTarget.getHeight(), searchHistorys, 5, new Search_History_ListViewAdapter.OnSearchHistoryListener() {
            @Override
            public void onItemClick(BaseAdapter baseAdapter, AdapterView<?> parent, View view, int position, long id) {
                selectSearch = searchHistorys.get(position);
                editTextTarget.setText(selectSearch);
                TheApplication.setCursorToLast(editTextTarget);
            }

            @Override
            public void onDelete(BaseAdapter baseAdapter, View view, int position) {
                searchHistorys.remove(position);
                setSearchHistorys(searchHistorys);
                baseAdapter.notifyDataSetChanged();
            }
        });
    }

    private void doSearch(final View view) {
        view.setEnabled(false);
        String target = editTextTarget.getText().toString();
        if (target.equals("")) {
            BaseActivity.showShortToast(DpopsActivity.this, getString(R.string.activity_dpops_confirmEmpty_tips));
            return;
        }
        final List<String> searchHistorys = getSearchHistorys();
        int position = -1;
        for (int i = 0; i < searchHistorys.size(); i++) {
            if (target.equals(searchHistorys.get(i))) {
                position = i;
                break;
            }
        }
        selectSearch = target;
        if (position != -1) {
            searchHistorys.remove(position);
            searchHistorys.add(0, target);
        } else {
            searchHistorys.add(0, target);
        }
        setSearchHistorys(searchHistorys);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                view.setEnabled(true);
            }
        }, 800);
        doRefresh();
    }

    private void checkVote(View view) {
        if (wallet == null) {
            return;
        }
        double balance = 0;
        try {
            balance = Double.parseDouble(wallet.getUnlockedBalance());
        } catch (Exception e) {
            e.printStackTrace();
        }
        final String value = editTextVoteValue.getText().toString();
        if (value.equals("")) {
            BaseActivity.showShortToast(DpopsActivity.this, getString(R.string.activity_dpops_checkVoteValue_tips));
            return;
        }
        if (balance <= 0) {
            BaseActivity.showShortToast(DpopsActivity.this, getString(R.string.activity_dpops_checkUnlockedBalance_tips));
            return;
        }

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeZone(TimeZone.getTimeZone("UTC"));
        int minute=calendar.get(Calendar.MINUTE);
        int second=calendar.get(Calendar.SECOND);
        int waitMinute=0;
        if(minute>=4){
            waitMinute=60+3-minute;
        }else{
            if (minute<3){
                waitMinute=3-minute;
            }else{
                waitMinute=0;
                second=0;
            }
        }
        final long delayMillis=waitMinute*60*1000-second*1000;
        final long voteTimestamp=System.currentTimeMillis()+delayMillis;
        final String voteTips=getString(R.string.activity_dpops_waiting_vote_tips)+" "+waitMinute+" "+getString(R.string.activity_dpops_waiting_vote_minute_tips);
        PopupWindowHelp.showPopupWindowCustomTips(DpopsActivity.this, view.getRootView(), view, new PopupWindowHelp.OnShowPopupWindowCustomTipsListener() {
            @Override
            public void initView(final PopupWindow popupWindow, TextView textViewTips, TextView textViewLeft, TextView textViewRight) {
                textViewTips.setText(voteTips);
                textViewLeft.setText(getString(R.string.activity_dpops_auto_waiting_vote_tips));
                textViewRight.setText(getString(R.string.activity_dpops_quick_vote_tips));
                textViewLeft.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        TheApplication.getTheApplication().getWalletServiceHelper().waitToVote(TheApplication.getTheApplication(),value,voteTimestamp,true);
                    }
                });
                textViewRight.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        popupWindow.dismiss();
                        quickToVote(value);
                    }
                });
            }
        });
    }

    private void quickToVote(String value){
        Object[] objects = ProgressDialogHelp.unEnabledView(DpopsActivity.this, null);
        final ProgressDialog progressDialog = (ProgressDialog) objects[0];
        final String progressDialogKey = (String) objects[1];
        TheApplication.getTheApplication().getWalletServiceHelper().quickToVote( value, new WalletServiceHelper.OnVoteListener() {
            @Override
            public void onSuccess(String tips) {
                ProgressDialogHelp.enabledView(DpopsActivity.this, progressDialog, progressDialogKey, null);
            }

            @Override
            public void onError(String error) {
                ProgressDialogHelp.enabledView(DpopsActivity.this, progressDialog, progressDialogKey, null);
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
