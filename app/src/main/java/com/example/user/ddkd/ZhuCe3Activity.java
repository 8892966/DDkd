package com.example.user.ddkd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
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
import com.example.user.ddkd.utils.PostUtil;
import com.example.user.ddkd.utils.UploadUtil;
import com.lidroid.xutils.http.RequestParams;

import org.w3c.dom.Text;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

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
    private ImageView iv_touxiang;
    private String fileName = "";//获取头像的中转文件
    private File tempFile;
    private SignUpInfo signUpInfo;

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
        et_id = (EditText) findViewById(R.id.et_id);//身份证
        et_xuehao = (EditText) findViewById(R.id.et_xuehao);//学号

        //*************判断是否是在注册页面4返回回来的，如果是回显数据**************
        signUpInfo = (SignUpInfo) getIntent().getSerializableExtra("SignUpInfo");
        if (signUpInfo.getUsername() != null) {
            et_name.setText(signUpInfo.getUsername());
            et_sex.setText(signUpInfo.getSex());
            et_class.setText(signUpInfo.getClazz());
            et_phone.setText(signUpInfo.getNumber());
            et_xueyuan.setText(signUpInfo.getCollege());
            et_id.setText(signUpInfo.getId_card());
            et_xuehao.setText(signUpInfo.getId_card());
            fileName = getIntent().getStringExtra("picture");
            tempFile = new File(fileName);
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = 1;
            Bitmap cameraBitmap = BitmapFactory.decodeFile(tempFile.getPath(), options);
            iv_touxiang.setImageBitmap(cameraBitmap);
        }
        //标题头的返回按钮
        TextView tv_head_fanghui = (TextView) findViewById(R.id.tv_head_fanghui);
        tv_head_fanghui.setOnClickListener(this);
    }

    public void next(View v) {
//        Toast.makeText(this, sp_diqu.getSelectedItem().toString(), Toast.LENGTH_LONG).show();
        //注册信息
        if (!TextUtils.isEmpty(et_xueyuan.getText().toString()) &&
                !TextUtils.isEmpty(et_name.getText().toString()) &&
                !TextUtils.isEmpty(et_xuehao.getText().toString()) &&
                !TextUtils.isEmpty(et_class.getText().toString()) &&
                !TextUtils.isEmpty(et_phone.getText().toString()) &&
                !TextUtils.isEmpty(et_id.getText().toString()) &&
                !TextUtils.isEmpty(et_sex.getText().toString()) &&
                new File(fileName).length() > 0) {
            if(!personIdValidation(et_id.getText().toString())){
                Toast.makeText(this, "身份证号码不对!!", Toast.LENGTH_LONG).show();
            }else {
                signUpInfo.setCollege(et_xueyuan.getText().toString());
                signUpInfo.setUsername(et_name.getText().toString());
                signUpInfo.setNumber(et_xuehao.getText().toString());
                signUpInfo.setClazz(et_class.getText().toString());
                signUpInfo.setShortnumber(et_phone.getText().toString());
                signUpInfo.setId_card(et_id.getText().toString());
                signUpInfo.setSex(et_sex.getText().toString());
                RequestParams requestParams = new RequestParams();
                requestParams.addBodyParameter("name", "touxiang");
                requestParams.addBodyParameter("phone", signUpInfo.getPhone());
                requestParams.addBodyParameter("file", new File(fileName));
                new UploadUtil().uploadMethod(requestParams, "http://www.louxiago.com/wc/ddkd/admin.php/User/uploadimage", null, null, null, null);
                Intent intent = new Intent(ZhuCe3Activity.this, ZhuCe4Activity.class);
                intent.putExtra("SignUpInfo", signUpInfo);//传递注册信息
                intent.putExtra("picture", fileName);
                startActivity(intent);
                finish();
            }
        } else {
            Toast.makeText(this, "请把信息填写完整!!", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        Intent intent = new Intent(ZhuCe3Activity.this, ZhuCe2Activity.class);
        intent.putExtra("SignUpInfo", getIntent().getSerializableExtra("SignUpInfo"));
        intent.putExtra("picture", getIntent().getSerializableExtra("picture"));
        startActivity(intent);
        finish();
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.tv_head_fanghui:
                intent = new Intent(ZhuCe3Activity.this, ZhuCe2Activity.class);
                intent.putExtra("SignUpInfo", getIntent().getSerializableExtra("SignUpInfo"));
                intent.putExtra("picture", getIntent().getSerializableExtra("picture"));
                startActivity(intent);
                finish();
                break;
            case R.id.iv_touxiang:
                getPhoto();
                break;
        }
    }

    private void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);// 打开相册
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
//        Log.e("ZhuCe3Activity", Uri.fromFile(tempFile).toString());
        intent.putExtra("output", Uri.fromFile(tempFile));
        startActivityForResult(intent, 11);
    }

    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
//        Log.e("crop", Uri.fromFile(tempFile).getPath());
        intent.putExtra("output", Uri.fromFile(tempFile));
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        startActivityForResult(intent, 10);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        System.out.println(resultCode);
        if (requestCode == 10) {
            if (data != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                Bitmap cameraBitmap = BitmapFactory.decodeFile(tempFile.getPath(), options);
                cameraBitmap=toRoundBitmap(cameraBitmap);
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
//                Log.e("uri", uri.toString());
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
                String path = Environment.getExternalStorageDirectory().getPath() + "/DDkdPhoto";
//                FileUtil.mkdir(path);
//                Logger.i("path:" + path);
                tempFile = new File(path);
                if (!tempFile.exists()){
                    tempFile.mkdir();
                }
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

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    //**************************将裁剪的图片转换为圆形*************************
    public Bitmap toRoundBitmap(Bitmap bitmap) {
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        float roundPx;
        float left,top,right,bottom,dst_left,dst_top,dst_right,dst_bottom;
        if (width <= height) {
            roundPx = width / 2;
            top = 0;
            bottom = width;
            left = 0;
            right = width;
            height = width;
            dst_left = 0;
            dst_top = 0;
            dst_right = width;
            dst_bottom = width;
        } else {
            roundPx = height / 2;
            float clip = (width - height) / 2;
            left = clip;
            right = width - clip;
            top = 0;
            bottom = height;
            width = height;
            dst_left = 0;
            dst_top = 0;
            dst_right = height;
            dst_bottom = height;
        }
        Bitmap output = Bitmap.createBitmap(width,
                height, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect src = new Rect((int)left, (int)top, (int)right, (int)bottom);
        final Rect dst = new Rect((int)dst_left, (int)dst_top, (int)dst_right, (int)dst_bottom);
        final RectF rectF = new RectF(dst);
        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, src, dst, paint);
        return output;
    }

    /**
     * 18位或者15位身份证验证 18位的最后一位可以是字母x
     *
     * @param text
     * @return
     */
    public static boolean personIdValidation(String text) {
            boolean flag = false;
        try {
            String regX = "[0-9]{17}X";
            String regx = "[0-9]{17}x";
            String reg1 = "[0-9]{15}";
            String regex = "[0-9]{18}";
            flag = text.matches(regx) || text.matches(reg1) || text.matches(regex)|| text.matches(regX);
        }catch (Exception e){
            Log.e("Exception", e.getMessage());
        }
        return flag;
    }

}
