package com.example.lsl.cameratoollsl;

import android.graphics.Rect;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

/**
 * 添加文字
 * Created by Administrator on 2017/10/20.
 */

public class AddTextActivity extends AppCompatActivity {
    private Rect mRect = new Rect();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_addtext);

    }
}
