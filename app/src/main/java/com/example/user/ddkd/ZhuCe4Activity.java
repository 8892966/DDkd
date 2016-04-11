package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.ddkd.beam.ZhuCeInfo;

import java.io.File;

/**
 * Created by User on 2016-04-03.
 */
public class ZhuCe4Activity extends Activity implements View.OnClickListener {
    //放照片文件
    private File file;
    //放照片的控件
    private ImageView iv_zhuce4_zhaopian1;
    private ImageView iv_zhuce4_zhaopian2;
    private ImageView iv_zhuce4_zhaopian3;
    private TextView textView;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce4_activity);
        //拍照
        TextView tv_button1_paizhao = (TextView) findViewById(R.id.tv_button1_paizhao);
        TextView tv_button2_paizhao = (TextView) findViewById(R.id.tv_button2_paizhao);
        TextView tv_button3_paizhao = (TextView) findViewById(R.id.tv_button3_paizhao);

        textView=(TextView)findViewById(R.id.tv_button_next);
        textView.setOnClickListener(this);

        //放照片的地方
        iv_zhuce4_zhaopian1= (ImageView) findViewById(R.id.iv_zhuce4_zhaopian1);
        iv_zhuce4_zhaopian2= (ImageView) findViewById(R.id.iv_zhuce4_zhaopian2);
        iv_zhuce4_zhaopian3= (ImageView) findViewById(R.id.iv_zhuce4_zhaopian3);

        tv_button1_paizhao.setOnClickListener(this);
        tv_button2_paizhao.setOnClickListener(this);
        tv_button3_paizhao.setOnClickListener(this);

        //返回
        TextView tv_head_fanghui = (TextView) findViewById(R.id.tv_head_fanghui);
        tv_head_fanghui.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_head_fanghui:
                Intent intent = new Intent(ZhuCe4Activity.this, ZhuCe3Activity.class);
                intent.putExtra("zhuCeInfo",getIntent().getSerializableExtra("zhuCeInfo"));
                startActivity(intent);
                finish();
                break;
            case R.id.tv_button1_paizhao:
                paizhao(100);
                break;
            case R.id.tv_button2_paizhao:
                paizhao(102);
                break;
            case R.id.tv_button3_paizhao:
                paizhao(103);
                break;
            case R.id.tv_button_next:
                ZhuCeInfo zhuCeInfo= (ZhuCeInfo) getIntent().getSerializableExtra("zhuCeInfo");
                Log.i("ZhuCe4Activity",zhuCeInfo.toString());
                Toast.makeText(this,"注册成功，请登录",Toast.LENGTH_SHORT).show();
                Intent intent1=new Intent(ZhuCe4Activity.this,MainActivity_login.class);
                startActivity(intent1);
                finish();
                break;
        }
    }
    private void paizhao(int code) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        startActivityForResult(intent,code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (data != null) {
                Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
                if (cameraBitmap != null) {
                    iv_zhuce4_zhaopian1.setImageBitmap(cameraBitmap);
                } else {
                    Toast.makeText(ZhuCe4Activity.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        }else if (requestCode == 102){
            if (data != null){
                Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
                if (cameraBitmap != null){
                    iv_zhuce4_zhaopian2.setImageBitmap(cameraBitmap);
                }else {
                    Toast.makeText(ZhuCe4Activity.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        }else if (requestCode == 103) {
            if (data != null) {
                Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
                if (cameraBitmap != null) {
                    iv_zhuce4_zhaopian3.setImageBitmap(cameraBitmap);
                } else {
                    Toast.makeText(ZhuCe4Activity.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
