package com.example.lsl.cameratoollsl;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lsl.cameratoollsl.utils.ImgUtil;

import java.io.File;

public class ThumbActivity extends AppCompatActivity implements View.OnClickListener {
    private ImageView mImageView;
    private final String[] items = {"图片详细信息", "添加文本", "裁剪图片", "打马赛克"};
    private TextView mTextView;
    private String path;
    private final String TAG = "thumb--->";

    //添加文本
    private LinearLayout mLinearLayout;
    private EditText mEditText;
    private Button mButton;
    private Handler mHandler;


    private final int CROP_CODE = 1000;
    private final int SHOW_IMG = 1003;
    private final int MASIC_CODE = 1002;
    private final int ADDTXT_CODE = 1004;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_thumb);

        iniview();

        ini();


        mHandler = new Handler(new Handler.Callback() {
            @Override
            public boolean handleMessage(Message message) {
                switch (message.what) {
                    case SHOW_IMG:
                        showImg();
                        mLinearLayout.setVisibility(View.GONE);
                        mEditText.getText().clear();
                        break;
                }
                return false;
            }
        });

    }

    private void iniview() {
        mImageView = (ImageView) findViewById(R.id.big_img);
        mTextView = (TextView) findViewById(R.id.more_opera);
        mLinearLayout = (LinearLayout) findViewById(R.id.add_text_layout);
        mEditText = (EditText) findViewById(R.id.et_add_text);
        mButton = (Button) findViewById(R.id.btn_add_text);
        mButton.setOnClickListener(this);
    }

    public void ini() {
        path = getIntent().getStringExtra("path");
        if (TextUtils.isEmpty(path)) {
            return;
        }
        showImg();
        mTextView.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.more_opera:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
            case R.id.btn_add_text:
                final String addtxt = mEditText.getText().toString().trim();
                if (TextUtils.isEmpty(addtxt)) {
                    Toast.makeText(ThumbActivity.this, "文本不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent_add = new Intent(this, AddTextActivity.class);
                intent_add.putExtra("path", path);
                intent_add.putExtra("txt", addtxt);
                startActivityForResult(intent_add, ADDTXT_CODE);

                mEditText.getText().clear();
                mLinearLayout.setVisibility(View.GONE);
                mTextView.setVisibility(View.VISIBLE);

                break;
        }
    }

    /**
     * {"0：图片详细信息", "1：添加文本", :2：裁剪图片", "3：打马赛克"};
     * 根据各个动作去相应功能
     *
     * @param which
     */
    private void dowhich(int which) {
        switch (which) {
            case 0:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage(ImgUtil.getExif(path));
                builder.create();
                builder.show();
                break;
            case 1:
                if (TextUtils.isEmpty(path)) return;
                mLinearLayout.setVisibility(View.VISIBLE);
                mTextView.setVisibility(View.GONE);

                break;
            case 2:
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                BitmapFactory.decodeFile(path, options);
                Intent intent = new Intent("com.android.camera.action.CROP");
                intent.setDataAndType(Uri.fromFile(new File(path)), "image/*");
//                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", options.outWidth);
                intent.putExtra("outputY", options.outHeight);
                intent.putExtra("scale", true);
                intent.putExtra("return-data", false);
                intent.putExtra("output", Uri.fromFile(new File(path)));//保存到原来的地方去
                intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
                intent = Intent.createChooser(intent, "裁剪图片");
                startActivityForResult(intent, CROP_CODE);
                break;
            case 3:
                if (TextUtils.isEmpty(path)) return;
                Intent intent1 = new Intent(this, MasicActivity.class);
                intent1.putExtra("path", path);
                startActivityForResult(intent1, MASIC_CODE);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == RESULT_OK) {
            switch (requestCode) {
                case CROP_CODE:
                case MASIC_CODE:
                case ADDTXT_CODE:
                    showImg();
                    break;
            }
        }
    }

    /**
     * 显示图片
     */
    private void showImg() {
        Bitmap bitmap = BitmapFactory.decodeFile(path);
        mImageView.setImageBitmap(bitmap);
    }

}
