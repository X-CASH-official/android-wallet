/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.xwallet;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.my.adapters.recyclerviewadapter.NodeManagerActivity_RecyclerViewAdapter;
import com.my.base.BaseActivity;
import com.my.base.recyclerviewlibrary.models.ViewItem;
import com.my.base.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
import com.my.utils.CoroutineHelper;
import com.my.utils.WalletServiceHelper;
import com.my.utils.database.AppDatabase;
import com.my.utils.database.entity.Node;
import com.my.utils.database.entity.Wallet;
import com.my.xwallet.aidl.manager.XManager;
import com.my.xwallet.uihelp.ActivityHelp;
import com.my.xwallet.uihelp.PopupWindowHelp;

import java.util.ArrayList;
import java.util.List;

public class NodeManagerActivity extends NewBaseActivity {

    private Wallet wallet;

    private RelativeLayout relativeLayoutRoot;
    private ImageView imageViewBack;
    private TextView textViewTitle;
    private ImageView imageViewRight;
    private BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout;

    private View.OnClickListener onClickListener;
    private CoroutineHelper coroutineHelper = new CoroutineHelper();
    private NodeManagerActivity_RecyclerViewAdapter nodeManagerActivity_RecyclerViewAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_node_manager);
        Intent intent = getIntent();
        wallet = (Wallet) intent.getSerializableExtra(ActivityHelp.WALLET_KEY);
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
        textViewTitle.setText(R.string.activity_node_manager_textViewTitle_text);
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
        TheApplication.setColorSchemeColors(NodeManagerActivity.this, baseRecyclerViewFromFrameLayout.getSwipeRefreshLayout());
        SwipeRefreshLayout.OnRefreshListener onRefreshListener = new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadNodes();
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
                        addNode();
                        break;
                    default:
                        break;
                }
            }
        };
        imageViewBack.setOnClickListener(onClickListener);
        imageViewRight.setOnClickListener(onClickListener);
    }

    private void initOrRefreshAdapter(List<ViewItem> viewItems, int activeIndex) {
        if (viewItems == null) {
            viewItems = new ArrayList<ViewItem>();
        }
        if (nodeManagerActivity_RecyclerViewAdapter == null) {
            nodeManagerActivity_RecyclerViewAdapter = new NodeManagerActivity_RecyclerViewAdapter(NodeManagerActivity.this, baseRecyclerViewFromFrameLayout.getRecyclerView(), viewItems);
            nodeManagerActivity_RecyclerViewAdapter.setSelectPosition(activeIndex);
            nodeManagerActivity_RecyclerViewAdapter.setOnNodeManagerListener(new NodeManagerActivity_RecyclerViewAdapter.OnNodeManagerListener() {
                @Override
                public void onItemSelect(Node node) {
                    updateNode(node);
                }

                @Override
                public void onLongClick(Node node) {
                    deleteNode(node);
                }
            });
            baseRecyclerViewFromFrameLayout.setAdapter(nodeManagerActivity_RecyclerViewAdapter);
        } else {
            nodeManagerActivity_RecyclerViewAdapter.setSelectPosition(activeIndex);
            TheApplication.replaceAllFormBaseRecyclerViewAdapter(nodeManagerActivity_RecyclerViewAdapter, viewItems, baseRecyclerViewFromFrameLayout.getRecyclerView());
        }
    }

    private void loadNodes() {
        if (wallet == null) {
            return;
        }
        coroutineHelper.launch(new CoroutineHelper.OnCoroutineListener<List<Node>>() {
            @Override
            public List<Node> runOnIo() {
                List<Node> nodes = null;
                try {
                    XManager.getInstance().insertNodes();
                    nodes = AppDatabase.getInstance().nodeDao().loadNodesBySymbol(wallet.getSymbol());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return nodes;
            }

            @Override
            public void overRunOnMain(List<Node> nodes) {
                List<ViewItem> viewItems = new ArrayList<ViewItem>();
                int activeIndex = -1;
                if (nodes != null) {
                    for (int i = 0; i < nodes.size(); i++) {
                        Node node = nodes.get(i);
                        ViewItem viewItem = new ViewItem(ViewItem.VIEW_TYPE_NORMAL_ITEM_TYPE1, node);
                        if (node.isActive()) {
                            activeIndex = i;
                        }
                        viewItems.add(viewItem);
                    }
                }
                initOrRefreshAdapter(viewItems, activeIndex);
            }
        });
    }

    private void addNode() {
        Intent intent = new Intent(NodeManagerActivity.this,
                NodeAddActivity.class);
        startActivityForResult(intent, ActivityHelp.REQUEST_CODE_ADD_NODE);
    }

    private void saveNode(final String ip, final String port, final String username,final String password, final String symbol) {
        if (ip == null || port == null) {
            return;
        }
        coroutineHelper.launch(new CoroutineHelper.OnCoroutineListener<Boolean>() {
            @Override
            public Boolean runOnIo() {
                boolean result = false;
                try {
                    Node node = new Node();
                    node.setUrl(ip + ":" + port);
                    node.setUsername(username);
                    node.setPassword(password);
                    node.setActive(false);
                    node.setSymbol(symbol);
                    AppDatabase.getInstance().nodeDao().insertNodes(node);
                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            public void overRunOnMain(Boolean result) {
                if (!result) {
                    BaseActivity.showShortToast(NodeManagerActivity.this, getString(R.string.activity_node_add_addNodeError_tips));
                } else {
                    BaseActivity.showShortToast(NodeManagerActivity.this, getString(R.string.activity_node_add_addNodeSuccess_tips));
                    doRefresh();
                }
            }
        });
    }

    private void updateNode(final Node node) {
        if (wallet == null) {
            return;
        }
        coroutineHelper.launch(new CoroutineHelper.OnCoroutineListener<Boolean>() {
            @Override
            public Boolean runOnIo() {
                boolean result = false;
                try {
                    List<Node> nodes = AppDatabase.getInstance().nodeDao().loadNodesBySymbol(wallet.getSymbol());
                    if (nodes != null) {
                        for (int i = 0; i < nodes.size(); i++) {
                            Node theNode = nodes.get(i);
                            if (node.getId() == theNode.getId()) {
                                theNode.setActive(true);
                            } else {
                                theNode.setActive(false);
                            }
                        }
                        AppDatabase.getInstance().nodeDao().updateNodes(nodes.toArray(new Node[]{}));
                    }
                    result = true;
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return result;
            }

            @Override
            public void overRunOnMain(Boolean result) {
                if (!result) {
                    BaseActivity.showShortToast(NodeManagerActivity.this, getString(R.string.activity_node_manager_changeNodeError_tips));
                } else {
                    TheApplication.getTheApplication().getWalletServiceHelper().setDaemon(NodeManagerActivity.this,node.getUrl(),node.getUsername(),node.getPassword(),new WalletServiceHelper.OnSetDaemonListener(){
                        @Override
                        public void onSuccess(String tips) {
                            BaseActivity.showShortToast(NodeManagerActivity.this, getString(R.string.activity_node_manager_changeNodeSuccess_tips));
                        }

                        @Override
                        public void onError(String error) {
                            BaseActivity.showShortToast(NodeManagerActivity.this, error);
                        }
                    });
                    finish();
                }
            }
        });
    }

    private void deleteNode(final Node node) {
        if (node == null) {
            return;
        }
        PopupWindowHelp.showPopupWindowNormalTips(NodeManagerActivity.this, relativeLayoutRoot, null, getString(R.string.activity_node_delete_node_tips), new PopupWindowHelp.OnShowPopupWindowNormalTipsListener() {
            @Override
            public void okClick(PopupWindow popupWindow, View view) {
                popupWindow.dismiss();
                coroutineHelper.launch(new CoroutineHelper.OnCoroutineListener<Boolean>() {
                    @Override
                    public Boolean runOnIo() {
                        boolean result = false;
                        try {
                            AppDatabase.getInstance().nodeDao().deleteNode(node);
                            result = true;
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        return result;
                    }

                    @Override
                    public void overRunOnMain(Boolean result) {
                        if (!result) {
                            BaseActivity.showShortToast(NodeManagerActivity.this, getString(R.string.activity_node_manager_deleteNodeError_tips));
                        } else {
                            BaseActivity.showShortToast(NodeManagerActivity.this, getString(R.string.activity_node_manager_deleteNodeSuccess_tips));
                            doRefresh();
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
        if (requestCode == ActivityHelp.REQUEST_CODE_ADD_NODE && resultCode == RESULT_OK && data != null) {
            String ip = data.getStringExtra(ActivityHelp.REQUEST_NODE_IP_KEY);
            String port = data.getStringExtra(ActivityHelp.REQUEST_NODE_PORT_KEY);
            String username = data.getStringExtra(ActivityHelp.REQUEST_NODE_USERNAME_KEY);
            String password = data.getStringExtra(ActivityHelp.REQUEST_NODE_PASSWORD_KEY);
            String symbol = data.getStringExtra(ActivityHelp.REQUEST_SYMBOL_KEY);
            saveNode(ip, port, username, password, symbol);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void doBack() {
        super.doBack();
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        coroutineHelper.onDestroy();
    }

}
