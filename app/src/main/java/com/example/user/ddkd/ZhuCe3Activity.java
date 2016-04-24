package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.example.user.ddkd.beam.SignUpInfo;

import org.w3c.dom.Text;

import java.io.File;

/**
 * Created by User on 2016-04-03.
 */
public class ZhuCe3Activity extends Activity implements View.OnClickListener {
    private EditText et_name;
    private EditText et_sex;
    private EditText et_xueyuan;
    private EditText et_class;
    private EditText et_phone;
    private EditText et_id;
    private EditText et_xuehao;
    private EditText room_number;
    private ImageView iv_touxiang;
    private Spinner sp_diqu;
    private Spinner sp_loudong;
    private String fileName = "";//帮助文件
    private File tempFile;
    private TextView exit;

    //地区
    private String[] mItems = {"中区", "南区", "东区"};
    //楼栋
    private String[][] mItems2 = {{"1", "2", "3"}, {"4", "5", "6"}, {"7", "8", "9"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce3_activity);
        initFile();
        iv_touxiang = (ImageView) findViewById(R.id.iv_touxiang);
        iv_touxiang.setOnClickListener(this);
        et_name = (EditText) findViewById(R.id.et_name);//姓名
        et_sex = (EditText) findViewById(R.id.et_sex);//性别
        et_xueyuan = (EditText) findViewById(R.id.et_xueyuan);//学院
        et_class = (EditText) findViewById(R.id.et_class);//班级
        et_phone = (EditText) findViewById(R.id.et_phone);//短号
        sp_diqu = (Spinner) findViewById(R.id.sp_diqu);//地区
        sp_loudong = (Spinner) findViewById(R.id.sp_loudong);//楼栋
        room_number = (EditText) findViewById(R.id.room_number);//房号
        et_id = (EditText) findViewById(R.id.et_id);//身份证
        et_xuehao = (EditText) findViewById(R.id.et_xuehao);//学号
        //**************************判断是否是在注册页面4返回回来的，如果是回显数据
        SignUpInfo signUpInfo = (SignUpInfo) getIntent().getSerializableExtra("SignUpInfo");
        if (signUpInfo.getUsername() != null) {
            et_name.setText(signUpInfo.getUsername());
            et_phone.setText(signUpInfo.getNumber());
            et_xueyuan.setText(signUpInfo.getCollege());
            fileName = getIntent().getStringExtra("picture");
            tempFile = new File(fileName);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 2;
            Bitmap cameraBitmap = BitmapFactory.decodeFile(tempFile.getPath(), options);
            iv_touxiang.setImageBitmap(cameraBitmap);
        }
        //*******************
        //**********************
        //下拉列表实现
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        //绑定 Adapter到控件
        sp_diqu.setPrompt("地区");
        sp_diqu.setAdapter(_Adapter);
        sp_diqu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 建立Adapter并且绑定数据源
                ArrayAdapter<String> _Adapter2 = new ArrayAdapter<String>(ZhuCe3Activity.this, android.R.layout.simple_spinner_item, mItems2[position]);
                //绑定 Adapter到控件
                sp_loudong.setPrompt("楼栋");
                sp_loudong.setAdapter(_Adapter2);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

//****************************
        //标题头的返回按钮
        TextView tv_head_fanghui = (TextView) findViewById(R.id.tv_head_fanghui);
        tv_head_fanghui.setOnClickListener(this);
        exit= (TextView) findViewById(R.id.tv_button_next);
        exit.setOnClickListener(this);

    }

    public void next(View v) {
//        Toast.makeText(this, sp_diqu.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
        //注册信息
        SignUpInfo signUpInfo = (SignUpInfo) getIntent().getSerializableExtra("SignUpInfo");
        if (!TextUtils.isEmpty(et_xueyuan.getText().toString()) &&
                !TextUtils.isEmpty(et_name.getText().toString()) &&
                !TextUtils.isEmpty(et_xuehao.getText().toString()) &&
                !TextUtils.isEmpty(et_class.getText().toString()) &&
                !TextUtils.isEmpty(et_phone.getText().toString()) &&
                !TextUtils.isEmpty(et_id.getText().toString()) &&
                !TextUtils.isEmpty(et_sex.getText().toString())) {
            signUpInfo.setCollege(et_xueyuan.getText().toString());
            signUpInfo.setUsername(et_name.getText().toString());
            signUpInfo.setNumber(et_xuehao.getText().toString());
            signUpInfo.setClazz(et_class.getText().toString());
            signUpInfo.setShortnumber(et_phone.getText().toString());
            signUpInfo.setId_card(et_id.getText().toString());
            signUpInfo.setSex(et_sex.getText().toString());
            Intent intent = new Intent(ZhuCe3Activity.this, ZhuCe4Activity.class);
            intent.putExtra("SignUpInfo", signUpInfo);//传递注册信息
            intent.putExtra("picture", fileName);
            startActivity(intent);
            finish();
        } else {
            Toast.makeText(this,"请把信息填写完整!!",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_head_fanghui:
//                intent = new Intent(ZhuCe3Activity.this, MainActivity_login.class);
//                startActivity(intent);
                finish();
                break;
            case R.id.iv_touxiang:
//                Uri uri = Uri.parse(Environment.getExternalStorageDirectory().getPath()+"/test.png");
//                Log.e("uri",uri.toString());
                getPhoto();
                break;
            case R.id.tv_button_next:
                intent=new Intent(ZhuCe3Activity.this,ZhuCe4Activity.class);
                startActivity(intent);
                break;
        }
    }

    private void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);// 打开相册
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        Log.e("ZhuCe3Activity", Uri.fromFile(tempFile).toString());
        intent.putExtra("output", Uri.fromFile(tempFile));
        startActivityForResult(intent, 11);
    }

    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        Log.e("crop", Uri.fromFile(tempFile).getPath());
        intent.putExtra("output", Uri.fromFile(tempFile));
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        System.out.println(resultCode);
        if (requestCode == 10) {
            if (data != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 2;
                Bitmap cameraBitmap = BitmapFactory.decodeFile(tempFile.getPath(), options);
//                Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
                if (cameraBitmap != null) {
                    iv_touxiang.setImageBitmap(cameraBitmap);
                } else {
                    Toast.makeText(ZhuCe3Activity.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == 11) {
            if (data != null) {
                Uri uri = data.getData();
                Log.e("uri", uri.toString());
                crop(uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void initFile() {
        if (fileName.equals("")) {
            boolean sdCardExist = Environment.getExternalStorageState()
                    .equals(android.os.Environment.MEDIA_MOUNTED);
            if (sdCardExist) {
                String path = Environment.getExternalStorageDirectory().getPath()+"/photo";
//                FileUtil.mkdir(path);
//                Logger.i("path:" + path);
                fileName = path + "/user1_head_photo.png";
                tempFile = new File(fileName);
            } else {
                Toast.makeText(this, "请插入SD卡", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        StatService.onPause(this);
    }
}
