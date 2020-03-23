/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.wallet;


import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.appbar.AppBarLayout;
import com.xcash.adapters.viewpageradapter.WalletRunningActivity_ViewPagerAdapter;
import com.xcash.utils.ClipboardTool;
import com.xcash.utils.database.entity.Wallet;
import com.xcash.views.tablayout.TabLayout;
import com.xcash.wallet.fragment.WalletRunningActivity_Fragment_Default;
import com.xcash.wallet.uihelp.ActivityHelp;
import com.xcash.wallet.uihelp.ColorHelp;

import java.util.List;

public class WalletRunningActivity extends NewBaseActivity {

    private Wallet wallet;
    private String set_wallet_password;

    private String[] titles;
    private int mainColorHelp;
    private int mainColorText;
    private int editText_normal_hint;

    private AppBarLayout appBarLayout;
    private ImageView imageViewBack;
    private TextView textViewTitle;
    private ImageView imageViewChangeNode;
    private TextView textViewAmount;
    private TextView textViewDpops;
    private TextView textViewUnlockedAmount;
    private TextView textViewSynchronizeStatus;
    private ProgressBar progressSynchronize;
    private RelativeLayout relativeLayoutAddress;
    private TextView textViewAddress;
    private Button buttonSend;
    private Button buttonReceive;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private Drawable drawableNormalBack;
    private Drawable drawableChangeNode;
    private View.OnClickListener onClickListener;
    private ViewPager.OnPageChangeListener onPageChangeListener;
    private WalletRunningActivity_ViewPagerAdapter walletRunningActivity_ViewPagerAdapter;
    private int walletId;
    private boolean pageChanged = false;
    private int currentPosition = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Window statusBar = getWindow();
            statusBar.getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
        setContentView(R.layout.activity_wallet_running);
        Intent intent = getIntent();
        wallet = (Wallet) intent.getSerializableExtra(ActivityHelp.WALLET_KEY);
        set_wallet_password = intent.getStringExtra(ActivityHelp.SET_WALLET_PASSWORD_KEY);
        setWindowType(1);
        initAll();
    }

    @SuppressWarnings("ResourceType")
    private void getTypeArrayColor() {
        TypedArray typedArray = getTheme().obtainStyledAttributes(new int[]{R.attr.mainColorHelp, R.attr.mainColorText});
        try {
            mainColorHelp = typedArray.getColor(0, 0xffffffff);
            mainColorText = typedArray.getColor(1, 0xffffffff);
        } finally {
            typedArray.recycle();
        }
    }

    @Override
    protected void initHandler() {

    }

    @Override
    protected void initUi() {
        titles = getResources().getStringArray(R.array.activity_wallet_running_tabLayout_titles);
        getTypeArrayColor();
        editText_normal_hint = ContextCompat.getColor(WalletRunningActivity.this, R.color.editText_normal_hint);
        appBarLayout = (AppBarLayout) findViewById(R.id.appBarLayout);
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        imageViewChangeNode = (ImageView) findViewById(R.id.imageViewChangeNode);
        textViewAmount = (TextView) findViewById(R.id.textViewAmount);
        textViewDpops = (TextView) findViewById(R.id.textViewDpops);
        textViewUnlockedAmount = (TextView) findViewById(R.id.textViewUnlockedAmount);
        textViewSynchronizeStatus = (TextView) findViewById(R.id.textViewSynchronizeStatus);
        progressSynchronize = (ProgressBar) findViewById(R.id.progressSynchronize);
        relativeLayoutAddress = (RelativeLayout) findViewById(R.id.relativeLayoutAddress);
        textViewAddress = (TextView) findViewById(R.id.textViewAddress);
        buttonSend = (Button) findViewById(R.id.buttonSend);
        buttonReceive = (Button) findViewById(R.id.buttonReceive);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        if (wallet != null) {
            walletId = wallet.getId();
            textViewTitle.setText(wallet.getSymbol());
            textViewAddress.setText(wallet.getAddress());
        }
        walletRunningActivity_ViewPagerAdapter = new WalletRunningActivity_ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, wallet, titles, WalletRunningActivity.this);
        viewPager.setAdapter(walletRunningActivity_ViewPagerAdapter);
        viewPager.setOffscreenPageLimit(3);
        tabLayout.setNeedSwitchAnimation(true);
        tabLayout.setIndicatorWidthWrapContent(true);
        tabLayout.setupWithViewPager(viewPager);
        drawableNormalBack = getResources().getDrawable(R.mipmap.normal_back);
        drawableChangeNode = getResources().getDrawable(R.mipmap.activity_main_navigationview_node_setting);
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                if (Math.abs(verticalOffset) <= appBarLayout.getTotalScrollRange() * 2 / 3) {
                    textViewTitle.setTextColor(mainColorText);
                    ColorHelp.setImageViewDrawableTint(imageViewBack, drawableNormalBack, mainColorText);
                    ColorHelp.setImageViewDrawableTint(imageViewChangeNode, drawableChangeNode, mainColorText);
                } else {
                    textViewTitle.setTextColor(mainColorHelp);
                    ColorHelp.setImageViewDrawableTint(imageViewBack, drawableNormalBack, mainColorHelp);
                    ColorHelp.setImageViewDrawableTint(imageViewChangeNode, drawableChangeNode, mainColorHelp);
                }
            }
        });
        addViewPagerListener();
        reset();
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {
        TheApplication.getTheApplication().getWalletServiceHelper().openWallet(wallet, set_wallet_password, false, null);
    }

    private void onClickListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageViewBack:
                        doBack();
                        break;
                    case R.id.imageViewChangeNode:
                        Intent intent = new Intent(WalletRunningActivity.this,
                                NodeManagerActivity.class);
                        intent.putExtra(ActivityHelp.WALLET_KEY, wallet);
                        startActivity(intent);
                        break;
                    case R.id.textViewDpops:
                        Intent intent1 = new Intent(WalletRunningActivity.this,
                                DpopsActivity.class);
                        intent1.putExtra(ActivityHelp.WALLET_KEY, wallet);
                        startActivity(intent1);
                        break;
                    case R.id.relativeLayoutAddress:
                        ClipboardTool.copyToClipboard(WalletRunningActivity.this, textViewAddress.getText().toString());
                        break;
                    case R.id.buttonSend:
                        Intent intent2 = new Intent(WalletRunningActivity.this,
                                PaymentActivity.class);
                        intent2.putExtra(ActivityHelp.WALLET_KEY, wallet);
                        startActivity(intent2);
                        break;
                    case R.id.buttonReceive:
                        if (wallet!=null){
                            Intent intent3 = new Intent(WalletRunningActivity.this,
                                    ReceiveActivity.class);
                            intent3.putExtra(ActivityHelp.ADDRESS_KEY, wallet.getAddress());
                            startActivity(intent3);
                        }
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        imageViewChangeNode.setOnClickListener(onClickListener);
        textViewDpops.setOnClickListener(onClickListener);
        relativeLayoutAddress.setOnClickListener(onClickListener);
        buttonSend.setOnClickListener(onClickListener);
        buttonReceive.setOnClickListener(onClickListener);
    }

    /**
     * Multiple page switching fluency optimization
     */
    private void addViewPagerListener() {
        onPageChangeListener = new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                currentPosition = position;
                if (!pageChanged && position != 0) {
                    pageChanged = true;
                }
                List<Fragment> fragments = getSupportFragmentManager().getFragments();
                if (fragments == null) {
                    return;
                }
                for (int i = 0; i < fragments.size(); i++) {
                    WalletRunningActivity_Fragment_Default walletRunningActivity_Fragment_Default = (WalletRunningActivity_Fragment_Default) fragments.get(i);
                    walletRunningActivity_Fragment_Default.setSelectPosition(position);
                }
                for (int i = 0; i < fragments.size(); i++) {
                    WalletRunningActivity_Fragment_Default walletRunningActivity_Fragment_Default = (WalletRunningActivity_Fragment_Default) fragments.get(i);
                    if (walletRunningActivity_Fragment_Default != null) {
                        boolean haveFind = false;
                        switch (position) {
                            case 0:
                                if (walletRunningActivity_Fragment_Default.getPosition() == 0) {
                                    walletRunningActivity_Fragment_Default.doRefreshWhenViewPagerSelect();
                                    haveFind = true;
                                }
                                break;
                            case 1:
                                if (walletRunningActivity_Fragment_Default.getPosition() == 1) {
                                    walletRunningActivity_Fragment_Default.doRefreshWhenViewPagerSelect();
                                    haveFind = true;
                                }
                                break;
                            case 2:
                                if (walletRunningActivity_Fragment_Default.getPosition() == 2) {
                                    walletRunningActivity_Fragment_Default.doRefreshWhenViewPagerSelect();
                                    haveFind = true;
                                }
                                break;
                            default:
                                break;
                        }
                        if (haveFind) {
                            break;
                        }
                    }
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        };
        viewPager.addOnPageChangeListener(onPageChangeListener);
    }

    private void reset() {
        textViewDpops.setTextColor(editText_normal_hint);
        buttonSend.setEnabled(false);
        buttonReceive.setEnabled(false);
        textViewUnlockedAmount.setVisibility(View.GONE);
        textViewAmount.setText(getString(R.string.number_amount_0_text));
        textViewUnlockedAmount.setText(getString(R.string.number_amount_0_text));
        textViewSynchronizeStatus.setText(getString(R.string.activity_wallet_running_textViewSynchronizeStatus_text));
        textViewSynchronizeStatus.setTextColor(mainColorText);
        progressSynchronize.setIndeterminate(true);
    }

    private void beginLoadWallet(int walletId) {
        if (this.walletId == walletId) {
            reset();
        }
    }

    private void showSynchronizeStatusError(int walletId, String error) {
        if (this.walletId == walletId) {
            textViewDpops.setTextColor(editText_normal_hint);
            textViewSynchronizeStatus.setText(error);
            textViewSynchronizeStatus.setTextColor(ContextCompat.getColor(WalletRunningActivity.this, R.color.textView_error));
        }
    }

    private void synchronizeStatusSuccess(int walletId) {
        if (this.walletId == walletId) {
            buttonReceive.setEnabled(true);
        }
    }

    private void refreshBalance(int walletId, String balance, String unlockedBalance) {
        if (this.walletId == walletId) {
            if (wallet != null) {
                wallet.setBalance(balance);
                wallet.setUnlockedBalance(unlockedBalance);
            }
            textViewAmount.setText(balance);
            if (balance != null && unlockedBalance != null && !balance.equals(unlockedBalance)) {
                textViewUnlockedAmount.setVisibility(View.VISIBLE);
                textViewUnlockedAmount.setText(unlockedBalance + getString(R.string.activity_wallet_running_unlocked_tips));
            } else {
                textViewUnlockedAmount.setVisibility(View.GONE);
            }
        }
    }

    private void showBlockProgress(int walletId, boolean result, long blockChainHeight, long daemonHeight, int progress) {
        if (this.walletId == walletId) {
            textViewSynchronizeStatus.setTextColor(mainColorText);
            progressSynchronize.setIndeterminate(false);
            buttonReceive.setEnabled(true);
            if (result) {
                textViewSynchronizeStatus.setText(getString(R.string.activity_wallet_running_SynchronizedSuccess_tips));
                progressSynchronize.setProgress(100);
                textViewDpops.setTextColor(mainColorText);
                buttonSend.setEnabled(true);
            } else {
                textViewSynchronizeStatus.setText(getString(R.string.activity_wallet_running_leaveDistance_tips) + blockChainHeight + "/" + daemonHeight);
                progressSynchronize.setProgress(progress);
                textViewDpops.setTextColor(editText_normal_hint);
            }
        }
    }

    public boolean isPageChanged() {
        return pageChanged;
    }

    public void doRefresh(int walletId) {
        if (this.walletId == walletId) {
            List<Fragment> fragments = getSupportFragmentManager().getFragments();
            if (fragments != null) {
                for (int i = 0; i < fragments.size(); i++) {
                    WalletRunningActivity_Fragment_Default walletRunningActivity_Fragment_Default = (WalletRunningActivity_Fragment_Default) fragments.get(i);
                    walletRunningActivity_Fragment_Default.doRefresh();
                }
            }
        }
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

    public static void beginLoadWalletIfActivityExist(int walletId) {
        WalletRunningActivity walletRunningActivity = (WalletRunningActivity) TheApplication.getActivityFromActivityManager(WalletRunningActivity.class.getName());
        if (walletRunningActivity != null) {
            walletRunningActivity.beginLoadWallet(walletId);
        }
    }

    public static void showSynchronizeStatusErrorIfActivityExist(int walletId, String error) {
        WalletRunningActivity walletRunningActivity = (WalletRunningActivity) TheApplication.getActivityFromActivityManager(WalletRunningActivity.class.getName());
        if (walletRunningActivity != null) {
            walletRunningActivity.showSynchronizeStatusError(walletId, error);
        }
    }

    public static void synchronizeStatusSuccessIfActivityExist(int walletId) {
        WalletRunningActivity walletRunningActivity = (WalletRunningActivity) TheApplication.getActivityFromActivityManager(WalletRunningActivity.class.getName());
        if (walletRunningActivity != null) {
            walletRunningActivity.synchronizeStatusSuccess(walletId);
        }
    }

    public static void refreshBalanceIfActivityExist(int walletId, String balance, String unlockedBalance) {
        WalletRunningActivity walletRunningActivity = (WalletRunningActivity) TheApplication.getActivityFromActivityManager(WalletRunningActivity.class.getName());
        if (walletRunningActivity != null) {
            walletRunningActivity.refreshBalance(walletId, balance, unlockedBalance);
        }
    }

    public static void showBlockProgressIfActivityExist(int walletId, boolean result, long blockChainHeight, long daemonHeight, int progress) {
        WalletRunningActivity walletRunningActivity = (WalletRunningActivity) TheApplication.getActivityFromActivityManager(WalletRunningActivity.class.getName());
        if (walletRunningActivity != null) {
            walletRunningActivity.showBlockProgress(walletId, result, blockChainHeight, daemonHeight, progress);
        }
    }

    public static void refreshTransactionIfActivityExist(int walletId) {
        WalletRunningActivity walletRunningActivity = (WalletRunningActivity) TheApplication.getActivityFromActivityManager(WalletRunningActivity.class.getName());
        if (walletRunningActivity != null) {
            walletRunningActivity.doRefresh(walletId);
        }
    }

}
