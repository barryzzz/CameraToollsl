package com.example.lsl.cameratoollsl.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.graphics.RectF;
import android.hardware.Camera;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

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

        return context.getSupportedFocusModes().contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
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

    /**
     * 获取最佳预览分辨率
     *
     * @param ratio 预览view的比例，注意h/w(ps:相机是水平预览的)
     * @return
     */
    public static Camera.Size getPreviewSize(Camera.Parameters parameters, float ratio) {
        Camera.Size defaultPreview = parameters.getPreviewSize();
        List<Camera.Size> sizes = parameters.getSupportedPreviewSizes();
        return findBestSize(sizes, defaultPreview, ratio);
    }


    /**
     * 获取最佳图片分辨率
     *
     * @param parameters
     * @param ratio
     * @return
     */

    public static Camera.Size getPictureSize(Camera.Parameters parameters, float ratio) {
        Camera.Size defaultPicture = parameters.getPictureSize();
        List<Camera.Size> sizes = parameters.getSupportedPictureSizes();
        return findBestSize(sizes, defaultPicture, ratio);
    }

    /**
     *
     * @param sizes
     * @param defalutSize
     * @param r
     * @return
     */
    public static Camera.Size findBestSize(List<Camera.Size> sizes, Camera.Size defalutSize, float r) {
        r = (int) (r * 100) / 100f;
        List<Camera.Size> temp = new ArrayList<>();
        for (Camera.Size s : sizes) {
            float ratio = (float) s.width / s.height;
            ratio = (int) (ratio * 100) / 100f;
            if (ratio == r) {
                temp.add(s);
            }
        }
        if (temp.size() == 0) {
            return defalutSize;
        }
        Collections.sort(temp, new Comparator<Camera.Size>() {
            @Override
            public int compare(Camera.Size a, Camera.Size b) {
                int aPixels = a.height * a.width;
                int bPixels = b.height * b.width;
                if (bPixels < aPixels) {
                    return -1;
                }
                if (bPixels > aPixels) {
                    return 1;
                }
                return 0;
            }
        });
        return temp.get(0);
    }

}
