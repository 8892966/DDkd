package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.ddkd.beam.SignUpInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

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


    private File tempFile;
    private Uri uri1;
    private Uri uri2;
    private Uri uri3;
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
                intent.putExtra("SignUpInfo",getIntent().getSerializableExtra("SignUpInfo"));
                intent.putExtra("picture",getIntent().getSerializableExtra("picture"));
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
                SignUpInfo signUpInfo = (SignUpInfo) getIntent().getSerializableExtra("SignUpInfo");
                File file1=new File(uri1.getPath());
                file1.delete();
                File file2=new File(uri2.getPath());
                file2.delete();
                File file3=new File(uri3.getPath());
                file3.delete();
                Log.i("ZhuCe4Activity", signUpInfo.toString());
                Toast.makeText(this,"提交成功，请登录",Toast.LENGTH_SHORT).show();
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
                    uri1=saveBitmap(cameraBitmap);
                } else {
                    Toast.makeText(ZhuCe4Activity.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        }else if (requestCode == 102){
            if (data != null){
                Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
                if (cameraBitmap != null){
                    iv_zhuce4_zhaopian2.setImageBitmap(cameraBitmap);
                    uri2=saveBitmap(cameraBitmap);
                }else {
                    Toast.makeText(ZhuCe4Activity.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        }else if (requestCode == 103) {
            if (data != null) {
                Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
                if (cameraBitmap != null) {
                    iv_zhuce4_zhaopian3.setImageBitmap(cameraBitmap);
                    uri3=saveBitmap(cameraBitmap);
                } else {
                    Toast.makeText(ZhuCe4Activity.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private Uri saveBitmap(Bitmap bm){
        File tmpDir = new File(Environment.getExternalStorageDirectory()+"/photo");
        if(!tmpDir.exists()){
            tmpDir.mkdir();
        }
        tempFile = new File(tmpDir,System.currentTimeMillis()+".png");
        try{
            FileOutputStream fos = new FileOutputStream(tempFile);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(tempFile);
        }catch(FileNotFoundException e){
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
}
