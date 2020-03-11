/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
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
