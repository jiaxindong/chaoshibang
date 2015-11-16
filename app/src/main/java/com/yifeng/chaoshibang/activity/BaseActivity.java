package com.yifeng.chaoshibang.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.yifeng.chaoshibang.utils.LogUtil;

/**
 * Created by jxd on 2015/10/30.
 */
public class BaseActivity extends AppCompatActivity {
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
