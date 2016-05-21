package com.example.user.ddkd;

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
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.user.ddkd.Presenter.ZhuCePresenterImpl;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 2016-04-19.
 */
public class ZhuCe3Fragment extends Fragment implements View.OnClickListener {
    private EditText et_name;
    private EditText et_sex;
    private EditText et_xueyuan;
    private EditText et_class;
    private EditText et_phone;
    private EditText et_id;
    private EditText et_xuehao;
    private ImageView iv_touxiang;
    public String fileName = "";//获取头像的中转文件
    private File tempFile;
    private ZhuCePresenterImpl zhuCePresenter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhuce3, container, false);
        zhuCePresenter=ZhuCePresenterImpl.getInstance(null);
        iv_touxiang = (ImageView) view.findViewById(R.id.iv_touxiang);
        iv_touxiang.setOnClickListener(this);
        et_name = (EditText) view.findViewById(R.id.et_name);//姓名
        et_sex = (EditText) view.findViewById(R.id.et_sex);//性别
        et_xueyuan = (EditText) view.findViewById(R.id.et_xueyuan);//学院
        et_class = (EditText) view.findViewById(R.id.et_class);//班级
        et_phone = (EditText) view.findViewById(R.id.et_phone);//短号
        et_id = (EditText) view.findViewById(R.id.et_id);//身份证
        et_xuehao = (EditText) view.findViewById(R.id.et_xuehao);//学号
        return view;
 }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_touxiang:
                initFile();
                getPhoto();
                break;
        }
    }

    public boolean next(){
        if (!TextUtils.isEmpty(et_xueyuan.getText().toString()) &&
                !TextUtils.isEmpty(et_name.getText().toString()) &&
                !TextUtils.isEmpty(et_xuehao.getText().toString()) &&
                !TextUtils.isEmpty(et_class.getText().toString()) &&
                !TextUtils.isEmpty(et_phone.getText().toString()) &&
                !TextUtils.isEmpty(et_id.getText().toString()) &&
                !TextUtils.isEmpty(et_sex.getText().toString()) &&
                new File(fileName).length() > 0) {
            if(!personIdValidation(et_id.getText().toString())){
                Toast.makeText(getActivity(), "身份证号码不对!!", Toast.LENGTH_LONG).show();
            }else {
                Map<String,String> map=new HashMap<>();
                map.put("class", et_class.getText().toString());
                map.put("college", et_xueyuan.getText().toString());
                map.put("number", et_xuehao.getText().toString());
                map.put("IdCardNum", et_id.getText().toString());
                map.put("sex", et_sex.getText().toString());
                map.put("shortphone", et_phone.getText().toString());
                map.put("username", et_name.getText().toString());
                zhuCePresenter.addmap(map);
                return true;
            }
        } else {
            Toast.makeText(getActivity(), "请把信息填写完整!!", Toast.LENGTH_LONG).show();
        }
        return false;
    }

    private void getPhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK);// 打开相册
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        intent.putExtra("output", Uri.fromFile(tempFile));
        startActivityForResult(intent, 11);
    }

    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("output", Uri.fromFile(tempFile));
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        startActivityForResult(intent, 10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (data != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                Log.e("onActivityResult",tempFile.getPath());
                Bitmap cameraBitmap = BitmapFactory.decodeFile(tempFile.getPath(), options);
                if (cameraBitmap != null) {
                    cameraBitmap=toRoundBitmap(cameraBitmap);
                    saveBitmap(cameraBitmap,new File(fileName));
                    iv_touxiang.setImageBitmap(cameraBitmap);
                } else {
                    Toast.makeText(getActivity(), "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == 11) {
            if (data != null) {
                Uri uri = data.getData();
                crop(uri);
            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    public void initFile() {
        if (fileName.equals("")) {
            boolean sdCardExist = Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED);
            if (sdCardExist) {
                String path = Environment.getExternalStorageDirectory().getPath() + "/DDkdPhoto";
                tempFile = new File(path);
                if (!tempFile.exists()){
                    tempFile.mkdir();
                }
                fileName = path + "/user1_head_photo.png";
                tempFile = new File(fileName);
            } else {
                Toast.makeText(getActivity(), "请插入SD卡", Toast.LENGTH_SHORT).show();
            }
        }
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

    //保存图片
    private Uri saveBitmap(Bitmap bm,File file) {
        try {
            FileOutputStream fos = new FileOutputStream(file);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
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
