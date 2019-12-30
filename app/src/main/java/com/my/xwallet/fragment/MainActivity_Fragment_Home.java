/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.xwallet.fragment;

import android.content.Intent;
import android.content.res.TypedArray;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.my.adapters.recyclerviewadapter.Transaction_Default_RecyclerViewAdapter;
import com.my.base.BaseActivity;
import com.my.base.recyclerviewlibrary.models.ViewItem;
import com.my.base.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
import com.my.utils.ClipboardTool;
import com.my.utils.CoroutineHelper;
import com.my.utils.database.AppDatabase;
import com.my.utils.database.entity.TransactionInfo;
import com.my.utils.database.entity.Wallet;
import com.my.xwallet.MainActivity;
import com.my.xwallet.R;
import com.my.xwallet.TheApplication;
import com.my.xwallet.TransactionDetailsActivity;
import com.my.xwallet.aidl.OnNormalListener;
import com.my.xwallet.aidl.WalletOperateManager;
import com.my.xwallet.uihelp.ActivityHelp;
import com.my.xwallet.uihelp.BaseRecyclerViewFromFrameLayoutHelp;
import com.my.xwallet.uihelp.PopupWindowHelp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_Fragment_Home extends BaseFragment {

    private View view;
    private int mainColorText;
    private RelativeLayout relativeLayoutRoot;
    private RelativeLayout relativeLayoutContent;
    private ImageView imageViewMenu;
    private ImageView imageViewLock;
    private TextView textViewTitle;
    private CardView cardView;
    private RelativeLayout relativeLayoutWalletTop;
    private RelativeLayout relativeLayoutWalletDetails;
    private ImageView imageViewWalletTopArrow;
    private TextView textViewWalletTopAmount;
    private TextView textViewAmount;
    private TextView textViewUnlockedAmount;
    private TextView textViewSynchronizeStatus;
    private ProgressBar progressSynchronize;
    private RelativeLayout relativeLayoutAddress;
    private TextView textViewAddress;
    private LinearLayout linearLayoutTransactionContent;
    private RelativeLayout relativeLayoutTransactionInfo;
    private LinearLayout linearLayoutTransactionDetails;
    private BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout;

    private boolean alreadyInitUi = true;
    private View.OnClickListener onClickListener;
    private CoroutineHelper coroutineHelper = new CoroutineHelper();
    private Transaction_Default_RecyclerViewAdapter transaction_Default_RecyclerViewAdapter;
    private Wallet activeWallet;
    private int walletId;
    private boolean isExpand = true;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_content_fragment_home, container, false);
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
        relativeLayoutRoot = (RelativeLayout) view.findViewById(R.id.relativeLayoutRoot);
        relativeLayoutContent = (RelativeLayout) view.findViewById(R.id.relativeLayoutContent);
        imageViewMenu = (ImageView) view.findViewById(R.id.imageViewMenu);
        imageViewLock = (ImageView) view.findViewById(R.id.imageViewLock);
        textViewTitle = (TextView) view.findViewById(R.id.textViewTitle);
        cardView = (CardView) view.findViewById(R.id.cardView);
        relativeLayoutWalletTop = (RelativeLayout) view.findViewById(R.id.relativeLayoutWalletTop);
        relativeLayoutWalletDetails = (RelativeLayout) view.findViewById(R.id.relativeLayoutWalletDetails);
        imageViewWalletTopArrow = (ImageView) view.findViewById(R.id.imageViewWalletTopArrow);
        textViewWalletTopAmount = (TextView) view.findViewById(R.id.textViewWalletTopAmount);
        textViewAmount = (TextView) view.findViewById(R.id.textViewAmount);
        textViewUnlockedAmount = (TextView) view.findViewById(R.id.textViewUnlockedAmount);
        textViewSynchronizeStatus = (TextView) view.findViewById(R.id.textViewSynchronizeStatus);
        progressSynchronize = (ProgressBar) view.findViewById(R.id.progressSynchronize);
        relativeLayoutAddress = (RelativeLayout) view.findViewById(R.id.relativeLayoutAddress);
        textViewAddress = (TextView) view.findViewById(R.id.textViewAddress);
        linearLayoutTransactionContent = (LinearLayout) view.findViewById(R.id.linearLayoutTransactionContent);
        relativeLayoutTransactionInfo= (RelativeLayout) view.findViewById(R.id.relativeLayoutTransactionInfo);
        linearLayoutTransactionDetails = (LinearLayout) view.findViewById(R.id.linearLayoutTransactionDetails);
        baseRecyclerViewFromFrameLayout = (BaseRecyclerViewFromFrameLayout) view.findViewById(R.id.baseRecyclerViewFromFrameLayout);

        initBaseRecyclerViewFromFrameLayout();
        alreadyInitUi = true;
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        adaptationStatusBar(relativeLayoutContent);
        cardView.setVisibility(View.GONE);
        linearLayoutTransactionContent.setVisibility(View.GONE);
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
                    case R.id.imageViewLock:
                        if (imageViewLock.getTag() != null && (boolean) imageViewLock.getTag()) {
                            closeActiveWallet(imageViewLock);
                        }else {
                            ((MainActivity) getBaseActivity()).showPassword(cardView, MainActivity.TYPE_SHOW_WALLET_DETAILS);
                        }
                        break;
                    case R.id.cardView:
                    case R.id.linearLayoutTransactionDetails:
                        ((MainActivity) getBaseActivity()).showPassword(cardView, MainActivity.TYPE_SHOW_WALLET_DETAILS);
                        break;
                    case R.id.imageViewWalletTopArrow:
                        if (isExpand) {
                            relativeLayoutWalletDetails.setVisibility(View.GONE);
                            textViewWalletTopAmount.setVisibility(View.VISIBLE);
                            imageViewWalletTopArrow.setImageResource(R.mipmap.activity_main_content_fragment_home_top_item_arrow_down);
                            isExpand = false;
                        } else {
                            relativeLayoutWalletDetails.setVisibility(View.VISIBLE);
                            textViewWalletTopAmount.setVisibility(View.GONE);
                            imageViewWalletTopArrow.setImageResource(R.mipmap.activity_main_content_fragment_home_top_item_arrow_up);
                            isExpand = true;
                        }
                        break;
                    case R.id.relativeLayoutAddress:
                        ClipboardTool.copyToClipboard(getBaseActivity(), textViewAddress.getText().toString());
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewMenu.setOnClickListener(onClickListener);
        imageViewLock.setOnClickListener(onClickListener);
        cardView.setOnClickListener(onClickListener);
        linearLayoutTransactionDetails.setOnClickListener(onClickListener);
        imageViewWalletTopArrow.setOnClickListener(onClickListener);
        relativeLayoutAddress.setOnClickListener(onClickListener);
    }

    private void initOrRefreshAdapter(List<ViewItem> viewItems) {
        if (viewItems == null) {
            viewItems = new ArrayList<ViewItem>();
        }
        if (viewItems.size()==0){
            relativeLayoutTransactionInfo.setVisibility(View.GONE);
        }else {
            relativeLayoutTransactionInfo.setVisibility(View.VISIBLE);
        }
        if (transaction_Default_RecyclerViewAdapter == null) {
            transaction_Default_RecyclerViewAdapter = new Transaction_Default_RecyclerViewAdapter(getBaseActivity(), baseRecyclerViewFromFrameLayout.getRecyclerView(), viewItems);
            transaction_Default_RecyclerViewAdapter.setOnTransactionDefaultListener(new Transaction_Default_RecyclerViewAdapter.OnTransactionDefaultListener() {
                @Override
                public void onItemSelect(TransactionInfo transactionInfo) {
                    Intent intent = new Intent(getBaseActivity(),
                            TransactionDetailsActivity.class);
                    intent.putExtra(ActivityHelp.WALLET_KEY, activeWallet);
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
        if (activeWallet == null) {
            return;
        }
        coroutineHelper.launch(new CoroutineHelper.OnCoroutineListener<List<TransactionInfo>>() {
            @Override
            public List<TransactionInfo> runOnIo() {
                List<TransactionInfo> transactionInfos = null;
                try {
                    transactionInfos = AppDatabase.getInstance().transactionInfoDao().loadTransactionInfoByWalletId(activeWallet.getSymbol(), activeWallet.getId());
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
                initOrRefreshAdapter(viewItems);
            }
        });
    }

    private void closeActiveWallet(final View unEnabledView) {
        if (activeWallet == null) {
            return;
        }
        PopupWindowHelp.showPopupWindowNormalTips(getBaseActivity(), relativeLayoutRoot, unEnabledView, getString(R.string.activity_main_home_close_active_wallet_tips), new PopupWindowHelp.OnShowPopupWindowNormalTipsListener() {
            @Override
            public void okClick(PopupWindow popupWindow, View view) {
                popupWindow.dismiss();
                TheApplication.getTheApplication().getWalletServiceHelper().closeActiveWallet();
                WalletOperateManager walletOperateManager = TheApplication.getTheApplication().getWalletServiceHelper().getWalletOperateManager();
                if (walletOperateManager == null) {
                    return;
                }
                unEnabledView.setEnabled(false);
                try {
                    walletOperateManager.closeWallet(activeWallet.getId(), new OnNormalListener.Stub() {
                        @Override
                        public void onSuccess(final String tips) throws RemoteException {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    unEnabledView.setEnabled(true);
                                    resetLockStatus();
                                }
                            });
                        }

                        @Override
                        public void onError(final String error) throws RemoteException {
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    unEnabledView.setEnabled(true);
                                    BaseActivity.showShortToast(getBaseActivity(), getString(R.string.close_active_wallet_error_tips));
                                }
                            });
                        }
                    });
                } catch (RemoteException e) {
                    e.printStackTrace();
                    unEnabledView.setEnabled(true);
                }
            }
        });
    }

    public void showActiveWallet(Wallet wallet) {
        if (wallet == null) {
            return;
        }
        if (alreadyInitUi) {
            resetLockStatus();
            walletId = wallet.getId();
            textViewTitle.setText(wallet.getSymbol());
            textViewAddress.setText(wallet.getAddress());
            activeWallet = wallet;
        }
    }

    public void resetLockStatus() {
        if (alreadyInitUi) {
            cardView.setVisibility(View.VISIBLE);
            imageViewLock.setImageResource(R.mipmap.activity_main_content_fragment_home_top_item_lock);
            imageViewLock.setTag(false);
            linearLayoutTransactionContent.setVisibility(View.GONE);
            progressSynchronize.setVisibility(View.GONE);
            textViewUnlockedAmount.setVisibility(View.GONE);
            progressSynchronize.setIndeterminate(true);
            textViewWalletTopAmount.setText(getString(R.string.number_amount_0_text));
            textViewAmount.setText(getString(R.string.number_amount_0_text));
            textViewUnlockedAmount.setText(getString(R.string.number_amount_0_text));
            textViewSynchronizeStatus.setText(getString(R.string.activity_main_content_fragment_home_top_item_textViewSynchronizeStatus_text));
            textViewSynchronizeStatus.setTextColor(mainColorText);
            baseRecyclerViewFromFrameLayout.reset();
        }
    }

    public void unLock() {
        if (alreadyInitUi) {
            setViewUnLockStatus();
            doRefresh();
        }
    }

    public void setViewUnLockStatus() {
        if (alreadyInitUi) {
            imageViewLock.setImageResource(R.mipmap.activity_main_content_fragment_home_top_item_unlock);
            imageViewLock.setTag(true);
            linearLayoutTransactionContent.setVisibility(View.VISIBLE);
        }
    }

    public void reset() {
        if (alreadyInitUi) {
            textViewUnlockedAmount.setVisibility(View.GONE);
            progressSynchronize.setVisibility(View.GONE);
            progressSynchronize.setIndeterminate(true);
            textViewWalletTopAmount.setText(getString(R.string.number_amount_0_text));
            textViewAmount.setText(getString(R.string.number_amount_0_text));
            textViewUnlockedAmount.setText(getString(R.string.number_amount_0_text));
            textViewSynchronizeStatus.setText(getString(R.string.activity_wallet_running_textViewSynchronizeStatus_text));
            textViewSynchronizeStatus.setTextColor(mainColorText);
        }
    }

    public void beginLoadWallet(int walletId) {
        if (this.walletId == walletId) {
            reset();
        }
    }

    public void showSynchronizeStatusError(int walletId, String error) {
        if (this.walletId == walletId) {
            if (alreadyInitUi) {
                progressSynchronize.setVisibility(View.GONE);
                textViewSynchronizeStatus.setText(error);
                textViewSynchronizeStatus.setTextColor(ContextCompat.getColor(getBaseActivity(), R.color.textView_error));
            }
        }
    }

    public void synchronizeStatusSuccess(int walletId) {
        if (this.walletId == walletId) {
            if (alreadyInitUi) {
                progressSynchronize.setVisibility(View.VISIBLE);
            }
        }
    }

    public void refreshBalance(int walletId, String balance, String unlockedBalance) {
        if (this.walletId == walletId) {
            if (alreadyInitUi) {
                textViewWalletTopAmount.setText(balance);
                textViewAmount.setText(balance);
                if (balance != null && unlockedBalance != null && !balance.equals(unlockedBalance)) {
                    textViewUnlockedAmount.setVisibility(View.VISIBLE);
                    textViewUnlockedAmount.setText(unlockedBalance + getString(R.string.activity_wallet_running_unlocked_tips));
                } else {
                    textViewUnlockedAmount.setVisibility(View.GONE);
                }
                setViewUnLockStatus();
            }
        }
    }

    public void showBlockProgress(int walletId, boolean result, long blockChainHeight, long daemonHeight, int progress) {
        if (this.walletId == walletId) {
            if (alreadyInitUi) {
                progressSynchronize.setVisibility(View.VISIBLE);
                textViewSynchronizeStatus.setTextColor(mainColorText);
                progressSynchronize.setIndeterminate(false);
                if (result) {
                    textViewSynchronizeStatus.setText(getString(R.string.activity_wallet_running_SynchronizedSuccess_tips));
                    progressSynchronize.setProgress(100);
                } else {
                    textViewSynchronizeStatus.setText(getString(R.string.activity_wallet_running_leaveDistance_tips) + blockChainHeight + "/" + daemonHeight);
                    progressSynchronize.setProgress(progress);
                }
            }
        }
    }

    public void refreshTransaction(int walletId) {
        if (this.walletId == walletId) {
            if (alreadyInitUi) {
                doRefresh();
            }
        }
    }

    public void closeWallet(int walletId) {
        if (this.walletId == walletId) {
            if (alreadyInitUi) {
                resetLockStatus();
            }
        }
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
