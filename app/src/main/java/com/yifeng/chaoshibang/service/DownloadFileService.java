package com.yifeng.chaoshibang.service;

import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;

import com.yifeng.chaoshibang.Config;
import com.yifeng.chaoshibang.interfaces.OnProgressListener;
import com.yifeng.chaoshibang.utils.FileUtil;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class DownloadFileService extends BaseService {

    private String urlPath="";
    //文件存储
    private File updateFile = null;
    private OnProgressListener onProgressListener;

    public DownloadFileService() {
    }

    public void setOnProgressListener(OnProgressListener onProgressListener) {
        this.onProgressListener = onProgressListener;
    }

    /**
     * 返回一个Binder对象
     */
    @Override
    public IBinder onBind(Intent intent) {
        super.onBind(intent);
        return new DownloadBinder();
    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        urlPath = intent.getStringExtra("url");
        //创建文件
        if(FileUtil.haveSDCard()) {
            updateFile = FileUtil.getFile(Config.sd_folder_name, Config.sd_apk_name);
        }
        //开启一个新的线程下载，如果使用Service同步下载，会导致ANR问题，Service本身也会阻塞
        new Thread(new DownloadRunnable()).start();
        return super.onStartCommand(intent, flags, startId);
    }

    class DownloadRunnable implements Runnable {
        @Override
        public void run() {
            HttpURLConnection httpURLConnection = null;
            InputStream input = null;
            FileOutputStream fos = null;
            try {
                URL url = new URL(urlPath);
                httpURLConnection = (HttpURLConnection) url.openConnection();
                httpURLConnection.setConnectTimeout(2000);
                httpURLConnection.setRequestMethod("GET");
                if(httpURLConnection.getResponseCode() == 200) {
                    int total = httpURLConnection.getContentLength();
                    if(onProgressListener != null) {
                        onProgressListener.onProgressInitialize(total);
                    }
                    input = httpURLConnection.getInputStream();
                    fos = new FileOutputStream(updateFile);
                    byte[] buffer = new byte[1024];
                    int len;
                    int progress = 0;
                    while((len = input.read(buffer)) != -1) {
                        fos.write(buffer, 0, len);
                        progress += len;
                        //进度发生变化通知调用方
                        if(onProgressListener != null){
                            onProgressListener.onProgressUpdate(progress);
                        }
                    }
                    fos.flush();
                    if(onProgressListener != null){
                        onProgressListener.onProgressComplete(updateFile);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    if(fos != null) {
                        fos.close();
                    }
                    if(input != null) {
                        input.close();
                    }
                    if(httpURLConnection != null) {
                        httpURLConnection.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class DownloadBinder extends Binder {
        /**
         * 获取当前Service的实例
         */
        public DownloadFileService getService() {
            return DownloadFileService.this;
        }
    }
}
