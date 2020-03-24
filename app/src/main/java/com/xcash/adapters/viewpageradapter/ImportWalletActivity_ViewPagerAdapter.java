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

import com.xcash.wallet.fragment.BaseFragment;
import com.xcash.wallet.fragment.ImportWalletActivity_Fragment_Keys;
import com.xcash.wallet.fragment.ImportWalletActivity_Fragment_Mnemonic;
import com.xcash.wallet.uihelp.ActivityHelp;

public class ImportWalletActivity_ViewPagerAdapter extends FragmentStatePagerAdapter {

    private String set_wallet_name;
    private String set_wallet_password;
    private String set_wallet_description;
    private String[] titles;

    public ImportWalletActivity_ViewPagerAdapter(@NonNull FragmentManager fm, int behavior, String set_wallet_name, String set_wallet_password, String set_wallet_description, String[] titles) {
        super(fm, behavior);
        if (titles == null) {
            throw new NullPointerException("titles为null");
        }
        this.set_wallet_name = set_wallet_name;
        this.set_wallet_password = set_wallet_password;
        this.set_wallet_description = set_wallet_description;
        this.titles = titles;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return titles[position];
    }

    @Override
    public Fragment getItem(int position) {
        Bundle bundle = new Bundle();
        bundle.putInt(BaseFragment.POSITION_KEY, position);
        bundle.putString(ActivityHelp.SET_WALLET_NAME_KEY, set_wallet_name);
        bundle.putString(ActivityHelp.SET_WALLET_PASSWORD_KEY, set_wallet_password);
        bundle.putString(ActivityHelp.SET_WALLET_DESCRIPTION_KEY, set_wallet_description);
        Fragment fragment = null;
        if (position == 0) {
            fragment = new ImportWalletActivity_Fragment_Mnemonic();
            fragment.setArguments(bundle);
        } else if (position == 1) {
            fragment = new ImportWalletActivity_Fragment_Keys();
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