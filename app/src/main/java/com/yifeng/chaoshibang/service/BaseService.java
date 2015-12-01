package com.yifeng.chaoshibang.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.yifeng.chaoshibang.utils.LogUtil;

/**
 * Created by 201508270170 on 2015/12/1.
 */
public class BaseService extends Service {
    @Override
    public void onCreate() {
        super.onCreate();
        LogUtil.d("Service", "OnCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        LogUtil.d("Service", "onDestroy");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        LogUtil.d("Service", "onStartCommand");
        return super.onStartCommand(intent, flags, startId);
    }

    @Override
    public boolean onUnbind(Intent intent) {
        LogUtil.d("Service", "onUnbind");
        return super.onUnbind(intent);
    }

    @Override
    public IBinder onBind(Intent intent) {
        LogUtil.d("Service", "onBind");
        return null;
    }
}
