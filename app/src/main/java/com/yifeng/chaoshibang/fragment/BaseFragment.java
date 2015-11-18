package com.yifeng.chaoshibang.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yifeng.chaoshibang.activity.BaseActivity;

/**
 * Created by jiaxindong on 2015/11/18.
 */
public abstract class BaseFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        init();
    }

    /**
     * 各个子类，重写该方法，来完成初始化
     */
    public abstract void init();

    protected void gotoActivity(Class<? extends Activity> clazz) {
        BaseActivity activity = (BaseActivity) getActivity();
        if (activity != null) {
            activity.gotoActivity(clazz);
        }
    }
}
