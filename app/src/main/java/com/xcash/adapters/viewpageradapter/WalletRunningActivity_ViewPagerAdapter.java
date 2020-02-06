/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.adapters.viewpageradapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.xcash.utils.database.entity.Wallet;
import com.xcash.wallet.WalletRunningActivity;
import com.xcash.wallet.fragment.BaseFragment;
import com.xcash.wallet.fragment.WalletRunningActivity_Fragment_Default;
import com.xcash.wallet.uihelp.ActivityHelp;


public class WalletRunningActivity_ViewPagerAdapter extends FragmentStatePagerAdapter {

    private String[] titles;
    private Wallet wallet;
    private WalletRunningActivity walletRunningActivity;

    public WalletRunningActivity_ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, Wallet wallet, String[] titles, WalletRunningActivity walletRunningActivity) {
        super(fm, behavior);
        if (titles == null) {
            throw new NullPointerException("titles为null");
        }
        this.wallet = wallet;
        this.titles = titles;
        this.walletRunningActivity = walletRunningActivity;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseFragment.POSITION_KEY, position);
        bundle.putSerializable(ActivityHelp.WALLET_KEY, wallet);
        Fragment fragment = null;
        if (position == 0) {
            fragment = new WalletRunningActivity_Fragment_Default();
            bundle.putString(ActivityHelp.TRANSACTION_TYPE_KEY, ActivityHelp.TRANSACTION_TYPE_ALL);
            if (!walletRunningActivity.isPageChanged()) {
                bundle.putBoolean(ActivityHelp.NEED_AUTO_REFRESH_KEY_KEY, true);
            }
            fragment.setArguments(bundle);
        } else if (position == 1) {
            fragment = new WalletRunningActivity_Fragment_Default();
            bundle.putString(ActivityHelp.TRANSACTION_TYPE_KEY, ActivityHelp.TRANSACTION_TYPE_RECEIVE);
            fragment.setArguments(bundle);
        } else if (position == 2) {
            fragment = new WalletRunningActivity_Fragment_Default();
            bundle.putString(ActivityHelp.TRANSACTION_TYPE_KEY, ActivityHelp.TRANSACTION_TYPE_SEND);
            fragment.setArguments(bundle);
        } else {
            fragment = new BaseFragment();
            fragment.setArguments(bundle);
        }
        return fragment;
    }

    @Override
    public int getCount() {
        return titles.length;
    }

}