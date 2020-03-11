/**
 * Copyright (c) 2019 by snakeway
 * <p>
 * All rights reserved.
 */
package com.xcash.views;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.appcompat.widget.AppCompatEditText;

public class EditTextInNestedScrollView extends AppCompatEditText {

    public EditTextInNestedScrollView(Context context) {
        super(context);
    }

    public EditTextInNestedScrollView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public EditTextInNestedScrollView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (canScrollVertically(1) || canScrollVertically(-1)) {
            getParent().requestDisallowInterceptTouchEvent(true);
            if (event.getAction() == MotionEvent.ACTION_UP) {
                getParent().requestDisallowInterceptTouchEvent(false);
            }
        }
        return super.onTouchEvent(event);
    }

}