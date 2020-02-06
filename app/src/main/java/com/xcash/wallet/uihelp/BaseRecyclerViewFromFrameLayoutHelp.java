/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.wallet.uihelp;

import android.view.View;
import android.widget.TextView;

import com.xcash.base.BaseActivity;
import com.xcash.base.recyclerviewlibrary.views.BaseRecyclerViewFromFrameLayout;
import com.xcash.wallet.R;

public class BaseRecyclerViewFromFrameLayoutHelp {

    public static void setEmptyTransactionTips(BaseActivity baseActivity, BaseRecyclerViewFromFrameLayout baseRecyclerViewFromFrameLayout) {
        View view = baseRecyclerViewFromFrameLayout.getFrameLayoutEmpty();
        TextView textView = view.findViewById(R.id.textView);
        textView.setText(baseActivity.getString(R.string.layout_transaction_item_empty_tips));
    }

}
