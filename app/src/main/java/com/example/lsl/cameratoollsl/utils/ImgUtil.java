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
 * 图片工具类
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
     * 获取一个裁剪区域
     *
     * @param data
     * @param rect
     * @return
     */
    public static Bitmap getCropBitmap(byte[] data, Rect rect, int preW, int preH, int mode) {
        Bitmap bitmap = BitmapFactory.decodeByteArray(data, 0, data.length);
        float bw = bitmap.getWidth();
        float bh = bitmap.getHeight();

        float wScale = bw / preW;
        float hScale = bh / preH;

        int cropLeft = (int) (rect.left * wScale);
        int cropTop = (int) (rect.top * hScale);
        int cropRigth = (int) (rect.width() * wScale);
        int cropBottom = (int) (rect.height() * hScale);

        Bitmap cropBitmap = Bitmap.createBitmap(bitmap, cropLeft, cropTop, cropRigth, cropBottom);
        if (mode == 1) {
            //进行圆形处理
            cropBitmap = getCircleCropBitmap(cropBitmap);
        }
        bitmap.recycle();

        return cropBitmap;
    }

    public static Bitmap getCircleCropBitmap(Bitmap bitmap) {
        if (bitmap == null) return null;
        Bitmap newBitmap = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);

        Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());

        Canvas canvas = new Canvas(newBitmap);
        int halfWidth = bitmap.getWidth() / 2;
        int halfHeight = bitmap.getHeight() / 2;

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setFilterBitmap(true);

        canvas.drawCircle(halfWidth, halfHeight, Math.min(halfWidth, halfHeight), paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));

        canvas.drawBitmap(bitmap, rect, rect, paint);

        return newBitmap;
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
        paint.setDither(true); //采集清晰点
        paint.setFilterBitmap(true); //过滤
        paint.setColor(Color.RED);
        paint.setTextSize(ScreenUtils.dp2px(context, 30));

        Canvas canvas = new Canvas(bitmap);
//        canvas.drawBitmap(bitmap, 0, 0, null);
        canvas.drawText(txt, bitmap.getWidth() / 2, bitmap.getHeight() / 2, paint);
//        canvas.save();
//        canvas.restore();

        return bitmap;
    }

    /**
     * 局部马赛克
     *
     * @param bitmap    源图像
     * @param zoneWidth
     * @param rect      马赛克区域
     * @return
     */
    public static Bitmap Masic(Bitmap bitmap, int zoneWidth, Rect rect) {
        int w = bitmap.getWidth();
        int h = bitmap.getHeight();

        Bitmap newBitmap = bitmap.copy(Bitmap.Config.ARGB_8888, true);

        Canvas canvas = new Canvas(newBitmap);
        Paint paint = new Paint();
        int left = rect.left;
        int top = rect.top;
        int right = rect.right;
        int bottom = rect.bottom;
        //马赛克算法
        for (int i = left; i < right; i += zoneWidth) {
            for (int j = top; j < bottom; j += zoneWidth) {
                int color = bitmap.getPixel(i, j);
                paint.setColor(color);
                int gridRight = Math.min(w, i + zoneWidth);
                int gridBottom = Math.min(h, j + zoneWidth);
                canvas.drawRect(i, j, gridRight, gridBottom, paint);
            }
        }
        return newBitmap;
    }

    /**
     * 全部马赛克
     *
     * @param bitmap
     * @param zoneWidth 马赛克方块的大小
     * @return
     */
    public static Bitmap Masic(Bitmap bitmap, int zoneWidth) {
        return Masic(bitmap, zoneWidth, new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()));
    }
}
