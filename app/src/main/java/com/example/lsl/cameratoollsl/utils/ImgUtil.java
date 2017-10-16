package com.example.lsl.cameratoollsl.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.media.ThumbnailUtils;

/**
 * Created by lsl on 17-10-15.
 */

public class ImgUtil {

    /**
     * 获取一个圆形区域
     *
     * @param bitmap
     * @return
     */
    public static Bitmap getCircleBitmap(Bitmap bitmap, int x, int y, int r) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();
        Bitmap newBitmap = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);
        canvas.drawCircle(x, y, r, paint);
        paint.reset();
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, 0, 0, paint);
        return newBitmap;
    }

    /**
     * 获取一个矩形区域
     *
     * @param bitmap
     * @param rect
     * @return
     */
    public static Bitmap getRectBitmap(Bitmap bitmap, Rect rect) {
        return null;
    }

    /**
     * 获取一个缩略图
     *
     * @param fileapth
     * @return
     */
    public static Bitmap getThumbBitmap(String fileapth, int width, int heigth) {
        Bitmap bitmap = null;
        BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        bitmap = BitmapFactory.decodeFile(fileapth, options);
        options.inJustDecodeBounds = false;
        int w = options.outWidth;
        int h = options.outHeight;
        int beW = w / width;
        int beH = h / heigth;
        int be = 1;
        if (beW < beH) {
            be = beW;
        } else {
            be = beH;
        }
        if (be <= 0) {
            be = 1;
        }

        options.inSampleSize = be;
        bitmap = BitmapFactory.decodeFile(fileapth, options);
        bitmap = ThumbnailUtils.extractThumbnail(bitmap, width, heigth, ThumbnailUtils.OPTIONS_RECYCLE_INPUT);
        return bitmap;

    }

    /**
     * 翻转图片
     *
     * @param bitmap
     * @return
     */
    public static Bitmap setRotate(Bitmap bitmap, float degrees) {
        //图像翻转
        Matrix matrix = new Matrix();
        matrix.setRotate(degrees, bitmap.getWidth() / 2, bitmap.getHeight() / 2);
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), matrix, true);

        return newBitmap;
    }


    /**
     * 把图片缩放到指定分辨率
     *
     * @param bitmap
     * @param width
     * @param height
     * @return
     */
    public static Bitmap getScale(Bitmap bitmap, int width, int height) {
        return Bitmap.createScaledBitmap(bitmap, width, height, true);
    }

    /**
     * 添加文本到图片中去
     *
     * @param txt
     * @return
     */
    public static Bitmap addText(Context context, Bitmap bitmap, String txt) {
        bitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setFilterBitmap(true);
        paint.setColor(Color.RED);
        paint.setTextSize(ScreenUtils.dp2px(context, 30));

        Canvas canvas = new Canvas(bitmap);
//        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawText(txt, bitmap.getWidth() / 2, bitmap.getHeight() / 2, paint);
//        canvas.save();
//        canvas.restore();

        return bitmap;
    }
}
