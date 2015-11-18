package com.yifeng.chaoshibang.view;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.widget.EditText;

import com.yifeng.chaoshibang.R;

/**
 * Created by 201508270170 on 2015/11/18.
 */
public class CustomeSearchEditText extends EditText {

    private Drawable searchIcon;

    public CustomeSearchEditText(Context context) {
        super(context);
        init();
    }

    public CustomeSearchEditText(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public CustomeSearchEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        searchIcon = getCompoundDrawables()[2];
        if (searchIcon == null) {
            searchIcon = getResources().getDrawable(R.drawable.icon_search);
        }
        searchIcon.setBounds(0, 0, searchIcon.getIntrinsicWidth(), searchIcon.getIntrinsicHeight());
        setCompoundDrawablePadding((int) getResources().getDimension(R.dimen.margin_micro));
    }
}
