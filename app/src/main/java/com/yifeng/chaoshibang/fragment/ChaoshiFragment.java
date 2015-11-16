package com.yifeng.chaoshibang.fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.yifeng.chaoshibang.R;

/**
 * Created by jiaxindong on 2015/11/16.
 */
public class ChaoshiFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View chaoshiLayout = inflater.inflate(R.layout.chaoshi_view, container, false);
        return chaoshiLayout;
    }
}
