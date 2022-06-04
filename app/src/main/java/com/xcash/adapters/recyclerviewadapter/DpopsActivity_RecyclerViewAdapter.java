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
package com.xcash.adapters.recyclerviewadapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.xcash.base.BaseActivity;
import com.xcash.base.recyclerviewlibrary.adapters.UnLoadMoreRecyclerViewAdapter;
import com.xcash.base.recyclerviewlibrary.models.ViewItem;
import com.xcash.models.Delegate;
import com.xcash.utils.CompactNumberFormatter;
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

                String delegate_fee = "0%";
                if (delegate.getDelegate_fee()!=null&&!delegate.getDelegate_fee().equals("")) {
                    delegate_fee = delegate.getDelegate_fee() + "%";
                }
                String block_verifier_online_percentage = "0%";
                if (delegate.getBlock_verifier_online_percentage()!=null&&!delegate.getBlock_verifier_online_percentage().equals("")) {
                    block_verifier_online_percentage = delegate.getBlock_verifier_online_percentage() + "%";
                }
                textViewDelegateName.setText(delegate.getDelegate_name());

                String total_vote_count = delegate.getTotal_vote_count();
                int decimals = CompactNumberFormatter.getXCASHDecimals();
                double total_vote_count_d = Double.parseDouble(total_vote_count);
                total_vote_count_d /= Math.pow(10, decimals);
                total_vote_count = CompactNumberFormatter.formatValue(total_vote_count_d);

                textViewAmount.setText(total_vote_count);

                textViewInfo.setText("online_status:" + delegate.getOnline_status() + "\ndelegate_status:" + delegate.getShared_delegate_status() + "|delegate_fee:" + delegate_fee  + "\nblock_verifier_total_rounds:" + delegate.getBlock_verifier_total_rounds()+ "\nblock_verifier_online_percentage:" + block_verifier_online_percentage);
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
