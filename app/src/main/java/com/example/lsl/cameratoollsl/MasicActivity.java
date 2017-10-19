package com.example.lsl.cameratoollsl;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.lsl.cameratoollsl.widget.MasicPreView;

/**
 * 马赛克
 * Created by Administrator on 2017/10/19.
 */

public class MasicActivity extends AppCompatActivity {
    private MasicPreView mView;



    private String mPath;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_masic);
        mView = (MasicPreView) findViewById(R.id.masic_img);

        mPath = getIntent().getStringExtra("path");
        showImg();
    }


    /**
     * 显示图片
     */
    private void showImg() {
        Bitmap bitmap = BitmapFactory.decodeFile(mPath);
        mView.setBitmapData(bitmap);
    }
}
