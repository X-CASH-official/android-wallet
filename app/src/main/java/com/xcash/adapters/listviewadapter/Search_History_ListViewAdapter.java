/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.adapters.listviewadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.xcash.base.BaseActivity;
import com.xcash.wallet.R;

import java.util.ArrayList;
import java.util.List;


public class Search_History_ListViewAdapter extends BaseAdapter {

    private BaseActivity baseActivity;
    private List<String> searchHistorys;
    private ListView listView;
    private LayoutInflater layoutInflater;
    private OnSearchHistoryListener onSearchHistoryListener;

    public Search_History_ListViewAdapter(BaseActivity baseActivity, List<String> searchHistorys, ListView listView, final OnSearchHistoryListener onSearchHistoryListener) {
        this.baseActivity = baseActivity;
        if (searchHistorys == null) {
            searchHistorys = new ArrayList<String>();
        }
        this.searchHistorys = searchHistorys;
        this.listView = listView;
        this.onSearchHistoryListener = onSearchHistoryListener;

        layoutInflater = this.baseActivity.getLayoutInflater();
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (onSearchHistoryListener != null) {
                    onSearchHistoryListener.onItemClick(Search_History_ListViewAdapter.this, parent, view, position, id);
                }
            }
        });
    }

    @Override
    public int getCount() {
        return searchHistorys.size();
    }

    @Override
    public Object getItem(int position) {
        return searchHistorys.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.popupwindow_search_listview_history_item, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            viewHolder.imageViewDelete = (ImageView) convertView.findViewById(R.id.imageViewDelete);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        String searchHistory = searchHistorys.get(position);
        if (searchHistory != null) {
            viewHolder.textView.setText(searchHistory);
        }
        viewHolder.imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSearchHistoryListener != null) {
                    onSearchHistoryListener.onDelete(Search_History_ListViewAdapter.this, v, position);
                }
            }
        });
        return convertView;
    }

    public class ViewHolder {
        private TextView textView;
        private ImageView imageViewDelete;
    }

    public interface OnSearchHistoryListener {

        void onItemClick(BaseAdapter baseAdapter, AdapterView<?> parent, View view, int position, long id);

        void onDelete(BaseAdapter baseAdapter, View view, int position);

    }
}
