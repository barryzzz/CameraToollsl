package com.example.lsl.cameratoollsl.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 时间工具类
 * Created by lsl on 17-10-15.
 */

public class TimeUtils {
    private TimeUtils(){
        /* cannot be instantiated */
    }
    public static String pareTime(long time, String format) {
        SimpleDateFormat dateFormat = new SimpleDateFormat(format);
        return dateFormat.format(new Date(time));
    }

    public static String pareTime(long time) {
        return pareTime(time, "yyyy年MM月dd日");
    }
}
