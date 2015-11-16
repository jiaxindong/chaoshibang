package com.yifeng.chaoshibang.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.yifeng.chaoshibang.R;

/**
 * Created by jiaxindong on 2015/11/13.
 */
public class CustomToolbarView extends FrameLayout {

    private Button btn_back;
    private TextView tv_location;
    private ImageView iv_locate;
    private ImageView iv_edit_place;
    private EditText et_edit_place;

    public CustomToolbarView(Context context, AttributeSet attrs) {
        super(context, attrs);
        LayoutInflater.from(context).inflate(R.layout.toolbar, this);
        btn_back = (Button) findViewById(R.id.activity_back);
        tv_location = (TextView) findViewById(R.id.activity_title);
        iv_edit_place = (ImageView) findViewById(R.id.iv_locate);
        iv_edit_place = (ImageView) findViewById(R.id.iv_edit_place);
        et_edit_place = (EditText) findViewById(R.id.et_edit_place);
    }

    public void setTVLocationText(String text) {
        tv_location.setText(text);
    }
}
