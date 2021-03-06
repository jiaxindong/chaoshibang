package com.yifeng.chaoshibang.utils;

import android.util.Log;

/**
 * Created by jiaxindong on 2015/11/10.
 */
public class LogUtil {
    public static final int VERBOSE = 1;
    public static final int DEBUG = 2;
    public static final int INFO = 3;
    public static final int WARN = 4;
    public static final int ERROR = 5;
    public static final int NOTHING = 6;
    public static final int LEVEL = VERBOSE; //用来控制打印哪个级别的日志，当发布app时改为NOTHING即不再打印log

    public static void v(String tag, String msg) {
        if(LEVEL <= VERBOSE) {
            Log.v(tag, msg);
        }
    }
    public static void d(String tag, String msg) {
        if(LEVEL <= DEBUG) {
            Log.d(tag, msg);
        }
    }
    public static void i(String tag, String msg) {
        if(LEVEL <= INFO) {
            Log.i(tag, msg);
        }
    }
    public static void w(String tag, String msg) {
        if(LEVEL <= WARN) {
            Log.w(tag, msg);
        }
    }
    public static void e(String tag, String msg) {
        if(LEVEL <= ERROR) {
            Log.e(tag, msg);
        }
    }
}
