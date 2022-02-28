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
package com.xcash.adapters.viewpageradapter;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.xcash.utils.database.entity.Wallet;
import com.xcash.testnetwallet.WalletRunningActivity;
import com.xcash.testnetwallet.fragment.BaseFragment;
import com.xcash.testnetwallet.fragment.WalletRunningActivity_Fragment_Default;
import com.xcash.testnetwallet.uihelp.ActivityHelp;


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