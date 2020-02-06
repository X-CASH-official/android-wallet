/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.adapters.recyclerviewadapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.xcash.base.BaseActivity;
import com.xcash.base.recyclerviewlibrary.adapters.UnLoadMoreRecyclerViewAdapter;
import com.xcash.base.recyclerviewlibrary.models.ViewItem;
import com.xcash.models.Delegate;
import com.xcash.wallet.R;

import java.util.List;


public class DpopsActivity_RecyclerViewAdapter extends UnLoadMoreRecyclerViewAdapter {

    private BaseActivity baseActivity;
    private RecyclerView recyclerView;
    private OnDpopsListener onDpopsListener;

    public DpopsActivity_RecyclerViewAdapter(BaseActivity baseActivity, RecyclerView recyclerView, List<ViewItem> datas) {
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
            return R.layout.activity_dpops_item;
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
            final Delegate delegate = (Delegate) viewItem.getModel();
            if (delegate != null) {
                TextView textViewDelegateName = (TextView) holder.getView(R.id.textViewDelegateName);
                TextView textViewAmount = (TextView) holder.getView(R.id.textViewAmount);
                TextView textViewInfo = (TextView) holder.getView(R.id.textViewInfo);

                String fee_structure = "0%";
                if (delegate.getFee_structure() != "") {
                    fee_structure = delegate.getFee_structure() + "%";
                }
                textViewDelegateName.setText(delegate.getDelegate_name());
                textViewAmount.setText(delegate.getTotal_vote_count());

                textViewInfo.setText("online_status:" + delegate.getOnline_status() + "\npool_mode:" + delegate.getPool_mode() + "|fee_structure:" + fee_structure + "\nblock_verifier_score:" + delegate.getBlock_verifier_score() + "\nblock_verifier_total_rounds:" + delegate.getBlock_verifier_total_rounds());
                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onDpopsListener != null) {
                            onDpopsListener.onItemSelect(delegate);
                        }
                    }
                });
            }
        }
    }

    public OnDpopsListener getOnDpopsListener() {
        return onDpopsListener;
    }

    public void setOnDpopsListener(OnDpopsListener onDpopsListener) {
        this.onDpopsListener = onDpopsListener;
    }

    public interface OnDpopsListener {

        void onItemSelect(Delegate delegate);

    }

}
