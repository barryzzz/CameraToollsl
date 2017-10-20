package com.example.lsl.cameratoollsl.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.example.lsl.cameratoollsl.utils.LogUtil;

/**
 * 马赛克view
 * Created by Administrator on 2017/10/19.
 */

@SuppressLint("AppCompatCustomView")
public class MasicPreView extends ImageView {

    private int viewW, viewH;
    private int centerX, centerY;
    private float raduis = 60f;
    private Paint mPaint;
    private Bitmap mBitmap;
    private Rect mRect;
    private Rect mLimitReact;

    private int mBitmapW, mBitmapH;
    private Paint mMasicPaint;
    private Canvas mMasicCanvas;
    private Bitmap mMasicBitmap;


    public MasicPreView(Context context) {
        this(context, null, 0);
    }

    public MasicPreView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MasicPreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        iniPaint();
    }

    /**
     * 初始化画笔
     */
    private void iniPaint() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(3f);
        mPaint.setStyle(Paint.Style.STROKE);

        mMasicPaint = new Paint();
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewW = getWidth();
        viewH = getHeight();

        centerX = viewW / 2;
        centerY = viewH / 2;

        int left = (int) (centerX - raduis);
        int top = (int) (centerY - raduis);

        int right = (int) (left + raduis);
        int bottom = (int) (top + raduis);

        mRect = new Rect(left, top, right, bottom);
        mLimitReact = new Rect(0, 0, viewW, viewH);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawMasic(canvas);
    }

    /**
     * 画标记
     *
     * @param canvas
     */
    private void drawMasic(Canvas canvas) {
        canvas.drawRect(mRect, mPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x, y;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();
                moveMasic(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                x = (int) event.getX();
                y = (int) event.getY();
                moveMasic(x, y);
                break;
        }
        return true;
    }


    /**
     * 检查边界
     */
    private void checkLimitMove() {
        int diff = mRect.left - mLimitReact.left;
        if (diff < 0) {
            mRect.left -= diff;
            mRect.right -= diff;
            centerX -= diff;
        }
        diff = mRect.right - mLimitReact.right;
        if (diff > 0) {
            mRect.left -= diff;
            mRect.right -= diff;
            centerX -= diff;
        }
        diff = mRect.top - mLimitReact.top;
        if (diff < 0) {
            mRect.top -= diff;
            mRect.bottom -= diff;
            centerY -= diff;
        }
        diff = mRect.bottom - mLimitReact.bottom;
        if (diff > 0) {
            mRect.top -= diff;
            mRect.bottom -= diff;
            centerY -= diff;
        }
    }


    /**
     * 移动马赛克区域
     *
     * @param x
     * @param y
     */
    private void moveMasic(int x, int y) {
        centerX = x;
        centerY = y;
        int left = (int) (centerX - raduis);
        int top = (int) (centerY - raduis);

        int right = (int) (left + raduis);
        int bottom = (int) (top + raduis);

        LogUtil.e("masic---->", "viewW:" + viewW + "viewH:" + viewH);
        mRect.left = left;
        mRect.top = top;
        mRect.right = right;
        mRect.bottom = bottom;

        checkLimitMove();
        LogUtil.e("masic---->", "点击矩阵:" + mRect.toString());

        Masic(mBitmap, 10, mRect, viewW, viewH);
        invalidate();
    }

    /**
     * 设置bitmap数据
     *
     * @param data
     */
    public void setBitmapData(Bitmap data) {
        this.mBitmap = data;
        setImageBitmap(data);
        mMasicBitmap = this.mBitmap.copy(Bitmap.Config.ARGB_8888, true);
        mMasicCanvas = new Canvas(mMasicBitmap);
        mBitmapW = this.mBitmap.getWidth();
        mBitmapH = this.mBitmap.getHeight();

    }

    /**
     * 获取bitmap数据
     *
     * @return
     */
    public Bitmap getBitmapData() {
        return mBitmap;
    }

    /**
     * 打码方法
     *
     * @param bitmap
     * @param zoneWidth
     * @param rect
     * @param preW
     * @param preH
     */
    private void Masic(Bitmap bitmap, int zoneWidth, Rect rect, int preW, int preH) {
        float wScale = mBitmapW / (float) preW;  //屏幕坐标和图片坐标的缩放比例
        float hScale = mBitmapH / (float) preH;

        int left = (int) (rect.left * wScale);
        int top = (int) (rect.top * hScale);
        int right = (int) (rect.right * wScale);
        int bottom = (int) (rect.bottom * hScale);
        //马赛克算法
        for (int i = left; i < right; i += zoneWidth) {
            for (int j = top; j < bottom; j += zoneWidth) {
                int color = bitmap.getPixel(i, j);
                mMasicPaint.setColor(color);
                int gridRight = Math.min(mBitmapW, i + zoneWidth);
                int gridBottom = Math.min(mBitmapH, j + zoneWidth);
                mMasicCanvas.drawRect(i, j, gridRight, gridBottom, mMasicPaint);
            }
        }
        mBitmap = mMasicBitmap;
        setImageBitmap(mMasicBitmap); //更新图片
    }


}
