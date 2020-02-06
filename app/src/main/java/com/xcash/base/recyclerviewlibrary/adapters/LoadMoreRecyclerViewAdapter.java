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


public abstract class LoadMoreRecyclerViewAdapter extends BaseRecyclerViewAdapter<ViewItem> {

    public static final int USE_DEFAULT_LOADMORELAYOUTID = -1;
    public static final int USE_DEFAULT_LOADCOMPLETEDLAYOUTID = -1;
    public static final int UNSHOW_LOADCOMPLETEDLAYOUTID = 0;

    private int loadMoreLayoutId;
    private int pageCount;
    private int loadCompletedLayoutId;
    private LoadMoreCallback loadMoreCallback;
    private boolean loading = false;
    private boolean loadCompleted = false;

    public LoadMoreRecyclerViewAdapter(List<ViewItem> datas) {
        super(datas);
        this.loadMoreLayoutId = R.layout.base_layout_more_progress;
        pageCount = 25;
        loadCompletedLayoutId = R.layout.base_layout_load_completed;
        if (getDatas().size() >= pageCount) {
            getDatas().add(getLoadMoreItem());
        } else {
            loadCompleted(getDatas());
            loadCompleted = true;
        }
    }

    public LoadMoreRecyclerViewAdapter(List<ViewItem> datas, int loadMoreLayoutId) {
        super(datas);
        if (loadMoreLayoutId == USE_DEFAULT_LOADMORELAYOUTID) {
            loadMoreLayoutId = R.layout.base_layout_more_progress;
        }
        this.loadMoreLayoutId = loadMoreLayoutId;
        pageCount = 25;
        loadCompletedLayoutId = R.layout.base_layout_load_completed;
        if (getDatas().size() >= pageCount) {
            getDatas().add(getLoadMoreItem());
        } else {
            loadCompleted(getDatas());
            loadCompleted = true;
        }
    }

    public LoadMoreRecyclerViewAdapter(List<ViewItem> datas, int loadMoreLayoutId, int pageCount) {
        super(datas);
        if (loadMoreLayoutId == USE_DEFAULT_LOADMORELAYOUTID) {
            loadMoreLayoutId = R.layout.base_layout_more_progress;
        }
        this.loadMoreLayoutId = loadMoreLayoutId;
        if (pageCount <= 0) {
            pageCount = 25;
        }
        this.pageCount = pageCount;
        loadCompletedLayoutId = R.layout.base_layout_load_completed;
        if (getDatas().size() >= this.pageCount) {
            getDatas().add(getLoadMoreItem());
        } else {
            loadCompleted(getDatas());
            loadCompleted = true;
        }
    }

    public LoadMoreRecyclerViewAdapter(List<ViewItem> datas, int loadMoreLayoutId, int pageCount, int loadCompletedLayoutId) {
        super(datas);
        if (loadMoreLayoutId == USE_DEFAULT_LOADMORELAYOUTID) {
            loadMoreLayoutId = R.layout.base_layout_more_progress;
        }
        this.loadMoreLayoutId = loadMoreLayoutId;
        if (pageCount <= 0) {
            pageCount = 25;
        }
        this.pageCount = pageCount;
        if (loadCompletedLayoutId == USE_DEFAULT_LOADCOMPLETEDLAYOUTID) {
            loadCompletedLayoutId = R.layout.base_layout_load_completed;
        }
        this.loadCompletedLayoutId = loadCompletedLayoutId;
        if (getDatas().size() >= this.pageCount) {
            getDatas().add(getLoadMoreItem());
        } else {
            loadCompleted(getDatas());
            loadCompleted = true;
        }
    }

    public abstract int getNormalLayoutId(int viewType);

    public abstract void onBindNormalViewHolder(BaseRecyclerViewAdapter.BaseRecyclerViewHolder holder, int position);

    @Override
    public int getLayoutId(int viewType) {
        if (viewType == ViewItem.VIEW_TYPE_ITEM_LOAD_COMPLETED) {
            return loadCompletedLayoutId;
        } else if (viewType == ViewItem.VIEW_TYPE_ITEM_LOAD_MORE) {
            return loadMoreLayoutId;
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
            } else if (datas.get(position).getViewType() == ViewItem.VIEW_TYPE_ITEM_LOAD_MORE) {
                ViewGroup.LayoutParams viewGroupLayoutParams = holder.itemView.getLayoutParams();
                if (viewGroupLayoutParams instanceof StaggeredGridLayoutManager.LayoutParams) {
                    StaggeredGridLayoutManager.LayoutParams staggeredGridLayoutManagerLayoutParams = (StaggeredGridLayoutManager.LayoutParams) viewGroupLayoutParams;
                    staggeredGridLayoutManagerLayoutParams.setFullSpan(true);
                }
                if (position == datas.size() - 1 && !loadCompleted && !loading) {
                    loading = true;
                    if (loadMoreCallback != null) {
                        ViewItem viewItem = null;
                        if (position - 1 >= 0 && datas.get(position - 1) != null) {
                            viewItem = datas.get(position - 1);
                        }
                        loadMoreCallback.loadMore(viewItem);
                    }
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
    public void addAll(List<ViewItem> newDatas) {
        super.addAll(convertAddAllDatas(newDatas, false));
        loading = false;
    }

    @Override
    public void addAllUseNotifyDataSetChanged(List<ViewItem> newDatas) {
        super.addAllUseNotifyDataSetChanged(convertAddAllDatas(newDatas, true));
        loading = false;
    }

    @Override
    public void replaceAll(List<ViewItem> newDatas) {
        super.replaceAll(convertReplaceAllDatas(newDatas));
        loading = false;
    }

    @Override
    public void replaceAllWithScrollTop(List<ViewItem> newDatas, RecyclerView recyclerView) {
        super.replaceAllWithScrollTop(convertReplaceAllDatas(newDatas), recyclerView);
        loading = false;
    }

    @Override
    public void replaceAllUseNotifyDataSetChanged(List<ViewItem> newDatas) {
        super.replaceAllUseNotifyDataSetChanged(convertReplaceAllDatas(newDatas));
        loading = false;
    }

    @Override
    public void replaceAllUseNotifyDataSetChangedWithScrollTop(List<ViewItem> newDatas, RecyclerView recyclerView) {
        super.replaceAllUseNotifyDataSetChangedWithScrollTop(convertReplaceAllDatas(newDatas), recyclerView);
        loading = false;
    }

    public void loadMoreError() {
        hideLoadMore(false);
        if (getDatas().size() >= pageCount) {
            List<ViewItem> newDatas = new ArrayList<ViewItem>();
            newDatas.add(getLoadMoreItem());
            super.addAll(newDatas);
        }
        loading = false;
    }

    public void loadMoreErrorUseNotifyDataSetChanged() {
        hideLoadMore(true);
        if (getDatas().size() >= pageCount) {
            List<ViewItem> newDatas = new ArrayList<ViewItem>();
            newDatas.add(getLoadMoreItem());
            super.addAllUseNotifyDataSetChanged(newDatas);
        }
        loading = false;
    }

    private List<ViewItem> convertAddAllDatas(List<ViewItem> newDatas, boolean useNotifyDataSetChanged) {
        if (newDatas == null) {
            newDatas = new ArrayList<ViewItem>();
        }
        hideLoadMore(useNotifyDataSetChanged);
        if (newDatas.size() < pageCount) {
            List<ViewItem> datas = new ArrayList<ViewItem>();
            datas.addAll(getDatas());
            datas.addAll(newDatas);
            if (datas.size() != 0 && loadCompletedLayoutId != UNSHOW_LOADCOMPLETEDLAYOUTID) {
                newDatas.add(getLoadCompletedItem());
            }
            loadCompleted = true;
        } else {
            newDatas.add(getLoadMoreItem());
        }
        return newDatas;
    }

    private List<ViewItem> convertReplaceAllDatas(List<ViewItem> newDatas) {
        if (newDatas == null) {
            newDatas = new ArrayList<ViewItem>();
        }
        loadCompleted = false;
        if (newDatas.size() < pageCount) {
            loadCompleted(newDatas);
            loadCompleted = true;
        } else {
            newDatas.add(getLoadMoreItem());
        }
        return newDatas;
    }

    private void hideLoadMore(boolean useNotifyDataSetChanged) {
        List<ViewItem> datas = getDatas();
        int lastPosition = datas.size() - 1;
        if (lastPosition >= 0) {
            ViewItem viewItem = datas.get(lastPosition);
            if (viewItem != null && viewItem.getViewType() == ViewItem.VIEW_TYPE_ITEM_LOAD_MORE) {
                datas.remove(lastPosition);
                if (!useNotifyDataSetChanged) {
                    notifyItemRemoved(lastPosition);
                }
            }
        }
    }

    private ViewItem getLoadCompletedItem() {
        return new ViewItem(ViewItem.VIEW_TYPE_ITEM_LOAD_COMPLETED, null);
    }

    private ViewItem getLoadMoreItem() {
        return new ViewItem(ViewItem.VIEW_TYPE_ITEM_LOAD_MORE, null);
    }

    private void loadCompleted(List<ViewItem> datas) {
        if (datas != null && datas.size() != 0 && loadCompletedLayoutId != UNSHOW_LOADCOMPLETEDLAYOUTID) {
            datas.add(getLoadCompletedItem());
        }
    }

    public int getLoadMoreLayoutId() {
        return loadMoreLayoutId;
    }

    public void setLoadMoreLayoutId(int loadMoreLayoutId) {
        this.loadMoreLayoutId = loadMoreLayoutId;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public int getLoadCompletedLayoutId() {
        return loadCompletedLayoutId;
    }

    public void setLoadCompletedLayoutId(int loadCompletedLayoutId) {
        this.loadCompletedLayoutId = loadCompletedLayoutId;
    }

    public void setLoadMoreCallback(LoadMoreCallback loadMoreCallback) {
        this.loadMoreCallback = loadMoreCallback;
    }

    public interface LoadMoreCallback {
        void loadMore(ViewItem viewItem);
    }

}
