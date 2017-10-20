package com.example.lsl.cameratoollsl.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by Administrator on 2017/10/20.
 */

public class TextPreView extends View {

    private String txt;
    private String mColor = "ffffff";
    private int textSize = 16;


    private Paint mPaint;
    private float txtX, txtY; //文字的坐标
    private int viewH, viewW; //图片的大小

    private Rect mBoundsRect;//文字区域
    private Rect mLimitRect; //移动限制区域


    private Context mContext;


    public TextPreView(Context context) {
        this(context, null, 0);
    }

    public TextPreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TextPreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    private void init() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

        mBoundsRect = new Rect();
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewW = getWidth();
        viewH = getHeight();

        txtX = viewW / 2f;
        txtY = viewH / 2f;

        mLimitRect = new Rect(0, 0, viewW, viewH);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas, txt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
    }

    private void drawText(Canvas canvas, String text) {
        mPaint.getTextBounds(txt, 0, text.length(), mBoundsRect);
        canvas.drawText(text, txtX, txtY, mPaint);
    }
}
