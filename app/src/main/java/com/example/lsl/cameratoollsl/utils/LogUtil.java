package com.example.lsl.cameratoollsl.utils;

import android.util.Log;

import com.example.lsl.cameratoollsl.BuildConfig;

/**
 * 日志工具类
 * Created by Administrator on 2017/10/19.
 */

public class LogUtil {
    public static void e(String tag, String str) {
        if (BuildConfig.DEBUG)
            Log.e(tag, str);
    }

    public static void i(String tag, String str) {
        if (BuildConfig.DEBUG)
            Log.i(tag, str);
    }
}
