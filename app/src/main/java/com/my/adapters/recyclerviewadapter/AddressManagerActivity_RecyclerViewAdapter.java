/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.adapters.recyclerviewadapter;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.my.base.BaseActivity;
import com.my.base.recyclerviewlibrary.adapters.UnLoadMoreRecyclerViewAdapter;
import com.my.base.recyclerviewlibrary.models.ViewItem;
import com.my.utils.database.entity.AddressBook;
import com.my.xwallet.R;

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
