/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.xwallet.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.my.adapters.recyclerviewadapter.AddressManagerActivity_RecyclerViewAdapter;
import com.my.base.recyclerviewlibrary.models.ViewItem;
import com.my.base.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
import com.my.models.local.KeyValueItem;
import com.my.utils.ClipboardTool;
import com.my.utils.CoroutineHelper;
import com.my.utils.database.AppDatabase;
import com.my.utils.database.entity.AddressBook;
import com.my.xwallet.AddressManagerActivity;
import com.my.xwallet.MainActivity;
import com.my.xwallet.R;
import com.my.xwallet.TheApplication;
import com.my.xwallet.uihelp.PopupWindowHelp;

import java.util.ArrayList;
import java.util.List;

public class MainActivity_Fragment_Find extends BaseFragment {

    private View view;
    private RelativeLayout relativeLayoutContent;
    private ImageView imageViewMenu;
    private ImageView imageViewMore;
    private BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout;

    private boolean alreadyInitUi = true;
    private View.OnClickListener onClickListener;
    private CoroutineHelper coroutineHelper = new CoroutineHelper();
    private AddressManagerActivity_RecyclerViewAdapter addressManagerActivity_RecyclerViewAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.activity_main_content_fragment_find, container, false);
        initAll();
        return view;
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    @Override
    protected void initUi() {
        relativeLayoutContent = (RelativeLayout) view.findViewById(R.id.relativeLayoutContent);
        imageViewMenu = (ImageView) view.findViewById(R.id.imageViewMenu);
        imageViewMore = (ImageView) view.findViewById(R.id.imageViewMore);
        baseRecyclerViewFromFrameLayout = (BaseRecyclerViewFromFrameLayout) view.findViewById(R.id.baseRecyclerViewFromFrameLayout);
        initBaseRecyclerViewFromFrameLayout();
        alreadyInitUi = true;
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        adaptationStatusBar(relativeLayoutContent);
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {

    }

    private void initBaseRecyclerViewFromFrameLayout() {
        TheApplication.setColorSchemeColors(getBaseActivity(), baseRecyclerViewFromFrameLayout.getSwipeRefreshLayout());
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAddresses();
            }
        };
        baseRecyclerViewFromFrameLayout.setOnRefreshListener(onRefreshListener);
        baseRecyclerViewFromFrameLayout.autoRefresh(handler, TheApplication.AUTOREFRESHDELAY);
    }

    private void onClickListener() {
        onClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (view.getId()) {
                    case R.id.imageViewMenu:
                        ((MainActivity) getBaseActivity()).openDrawer();
                        break;
                    case R.id.imageViewMore:
                        showMore();
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewMenu.setOnClickListener(onClickListener);
        imageViewMore.setOnClickListener(onClickListener);
    }

    private void initOrRefreshAdapter(List<ViewItem> viewItems) {
        if (viewItems == null) {
            viewItems = new ArrayList<ViewItem>();
        }
        if (addressManagerActivity_RecyclerViewAdapter == null) {
            addressManagerActivity_RecyclerViewAdapter = new AddressManagerActivity_RecyclerViewAdapter(getBaseActivity(), baseRecyclerViewFromFrameLayout.getRecyclerView(), viewItems);
            addressManagerActivity_RecyclerViewAdapter.setOnAddressManagerListener(new AddressManagerActivity_RecyclerViewAdapter.OnAddressManagerListener() {
                @Override
                public void onItemSelect(AddressBook addressBook) {
                    selectAddress(addressBook);
                }

                @Override
                public void onLongClick(AddressBook addressBook) {

                }
            });
            baseRecyclerViewFromFrameLayout.setAdapter(addressManagerActivity_RecyclerViewAdapter);
        } else {
            TheApplication.replaceAllFormBaseRecyclerViewAdapter(addressManagerActivity_RecyclerViewAdapter, viewItems, baseRecyclerViewFromFrameLayout.getRecyclerView());
        }
    }

    private void loadAddresses() {
        coroutineHelper.launch(new CoroutineHelper.OnCoroutineListener<List<AddressBook>>() {
            @Override
            public List<AddressBook> runOnIo() {
                List<AddressBook> addressBooks = null;
                try {
                    addressBooks = AppDatabase.getInstance().addressBookDao().loadAddressBooks();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return addressBooks;
            }

            @Override
            public void overRunOnMain(List<AddressBook> addressBooks) {
                List<ViewItem> viewItems = new ArrayList<ViewItem>();
                if (addressBooks != null) {
                    for (int i = 0; i < addressBooks.size(); i++) {
                        AddressBook addressBook = addressBooks.get(i);
                        ViewItem viewItem = new ViewItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1, addressBook);
                        viewItems.add(viewItem);
                    }
                }
                initOrRefreshAdapter(viewItems);
            }
        });
    }

    private void selectAddress(AddressBook addressBook) {
        ClipboardTool.copyToClipboard(getBaseActivity(), addressBook.getAddress());
    }

    private void showMore() {
        final int[] locations = new int[2];
        imageViewMore.getLocationOnScreen(locations);
        List<KeyValueItem> keyValueItems = new ArrayList<KeyValueItem>();
        String show_more_tips = getString(R.string.show_more_tips);
        keyValueItems.add(new KeyValueItem(show_more_tips, show_more_tips));
        PopupWindowHelp.showPopupWindowMenuListViewMore(getBaseActivity(), imageViewMore.getRootView(), imageViewMore, 0, locations[1] + imageViewMore.getHeight(), keyValueItems, 2, new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (position == 0) {
                    Intent intent = new Intent(getBaseActivity(),
                            AddressManagerActivity.class);
                    getBaseActivity().startActivity(intent);
                }
            }
        });
    }

    public void doRefresh() {
        if (alreadyInitUi) {
            baseRecyclerViewFromFrameLayout.autoRefresh(handler, TheApplication.AUTOREFRESHDELAY);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        coroutineHelper.onDestroy();
    }

}
