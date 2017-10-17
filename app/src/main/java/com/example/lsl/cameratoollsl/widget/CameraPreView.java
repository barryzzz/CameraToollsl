package com.example.lsl.cameratoollsl.widget;

import android.content.Context;
import android.hardware.Camera;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.SurfaceHolder;
import android.view.SurfaceView;

import com.example.lsl.cameratoollsl.utils.CameraUtil;
import com.example.lsl.cameratoollsl.utils.FileUtils;

import java.io.IOException;


/**
 * Created by lsl on 17-10-15.
 */

public class CameraPreView extends SurfaceView implements SurfaceHolder.Callback, Camera.PictureCallback {
    private Context mContext;

    private SurfaceHolder mHolder;

    private Camera mCamera;


    private CallBack mTakePickCallBack;
    public boolean isFinshTakePick = true; //用于判断相机是否已经拍照完成


//    private Point mPoint;
//    private int r;

    public CameraPreView(Context context) {
        this(context, null, 0);
    }

    public CameraPreView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CameraPreView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;

        mHolder = getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);


    }


    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (mCamera == null) {
            mCamera = CameraUtil.getCamera();
            try {
                mCamera.setPreviewDisplay(mHolder);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {
        try {
            mCamera.stopPreview();
            mCamera.setPreviewDisplay(holder);
            mCamera.startPreview();
            setCameraParams(mCamera);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    /**
     * 拍照回调
     *
     * @param bytes
     * @param camera
     */
    @Override
    public void onPictureTaken(final byte[] bytes, Camera camera) {
        isFinshTakePick = false;
        if (mTakePickCallBack != null) {
            mTakePickCallBack.success(bytes);
            mCamera.startPreview();
            isFinshTakePick = true;
        }
    }

    /**
     * 设置相机设置
     *
     * @param camera
     */

    private void setCameraParams(Camera camera) {
        camera.stopPreview();
        Camera.Parameters parameters = camera.getParameters();
        parameters.setJpegQuality(100);
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        camera.cancelAutoFocus();

        camera.setDisplayOrientation(90);
        camera.setParameters(parameters);
        camera.startPreview();
    }

    /**
     * 拍照
     */
    public void takepick() {
        if (mCamera != null)
            mCamera.takePicture(null, null, this);
    }

    /**
     * 开始预览
     */
    public void startPreview() {
        if (mCamera != null) {
            mCamera.startPreview();
        }
    }

    /**
     * 停止预览
     */
    public void stopPreview() {
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    /**
     * 释放资源
     */
    public void release() {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
            mHolder = null;
        }
    }

    public void setTakePickCallBack(CallBack callBack) {
        this.mTakePickCallBack = callBack;
    }


}
