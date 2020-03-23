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

import android.content.res.TypedArray;
import android.view.View;
import android.widget.TextView;

import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.xcash.base.BaseActivity;
import com.xcash.base.recyclerviewlibrary.adapters.UnLoadMoreRecyclerViewAdapter;
import com.xcash.base.recyclerviewlibrary.models.ViewItem;
import com.xcash.utils.StringTool;
import com.xcash.utils.database.entity.OperationHistory;
import com.xcash.wallet.R;
import com.xcash.wallet.uihelp.ActivityHelp;

import java.util.List;


public class DpopsOperationHistoryActivity_RecyclerViewAdapter extends UnLoadMoreRecyclerViewAdapter {

    private BaseActivity baseActivity;
    private RecyclerView recyclerView;
    private int colorPrimary;
    private int textView_error;
    private OnDpopsOperationHistory onDpopsOperationHistory;

    public DpopsOperationHistoryActivity_RecyclerViewAdapter(BaseActivity baseActivity, RecyclerView recyclerView, List<ViewItem> datas) {
        super(datas);
        init(baseActivity, recyclerView);
    }

    private void init(BaseActivity baseActivity, RecyclerView recyclerView) {
        setLoadCompletedLayoutId(R.layout.normal_load_completed);
        this.baseActivity = baseActivity;
        this.recyclerView = recyclerView;

        TypedArray typedArray = this.baseActivity.getTheme().obtainStyledAttributes(new int[]{R.attr.colorPrimary});
        try {
            colorPrimary = typedArray.getColor(0, 0xffffffff);
        } finally {
            typedArray.recycle();
        }
        textView_error = ContextCompat.getColor(this.baseActivity, R.color.textView_error);
    }

    @Override
    public int getNormalLayoutId(int viewType) {
        if (viewType == ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1) {
            return R.layout.activity_dpops_operation_history_item;
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
            final OperationHistory operationHistory = (OperationHistory) viewItem.getModel();
            if (operationHistory != null) {
                TextView textViewTitle = (TextView) holder.getView(R.id.textViewTitle);
                TextView textViewStatus = (TextView) holder.getView(R.id.textViewStatus);
                TextView textViewTime = (TextView) holder.getView(R.id.textViewTime);
                TextView textViewContent = (TextView) holder.getView(R.id.textViewContent);

                textViewTitle.setText(operationHistory.getOperation());
                textViewStatus.setText(operationHistory.getStatus());
                if (operationHistory.getStatus() != null && operationHistory.getStatus().equals(ActivityHelp.DELEGATE_SUCCESS)) {
                    textViewStatus.setTextColor(colorPrimary);
                } else {
                    textViewStatus.setTextColor(textView_error);
                }
                textViewTime.setText(StringTool.getDateTime(operationHistory.getTimestamp()));
                textViewContent.setText(operationHistory.getDescription());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onDpopsOperationHistory != null) {
                            onDpopsOperationHistory.onItemSelect(operationHistory);
                        }
                    }
                });
            }
        }
    }

    public OnDpopsOperationHistory getOnDpopsOperationHistory() {
        return onDpopsOperationHistory;
    }

    public void setOnDpopsOperationHistory(OnDpopsOperationHistory onDpopsOperationHistory) {
        this.onDpopsOperationHistory = onDpopsOperationHistory;
    }

    public interface OnDpopsOperationHistory {

        void onItemSelect(OperationHistory operationHistory);

    }

}
