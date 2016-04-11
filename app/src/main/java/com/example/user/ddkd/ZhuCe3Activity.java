package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Parcelable;
import android.text.method.CharacterPickerDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.ddkd.beam.ZhuCeInfo;

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

    //地区
    private String[] mItems = {"中区", "南区", "东区"};
    //楼栋
    private String[][] mItems2 = {{"1","2","3"},{"4","5","6"},{"7","8","9"}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce3_activity);

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
        ZhuCeInfo zhuCeInfo= (ZhuCeInfo) getIntent().getSerializableExtra("zhuCeInfo");
        zhuCeInfo.getUsername();
        zhuCeInfo.getNumber();
        zhuCeInfo.getCollege();
        //*******************
        //**********************
        //下拉列表实现
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> _Adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, mItems);
        //绑定 Adapter到控件
        sp_diqu.setAdapter(_Adapter);
        sp_diqu.setPrompt("地区");
        sp_diqu.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                // 建立Adapter并且绑定数据源
                ArrayAdapter<String> _Adapter2 = new ArrayAdapter<String>(ZhuCe3Activity.this, android.R.layout.simple_spinner_item, mItems2[position]);
                //绑定 Adapter到控件
                sp_loudong.setAdapter(_Adapter2);
                sp_loudong.setPrompt("楼栋");
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//****************************
        TextView tv_head_fanghui = (TextView) findViewById(R.id.tv_head_fanghui);
        tv_head_fanghui.setOnClickListener(this);
    }

    public void next(View v) {
//        Toast.makeText(this, sp_diqu.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
        //注册信息
        ZhuCeInfo zhuCeInfo= (ZhuCeInfo) getIntent().getSerializableExtra("zhuCeInfo");
        zhuCeInfo.setCollege(et_xueyuan.getText().toString());
        zhuCeInfo.setUsername(et_name.getText().toString());
        zhuCeInfo.setNumber(et_xuehao.getText().toString());
        Intent intent = new Intent(ZhuCe3Activity.this, ZhuCe4Activity.class);
        intent.putExtra("zhuCeInfo",zhuCeInfo);//传递注册信息
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_head_fanghui:
                intent = new Intent(ZhuCe3Activity.this, MainActivity_login.class);
                startActivity(intent);
                finish();
                break;
            case R.id.iv_touxiang:
                Uri uri=Uri.parse(Environment.getDataDirectory().getPath()+"/123.png");
                crop(uri);
                break;
        }
    }

    private void crop(Uri uri){
        Intent intent;
        intent = new Intent("com.android.camera.action.CROP");
//                intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 250);
        intent.putExtra("outputY", 250);
//        intent.putExtra("data"，);

        intent.putExtra("return-data", true);
        startActivityForResult(intent, 0);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(resultCode);
        if (data != null){
            Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
            if (cameraBitmap != null) {
                iv_touxiang.setImageBitmap(cameraBitmap);
            }else {
                Toast.makeText(ZhuCe3Activity.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
