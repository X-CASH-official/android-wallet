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
 you may not use this file except in compliance with the License.
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
package com.xcash.adapters.recyclerviewadapter;

import android.content.Intent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.xcash.base.BaseActivity;
import com.xcash.base.recyclerviewlibrary.adapters.UnLoadMoreRecyclerViewAdapter;
import com.xcash.base.recyclerviewlibrary.models.ViewItem;
import com.xcash.utils.database.entity.Wallet;
import com.xcash.wallet.R;
import com.xcash.wallet.WalletDetailsActivity;
import com.xcash.wallet.uihelp.ActivityHelp;

import java.util.List;

public class WalletManagerActivity_RecyclerViewAdapter extends UnLoadMoreRecyclerViewAdapter {

    private BaseActivity baseActivity;
    private RecyclerView recyclerView;
    private OnWalletManagerListener onWalletManagerListener;

    public WalletManagerActivity_RecyclerViewAdapter(BaseActivity baseActivity, RecyclerView recyclerView, List<ViewItem> datas) {
        super(datas);
        init(baseActivity, recyclerView);
    }

    private void init(BaseActivity baseActivity, RecyclerView recyclerView) {
        setLoadCompletedLayoutId(R.layout.normal_load_completed);
        this.baseActivity = baseActivity;
        this.recyclerView = recyclerView;
    }

    @Override
    public int getNormalLayoutId(int viewType) {
        if (viewType == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1) {
            return R.layout.activity_wallet_manager_item;
        } else {
            return R.layout.base_recyclerview_lostviewtype_item;
        }
    }

    @Override
    public void onBindNormalViewHolder(final BaseRecyclerViewHolder holder, final int position) {
        ViewItem viewItem = getDatas().get(position);
        if (viewItem == null) {
            return;
        }
        if (viewItem.getViewType() == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1) {
            final Wallet wallet = (Wallet) viewItem.getModel();
            if (wallet != null) {
                TextView textViewWalletName = (TextView) holder.getView(R.id.textViewWalletName);
                RelativeLayout relativeLayoutActiveStatus = (RelativeLayout) holder.getView(R.id.relativeLayoutActiveStatus);
                FrameLayout frameLayoutSwitch = (FrameLayout) holder.getView(R.id.frameLayoutSwitch);
                TextView textViewAccount = (TextView) holder.getView(R.id.textViewAccount);

                textViewWalletName.setText(wallet.getName());
                if (wallet.isActive()) {
                    relativeLayoutActiveStatus.setVisibility(View.VISIBLE);
                    frameLayoutSwitch.setVisibility(View.GONE);
                } else {
                    relativeLayoutActiveStatus.setVisibility(View.GONE);
                    frameLayoutSwitch.setVisibility(View.VISIBLE);
                }
                String balance = wallet.getBalance();
                if (balance == null) {
                    balance = baseActivity.getString(R.string.number_amount_0_text);
                }

                textViewAccount.setText(balance);

                frameLayoutSwitch.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onWalletManagerListener != null) {
                            onWalletManagerListener.onItemSelect(wallet);
                        }
                    }
                });

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(baseActivity,
                                WalletDetailsActivity.class);
                        intent.putExtra(ActivityHelp.WALLET_KEY, wallet);
                        baseActivity.startActivity(intent);
                    }
                });
            }
        }
    }

    public OnWalletManagerListener getOnWalletManagerListener() {
        return onWalletManagerListener;
    }

    public void setOnWalletManagerListener(OnWalletManagerListener onWalletManagerListener) {
        this.onWalletManagerListener = onWalletManagerListener;
    }

    public interface OnWalletManagerListener {
        void onItemSelect(Wallet wallet);
    }

}
