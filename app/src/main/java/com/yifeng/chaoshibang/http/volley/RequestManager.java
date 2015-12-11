package com.yifeng.chaoshibang.http.volley;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;

import java.util.Map;

/**
 * Created by jiaxindong on 2015/12/3.
 */
public class RequestManager {
    public static final String encodingType = "UTF-8";
    private static RequestQueue requestQueue;
    private static ImageLoader imageLoader;
    private static Map<String, String> requestHeaders;
    private static ImageLoader.ImageCache imageCache;

    private RequestManager() {

    }

}
