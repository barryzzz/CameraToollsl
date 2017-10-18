package com.example.lsl.cameratoollsl.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;

/**
 * 相机工具类
 * Created by lsl on 2017/10/13.
 */

public class CameraUtil {
    /**
     * 判断是否有相机
     *
     * @param context
     * @return
     */
    public static boolean hasCameraDevices(Context context) {
        return context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA);
    }

    /**
     * 判断是够支持自动对焦
     *
     * @param context
     * @return
     */
    public static boolean isAutoFocusSuppored(Camera.Parameters context) {

        return false;
    }

    /**
     * 获取相机实例
     *
     * @return
     */
    public static Camera getCamera() {
        Camera camera = null;
        try {
            camera = Camera.open();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return camera;
    }


    /**
     * 对焦区域坐标转换
     *
     * @param x
     * @param y
     * @param coefficient
     * @param width
     * @param height
     * @return
     */
    public static Rect calculateTapArea(float x, float y, float coefficient, int width, int height) {
        float focusAreaSize = 300;
        int areaSize = Float.valueOf(focusAreaSize * coefficient).intValue();
        int centerX = (int) (x / width * 2000 - 1000);
        int centerY = (int) (y / height * 2000 - 1000);

        int halfAreaSize = areaSize / 2;
        RectF rectF = new RectF(clamp(centerX - halfAreaSize, -1000, 1000)
                , clamp(centerY - halfAreaSize, -1000, 1000)
                , clamp(centerX + halfAreaSize, -1000, 1000)
                , clamp(centerY + halfAreaSize, -1000, 1000));
        return new Rect(Math.round(rectF.left), Math.round(rectF.top), Math.round(rectF.right), Math.round(rectF.bottom));
    }

    private static int clamp(int x, int min, int max) {
        if (x > max) {
            return max;
        }
        if (x < min) {
            return min;
        }
        return x;
    }
}
