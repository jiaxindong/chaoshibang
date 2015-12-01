package com.yifeng.chaoshibang.interfaces;

import java.io.File;

/**
 * Created by jiaxindong on 2015/11/30.
 */
public interface OnProgressListener {
    void onProgressInitialize(int total);
    void onProgressUpdate(int progress);
    void onProgressComplete(File file);
}
