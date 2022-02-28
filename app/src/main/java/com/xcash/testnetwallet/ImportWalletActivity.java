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
import android.widget.TextView;

import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.xcash.adapters.viewpageradapter.ImportWalletActivity_ViewPagerAdapter;
import com.xcash.views.tablayout.TabLayout;
import com.xcash.wallet.uihelp.ActivityHelp;


public class ImportWalletActivity extends NewBaseActivity {

    private String set_wallet_name;
    private String set_wallet_password;
    private String set_wallet_description;

    private String[] titles;

    private ImageView imageViewBack;
    private TextView textViewTitle;
    private View.OnClickListener onClickListener;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private ImportWalletActivity_ViewPagerAdapter importWalletActivity_ViewPagerAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_import_wallet);
        Intent intent = getIntent();
        set_wallet_name = intent.getStringExtra(ActivityHelp.SET_WALLET_NAME_KEY);
        set_wallet_password = intent.getStringExtra(ActivityHelp.SET_WALLET_PASSWORD_KEY);
        set_wallet_description = intent.getStringExtra(ActivityHelp.SET_WALLET_DESCRIPTION_KEY);
        initAll();
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    @Override
    protected void initUi() {
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        titles = getResources().getStringArray(R.array.activity_import_wallet_tabLayout_titles);
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        tabLayout = (TabLayout) findViewById(R.id.tabLayout);
        viewPager = (ViewPager) findViewById(R.id.viewPager);
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewTitle.setText(R.string.activity_import_wallet_textViewTitle_text);
        importWalletActivity_ViewPagerAdapter = new ImportWalletActivity_ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, set_wallet_name, set_wallet_password, set_wallet_description, titles);
        viewPager.setAdapter(importWalletActivity_ViewPagerAdapter);
        viewPager.setOffscreenPageLimit(1);
        tabLayout.setNeedSwitchAnimation(true);
        tabLayout.setIndicatorWidthWrapContent(true);
        tabLayout.setupWithViewPager(viewPager);
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {

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

    @Override
    protected void onResume() {
        super.onResume();
        TheApplication.refreshPageScrolled(handler, tabLayout);
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

}
