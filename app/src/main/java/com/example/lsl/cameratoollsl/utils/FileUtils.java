package com.example.lsl.cameratoollsl.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Point;
import android.os.Environment;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

/**
 * Created by lsl on 2017/10/13.
 */

public class FileUtils {

    /**
     * 保存图片并对图片进行旋转
     *
     * @param data
     * @throws IOException
     */
    public static String savePic(byte[] data) throws IOException {
        File pics = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "images");
        if (!pics.exists()) {
            pics.mkdirs();
        }
        File file = new File(pics, System.currentTimeMillis() + ".jpg");
        Log.e("info---ssssss->", file.getAbsolutePath());
//        FileOutputStream fileOutputStream = new FileOutputStream(file);

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        Log.w("info--->", "图片的宽高:" + bitmap.getWidth() + "---:" + bitmap.getHeight());
        //图像翻转
//        bitmap = ImgUtil.getScale(bitmap, 1080, 1650);

//        Bitmap newBitmap = ImgUtil.setRotate(bitmap, 90f);

        saveFile(bitmap, file);
//        newBitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

        bitmap.recycle();
//        newBitmap.recycle();

        return file.getAbsolutePath();
    }

    /**
     * 保存文件
     *
     * @param bitmap
     * @return
     */
    public static String saveFile(Bitmap bitmap, File file) throws IOException {
        FileOutputStream fileOutputStream = new FileOutputStream(file);
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);
        fileOutputStream.close();
        return file.getAbsolutePath();
    }

    /**
     * 保存圆形区域图片
     *
     * @param data
     * @param circlepoint
     * @param r
     * @throws FileNotFoundException
     */
    public static void saveCirclePic(byte[] data, Point circlepoint, int r) throws IOException {
        File pics = new File(Environment.getExternalStorageDirectory().getAbsolutePath() + File.separator + "images");
        if (!pics.exists()) {
            pics.mkdirs();
        }
        File file = new File(pics, System.currentTimeMillis() + ".jpg");

        FileOutputStream fileOutputStream = new FileOutputStream(file);

        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);

        Bitmap newBitmap = ImgUtil.getCircleBitmap(bitmap, circlepoint.x, circlepoint.y, r);
        Bitmap bitmap1 = ImgUtil.setRotate(newBitmap, 90f);

        bitmap1.compress(Bitmap.CompressFormat.JPEG, 100, fileOutputStream);

        bitmap.recycle();
        newBitmap.recycle();
        bitmap1.recycle();
        fileOutputStream.close();
    }


}
