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
package com.xcash.wallet.fragment;

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

 import com.xcash.adapters.recyclerviewadapter.AddressManagerActivity_RecyclerViewAdapter;
 import com.xcash.base.recyclerviewlibrary.models.ViewItem;
 import com.xcash.base.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
 import com.xcash.models.local.KeyValueItem;
 import com.xcash.utils.CoroutineHelper;
 import com.xcash.utils.database.AppDatabase;
 import com.xcash.utils.database.entity.AddressBook;
 import com.xcash.wallet.AddressManagerActivity;
 import com.xcash.wallet.MainActivity;
 import com.xcash.wallet.R;
 import com.xcash.wallet.ReceiveActivity;
 import com.xcash.wallet.TheApplication;
 import com.xcash.wallet.uihelp.ActivityHelp;
 import com.xcash.wallet.uihelp.PopupWindowHelp;

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
                        showMore(imageViewMore);
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
        Intent intent = new Intent(getBaseActivity(), ReceiveActivity.class);
        intent.putExtra(ActivityHelp.ADDRESS_KEY, addressBook.getAddress());
        startActivity(intent);
    }

    private void showMore(View view) {
        final int[] locations = new int[2];
        view.getLocationOnScreen(locations);
        List<KeyValueItem> keyValueItems = new ArrayList<KeyValueItem>();
        String show_more_tips = getString(R.string.show_more_tips);
        keyValueItems.add(new KeyValueItem(show_more_tips, show_more_tips));
        PopupWindowHelp.showPopupWindowMenuListViewMore(getBaseActivity(), view.getRootView(), view, 0, locations[1] + view.getHeight(), keyValueItems, 2, new AdapterView.OnItemClickListener() {
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
