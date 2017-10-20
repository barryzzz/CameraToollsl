package com.example.lsl.cameratoollsl;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.graphics.Rect;
import android.hardware.Camera;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.Surface;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lsl.cameratoollsl.utils.CameraUtil;
import com.example.lsl.cameratoollsl.utils.FileUtils;
import com.example.lsl.cameratoollsl.utils.ImgUtil;
import com.example.lsl.cameratoollsl.utils.LogUtil;
import com.example.lsl.cameratoollsl.utils.SPUtils;
import com.example.lsl.cameratoollsl.utils.ScreenUtils;
import com.example.lsl.cameratoollsl.widget.CameraPreView;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by lsl on 17-10-14.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {
    private TextView mCapturetextView;//框框选择弹出按钮
    private ImageView mThumbimageView;    //缩略图
    private Button mTakePickbutton;  //拍照按钮

    //预览界面
    private CameraPreView mPreView;

    //相机对象
    private Camera mCamera;
    private SurfaceHolder mHolder;

    private Context mContext;

    private boolean isFocus; //对焦是否完毕

    private String mPath;  //图片路径


    private final String[] captures = {"无", "正方形", "长方形", "圆形"};


    private final String TAG = "info----->";

    private Handler mHandler;

    private final int SHOW_THUMB = 1000; //更新缩略图通知


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        iniView();

        initHandler();

        initData();
    }

    private void initData() {
        String path = SPUtils.getPath(mContext);
        if (path != null && !"".equals(path)) {
            File file = new File(path);
            if (!file.exists()) {
                return;
            }
            mPath = path;
            Bitmap thumb = ImgUtil.getThumbBitmap(mPath, ScreenUtils.dp2px(mContext, 50), ScreenUtils.dp2px(mContext, 50));
            mThumbimageView.setImageBitmap(thumb);
        }
    }

    private void initHandler() {
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                switch (msg.what) {
                    case SHOW_THUMB:
                        mPath = (String) msg.obj;
                        Bitmap thumb = ImgUtil.getThumbBitmap(mPath, ScreenUtils.dp2px(mContext, 50), ScreenUtils.dp2px(mContext, 50));
                        mThumbimageView.setImageBitmap(thumb);
                        SPUtils.savePath(mContext, mPath); //保存拍照路径
                        break;
                }
                return true;
            }
        });
    }


    private void iniView() {
        mPreView = (CameraPreView) findViewById(R.id.camera_pre);
        mCapturetextView = (TextView) findViewById(R.id.capture_area);
        mThumbimageView = (ImageView) findViewById(R.id.thumb);
        mTakePickbutton = (Button) findViewById(R.id.takepick);

        mThumbimageView.setOnClickListener(this);
        mTakePickbutton.setOnClickListener(this);
        mCapturetextView.setOnClickListener(this);

        mHolder = mPreView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

        mPreView.setOnTouchFocusListener(new CameraPreView.onTouchFocusListener() {
            @Override
            public void focus(Point point) {
                LogUtil.e(TAG, "触摸回调了" + point.toString());
                focusOnTouch(point);
            }
        });

    }

    private void iniCamera() {
        if (!CameraUtil.hasCameraDevices(this)) {
            Toast.makeText(this, "设备没有可用相机", Toast.LENGTH_LONG).show();
            return;
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,}, 1000);
        } else {
            try {
                mCamera = CameraUtil.getCamera();
                mCamera.setPreviewDisplay(mHolder);
            } catch (Exception e) {
                Toast.makeText(this, "camera open faild", Toast.LENGTH_SHORT).show();
                finish();
                e.printStackTrace();
            }
        }
    }

    private void setCameraParams() {
        if (mCamera == null) {
            return;
        }
        mCamera.stopPreview();
        mCamera.cancelAutoFocus();

        Camera.Parameters parameters = mCamera.getParameters();
        //设置图片和预览方向
        int degree = getCameraDisplayOrientation(1);
        mCamera.setDisplayOrientation(degree);//预览画面翻转
        parameters.setRotation(degree); //输出的图片翻转

        parameters.setJpegQuality(100);

        if (CameraUtil.isAutoFocusSuppored(parameters)) {
            parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE);
        }

        float ratio = (float) mPreView.getHeight() / mPreView.getWidth();
        Camera.Size size2 = CameraUtil.getPreviewSize(parameters, ratio);
        LogUtil.e(TAG, "最佳预览大小:" + size2.width + "X" + size2.height);

        Camera.Size size3 = CameraUtil.getPictureSize(parameters, (float) size2.width / size2.height);
        LogUtil.e(TAG, "最佳图片大小:" + size3.width + "X" + size3.height);
        parameters.setPreviewSize(size2.width, size2.height);
        parameters.setPictureSize(size3.width, size3.height);
        mCamera.setParameters(parameters);

        mCamera.startPreview();
    }

    /**
     * 预览和图片方向
     *
     * @param cameraId 启动的相机编号
     * @return
     */
    private int getCameraDisplayOrientation(int cameraId) {
        android.hardware.Camera.CameraInfo info =
                new android.hardware.Camera.CameraInfo();
        android.hardware.Camera.getCameraInfo(cameraId, info);
        int rotation = judgeScreenOrientation();
        int degrees = 0;
        switch (rotation) {
            case Surface.ROTATION_0:
                degrees = 0;
                break;
            case Surface.ROTATION_90:
                degrees = 90;
                break;
            case Surface.ROTATION_180:
                degrees = 180;
                break;
            case Surface.ROTATION_270:
                degrees = 270;
                break;
        }
        int result;
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360;
            result = (360 - result) % 360;  // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360;
        }
        return result;
    }

    /**
     * 对焦拍照
     */
    private void takePicture() {
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean b, Camera camera) {
                isFocus = b;
                if (b) {
                    mCamera.takePicture(null, null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] bytes, Camera camera) {
                            takePicture2(bytes);
                        }
                    });
                }
            }
        });
    }

    /**
     * 拍照后图片处理过程
     *
     * @param data
     * @throws IOException
     */
    private void takePicture2(final byte[] data) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                String path;
                try {
                    if (mPreView.getCropMode() == CameraPreView.CropMode.NORMAL) {
                        path = FileUtils.savePic(data);
                    } else {
                        int mode = 0;
                        if (mPreView.getCropMode() == CameraPreView.CropMode.CIRCLE) {
                            mode = 1;
                        }
                        Bitmap bitmap = ImgUtil.getCropBitmap(data, mPreView.getRect(), mPreView.getWidth(), mPreView.getHeight(), mode);
                        path = FileUtils.saveBitmap(bitmap);
                        if (bitmap.isRecycled()) bitmap.recycle();
                    }
                    Message.obtain(mHandler, SHOW_THUMB, path).sendToTarget();  //发送消息去更新缩略图
                } catch (IOException e) {
                    e.printStackTrace();

                } finally {
                    mCamera.startPreview();
                }
                isFocus = false;
            }
        }).start();

    }

    /**
     * 触摸对焦
     *
     * @param point
     */
    private void focusOnTouch(Point point) {
        if (mCamera == null) return;
        mCamera.cancelAutoFocus();
        Rect rect = CameraUtil.calculateTapArea(point.x, point.y, 1.0f, mPreView.getWidth(), mPreView.getHeight());
        LogUtil.e(TAG, "对焦区域" + rect.toString());
        Camera.Parameters parameters = mCamera.getParameters();
        parameters.setFocusMode(Camera.Parameters.FOCUS_MODE_AUTO);
        List<Camera.Area> areas = new ArrayList<>();
        areas.add(new Camera.Area(rect, 400));
        parameters.setFocusAreas(areas);
        mCamera.setParameters(parameters);
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean success, Camera camera) {
                LogUtil.e(TAG, "手动对焦成功" + success);
                Camera.Parameters param = mCamera.getParameters();
                if (CameraUtil.isAutoFocusSuppored(param)) {
                    param.setFocusMode(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE); //设置会自动对焦模式
                    mCamera.cancelAutoFocus();
                    mCamera.setParameters(param);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.capture_area:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("选择框框形状");
                builder.setItems(captures, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //切换裁剪模式
                        setCropModel(which);
                    }
                });
                builder.create();
                builder.show();
                break;
            case R.id.thumb:
                if (!TextUtils.isEmpty(mPath)) {
                    Intent intent = new Intent(mContext, ThumbActivity.class);
                    intent.putExtra("path", mPath);
                    startActivity(intent);
                }
                break;
            case R.id.takepick:
                if (!isFocus)
                    takePicture();
                break;
        }
    }

    /**
     * 切换裁剪模式
     *
     * @param model
     */
    private void setCropModel(int model) {
        switch (model) {
            case 0:
                mPreView.setCropMode(CameraPreView.CropMode.NORMAL);
                break;
            case 1:
                mPreView.setCropMode(CameraPreView.CropMode.SQUARE);
                break;
            case 2:
                mPreView.setCropMode(CameraPreView.CropMode.RECTANGLE);
                break;
            case 3:
                mPreView.setCropMode(CameraPreView.CropMode.CIRCLE);
                break;
        }
    }

    private int judgeScreenOrientation() {
        return getWindowManager().getDefaultDisplay().getRotation();
    }


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        iniCamera();
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
        setCameraParams();
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        if (mCamera != null) {
            mCamera.stopPreview();
            mCamera.release();
            mCamera = null;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    iniCamera();
                    setCameraParams();
                } else {
                    //faild
                    Toast.makeText(mContext, "没有授予相机权限,请到程序列表中进行授权", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (mCamera != null) {
            mCamera.stopPreview();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mCamera != null) {
            mCamera.startPreview();
        }
    }

}
