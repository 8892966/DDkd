package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by User on 2016-04-03.
 */
public class ZhuCe3Activity extends Activity implements View.OnClickListener{
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce3_activity);

        iv_touxiang = (ImageView) findViewById(R.id.iv_touxiang);
        iv_touxiang.setOnClickListener(this);

        et_name= (EditText) findViewById(R.id.et_name);//姓名
        et_sex= (EditText) findViewById(R.id.et_sex);//性别
        et_xueyuan= (EditText) findViewById(R.id.et_xueyuan);//学院
        et_class= (EditText) findViewById(R.id.et_class);//班级
        et_phone= (EditText) findViewById(R.id.et_phone);//短号
        sp_diqu= (Spinner) findViewById(R.id.sp_diqu);//地区
        sp_loudong= (Spinner) findViewById(R.id.sp_loudong);//楼栋
        room_number= (EditText) findViewById(R.id.room_number);//房号
        et_id= (EditText) findViewById(R.id.et_id);//身份证
        et_xuehao= (EditText) findViewById(R.id.et_xuehao);//学号

     //**********************
        //下拉列表实现
        //地区
        String[] mItems ={"中区","南区","东区"};
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> _Adapter=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        //绑定 Adapter到控件
        sp_diqu.setAdapter(_Adapter);
        //楼栋
        String[] mItems2 ={"1","2","3"};
        // 建立Adapter并且绑定数据源
        ArrayAdapter<String> _Adapter2=new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, mItems);
        //绑定 Adapter到控件
        sp_loudong.setAdapter(_Adapter2);
//****************************
        TextView tv_head_fanghui= (TextView) findViewById(R.id.tv_head_fanghui);
        tv_head_fanghui.setOnClickListener(this);
    }
    public void next(View v){
        Toast.makeText(this,sp_diqu.getSelectedItem().toString(),Toast.LENGTH_LONG).show();

        Intent intent=new Intent(ZhuCe3Activity.this,ZhuCe4Activity.class);
        startActivity(intent);
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.tv_head_fanghui:
                intent=new Intent(ZhuCe3Activity.this,ZhuCe2Activity.class);
                startActivity(intent);
                finish();
                break;
            case R.id.iv_touxiang:
                intent=new Intent(Intent.ACTION_GET_CONTENT);
                intent.addCategory(Intent.CATEGORY_OPENABLE);
                intent.setType("image/*");
                intent.putExtra("crop", "true");
                intent.putExtra("aspectX", 1);
                intent.putExtra("aspectY", 1);
                intent.putExtra("outputX", 80);
                intent.putExtra("outputY", 80);
                intent.putExtra("return-data", true);
                startActivityForResult(intent,0);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        System.out.println(resultCode);
        if(data!=null) {
            Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
            if (cameraBitmap!=null) {
                iv_touxiang.setImageBitmap(cameraBitmap);
            }else{
                Toast.makeText(ZhuCe3Activity.this,"获取图片出错，请再次获取",Toast.LENGTH_SHORT).show();
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }
}
