/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.base.recyclerviewlibrary.adapters;

import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;

import com.xcash.base.recyclerviewlibrary.models.ViewItem;
import com.xcash.wallet.R;

import java.util.ArrayList;
import java.util.List;

public abstract class UnLoadMoreRecyclerViewAdapter extends BaseRecyclerViewAdapter<ViewItem> {

    public static final int USE_DEFAULT_LOADCOMPLETEDLAYOUTID = -1;
    public static final int UNSHOW_LOADCOMPLETEDLAYOUTID = 0;

    private int loadCompletedLayoutId;

    public UnLoadMoreRecyclerViewAdapter(List<ViewItem> datas) {
        super(datas);
        loadCompletedLayoutId = R.layout.base_layout_load_completed;
        loadCompleted(getDatas());
    }

    public UnLoadMoreRecyclerViewAdapter(List<ViewItem> datas, int loadCompletedLayoutId) {
        super(datas);
        if (loadCompletedLayoutId == USE_DEFAULT_LOADCOMPLETEDLAYOUTID) {
            loadCompletedLayoutId = R.layout.base_layout_load_completed;
        }
        this.loadCompletedLayoutId = loadCompletedLayoutId;
        loadCompleted(getDatas());
    }

    public abstract int getNormalLayoutId(int viewType);

    public abstract void onBindNormalViewHolder(BaseRecyclerViewHolder holder, int position);

    @Override
    public int getLayoutId(int viewType) {
        if (viewType == ViewItem.VIEW_TYPE_ITEM_LOAD_COMPLETED) {
            return loadCompletedLayoutId;
        } else {
            return getNormalLayoutId(viewType);
        }
    }

    @Override
    public void onBindViewHolder(BaseRecyclerViewAdapter.BaseRecyclerViewHolder holder, int position) {
        List<ViewItem> datas = getDatas();
        if (datas.get(position) != null) {
            if (datas.get(position).getViewType() == ViewItem.VIEW_TYPE_ITEM_LOAD_COMPLETED) {
                ViewGroup.LayoutParams viewGroupLayoutParams = holder.itemView.getLayoutParams();
                if (viewGroupLayoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams staggeredGridLayoutManagerLayoutParams = (StaggeredGridLayoutManager.LayoutParams) viewGroupLayoutParams;
                    staggeredGridLayoutManagerLayoutParams.setFullSpan(true);
                }
            } else {
                onBindNormalViewHolder(holder, position);
            }
        }
    }

    @Override
    public int getItemViewType(int position) {
        ViewItem viewItem = getDatas().get(position);
        if (viewItem == null) {
            return -1;
        } else {
            return viewItem.getViewType();
        }
    }

    @Override
    public void replaceAll(List<ViewItem> newDatas) {
        super.replaceAll(convertReplaceAllDatas(newDatas));
    }

    @Override
    public void replaceAllWithScrollTop(List<ViewItem> newDatas, RecyclerView recyclerView) {
        super.replaceAllWithScrollTop(convertReplaceAllDatas(newDatas), recyclerView);
    }

    @Override
    public void replaceAllUseNotifyDataSetChanged(List<ViewItem> newDatas) {
        super.replaceAllUseNotifyDataSetChanged(convertReplaceAllDatas(newDatas));
    }

    @Override
    public void replaceAllUseNotifyDataSetChangedWithScrollTop(List<ViewItem> newDatas, RecyclerView recyclerView) {
        super.replaceAllUseNotifyDataSetChangedWithScrollTop(convertReplaceAllDatas(newDatas), recyclerView);
    }

    private List<ViewItem> convertReplaceAllDatas(List<ViewItem> newDatas) {
        if (newDatas == null) {
            newDatas = new ArrayList<ViewItem>();
        }
        loadCompleted(newDatas);
        return newDatas;
    }

    private ViewItem getLoadCompletedItem() {
        return new ViewItem(ViewItem.VIEW_TYPE_ITEM_LOAD_COMPLETED, null);
    }

    private void loadCompleted(List<ViewItem> datas) {
        if (datas != null && datas.size() != 0 && loadCompletedLayoutId != UNSHOW_LOADCOMPLETEDLAYOUTID) {
            datas.add(getLoadCompletedItem());
        }
    }

    public int getLoadCompletedLayoutId() {
        return loadCompletedLayoutId;
    }

    public void setLoadCompletedLayoutId(int loadCompletedLayoutId) {
        this.loadCompletedLayoutId = loadCompletedLayoutId;
    }

}
