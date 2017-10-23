package com.example.lsl.cameratoollsl.widget;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.ImageView;

import com.example.lsl.cameratoollsl.utils.LogUtil;
import com.example.lsl.cameratoollsl.utils.ScreenUtils;

/**
 * Created by Administrator on 2017/10/20.
 */

@SuppressLint("AppCompatCustomView")
public class TextPreView extends ImageView {

    private String txt = "我是移动文字";


    private TextPaint mPaint;
    private float txtX, txtY; //文字的坐标
    private int viewH, viewW; //预览界面大小
    private int mBitmapW, mBitmapH;


    private Bitmap mBitmap;

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
        mPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setTextAlign(Paint.Align.CENTER);
        mPaint.setTextSize(ScreenUtils.sp2px(mContext, 30));
        LogUtil.e("info---->", "textSize:" + ScreenUtils.sp2px(mContext, 30));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);

        viewW = getWidth();
        viewH = getHeight();

        txtX = viewW / 2f;
        txtY = viewH / 2f;

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawText(canvas, txt);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int x, y;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                x = (int) event.getX();
                y = (int) event.getY();
                moveText(x, y);
                break;
            case MotionEvent.ACTION_MOVE:
                x = (int) event.getX();
                y = (int) event.getY();
                moveText(x, y);
                break;
        }
        return true;
    }

    /**
     * 移动文字
     *
     * @param x
     * @param y
     */
    private void moveText(int x, int y) {
        txtX = x;
        txtY = y;
        invalidate();
    }

    private void drawText(Canvas canvas, String text) {
        txtY = txtY - (mPaint.descent() + mPaint.ascent()) / 2; //计算出基准线
        canvas.drawText(text, txtX, txtY, mPaint);
    }


    /**
     * 设置bitmap数据
     *
     * @param data
     */
    public void setBitmapData(Bitmap data) {
        this.mBitmap = data;
        setImageBitmap(data);
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
     * 设置文字数据
     *
     * @param txt
     */
    public void setTxT(String txt) {
        this.txt = txt;
        invalidate();
    }

    /**
     * 合并图片和文字数据
     */
    public Bitmap saveTxt() {
        Bitmap bitmap = mBitmap.copy(mBitmap.getConfig(), true);

        Canvas canvas = new Canvas(bitmap);
        float wScale = (float) mBitmapW / viewW;
        float hScale = (float) mBitmapH / viewH;

        txtX = txtX * wScale;
        txtY = txtY * hScale;

        mPaint.setTextSize(mPaint.getTextSize() * Math.min(wScale, hScale)); //缩放处理
        canvas.drawText(txt, txtX, txtY, mPaint);
        return bitmap;
    }

}
