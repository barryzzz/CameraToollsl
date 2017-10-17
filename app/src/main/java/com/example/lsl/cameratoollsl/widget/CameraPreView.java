package com.example.lsl.cameratoollsl.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.SurfaceView;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import com.example.lsl.cameratoollsl.SimpleValueAnimator;
import com.example.lsl.cameratoollsl.SimpleValueAnimatorListener;
import com.example.lsl.cameratoollsl.ValueAnimatorV14;
import com.example.lsl.cameratoollsl.utils.ScreenUtils;


/**
 * Created by lsl on 17-10-15.
 */

public class CameraPreView extends SurfaceView {
    private Context mContext;

    /**
     * 画笔对象
     */
    private Paint mPaint;
    /**
     * 裁剪区域
     */
    private Rect mRect;
    /**
     * 裁剪模式
     */
    private CropMode mCropMode = CropMode.NORMAL;

    private float viewH, viewW;//预览界面的长宽

    private final String TAG = "info------>surface";
    private int CropWidth;
    private int CropHeigth;

    private SimpleValueAnimator mAnimator;
    private final Interpolator DEFAULT_INTERPOLATOR = new DecelerateInterpolator();
    private Interpolator mInterpolator = DEFAULT_INTERPOLATOR;
    private boolean mIsAnimating = false;


    public CameraPreView(Context context) {
        this(context, null, 0);
    }

    public CameraPreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraPreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        initPaint();
    }

    private void initPaint() {
        mPaint = new Paint();
        mPaint.setColor(Color.GREEN);
        mPaint.setStrokeWidth(3f);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);

        CropWidth = ScreenUtils.dp2px(mContext, 200f);
        CropHeigth = CropWidth;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int w = MeasureSpec.getSize(widthMeasureSpec);
        int h = MeasureSpec.getSize(heightMeasureSpec);

        viewW = w - getPaddingLeft() - getPaddingRight();
        viewH = h - getPaddingTop() - getPaddingBottom();

        Log.e(TAG, "viewW:" + viewW + " viewH" + viewH);

    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        setLayout();
    }

    /**
     * 设置布局位置
     */
    private void setLayout() {
        if (viewH <= 0 || viewW <= 0) {
            return;
        }
        int left = (int) ((viewW - CropWidth) / 2);
        int top = (int) ((viewH - CropHeigth) / 2);
        int rigth = left + CropWidth;
        int bottom = top + CropHeigth;
        mRect = new Rect(left, top, rigth, bottom);
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        super.onDraw(canvas);
//        drawFrame(canvas);
        drawRectangle(canvas);
    }

    private void drawFrame(Canvas canvas) {
        if (mCropMode == CropMode.NORMAL) clearCanvas(canvas);
        if (mCropMode == CropMode.RECTANGLE) drawRectangle(canvas);
        if (mCropMode == CropMode.SQUARE) drawRectangle(canvas);
        if (mCropMode == CropMode.CIRCLE) drawCircle(canvas);
    }


    /**
     * 画长方形
     *
     * @param canvas
     */
    public void drawRectangle(Canvas canvas) {
        canvas.drawRect(mRect, mPaint);
    }

    /**
     * 清除画布
     *
     * @param canvas
     */
    public void clearCanvas(Canvas canvas) {
        canvas.drawColor(Color.TRANSPARENT, PorterDuff.Mode.CLEAR);
    }

    /**
     * 画圆形
     *
     * @param canvas
     */
    public void drawCircle(Canvas canvas) {
//        canvas.drawCircle();

//            canvas.drawRect();
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return super.onTouchEvent(event);
    }


    /**
     * 裁剪模式RECTANGLE 长方形，SQUARE正方形，CIRCLE圆形,NORMAL 无
     */
    public enum CropMode {
        RECTANGLE(0), SQUARE(1), CIRCLE(2), NORMAL(3);
        private final int ID;

        CropMode(final int id) {
            this.ID = id;
        }

        public int getId() {
            return ID;
        }
    }

    /**
     * 设置拍照裁剪模式
     *
     * @param mode
     */
    public void setCropMode(CropMode mode) {
        this.mCropMode = mode;
//        recalculateFrameRect(100);
    }

    /**
     * 获取矩形区域
     *
     * @return
     */
    public Rect getRect() {
        return mRect;
    }


//    private SimpleValueAnimator getAnimator() {
//        mAnimator = new ValueAnimatorV14(mInterpolator);
//        return mAnimator;
//    }
//
//    private void recalculateFrameRect(int durationMillis) {
//        if (mIsAnimating) {
//            getAnimator().cancelAnimation();
//        }
//
//        SimpleValueAnimator animator = getAnimator();
//        animator.addAnimatorListener(new SimpleValueAnimatorListener() {
//            @Override
//            public void onAnimationStarted() {
//                mIsAnimating = true;
//            }
//
//            @Override
//            public void onAnimationUpdated(float scale) {
//                Log.e(TAG, "动画执行中");
//                invalidate();
//            }
//
//            @Override
//            public void onAnimationFinished() {
//                mIsAnimating = false;
//                invalidate();
//            }
//        });
//        animator.startAnimation(durationMillis);
//
//    }
}
