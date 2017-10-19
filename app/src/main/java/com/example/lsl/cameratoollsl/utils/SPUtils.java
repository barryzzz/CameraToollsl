package com.example.lsl.cameratoollsl.utils;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * 文件临时配置文件
 * Created by Administrator on 2017/10/18.
 */

public class SPUtils {
    /**
     * 保存最后一次拍照照片存储路径
     *
     * @param context
     * @param path
     */
    public static void savePath(Context context, String path) {
        SharedPreferences preferences = context.getSharedPreferences("lastImg", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("path", path);
        editor.apply();
    }

    /**
     * 获取最后一次拍照照片存储路径
     *
     * @param context
     * @return 如果没有返回""
     */
    public static String getPath(Context context) {
        SharedPreferences preferences = context.getSharedPreferences("lastImg", Context.MODE_PRIVATE);
        String path = preferences.getString("path", "");
        return path;
    }
}
