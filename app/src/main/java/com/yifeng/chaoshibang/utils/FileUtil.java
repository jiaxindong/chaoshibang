package com.yifeng.chaoshibang.utils;

import android.os.Environment;

import java.io.File;
import java.io.IOException;

/**
 * Created by jiaxindong on 2015/11/30.
 */
public class FileUtil {

    public static boolean haveSDCard(){
        if(Environment.MEDIA_MOUNTED.equals(Environment.getExternalStorageState())){
            return true;
        }
        return false;
    }

    public static File getFolder(String foldername){
        if(haveSDCard()){
            File folderDir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + foldername);
            if (!folderDir.exists()) {
                if (!folderDir.mkdirs()) return null;
            }
            return folderDir;
        }

        return null;
    }

    public static String getFolderString(String foldername){
        if(haveSDCard()){
            File folderDir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + foldername);
            if (!folderDir.exists()){
                if (!folderDir.mkdirs()) return null;
            }
            return folderDir.getPath();
        }

        return null;
    }

    public static File getFile(String foldername, String filename){
        if(haveSDCard()){
            File folderDir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + foldername);
            if (!folderDir.exists()) {
                if (!folderDir.mkdirs()) return null;
            }

            File file = new File(folderDir.getPath() + "/" + filename);
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return file;
        }
        return null;
    }

    public static String getFileString(String foldername, String filename){
        if(haveSDCard()){
            File folderDir = new File(Environment.getExternalStorageDirectory().getPath() + "/" + foldername);
            if (!folderDir.exists()) {
                if (!folderDir.mkdirs()) return null;
            }

            File file = new File(folderDir.getPath() + "/" + filename);
            if(!file.exists()){
                try {
                    file.createNewFile();
                } catch (IOException e) {
                    e.printStackTrace();
                    return null;
                }
            }
            return file.getPath();
        }
        return null;
    }
}
