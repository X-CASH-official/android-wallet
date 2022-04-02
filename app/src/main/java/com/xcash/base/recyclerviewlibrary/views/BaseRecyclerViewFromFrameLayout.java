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
package com.xcash.base.recyclerviewlibrary.views;

import android.content.Context;
import android.content.res.TypedArray;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xcash.base.recyclerviewlibrary.adapters.BaseRecyclerViewAdapter;
import com.xcash.base.recyclerviewlibrary.models.ViewItem;
import com.xcash.testnetwallet.R;

public class BaseRecyclerViewFromFrameLayout extends FrameLayout {

    private Context context;
    private int mainId = R.layout.base_layout_main;
    private int emptyId = 0;
    private boolean clipToPadding = false;
    private int padding = -1;
    private int paddingLeft = 0;
    private int paddingRight = 0;
    private int paddingTop = 0;
    private int paddingBottom = 0;
    private int layoutManager = 1;
    private int spanCount = 1;
    private int layoutManagerOrientation = 1;
    private int scrollbarStyle = 0;
    private FrameLayout frameLayoutEmpty;
    private SwipeRefreshLayout swipeRefreshLayout;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout.OnRefreshListener onRefreshListener;

    public BaseRecyclerViewFromFrameLayout(Context context) {
        super(context);
        this.context = context;
        initView();
    }

    public BaseRecyclerViewFromFrameLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
        initAttrs(attrs);
        initView();
    }

    public BaseRecyclerViewFromFrameLayout(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        this.context = context;
        initAttrs(attrs);
        initView();
    }

    private void initAttrs(AttributeSet attrs) {
        if (attrs == null) {
            return;
        }
        TypedArray typedArray = getContext().obtainStyledAttributes(attrs, R.styleable.extendedRecyclerView);
        try {
            mainId = typedArray.getResourceId(R.styleable.extendedRecyclerView_layout_main, R.layout.base_layout_main);
            emptyId = typedArray.getResourceId(R.styleable.extendedRecyclerView_layout_empty, 0);
            clipToPadding = typedArray.getBoolean(R.styleable.extendedRecyclerView_recyclerClipToPadding, false);
            padding = (int) typedArray.getDimension(R.styleable.extendedRecyclerView_recyclerPadding, -1.0f);
            paddingLeft = (int) typedArray.getDimension(R.styleable.extendedRecyclerView_recyclerPaddingLeft, 0.0f);
            paddingRight = (int) typedArray.getDimension(R.styleable.extendedRecyclerView_recyclerPaddingRight, 0.0f);
            paddingTop = (int) typedArray.getDimension(R.styleable.extendedRecyclerView_recyclerPaddingTop, 0.0f);
            paddingBottom = (int) typedArray.getDimension(R.styleable.extendedRecyclerView_recyclerPaddingBottom, 0.0f);
            layoutManager = typedArray.getInt(R.styleable.extendedRecyclerView_recyclerLayoutManager, 1);
            spanCount = typedArray.getInt(R.styleable.extendedRecyclerView_recyclerSpanCount, 1);
            layoutManagerOrientation = typedArray.getInt(R.styleable.extendedRecyclerView_recyclerLayoutManagerOrientation, 1);
            scrollbarStyle = typedArray.getInt(R.styleable.extendedRecyclerView_scrollbarStyle, 0);
        } finally {
            typedArray.recycle();
        }
    }

    private void initView() {
        if (!isInEditMode()) {
            View view = LayoutInflater.from(getContext()).inflate(mainId, this, true);
            frameLayoutEmpty = (FrameLayout) view.findViewById(R.id.frameLayoutEmpty);
            swipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeRefreshLayout);
            recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
            initFrameLayoutEmpty(frameLayoutEmpty);
            initSwipeRefreshLayout(swipeRefreshLayout);
            initRecyclerView(recyclerView);
        }
    }

    private void initFrameLayoutEmpty(FrameLayout frameLayoutEmpty) {
        if (emptyId != 0) {
            LayoutInflater.from(getContext()).inflate(emptyId, frameLayoutEmpty, true);
        }
        frameLayoutEmpty.setVisibility(View.GONE);
    }

    private void initSwipeRefreshLayout(SwipeRefreshLayout swipeRefreshLayout) {
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setColorSchemeColors(0xfff2a670, 0xffff82a5, 0xfffd584b, 0xff94db41);
    }

    private void initRecyclerView(RecyclerView recyclerView) {
        recyclerView.setHasFixedSize(true);
        recyclerView.setClipToPadding(clipToPadding);
        if (padding != -1) {
            recyclerView.setPadding(padding, padding, padding, padding);
        } else {
            recyclerView.setPadding(paddingLeft, paddingTop, paddingRight, paddingBottom);
        }
        if (scrollbarStyle == 1) {
            recyclerView.setScrollBarStyle(View.SCROLLBARS_INSIDE_INSET);
        } else if (scrollbarStyle == 2) {
            recyclerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_OVERLAY);
        } else if (scrollbarStyle == 3) {
            recyclerView.setScrollBarStyle(View.SCROLLBARS_OUTSIDE_INSET);
        } else {
            recyclerView.setScrollBarStyle(View.SCROLLBARS_INSIDE_OVERLAY);
        }
        initLayoutManager();
    }

    private void initLayoutManager() {
        if (layoutManager == 3) {
            if (layoutManagerOrientation == 1) {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.VERTICAL));
            } else {
                recyclerView.setLayoutManager(new StaggeredGridLayoutManager(spanCount, StaggeredGridLayoutManager.HORIZONTAL));
            }
        } else if (layoutManager == 2) {
            GridLayoutManager gridLayoutManager = null;
            if (layoutManagerOrientation == 1) {
                gridLayoutManager = new GridLayoutManager(context, spanCount, GridLayoutManager.VERTICAL, false);
            } else {
                gridLayoutManager = new GridLayoutManager(context, spanCount, GridLayoutManager.HORIZONTAL, false);
            }
            recyclerView.setLayoutManager(gridLayoutManager);
            final int theSpanCount = spanCount;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    RecyclerView.Adapter recyclerViewAdapter = recyclerView.getAdapter();
                    if (recyclerViewAdapter != null) {
                        switch (recyclerViewAdapter.getItemViewType(position)) {
                            case ViewItem.VIEW_TYPE_ITEM_LOAD_COMPLETED:
                                return theSpanCount;
                            case ViewItem.VIEW_TYPE_ITEM_LOAD_MORE:
                                return theSpanCount;
                            case ViewItem.VIEW_TYPE_ITEM_VIEWPAGER:
                                return theSpanCount;
                            default:
                                return 1;
                        }
                    }
                    return -1;
                }
            });
        } else {
            if (layoutManagerOrientation == 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
            } else {
                recyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
            }
        }
    }

    public void reset() {
        RecyclerView.Adapter recyclerViewAdapter = recyclerView.getAdapter();
        if (recyclerViewAdapter != null && recyclerViewAdapter instanceof BaseRecyclerViewAdapter) {
            BaseRecyclerViewAdapter baseRecyclerViewAdapter = (BaseRecyclerViewAdapter) recyclerViewAdapter;
            baseRecyclerViewAdapter.replaceAll(null);
            frameLayoutEmpty.setVisibility(View.GONE);
        }
    }

    public void setAdapter(final BaseRecyclerViewAdapter baseRecyclerViewAdapter) {
        if (baseRecyclerViewAdapter == null) {
            return;
        }
        recyclerView.setAdapter(baseRecyclerViewAdapter);
        recyclerView.setVisibility(View.VISIBLE);
        baseRecyclerViewAdapter.registerAdapterDataObserver(new RecyclerView.AdapterDataObserver() {
            @Override
            public void onChanged() {
                super.onChanged();
                update(baseRecyclerViewAdapter, BaseRecyclerViewAdapter.ONITEMUPDATE);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount) {
                super.onItemRangeChanged(positionStart, itemCount);
                update(baseRecyclerViewAdapter, BaseRecyclerViewAdapter.ONITEMRANGECHANGED);
            }

            @Override
            public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
                super.onItemRangeChanged(positionStart, itemCount, payload);
                update(baseRecyclerViewAdapter, BaseRecyclerViewAdapter.ONITEMRANGECHANGED);
            }

            @Override
            public void onItemRangeInserted(int positionStart, int itemCount) {
                super.onItemRangeInserted(positionStart, itemCount);
                update(baseRecyclerViewAdapter, BaseRecyclerViewAdapter.ONITEMRANGEINSERTED);
            }

            @Override
            public void onItemRangeRemoved(int positionStart, int itemCount) {
                super.onItemRangeRemoved(positionStart, itemCount);
                update(baseRecyclerViewAdapter, BaseRecyclerViewAdapter.ONITEMRANGEREMOVED);
            }

            @Override
            public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
                super.onItemRangeMoved(fromPosition, toPosition, itemCount);
                update(baseRecyclerViewAdapter, BaseRecyclerViewAdapter.ONITEMRANGEMOVED);
            }
        });
        update(baseRecyclerViewAdapter, -1);
    }

    private void update(BaseRecyclerViewAdapter baseRecyclerViewAdapter, int itemType) {
        if (itemType == -1 || baseRecyclerViewAdapter.getItemType() == itemType) {
            swipeRefreshLayout.setRefreshing(false);
            if (emptyId != 0) {
                if (recyclerView.getAdapter().getItemCount() == 0) {
                    frameLayoutEmpty.setVisibility(View.VISIBLE);
                } else {
                    frameLayoutEmpty.setVisibility(View.GONE);
                }
            }
            baseRecyclerViewAdapter.setItemType(BaseRecyclerViewAdapter.ONITEMDEFAULT);
        }
    }

    public FrameLayout getFrameLayoutEmpty() {
        return frameLayoutEmpty;
    }

    public SwipeRefreshLayout getSwipeRefreshLayout() {
        return swipeRefreshLayout;
    }

    public RecyclerView getRecyclerView() {
        return recyclerView;
    }

    public void setOnRefreshListener(SwipeRefreshLayout.OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
        swipeRefreshLayout.setEnabled(true);
        swipeRefreshLayout.setOnRefreshListener(this.onRefreshListener);
    }

    public SwipeRefreshLayout.OnRefreshListener getOnRefreshListener() {
        return onRefreshListener;
    }

    public void autoRefresh() {
        if (onRefreshListener != null) {
            swipeRefreshLayout.post(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    onRefreshListener.onRefresh();
                }
            });
        }
    }

    public void autoRefresh(Handler handler, long delayMillis) {
        if (handler != null && onRefreshListener != null) {
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {
                    swipeRefreshLayout.setRefreshing(true);
                    onRefreshListener.onRefresh();
                }
            }, delayMillis);
        }
    }

}
