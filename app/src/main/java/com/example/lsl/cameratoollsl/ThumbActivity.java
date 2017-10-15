package com.example.lsl.cameratoollsl;

import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.media.ExifInterface;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lsl.cameratoollsl.utils.ImgUtil;
import com.example.lsl.cameratoollsl.utils.ScreenUtils;
import com.example.lsl.cameratoollsl.utils.TimeUtils;

import java.io.File;
import java.io.IOException;

public class ThumbActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mImageView;
    private final String[] items = {"图片详细信息", "添加文本", "裁剪图片", "打马赛克"};
    private TextView mTextView;
    private String path;
    private final String TAG = "thumb--->";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumb);

        mImageView = (ImageView) findViewById(R.id.big_img);
        mTextView = (TextView) findViewById(R.id.more_opera);

        ini();

    }

    public void ini() {
        path = getIntent().getStringExtra("path");
        if (path == null || path.equals("")) {
            return;
        }
        Bitmap bitmap = ImgUtil.getThumbBitmap(path, ScreenUtils.getScreenWidth(this), ScreenUtils.getScreenHeight(this));
        mImageView.setImageBitmap(bitmap);

        mTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_opera:
                AlertDialog.Builder builder = new AlertDialog.Builder(this, android.R.style.ThemeOverlay_Material_Dialog);
                builder.setItems(items, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //do something about capture
                        dowhich(which);
                    }
                });
                builder.create();
                builder.show();
                break;
        }
    }

    /**
     * {"图片详细信息", "添加文本", "裁剪图片", "打马赛克"};
     *
     * @param which
     */
    private void dowhich(int which) {
        switch (which) {
            case 0:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(getExif(path));
                builder.create();
                builder.show();
                break;
            case 1:
                break;
        }
    }

    private String getExif(String path) {
        StringBuffer stringBuffer = new StringBuffer();
        try {
            ExifInterface exifInterface = new ExifInterface(path);

            String TAG_DATETIME = exifInterface.getAttribute(ExifInterface.TAG_DATETIME);
            String TAG_IMAGE_LENGTH = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_LENGTH);
            String TAG_IMAGE_WIDTH = exifInterface.getAttribute(ExifInterface.TAG_IMAGE_WIDTH);

            File file = new File(path);
            if (TAG_DATETIME == null || TAG_DATETIME.equals("")) {
                TAG_DATETIME = TimeUtils.pareTime(file.lastModified());
            }
            Log.i(TAG, "拍摄时间:" + TAG_DATETIME);
            Log.i(TAG, "图片高度:" + TAG_IMAGE_LENGTH);
            Log.i(TAG, "图片宽度:" + TAG_IMAGE_WIDTH);
            Log.e(TAG, file.length() + "");
            stringBuffer.append("拍摄时间:").append(TAG_DATETIME).append("\n")
                    .append("文件大小:").append(file.length() / 1024f / 1024f).append("M").append("\n")
                    .append("像素:" + TAG_IMAGE_LENGTH + "x" + TAG_IMAGE_WIDTH).append("\n")
                    .append("拍摄路径:" + path);


        } catch (IOException e) {
            e.printStackTrace();
        }
        return stringBuffer.toString();
    }
}
