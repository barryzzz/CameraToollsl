package com.example.lsl.cameratoollsl.utils;

import android.view.MotionEvent;

/**
 * view相关工具类
 * Created by lsl on 17-10-15.
 */

public class ViewUtil {
    private ViewUtil() {
        /* cannot be instantiated */
    }

    /**
     * 计算两点之间的距离
     *
     * @param event
     * @return
     */
    public static double distanceBetweenFingers(MotionEvent event) {
        float disX = Math.abs(event.getX(0) - event.getX(1));
        float disY = Math.abs(event.getY(0) - event.getY(1));
        return Math.sqrt(disX * disX + disY * disY);
    }

}
