package com.example.lsl.cameratoollsl;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lsl.cameratoollsl.utils.ImgUtil;
import com.example.lsl.cameratoollsl.utils.ScreenUtils;
import com.example.lsl.cameratoollsl.wiget.CallBack;
import com.example.lsl.cameratoollsl.wiget.CameraPreView;


/**
 * Created by lsl on 17-10-14.
 */

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    private FrameLayout mCameraFrameLayout;  //预览界面
    private TextView mCapturetextView;//框框选择弹出
    private ImageView mThumbimageView;    //缩略图
    private Button mTakePickbutton;  //拍照
    private Button mAdd, mDel;

    private CameraPreView mCameraView;

    private Context mContext;

    private String mPath;
//    private CaptureView mCaptureView;

    private final String[] captures = {"无", "正方形", "长方形", "圆形"};


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mContext = this;
        iniView();
    }


    private void iniView() {
        mCameraFrameLayout = (FrameLayout) findViewById(R.id.camera);
//        mCaptureView = (CaptureView) findViewById(R.id.capture);

        mCameraView = (CameraPreView) findViewById(R.id.camera_pre);
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

        /**
         * 设置拍照成功回调
         */
        mCameraView.setTakePickCallBack(new CallBack() {
            @Override
            public void success(String path) {
                Bitmap bitmap = ImgUtil.getThumbBitmap(path, ScreenUtils.dp2px(mContext, 50), ScreenUtils.dp2px(mContext, 50));
                mThumbimageView.setImageBitmap(bitmap);
                mPath = path;
            }

            @Override
            public void faild() {

            }
        });


        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CAMERA,}, 1000);
        }

    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.capture_area:
                AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.ThemeOverlay_Material_Dialog);
                builder.setTitle("选择框框形状");
                builder.setItems(captures, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do something about capture
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
                mCameraView.takepick();
                break;
            case R.id.capture_add:
//                mCaptureView.setZoomOut();
                break;
            case R.id.capture_del:
//                mCaptureView.setZoomIn();
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1000:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                } else {
                    //faild
                }
                break;
        }
    }


//    @Override
//    protected void onPause() {
//        super.onPause();
//        mCameraView.stopPreview();
//    }
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        mCameraView.startPreview();
//    }
//
//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
////        mCameraView.release();
//    }


}
