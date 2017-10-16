package com.example.lsl.cameratoollsl.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import com.example.lsl.cameratoollsl.utils.ViewUtil;


/**
 * Description:
 * Author   :lishoulin
 * Date     :2017/6/5.
 */

public class CaptureView extends View {

    private Paint mPaint;
    private int w, h;
    private int raduis;

    private CircleCapture mCapture;
    private Point mPoint;
    private final String TAG = "info----->";


    public CaptureView(Context context) {
        this(context, null, 0);
    }

    public CaptureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);

    }

    public CaptureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(1f);
        mPaint.setStyle(Paint.Style.STROKE);
        mCapture = new CircleCapture();

    }


    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        this.w = w;
        this.h = h;
        mPoint = new Point(w / 2, h / 2);
        raduis = w / 4;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        mCapture.setCxCy(mPoint.x, mPoint.y, raduis);
        mCapture.draw(canvas, mPaint);

    }

    double defs = 0;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        Log.e(TAG, "触摸点:" + event.getPointerCount());

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                if (event.getPointerCount() == 2) {
                    defs = ViewUtil.distanceBetweenFingers(event);
                    Log.e(TAG, "距离:" + defs);
                }
                break;
            case MotionEvent.ACTION_UP:
                int x = (int) event.getX();
                int y = (int) event.getY();
                mPoint = new Point(x, y);
                invalidate();
                break;

        }
        return true;
    }

    /**
     * 缩小
     */
    public void setZoomIn() {
        if (raduis <= 0) {
            return;
        }
        raduis -= 2;
        invalidate();
    }

    /**
     * 放大
     */
    public void setZoomOut() {
        if (raduis >= w / 2) {
            return;
        }
        raduis += 2;
        invalidate();
    }

    public Point getPoint() {
        return mPoint;
    }

    public int getR() {
        return raduis;
    }


}

