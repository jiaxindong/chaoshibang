package com.yifeng.chaoshibang;

import android.app.Application;
import android.app.Service;
import android.content.Context;
import android.os.Vibrator;

import com.baidu.mapapi.SDKInitializer;
import com.yifeng.chaoshibang.service.BaiduLocationService;
import com.yifeng.chaoshibang.utils.WriteLog;

/**
 * Created by 201508270170 on 2015/10/30.
 */
public class ChaoshibangApplication extends Application {

    private static Context context;
    public BaiduLocationService locationService;
    public Vibrator mVibrator;
    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();  //获取一个应用级别的Context
        /***
         * 初始化定位sdk，建议在Application中创建
         */
        locationService = new BaiduLocationService(context);
        mVibrator =(Vibrator)context.getSystemService(Service.VIBRATOR_SERVICE);
        WriteLog.getInstance().init(); // 初始化日志
        SDKInitializer.initialize(context);
    }

    public static Context getContext() {
        return context;
    }
}
