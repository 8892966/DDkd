package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce4_activity);
        //拍照
        TextView tv_button1_paizhao = (TextView) findViewById(R.id.tv_button1_paizhao);
        TextView tv_button2_paizhao = (TextView) findViewById(R.id.tv_button2_paizhao);
        TextView tv_button3_paizhao = (TextView) findViewById(R.id.tv_button3_paizhao);

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
        }
    }
    private void paizhao(int code) {
        Intent intent = new Intent();
        // 指定拍照的意图。
        intent.setAction(MediaStore.ACTION_IMAGE_CAPTURE);
        file = new File(Environment.getExternalStorageDirectory(),"a"+System.currentTimeMillis() + ".jpg");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,Uri.fromFile(file)); // 指定保存文件的路径
        startActivityForResult(intent,code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if(resultCode == RESULT_OK){
                iv_zhuce4_zhaopian1.setImageURI(Uri.fromFile(file));
            }else if(resultCode ==RESULT_CANCELED){
                Toast.makeText(this,"拍照取消",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"拍照失败",Toast.LENGTH_LONG).show();
            }

        }else if (requestCode == 102) {
            if(resultCode == RESULT_OK){
                iv_zhuce4_zhaopian2.setImageURI(Uri.fromFile(file));
            }else if(resultCode ==RESULT_CANCELED){
                Toast.makeText(this,"拍照取消",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"拍照失败",Toast.LENGTH_LONG).show();
            }

        }else if (requestCode == 103) {
            if(resultCode == RESULT_OK){
                iv_zhuce4_zhaopian3.setImageURI(Uri.fromFile(file));
            }else if(resultCode ==RESULT_CANCELED){
                Toast.makeText(this,"拍照取消",Toast.LENGTH_LONG).show();
            }else {
                Toast.makeText(this,"拍照失败",Toast.LENGTH_LONG).show();
            }

        }
//        super.onActivityResult(requestCode, resultCode, data);
    }
}
