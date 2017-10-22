package com.example.lsl.cameratoollsl;

import android.content.Context;
import android.content.Intent;
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
import com.example.lsl.cameratoollsl.widget.TextPreView;

import java.io.File;
import java.io.IOException;

/**
 * 添加文字
 * Created by Administrator on 2017/10/20.
 */

public class AddTextActivity extends AppCompatActivity {
    private TextPreView mTextPreView;

    private String mPath;
    private String txt;

    private Handler mHandler;

    private Context mContext;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtext);
        mContext = this;
        iniView();
        iniData();

    }

    private void iniView() {
        mTextPreView = (TextPreView) findViewById(R.id.addtxt);
    }

    private void iniData() {
        mPath = getIntent().getStringExtra("path");
        txt = getIntent().getStringExtra("txt");
        if (TextUtils.isEmpty(mPath)) return;
        mTextPreView.setBitmapData(BitmapFactory.decodeFile(mPath));
        mTextPreView.setTxT(txt);

        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message msg) {
                if (msg.what == RESULT_OK) {
                    Toast.makeText(mContext, "保存成功!", Toast.LENGTH_SHORT).show();
                    setResult(RESULT_OK);
                    finish();
                }
                return true;
            }
        });

    }


    /**
     * 保存图片数据
     *
     * @param view
     */
    public void doSaveTxt(View view) {
        if (TextUtils.isEmpty(mPath)) return;
        final Bitmap bitmap = mTextPreView.saveTxt();
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    FileUtils.saveFile(bitmap, new File(mPath));
                    Message.obtain(mHandler, RESULT_OK).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
