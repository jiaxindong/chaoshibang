package com.yifeng.chaoshibang.activity;

import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;

import com.yifeng.chaoshibang.utils.LogUtil;

/**
 * Created by jxd on 2015/10/30.
 */
public class BaseActivity extends ActionBarActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LogUtil.d("BaseActivity", getClass().getSimpleName());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }
}
