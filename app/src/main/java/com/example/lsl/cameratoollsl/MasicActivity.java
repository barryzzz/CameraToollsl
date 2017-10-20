package com.example.lsl.cameratoollsl;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import com.example.lsl.cameratoollsl.utils.FileUtils;
import com.example.lsl.cameratoollsl.widget.MasicPreView;

import java.io.File;
import java.io.IOException;

/**
 * 马赛克
 * Created by Administrator on 2017/10/19.
 */

public class MasicActivity extends AppCompatActivity {
    private MasicPreView mView;


    private String mPath;

    private Handler mHandler;

    private final int SAVE_CODE = 10001;

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masic);
        mContext = this;
        mView = (MasicPreView) findViewById(R.id.masic_img);

        mPath = getIntent().getStringExtra("path");
        showImg();
        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == SAVE_CODE) {
                    Toast.makeText(mContext, "保存成功", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
                return true;
            }
        });
    }


    /**
     * 显示图片
     */
    private void showImg() {
        Bitmap bitmap = BitmapFactory.decodeFile(mPath);
        mView.setBitmapData(bitmap);
    }

    /**
     * 保存马赛克图片
     *
     * @param v
     */
    public void doSave(View v) {
        if (TextUtils.isEmpty(mPath)) return;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileUtils.saveFile(mView.getBitmapData(), new File(mPath));
                    Message.obtain(mHandler, SAVE_CODE).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
