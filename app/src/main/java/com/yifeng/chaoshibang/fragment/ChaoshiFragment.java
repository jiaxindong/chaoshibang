package com.yifeng.chaoshibang.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.yifeng.chaoshibang.R;
import com.yifeng.chaoshibang.activity.BaseActivity;
import com.yifeng.chaoshibang.widget.HomeAdverViewPager;

import java.util.ArrayList;

/**
 * Created by jiaxindong on 2015/11/16.
 */
public class ChaoshiFragment extends BaseFragment {

    BaseActivity activity;
    private HomeAdverViewPager viewPager;
    private int[] adverResources = { 0, 1, 2};

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View chaoshiLayout = inflater.inflate(R.layout.chaoshi_view, container, false);
        viewPager = (HomeAdverViewPager) chaoshiLayout.findViewById(R.id.adver_container);

        return chaoshiLayout;
    }

    /**
     * init完成相应Fragment的初始化
     */
    @Override
    public void init() {
        activity = (BaseActivity) getActivity();
        initViewPager();
    }

    private void initViewPager() {
        ImageView imageView1 = new ImageView(activity);
        imageView1.setImageResource(adverResources[0]);
        ImageView imageView2 = new ImageView(activity);
        imageView2.setImageResource(adverResources[1]);
        ImageView imageView3 = new ImageView(activity);
        imageView3.setImageResource(adverResources[2]);
        final ArrayList<ImageView> views = new ArrayList<ImageView>();
        views.add(imageView1);
        views.add(imageView2);
        views.add(imageView3);
    }
}
