package com.example.lsl.cameratoollsl.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.lsl.cameratoollsl.utils.ScreenUtils;

import java.util.HashMap;
import java.util.Map;


/**
 * Description: 选择框View
 * Author   :lishoulin
 * Date     :2017/6/5.
 */

public class CaptureView extends View {

    private Rect mRect;//矩形

    private Paint mPaint;

    private int viewW, viewH;//布局长宽

    private int radius;
    private int centerX, centerY;//中心点坐标

    private int long_radius;

    public static final int CAPTURE_LONGRECT = 1000;
    public static final int CAPTURE_RECT = 1001;
    public static final int CAPTURE_CIRCLE = 1002;

    public int CURRENT_FORM = 1001;


    private final String TAG = "capture----->";


    public CaptureView(Context context) {
        this(context, null, 0);
    }

    public CaptureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CaptureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        //初始化画笔
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.GREEN);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(3f);

        radius = ScreenUtils.getScreenWidth(context) / 4;
        long_radius = radius + 50;
    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        viewW = w;
        viewH = h;
        Log.e(TAG, "viewW:" + viewW + " viewH" + viewH);
        //初始化正方形
        centerX = viewW / 2;
        centerY = viewH / 2;
//        radius = 100;
        if (CURRENT_FORM == CAPTURE_RECT) {
            mRect = new Rect(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        }
        if (CURRENT_FORM == CAPTURE_LONGRECT) {
            mRect = new Rect(centerX - radius, centerY - long_radius, centerX + radius, centerY + long_radius);
        }

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (CURRENT_FORM == CAPTURE_RECT || CURRENT_FORM == CAPTURE_LONGRECT) {
            canvas.drawRect(mRect, mPaint);
        } else {
            canvas.drawCircle(centerX, centerY, radius, mPaint);
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_UP:
                int x = (int) event.getX();
                int y = (int) event.getY();
                changePoint(x, y);
                break;
        }
        return true;
    }

    /**
     * 改变中心点
     *
     * @param x x坐标
     * @param y y坐标
     */
    private void changePoint(int x, int y) {
        centerX = x;
        centerY = y;
        if (CURRENT_FORM == CAPTURE_RECT) {
            mRect = new Rect(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        } else if (CURRENT_FORM == CAPTURE_LONGRECT) {
            mRect = new Rect(centerX - radius, centerY - long_radius, centerX + radius, centerY + long_radius);
        }

        invalidate();
    }

    /**
     * 切换不同形状框框
     *
     * @param capture 形状类型
     */
    public void setDrawCapture(int capture) {
        //重置参数
        centerX = viewW / 2;
        centerY = viewH / 2;
        Log.e(TAG, "选择时候:viewW:" + viewW + " viewH" + viewH);
        switch (capture) {
            case CAPTURE_LONGRECT:
                mRect = new Rect(centerX - radius, centerY - long_radius, centerX + radius, centerY + long_radius);
                CURRENT_FORM = CAPTURE_LONGRECT;
                break;
            case CAPTURE_RECT:
                mRect = new Rect(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
                CURRENT_FORM = CAPTURE_RECT;
                break;
            case CAPTURE_CIRCLE:
                CURRENT_FORM = CAPTURE_CIRCLE;
//                radius = 100;
                break;
        }

        invalidate();
    }

    /**
     * 获取当前矩形的坐标参数 react
     *
     * @return Rect
     */
    public Rect getRect() {
        if (CURRENT_FORM == CAPTURE_RECT || CURRENT_FORM == CAPTURE_LONGRECT) {
            return mRect;
        } else {
            return null;
        }
    }

    /**
     * 获取到圆形坐标,半径参数 map
     *
     * @return Map
     */
    public Map getCircle() {
        Map<String, Integer> map = new HashMap<>();
        map.put("x", centerX);
        map.put("y", centerY);
        map.put("r", radius);
        return map;
    }

    /**
     * 放大
     */
    public void setZoomOut() {
        if (CURRENT_FORM == CAPTURE_RECT) {
            radius += 2;
            mRect = new Rect(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        } else if (CURRENT_FORM == CAPTURE_LONGRECT) {
            radius += 2;
            long_radius += 4;
            mRect = new Rect(centerX - radius, centerY - long_radius, centerX + radius, centerY + long_radius);
        } else {
            radius += 2;
        }
        invalidate();
    }

    /**
     * 缩小
     */
    public void setZoomIn() {
        if (CURRENT_FORM == CAPTURE_RECT) {
            radius -= 2;
            mRect = new Rect(centerX - radius, centerY - radius, centerX + radius, centerY + radius);
        } else if (CURRENT_FORM == CAPTURE_LONGRECT) {
            radius -= 2;
            long_radius -= 4;
            mRect = new Rect(centerX - radius, centerY - long_radius, centerX + radius, centerY + long_radius);
        } else {
            radius -= 2;
        }
        invalidate();
    }


}

