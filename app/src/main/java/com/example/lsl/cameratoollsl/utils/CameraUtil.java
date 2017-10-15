package com.example.lsl.cameratoollsl.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.hardware.Camera;

/**
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
}
