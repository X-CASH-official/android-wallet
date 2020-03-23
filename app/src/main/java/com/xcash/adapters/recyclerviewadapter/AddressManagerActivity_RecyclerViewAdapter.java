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
import com.xcash.utils.database.entity.AddressBook;
import com.xcash.wallet.R;

import java.util.List;


public class AddressManagerActivity_RecyclerViewAdapter extends UnLoadMoreRecyclerViewAdapter {

    private BaseActivity baseActivity;
    private RecyclerView recyclerView;
    private OnAddressManagerListener onAddressManagerListener;

    public AddressManagerActivity_RecyclerViewAdapter(BaseActivity baseActivity, RecyclerView recyclerView, List<ViewItem> datas) {
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
            return R.layout.activity_address_manager_item;
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
            final AddressBook addressBook = (AddressBook) viewItem.getModel();
            if (addressBook != null) {
                TextView textViewNotes = (TextView) holder.getView(R.id.textViewNotes);
                TextView textViewAddress = (TextView) holder.getView(R.id.textViewAddress);

                textViewNotes.setText(addressBook.getNotes());
                textViewAddress.setText(addressBook.getAddress());

                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (onAddressManagerListener != null) {
                            onAddressManagerListener.onItemSelect(addressBook);
                        }
                    }
                });
                holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View v) {
                        if (onAddressManagerListener != null) {
                            onAddressManagerListener.onLongClick(addressBook);
                        }
                        return true;
                    }
                });
            }
        }
    }

    public OnAddressManagerListener getOnAddressManagerListener() {
        return onAddressManagerListener;
    }

    public void setOnAddressManagerListener(OnAddressManagerListener onAddressManagerListener) {
        this.onAddressManagerListener = onAddressManagerListener;
    }

    public interface OnAddressManagerListener {

        void onItemSelect(AddressBook addressBook);

        void onLongClick(AddressBook addressBook);

    }

}
