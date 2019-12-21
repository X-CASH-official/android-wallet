/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.my.xwallet.uihelp;

import android.view.View;
import android.widget.TextView;

import com.my.base.BaseActivity;
import com.my.base.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
import com.my.xwallet.R;

public class BaseRecyclerViewFromFrameLayoutHelp {

    public static void setEmptyTransactionTips(BaseActivity baseActivity, BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout) {
        View view = baseRecyclerViewFromFrameLayout.getFrameLayoutEmpty();
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(baseActivity.getString(R.string.layout_transaction_item_empty_tips));
    }
}
