package com.yifeng.chaoshibang.activity;

import android.app.Activity;
import android.os.Bundle;

import com.yifeng.chaoshibang.utils.LogUtil;

/**
 * Created by jxd on 2015/10/30.
 */
public class BaseActivity extends Activity {
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
