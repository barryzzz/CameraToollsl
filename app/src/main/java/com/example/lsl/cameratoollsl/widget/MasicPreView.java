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

import com.example.lsl.cameratoollsl.utils.ImgUtil;

/**
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

    public MasicPreView(Context context) {
        super(context);
    }

    public MasicPreView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(5f);
    }

    public MasicPreView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        viewW = MeasureSpec.getSize(widthMeasureSpec) - getPaddingRight() - getPaddingLeft();
        viewH = MeasureSpec.getSize(heightMeasureSpec) - getPaddingTop() - getPaddingBottom();
        centerX = viewW / 2;
        centerY = viewH / 2;

        int left = (int) (centerX - raduis);
        int top = (int) (centerY - raduis);

        int right = (int) (left + raduis);
        int bottom = (int) (top + raduis);

        mRect = new Rect(left, top, right, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawCircle(canvas);
    }


    private void drawCircle(Canvas canvas) {
//        canvas.drawCircle(centerX, centerY, raduis, mPaint);

        canvas.drawRect(mRect, mPaint);

//        canvas.restore();
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
            case MotionEvent.ACTION_UP:
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

        mRect = new Rect(left, top, right, bottom);
        mBitmap = ImgUtil.Masic(mBitmap, 10, mRect);
        setImageBitmap(mBitmap);
        invalidate();
    }

    public void setBitmapData(Bitmap data) {
        this.mBitmap = data;
        setImageBitmap(data);
    }
}
