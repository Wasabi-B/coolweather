package com.coolweather.android.util;

import okhttp3.OkHttpClient;
import okhttp3.Request;

/**
 * 项目名：     CoolWeather
 * 包名：       com.coolweather.android.util
 * 文件名：     HttpUtil
 * 创建者：     loovee
 * 创建时间：   2017/8/25
 * 描述：      TODO
 */

public class HttpUtil {

    public static void senOkHttpRequest(String address,okhttp3.Callback callback){
        OkHttpClient client =  new OkHttpClient();
        Request request = new Request.Builder()
                .url(address)
                .build();
        client.newCall(request).enqueue(callback);
    }
}
