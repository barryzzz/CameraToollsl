package com.example.lsl.cameratoollsl;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.hardware.Camera;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.SurfaceHolder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lsl.cameratoollsl.utils.CameraUtil;
import com.example.lsl.cameratoollsl.utils.FileUtils;
import com.example.lsl.cameratoollsl.utils.ImgUtil;
import com.example.lsl.cameratoollsl.utils.ScreenUtils;
import com.example.lsl.cameratoollsl.widget.CameraPreView;

import java.io.IOException;


/**
 * Created by lsl on 17-10-14.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener, SurfaceHolder.Callback {
    private TextView mCapturetextView;//框框选择弹出按钮
    private ImageView mThumbimageView;    //缩略图
    private Button mTakePickbutton;  //拍照按钮
    private Button mAdd, mDel;  //扩大,缩小按钮

    //预览界面
    private CameraPreView mPreView;

    //相机对象
    private Camera mCamera;
    private SurfaceHolder mHolder;

    private Context mContext;

    private boolean isFocus; //对焦是否完毕

    private String mPath;


    private final String[] captures = {"无", "正方形", "长方形", "圆形"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        iniView();
    }


    private void iniView() {
        mPreView = (CameraPreView) findViewById(R.id.camera_pre);
        mCapturetextView = (TextView) findViewById(R.id.capture_area);
        mThumbimageView = (ImageView) findViewById(R.id.thumb);
        mTakePickbutton = (Button) findViewById(R.id.takepick);
        mAdd = (Button) findViewById(R.id.capture_add);
        mDel = (Button) findViewById(R.id.capture_del);

        mAdd.setOnClickListener(this);
        mDel.setOnClickListener(this);
        mThumbimageView.setOnClickListener(this);
        mTakePickbutton.setOnClickListener(this);
        mCapturetextView.setOnClickListener(this);

        mHolder = mPreView.getHolder();
        mHolder.addCallback(this);
        mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS);

    }

    private void iniCamera() {
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
        Camera.Parameters parameters = mCamera.getParameters();
        mCamera.setDisplayOrientation(90);//预览画面翻转90°
        parameters.setRotation(90); //输出的图片翻转90°

        parameters.setPictureSize(1280, 720);
        parameters.setPreviewSize(1280, 720);
        mCamera.setParameters(parameters);
        mCamera.startPreview();
    }


    private void takePicture() {
        mCamera.autoFocus(new Camera.AutoFocusCallback() {
            @Override
            public void onAutoFocus(boolean b, Camera camera) {
                isFocus = b;
                if (b) {
                    mCamera.cancelAutoFocus();
                    mCamera.takePicture(null, null, null, new Camera.PictureCallback() {
                        @Override
                        public void onPictureTaken(byte[] bytes, Camera camera) {
                            try {
                                String path = FileUtils.savePic(bytes);
                                mPath = path;
                                Bitmap bitmap = ImgUtil.getThumbBitmap(path, ScreenUtils.dp2px(mContext, 50), ScreenUtils.dp2px(mContext, 50));
                                mThumbimageView.setImageBitmap(bitmap);
                            } catch (IOException e) {
                                e.printStackTrace();
                            } finally {
                                mCamera.startPreview();
                            }
                            isFocus = false;
                        }
                    });
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
                Intent intent = new Intent(mContext, ThumbActivity.class);
                if (mPath != null) {
                    intent.putExtra("path", mPath);
                }
                startActivity(intent);
                break;
            case R.id.takepick:
                if (!isFocus)
                    takePicture();
                break;
            case R.id.capture_add:

                break;
            case R.id.capture_del:

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


    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        iniCamera();
        mPreView.startTimer();
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
