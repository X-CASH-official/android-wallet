/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.xwallet;


import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.RemoteException;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.navigation.NavigationView;
import com.my.base.BaseActivity;
import com.my.base.utils.ActivityManager;
import com.my.utils.CoroutineHelper;
import com.my.utils.WalletServiceHelper;
import com.my.utils.database.AppDatabase;
import com.my.utils.database.entity.Wallet;
import com.my.xwallet.aidl.OnNormalListener;
import com.my.xwallet.aidl.WalletOperateManager;
import com.my.xwallet.fragment.MainActivity_Fragment_Find;
import com.my.xwallet.fragment.MainActivity_Fragment_Home;
import com.my.xwallet.fragment.MainActivity_Fragment_Wallet;
import com.my.xwallet.uihelp.ActivityHelp;
import com.my.xwallet.uihelp.ColorHelp;
import com.my.xwallet.uihelp.PopupWindowHelp;

public class MainActivity extends NewBaseActivity {

    public static final int TYPE_SHOW_WALLET_DETAILS = 1;
    public static int TYPE_CHOOSE_NODE = 2;

    private final String FRAGMENT_HOME = "mainActivity_Fragment_Home";
    private final String FRAGMENT_FIND = "mainActivity_Fragment_Find";
    private final String FRAGMENT_Wallet = "mainActivity_Fragment_Wallet";
    private final String NOWSELECT_KEY = "nowSelect_key";

    private FragmentManager fragmentManager;
    private MainActivity_Fragment_Home mainActivity_Fragment_Home;
    private MainActivity_Fragment_Find mainActivity_Fragment_Find;
    private MainActivity_Fragment_Wallet mainActivity_Fragment_Wallet;

    private int nowSelect = -1;
    private long exitTime = 0;
    private int colorPrimary;
    private int mainColorText;

    private LinearLayout linearLayoutHomeTab;
    private LinearLayout linearLayoutFindTab;
    private LinearLayout linearLayoutWalletTab;
    private ImageView imageViewHomeTab;
    private ImageView imageViewFindTab;
    private ImageView imageViewWalletTab;
    private TextView textViewHomeTab;
    private TextView textViewFindTab;
    private TextView textViewWalletTab;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private View activity_main_navigationview;
    private FrameLayout frameLayoutActiveWallet;
    private TextView textViewWalletName;
    private TextView textViewAddress;
    private RelativeLayout relativeLayoutWallet;
    private RelativeLayout relativeLayoutAddress;
    private RelativeLayout relativeLayoutNodeSetting;
    private RelativeLayout relativeLayoutLanguageSetting;
    private RelativeLayout relativeLayoutContactUs;
    private RelativeLayout relativeLayoutAboutUs;
    private TextView textViewVersion;

    private View.OnClickListener onClickListener;
    private CoroutineHelper coroutineHelper = new CoroutineHelper();
    private Wallet wallet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        int selectIndex = 0;
        if (savedInstanceState != null) {
            selectIndex = savedInstanceState.getInt(NOWSELECT_KEY);
        }
        fragmentManager = getSupportFragmentManager();
        mainActivity_Fragment_Home = (MainActivity_Fragment_Home) fragmentManager.findFragmentByTag(FRAGMENT_HOME);
        mainActivity_Fragment_Find = (MainActivity_Fragment_Find) fragmentManager.findFragmentByTag(FRAGMENT_FIND);
        mainActivity_Fragment_Wallet = (MainActivity_Fragment_Wallet) fragmentManager.findFragmentByTag(FRAGMENT_Wallet);
        setWindowType(1);
        initAll();
        selectFragment(selectIndex);
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
        linearLayoutHomeTab = (LinearLayout) findViewById(R.id.linearLayoutHomeTab);
        linearLayoutFindTab = (LinearLayout) findViewById(R.id.linearLayoutFindTab);
        linearLayoutWalletTab = (LinearLayout) findViewById(R.id.linearLayoutWalletTab);
        imageViewHomeTab = (ImageView) findViewById(R.id.imageViewHomeTab);
        imageViewFindTab = (ImageView) findViewById(R.id.imageViewFindTab);
        imageViewWalletTab = (ImageView) findViewById(R.id.imageViewWalletTab);
        textViewHomeTab = (TextView) findViewById(R.id.textViewHomeTab);
        textViewFindTab = (TextView) findViewById(R.id.textViewFindTab);
        textViewWalletTab = (TextView) findViewById(R.id.textViewWalletTab);
        drawerLayout = (DrawerLayout) findViewById(R.id.drawerLayout);
        navigationView = (NavigationView) findViewById(R.id.navigationView);
        activity_main_navigationview = navigationView.inflateHeaderView(R.layout.activity_main_navigationview);
        frameLayoutActiveWallet = (FrameLayout) activity_main_navigationview.findViewById(R.id.frameLayoutActiveWallet);
        textViewWalletName = (TextView) activity_main_navigationview.findViewById(R.id.textViewWalletName);
        textViewAddress = (TextView) activity_main_navigationview.findViewById(R.id.textViewAddress);
        relativeLayoutWallet = (RelativeLayout) activity_main_navigationview.findViewById(R.id.relativeLayoutWallet);
        relativeLayoutAddress = (RelativeLayout) activity_main_navigationview.findViewById(R.id.relativeLayoutAddress);
        relativeLayoutNodeSetting = (RelativeLayout) activity_main_navigationview.findViewById(R.id.relativeLayoutNodeSetting);
        relativeLayoutLanguageSetting = (RelativeLayout) activity_main_navigationview.findViewById(R.id.relativeLayoutLanguageSetting);
        relativeLayoutContactUs = (RelativeLayout) activity_main_navigationview.findViewById(R.id.relativeLayoutContactUs);
        relativeLayoutAboutUs = (RelativeLayout) activity_main_navigationview.findViewById(R.id.relativeLayoutAboutUs);
        textViewVersion = (TextView) activity_main_navigationview.findViewById(R.id.textViewVersion);
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        adaptationStatusBar(frameLayoutActiveWallet);
        TheApplication.killAllActivityExceptMe(activityKey);
        //  textViewVersion.setText("v"+MachineInformationTool.getVersionName(MainActivity.this));
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {
        loadActiveWallet();
    }

    private void onClickListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.linearLayoutHomeTab:
                        selectFragment(0);
                        break;
                    case R.id.linearLayoutFindTab:
                        selectFragment(1);
                        break;
                    case R.id.linearLayoutWalletTab:
                        selectFragment(2);
                        break;
                    case R.id.frameLayoutActiveWallet:
                        showPassword(frameLayoutActiveWallet, TYPE_SHOW_WALLET_DETAILS);
                        break;
                    case R.id.relativeLayoutWallet:
                        Intent intent2 = new Intent(MainActivity.this,
                                WalletManagerActivity.class);
                        startActivity(intent2);
                        break;
                    case R.id.relativeLayoutAddress:
                        Intent intent3 = new Intent(MainActivity.this,
                                AddressManagerActivity.class);
                        startActivity(intent3);
                        break;
                    case R.id.relativeLayoutNodeSetting:
                        showPassword(relativeLayoutNodeSetting, TYPE_CHOOSE_NODE);
                        break;
                    case R.id.relativeLayoutLanguageSetting:
                        Intent intent4 = new Intent(MainActivity.this,
                                LanguageSettingActivity.class);
                        startActivity(intent4);
                        break;
                    case R.id.relativeLayoutContactUs:
                        Intent intent5 = new Intent(MainActivity.this,
                                WebViewActivity.class);
                        intent5.putExtra(ActivityHelp.REQUEST_TITLE_KEY, getString(R.string.activity_main_navigationview_textViewContactUs_text));
                        intent5.putExtra(ActivityHelp.REQUEST_URL_KEY, getString(R.string.contact_us_url));
                        startActivity(intent5);
                        break;
                    case R.id.relativeLayoutAboutUs:
                        Intent intent6 = new Intent(MainActivity.this,
                                AboutUsActivity.class);
                        startActivity(intent6);
                        break;
                    default:
                        break;
                }
            }
        };
        linearLayoutHomeTab.setOnClickListener(onClickListener);
        linearLayoutFindTab.setOnClickListener(onClickListener);
        linearLayoutWalletTab.setOnClickListener(onClickListener);
        frameLayoutActiveWallet.setOnClickListener(onClickListener);
        relativeLayoutWallet.setOnClickListener(onClickListener);
        relativeLayoutAddress.setOnClickListener(onClickListener);
        relativeLayoutNodeSetting.setOnClickListener(onClickListener);
        relativeLayoutLanguageSetting.setOnClickListener(onClickListener);
        relativeLayoutContactUs.setOnClickListener(onClickListener);
        relativeLayoutAboutUs.setOnClickListener(onClickListener);
    }

    private void selectFragment(int index) {
        if (nowSelect != index) {
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            hideFragments(fragmentTransaction);
            switch (index) {
                case 0:
                    if (mainActivity_Fragment_Home == null) {
                        mainActivity_Fragment_Home = new MainActivity_Fragment_Home();
                        fragmentTransaction.add(R.id.frameLayoutContainer, mainActivity_Fragment_Home, FRAGMENT_HOME);
                    } else {
                        fragmentTransaction.show(mainActivity_Fragment_Home);
                    }
                    break;
                case 1:
                    if (mainActivity_Fragment_Find == null) {
                        mainActivity_Fragment_Find = new MainActivity_Fragment_Find();
                        fragmentTransaction.add(R.id.frameLayoutContainer, mainActivity_Fragment_Find, FRAGMENT_FIND);
                    } else {
                        fragmentTransaction.show(mainActivity_Fragment_Find);
                    }
                    break;
                case 2:
                    if (mainActivity_Fragment_Wallet == null) {
                        mainActivity_Fragment_Wallet = new MainActivity_Fragment_Wallet();
                        fragmentTransaction.add(R.id.frameLayoutContainer, mainActivity_Fragment_Wallet, FRAGMENT_Wallet);
                    } else {
                        fragmentTransaction.show(mainActivity_Fragment_Wallet);
                    }
                    break;
                default:
                    break;
            }
            fragmentTransaction.commitAllowingStateLoss();
            selectBottomTab(index);
            nowSelect = index;
        }
    }

    private void hideFragments(FragmentTransaction fragmentTransaction) {
        if (mainActivity_Fragment_Home != null) {
            fragmentTransaction.hide(mainActivity_Fragment_Home);
        }
        if (mainActivity_Fragment_Find != null) {
            fragmentTransaction.hide(mainActivity_Fragment_Find);
        }
        if (mainActivity_Fragment_Wallet != null) {
            fragmentTransaction.hide(mainActivity_Fragment_Wallet);
        }
    }

    private void selectBottomTab(int index) {
        Drawable drawableHomeUnSelect = getResources().getDrawable(R.mipmap.activity_main_home_unselect);
        Drawable drawableHomeSelect = getResources().getDrawable(R.mipmap.activity_main_home_select);
        Drawable drawableFindUnSelect = getResources().getDrawable(R.mipmap.activity_main_find_unselect);
        Drawable drawableFindSelect = getResources().getDrawable(R.mipmap.activity_main_find_select);
        Drawable drawableWalletUnSelect = getResources().getDrawable(R.mipmap.activity_main_wallet_unselect);
        Drawable drawableWalletSelect = getResources().getDrawable(R.mipmap.activity_main_wallet_select);
        int unSelectColor = mainColorText;
        int selectColor = colorPrimary;
        imageViewHomeTab.setImageDrawable(drawableHomeUnSelect);
        ColorHelp.setImageViewDrawableTint(imageViewHomeTab, drawableHomeUnSelect, unSelectColor);
        textViewHomeTab.setTextColor(unSelectColor);
        ColorHelp.setImageViewDrawableTint(imageViewFindTab, drawableFindUnSelect, unSelectColor);
        textViewFindTab.setTextColor(unSelectColor);
        ColorHelp.setImageViewDrawableTint(imageViewWalletTab, drawableWalletUnSelect, unSelectColor);
        textViewWalletTab.setTextColor(unSelectColor);
        if (index == 0) {
            ColorHelp.setImageViewDrawableTint(imageViewHomeTab, drawableHomeSelect, selectColor);
            textViewHomeTab.setTextColor(selectColor);
        } else if (index == 1) {
            ColorHelp.setImageViewDrawableTint(imageViewFindTab, drawableFindSelect, selectColor);
            textViewFindTab.setTextColor(selectColor);
        } else if (index == 2) {
            ColorHelp.setImageViewDrawableTint(imageViewWalletTab, drawableWalletSelect, selectColor);
            textViewWalletTab.setTextColor(selectColor);
        }
        nowSelect = index;
    }

    private void loadActiveWallet() {
        coroutineHelper.launch(new CoroutineHelper.OnCoroutineListener<Wallet>() {
            @Override
            public Wallet runOnIo() {
                Wallet wallet = null;
                try {
                    wallet = AppDatabase.getInstance().walletDao().loadActiveWallet();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return wallet;
            }

            @Override
            public void overRunOnMain(Wallet wallet) {
                if (wallet == null) {
                    BaseActivity.showShortToast(MainActivity.this, getString(R.string.activity_create_wallet_getActiveWalletError_tips));
                } else {
                    MainActivity.this.wallet = wallet;
                    textViewWalletName.setText(wallet.getName());
                    textViewAddress.setText(wallet.getAddress());
                    if (mainActivity_Fragment_Home != null) {
                        mainActivity_Fragment_Home.showActiveWallet(wallet);
                    }
                }
            }
        });
    }

    private void beginLoadWallet(int walletId) {
        if (mainActivity_Fragment_Home != null) {
            mainActivity_Fragment_Home.beginLoadWallet(walletId);
        }
    }

    private void showSynchronizeStatusError(int walletId, String error) {
        if (mainActivity_Fragment_Home != null) {
            mainActivity_Fragment_Home.showSynchronizeStatusError(walletId, error);
        }
    }

    private void synchronizeStatusSuccess(int walletId) {
        if (mainActivity_Fragment_Home != null) {
            mainActivity_Fragment_Home.synchronizeStatusSuccess(walletId);
        }
    }

    private void refreshBalance(int walletId, String balance, String unlockedBalance) {
        if (mainActivity_Fragment_Home != null) {
            if (wallet != null) {
                wallet.setBalance(balance);
                wallet.setUnlockedBalance(unlockedBalance);
            }
            mainActivity_Fragment_Home.refreshBalance(walletId, balance, unlockedBalance);
        }
    }

    private void showBlockProgress(int walletId, boolean result, long blockChainHeight, long daemonHeight, int progress) {
        if (mainActivity_Fragment_Home != null) {
            mainActivity_Fragment_Home.showBlockProgress(walletId, result, blockChainHeight, daemonHeight, progress);
        }
    }

    private void refreshTransaction(int walletId) {
        if (mainActivity_Fragment_Home != null) {
            mainActivity_Fragment_Home.refreshTransaction(walletId);
        }
    }

    private void closeWallet(int walletId) {
        if (mainActivity_Fragment_Home != null) {
            mainActivity_Fragment_Home.closeWallet(walletId);
        }
    }

    private void refreshWallet() {
        if (mainActivity_Fragment_Wallet != null) {
            mainActivity_Fragment_Wallet.doRefresh();
        }
    }

    private void refreshAddress() {
        if (mainActivity_Fragment_Find != null) {
            mainActivity_Fragment_Find.doRefresh();
        }
    }

    private void closeWallet() {
        if (wallet == null) {
            return;
        }
        WalletOperateManager walletOperateManager = TheApplication.getTheApplication().getWalletServiceHelper().getWalletOperateManager();
        if (walletOperateManager == null) {
            return;
        }
        try {
            walletOperateManager.closeWallet(wallet.getId(), new OnNormalListener.Stub() {
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
                            BaseActivity.showShortToast(MainActivity.this, error);
                        }
                    });
                }
            });
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    public void openDrawer() {
        drawerLayout.openDrawer(navigationView);
    }

    public void showPassword(View view, final int type) {
        if (wallet == null) {
            BaseActivity.showShortToast(MainActivity.this, getString(R.string.wallet_not_exists_tips));
            return;
        }
        PopupWindowHelp.showPopupWindowPasswordTips(MainActivity.this, view.getRootView(), view, new PopupWindowHelp.OnShowPopupWindowPasswordTipsListener() {
            @Override
            public void okClick(final PopupWindow popupWindow, EditText editTextPassword, View view) {
                final String password = editTextPassword.getText().toString();
                popupWindow.dismiss();
                if (password.equals("")) {
                    BaseActivity.showShortToast(MainActivity.this, getString(R.string.password_can_not_empty_tips));
                    return;
                }
                WalletServiceHelper.verifyWalletPasswordOnly(MainActivity.this, wallet.getName(), password, new WalletServiceHelper.OnVerifyWalletPasswordListener() {
                    @Override
                    public void onSuccess(String tips) {
                        if (type == TYPE_SHOW_WALLET_DETAILS) {
                            if (mainActivity_Fragment_Home != null) {
                                mainActivity_Fragment_Home.unLock();
                            }
                            Intent intent = new Intent(MainActivity.this,
                                    WalletRunningActivity.class);
                            intent.putExtra(ActivityHelp.WALLET_KEY, wallet);
                            intent.putExtra(ActivityHelp.SET_WALLET_PASSWORD_KEY, password);
                            startActivity(intent);
                        } else if (type == TYPE_CHOOSE_NODE) {
                            Intent intent = new Intent(MainActivity.this,
                                    NodeManagerActivity.class);
                            intent.putExtra(ActivityHelp.WALLET_KEY, wallet);
                            intent.putExtra(ActivityHelp.SET_WALLET_PASSWORD_KEY, password);
                            startActivity(intent);
                        }
                    }

                    @Override
                    public void onError(String error) {
                        BaseActivity.showShortToast(MainActivity.this, error);
                    }
                });
            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(NOWSELECT_KEY, nowSelect);
        super.onSaveInstanceState(outState);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getAction() == KeyEvent.ACTION_DOWN) {
            if ((System.currentTimeMillis() - exitTime) > 2000) {
                BaseActivity.showShortToast(MainActivity.this, getString(R.string.activity_main_exitTips));
                exitTime = System.currentTimeMillis();
            } else {
                ActivityManager.getInstance().killAllActivity();
                //closeWallet();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        coroutineHelper.onDestroy();
    }

    public static void enterAndRefreshIfActivityExist() {
        MainActivity mainActivity = (MainActivity) TheApplication.getActivityFromActivityManager(MainActivity.class.getName());
        if (mainActivity != null) {
            TheApplication.killAllActivityExceptMe(mainActivity.activityKey);
            mainActivity.loadActiveWallet();
            mainActivity.refreshWallet();
        }
    }

    public static void doRefreshIfActivityExist() {
        MainActivity mainActivity = (MainActivity) TheApplication.getActivityFromActivityManager(MainActivity.class.getName());
        if (mainActivity != null) {
            mainActivity.loadActiveWallet();
            mainActivity.refreshWallet();
        }
    }

    public static void doRefreshAddressIfActivityExist() {
        MainActivity mainActivity = (MainActivity) TheApplication.getActivityFromActivityManager(MainActivity.class.getName());
        if (mainActivity != null) {
            mainActivity.refreshAddress();
        }
    }

    public static void beginLoadWalletIfActivityExist(int walletId) {
        MainActivity mainActivity = (MainActivity) TheApplication.getActivityFromActivityManager(MainActivity.class.getName());
        if (mainActivity != null) {
            mainActivity.beginLoadWallet(walletId);
        }
    }

    public static void showSynchronizeStatusErrorIfActivityExist(int walletId, String error) {
        MainActivity mainActivity = (MainActivity) TheApplication.getActivityFromActivityManager(MainActivity.class.getName());
        if (mainActivity != null) {
            mainActivity.showSynchronizeStatusError(walletId, error);
        }
    }

    public static void synchronizeStatusSuccessIfActivityExist(int walletId) {
        MainActivity mainActivity = (MainActivity) TheApplication.getActivityFromActivityManager(MainActivity.class.getName());
        if (mainActivity != null) {
            mainActivity.synchronizeStatusSuccess(walletId);
        }
    }

    public static void refreshBalanceIfActivityExist(int walletId, String balance, String unlockedBalance) {
        MainActivity mainActivity = (MainActivity) TheApplication.getActivityFromActivityManager(MainActivity.class.getName());
        if (mainActivity != null) {
            mainActivity.refreshBalance(walletId, balance, unlockedBalance);
        }
    }

    public static void showBlockProgressIfActivityExist(int walletId, boolean result, long blockChainHeight, long daemonHeight, int progress) {
        MainActivity mainActivity = (MainActivity) TheApplication.getActivityFromActivityManager(MainActivity.class.getName());
        if (mainActivity != null) {
            mainActivity.showBlockProgress(walletId, result, blockChainHeight, daemonHeight, progress);
        }
    }

    public static void refreshTransactionIfActivityExist(int walletId) {
        MainActivity mainActivity = (MainActivity) TheApplication.getActivityFromActivityManager(MainActivity.class.getName());
        if (mainActivity != null) {
            mainActivity.refreshTransaction(walletId);
        }
    }

    public static void closeWalletIfActivityExist(int walletId) {
        MainActivity mainActivity = (MainActivity) TheApplication.getActivityFromActivityManager(MainActivity.class.getName());
        if (mainActivity != null) {
            mainActivity.closeWallet(walletId);
        }
    }

}
