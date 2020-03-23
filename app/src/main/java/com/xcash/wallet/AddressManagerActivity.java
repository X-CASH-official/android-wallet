/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.wallet;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.xcash.adapters.recyclerviewadapter.AddressManagerActivity_RecyclerViewAdapter;
import com.xcash.base.BaseActivity;
import com.xcash.base.recyclerviewlibrary.models.ViewItem;
import com.xcash.base.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
import com.xcash.utils.ClipboardTool;
import com.xcash.utils.CoroutineHelper;
import com.xcash.utils.database.AppDatabase;
import com.xcash.utils.database.entity.AddressBook;
import com.xcash.wallet.uihelp.ActivityHelp;
import com.xcash.wallet.uihelp.PopupWindowHelp;

import java.util.ArrayList;
import java.util.List;

public class AddressManagerActivity extends NewBaseActivity {

    private RelativeLayout relativeLayoutRoot;
    private ImageView imageViewBack;
    private TextView textViewTitle;
    private ImageView imageViewRight;
    private BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout;

    private View.OnClickListener onClickListener;
    private CoroutineHelper coroutineHelper = new CoroutineHelper();
    private AddressManagerActivity_RecyclerViewAdapter addressManagerActivity_RecyclerViewAdapter;
    private boolean choose_address = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_address_manager);
        Intent intent = getIntent();
        choose_address = intent.getBooleanExtra(ActivityHelp.CHOOSE_ADDRESS_KEY, false);
        initAll();
    }

    @Override
    protected void initHandler() {
        handler = new Handler();
    }

    @Override
    protected void initUi() {
        relativeLayoutRoot = (RelativeLayout) findViewById(R.id.relativeLayoutRoot);
        imageViewBack = (ImageView) findViewById(R.id.imageViewBack);
        textViewTitle = (TextView) findViewById(R.id.textViewTitle);
        imageViewRight = (ImageView) findViewById(R.id.imageViewRight);
        baseRecyclerViewFromFrameLayout = (BaseRecyclerViewFromFrameLayout) findViewById(R.id.baseRecyclerViewFromFrameLayout);
        initBaseRecyclerViewFromFrameLayout();
        onClickListener();
    }

    @Override
    protected void initConfigUi() {
        textViewTitle.setText(R.string.activity_address_manager_textViewTitle_text);
        imageViewRight.setVisibility(View.VISIBLE);
        imageViewRight.setImageResource(R.mipmap.normal_add);
    }

    @Override
    protected void initHttp() {

    }

    @Override
    protected void initOther() {

    }

    private void initBaseRecyclerViewFromFrameLayout() {
        TheApplication.setColorSchemeColors(AddressManagerActivity.this, baseRecyclerViewFromFrameLayout.getSwipeRefreshLayout());
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
                    case R.id.imageViewBack:
                        doBack();
                        break;
                    case R.id.imageViewRight:
                        addAddress();
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        imageViewRight.setOnClickListener(onClickListener);
    }

    private void initOrRefreshAdapter(List<ViewItem> viewItems) {
        if (viewItems == null) {
            viewItems = new ArrayList<ViewItem>();
        }
        if (addressManagerActivity_RecyclerViewAdapter == null) {
            addressManagerActivity_RecyclerViewAdapter = new AddressManagerActivity_RecyclerViewAdapter(AddressManagerActivity.this, baseRecyclerViewFromFrameLayout.getRecyclerView(), viewItems);
            addressManagerActivity_RecyclerViewAdapter.setOnAddressManagerListener(new AddressManagerActivity_RecyclerViewAdapter.OnAddressManagerListener() {
                @Override
                public void onItemSelect(AddressBook addressBook) {
                    selectAddress(addressBook);
                }

                @Override
                public void onLongClick(AddressBook addressBook) {
                    deleteAddress(addressBook);
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
        if (choose_address) {
            Intent intent = new Intent();
            intent.putExtra(ActivityHelp.REQUEST_ADDRESS_KEY, addressBook.getAddress());
            intent.putExtra(ActivityHelp.REQUEST_NOTES_KEY, addressBook.getNotes());
            intent.putExtra(ActivityHelp.REQUEST_SYMBOL_KEY, addressBook.getSymbol());
            setResult(Activity.RESULT_OK, intent);
            finish();
        } else {
            Intent intent = new Intent(AddressManagerActivity.this, ReceiveActivity.class);
            intent.putExtra(ActivityHelp.ADDRESS_KEY, addressBook.getAddress());
            startActivity(intent);
        }
    }

    private void addAddress() {
        Intent intent = new Intent(AddressManagerActivity.this,
                AddressAddActivity.class);
        startActivityForResult(intent, ActivityHelp.REQUEST_CODE_ADD_ADDRESS);
    }

    private void saveAddress(final String address, final String notes, final String symbol) {
        if (address == null || notes == null) {
            return;
        }
        coroutineHelper.launch(new CoroutineHelper.OnCoroutineListener<Boolean>() {
            @Override
            public Boolean runOnIo() {
                boolean result = false;
                try {
                    AddressBook addressBook = new AddressBook();
                    addressBook.setAddress(address);
                    addressBook.setNotes(notes);
                    addressBook.setSymbol(symbol);
                    AppDatabase.getInstance().addressBookDao().insertAddressBooks(addressBook);
                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            public void overRunOnMain(Boolean result) {
                if (!result) {
                    BaseActivity.showShortToast(AddressManagerActivity.this, getString(R.string.activity_address_manager_addAddressError_tips));
                } else {
                    BaseActivity.showShortToast(AddressManagerActivity.this, getString(R.string.activity_address_manager_addAddressSuccess_tips));
                    doRefresh();
                    MainActivity.doRefreshAddressIfActivityExist();
                }
            }
        });
    }

    private void deleteAddress(final AddressBook addressBook) {
        if (addressBook == null) {
            return;
        }
        PopupWindowHelp.showPopupWindowNormalTips(AddressManagerActivity.this, relativeLayoutRoot, null, getString(R.string.activity_wallet_manager_delete_address_tips), new PopupWindowHelp.OnShowPopupWindowNormalTipsListener() {
            @Override
            public void okClick(PopupWindow popupWindow, View view) {
                popupWindow.dismiss();
                coroutineHelper.launch(new CoroutineHelper.OnCoroutineListener<Boolean>() {
                    @Override
                    public Boolean runOnIo() {
                        boolean result = false;
                        try {
                            AppDatabase.getInstance().addressBookDao().deleteAddressBook(addressBook);
                            result = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return result;
                    }

                    @Override
                    public void overRunOnMain(Boolean result) {
                        if (!result) {
                            BaseActivity.showShortToast(AddressManagerActivity.this, getString(R.string.activity_address_manager_deleteAddressError_tips));
                        } else {
                            BaseActivity.showShortToast(AddressManagerActivity.this, getString(R.string.activity_address_manager_deleteAddressSuccess_tips));
                            doRefresh();
                            MainActivity.doRefreshAddressIfActivityExist();
                        }
                    }
                });
            }
        });
    }

    public void doRefresh() {
        baseRecyclerViewFromFrameLayout.autoRefresh(handler, TheApplication.AUTOREFRESHDELAY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ActivityHelp.REQUEST_CODE_ADD_ADDRESS && resultCode == RESULT_OK && data != null) {
            String address = data.getStringExtra(ActivityHelp.REQUEST_ADDRESS_KEY);
            String notes = data.getStringExtra(ActivityHelp.REQUEST_NOTES_KEY);
            String symbol = data.getStringExtra(ActivityHelp.REQUEST_SYMBOL_KEY);
            saveAddress(address, notes, symbol);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void doBack() {
        super.doBack();
        Intent intent = new Intent();
        setResult(RESULT_CANCELED, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        coroutineHelper.onDestroy();
    }

}
