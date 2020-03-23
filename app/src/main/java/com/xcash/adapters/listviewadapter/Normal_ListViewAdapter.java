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
package com.xcash.adapters.listviewadapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.xcash.base.BaseActivity;
import com.xcash.models.local.KeyValueItem;
import com.xcash.wallet.R;

import java.util.ArrayList;
import java.util.List;


public class Normal_ListViewAdapter extends BaseAdapter {

    private BaseActivity baseActivity;
    private List<KeyValueItem> keyValueItems;
    private ListView listView;
    private LayoutInflater layoutInflater;

    public Normal_ListViewAdapter(BaseActivity baseActivity, List<KeyValueItem> keyValueItems, ListView listView, final AdapterView.OnItemClickListener adapterViewOnItemClickListener) {
        this.baseActivity = baseActivity;
        if (keyValueItems == null) {
            keyValueItems = new ArrayList<KeyValueItem>();
        }
        this.keyValueItems = keyValueItems;
        this.listView = listView;
        layoutInflater = this.baseActivity.getLayoutInflater();
        this.listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (adapterViewOnItemClickListener != null) {
                    adapterViewOnItemClickListener.onItemClick(parent, view, position, id);
                }
            }
        });
    }

    @Override
    public int getCount() {
        return keyValueItems.size();
    }

    @Override
    public Object getItem(int position) {
        return keyValueItems.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = layoutInflater.inflate(R.layout.popupwindow_listview_normal_item, parent, false);
            viewHolder.textView = (TextView) convertView.findViewById(R.id.textView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        KeyValueItem keyValueItem = keyValueItems.get(position);
        if (keyValueItem != null) {
            viewHolder.textView.setText(keyValueItem.getKey());
        }
        return convertView;
    }

    public class ViewHolder {
        private TextView textView;
    }

}
