package com.yifeng.chaoshibang.interfaces;

/**
 * Created by jiaxindong on 2015/11/30.
 */
public interface OnProgressListener {
    void onProgressUpdate(int progress, int total);
    void onProgressComplete(boolean isCompleted);
}
