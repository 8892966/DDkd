package com.example.user.ddkd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.BitmaoCache;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.MyStringRequest;
import com.example.user.ddkd.utils.PostUtil;
import com.squareup.picasso.Picasso;
import com.tencent.android.tpush.XGPushManager;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.Objects;

/**
 * Created by Administrator on 2016/4/4.
 */
public class MainActivity_setting extends Activity implements View.OnClickListener {
    private TextView exit;
    private ImageView userimage;
    private TextView updatepwd;
    private TextView clime;
    private TextView version;
    private RelativeLayout updateapp;
    private TextView aboutDD;
    private ImageView imageView;
    private RelativeLayout changeimage;
    private Uri uri;
    private File tempFile;
    private String fileName = "";
    private Bitmap cameraBitmap;
    private BitmaoCache bitmaoCache;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MyApplication.GET_TOKEN_SUCCESS:
                    userimage.setImageBitmap(cameraBitmap);
                    Toast.makeText(MainActivity_setting.this, "图片修改成功", Toast.LENGTH_SHORT).show();
                    volley_Get_Image();
                    break;
                case MyApplication.GET_TOKEN_ERROR:
                    Toast.makeText(MainActivity_setting.this, "图片修改失败，请重试", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private Handler handler1=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MyApplication.GET_TOKEN_SUCCESS:
                    break;
                case MyApplication.GET_TOKEN_ERROR:
                    break;
            }
        }
    };
    //获取图片之后的中转文件;
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        initFile();
        imageView = (ImageView) findViewById(R.id.setExit);
        imageView.setOnClickListener(this);
        exit = (TextView) findViewById(R.id.exit);
        exit.setOnClickListener(this);
        userimage = (ImageView) findViewById(R.id.userimage);
        updatepwd = (TextView) findViewById(R.id.updatepwd);
        updatepwd.setOnClickListener(this);
        clime = (TextView) findViewById(R.id.cline);
        clime.setOnClickListener(this);
        updateapp = (RelativeLayout) findViewById(R.id.updateApp);
        version= (TextView) findViewById(R.id.version);
        updateapp.setOnClickListener(this);
        aboutDD = (TextView) findViewById(R.id.aboutDD);
        aboutDD.setOnClickListener(this);
        changeimage = (RelativeLayout) findViewById(R.id.changeimage);
        changeimage.setOnClickListener(this);
        ExitApplication.getInstance().addActivity(this);
        SharedPreferences sharedPreferences=getSharedPreferences("config", MODE_PRIVATE);
        version.setText(sharedPreferences.getString("version", ""));

        //用Picasso的缓存;
        String imageuri=sharedPreferences.getString("imageuri", "");
        Picasso.with(MainActivity_setting.this).load(imageuri).into(userimage);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.exit:
                AlertDialog isExit = new AlertDialog.Builder(this).create();
                // 设置对话框标题
                isExit.setTitle("系统提示");
                // 设置对话框消息
                isExit.setMessage("确定要退出吗?");
                // 添加选择按钮并注册监听
                isExit.setButton("确定", listener);
                isExit.setButton2("取消", listener);
                // 显示对话框
                isExit.show();
                break;
            case R.id.setExit:
                finish();
                break;
            //*****更换头像*****
            case R.id.changeimage:
                new AlertDialog.Builder(this).setPositiveButton("更换头像", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getImage();
                    }
                }).show();
                break;
            case R.id.updatepwd:
                intent = new Intent(MainActivity_setting.this, MainActivity_forget.class);
                startActivity(intent);
                break;
            case R.id.cline:
                intent = new Intent(MainActivity_setting.this, Activity_feedback.class);
                startActivity(intent);
                break;
            case R.id.updateApp:
                volley_Get();
                break;
            case R.id.aboutDD:
                intent = new Intent(MainActivity_setting.this, WebActivity.class);
                intent.putExtra("title", "关于DD快递");
                intent.putExtra("url", "http://www.louxiago.com/wc/ddkd/index.php/AboutDD/index.html");
                startActivity(intent);
                break;
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
    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    //点击确定退出以后，重新将loginstatic的值设置为“1”
                    Exit.exit(MainActivity_setting.this);
                    Toast.makeText(MainActivity_setting.this,"退出成功", Toast.LENGTH_SHORT).show();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    //*********************调用手机的相册********************************
    private void getImage() {
        Intent intent = new Intent(Intent.ACTION_PICK);// 打开相册
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        Log.e("ZhuCe3Activity", Uri.fromFile(tempFile).toString());
        intent.putExtra("output", Uri.fromFile(tempFile));
        startActivityForResult(intent, 11);
    }

    //***************************调用截图功能*************************
    private void crop(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        Log.e("crop", Uri.fromFile(tempFile).getPath());
        intent.putExtra("output", Uri.fromFile(tempFile));
        intent.putExtra("crop", true);
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        intent.putExtra("outputX", px2dip(this, 150));
        intent.putExtra("outputY", px2dip(this, 150));
        startActivityForResult(intent, 10);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (data != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;//图片的保存比例
                cameraBitmap = BitmapFactory.decodeFile(tempFile.getPath(), options);//设置图片的保存路径;
                if (cameraBitmap != null) {
                    uri = saveBitmap(cameraBitmap);
                    new Thread(){
                        @Override
                        public void run() {
                            super.run();
                            SharedPreferences sharedPreferences1 = getSharedPreferences("config", MODE_PRIVATE);
                            Map<String, String> map1 = new HashMap<String, String>();
                            map1.put("phone", sharedPreferences1.getString("phone", ""));
                            map1.put("name", "touxiang");
                            File file = new File(uri.getPath());
                            Map<String, File> mapfile = new HashMap<String, File>();
                            mapfile.put("touxiang", file);
                            //**********************图片修改成功之后就开始上传图片*********************************
                            try {
                                String msg1 = PostUtil.post("http://www.louxiago.com/wc/ddkd/admin.php/User/uploadimage/name/touxiang/phone/" + sharedPreferences1.getString("phone", ""), map1, mapfile);
                                if(msg1.equals("SUCCESS")) {
                                    volley_change_Get(sharedPreferences1.getString("phone", ""), sharedPreferences1.getString("token", ""));
                                }else{
                                    Log.i("Image_Get", msg1);
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            file.delete();
                        }
                    }.start();
                } else {
                    Toast.makeText(MainActivity_setting.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
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
    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    //保存图片
    private Uri saveBitmap(Bitmap bm) {
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/DDkdphoto");
        if (!tmpDir.exists()) {
            tmpDir.mkdir();
        }
        tempFile = new File(tmpDir, System.currentTimeMillis() + ".png");
        try {
            FileOutputStream fos = new FileOutputStream(tempFile);
            bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
            fos.flush();
            fos.close();
            return Uri.fromFile(tempFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            return null;
        } catch (IOException e) {
            e.printStackTrace();
            return null;
        }
    }
    public void volley_Get(){
        SharedPreferences sharedPreferences01=getSharedPreferences("config", MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences01.edit();
        editor.putString("version","1");
        editor.commit();
        String url="http://www.louxiago.com/wc/ddkd/admin.php/User/update";
        StringRequest request=new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s= (String) o;
                Log.i("version", s);
                SharedPreferences sharedPreferences02=getSharedPreferences("config",MODE_PRIVATE);
                if(sharedPreferences02.getString("version","").equals(s)){
                    new AlertDialog.Builder(MainActivity_setting.this).setTitle("系统提示").setMessage("当前已是最新版本").show();
                }else{
                    Toast.makeText(MainActivity_setting.this,"DD快递更新啦，快去应用商店下载吧",Toast.LENGTH_SHORT).show();
                    SharedPreferences.Editor editor1=sharedPreferences02.edit();
                    editor1.putString("version",s);
                    editor1.commit();
                }
                version.setText(s);
            }
            @Override
            public void tokenouttime() {
                Log.i("Token","token outtime");
                AutologonUtil autologonUtil=new AutologonUtil(MainActivity_setting.this,handler1, null);
                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
                Exit.exit(MainActivity_setting.this);
                Toast.makeText(MainActivity_setting.this,"您的账户已在异地登录",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity_setting.this,"网络连接出错",Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag("Get_Setting");
        MyApplication.getQueue().add(request);
    }
    public void volley_change_Get(String phone ,String token){
        String url="http://www.louxiago.com/wc/ddkd/admin.php/User/updateLogo/phone/"+phone+"/token/"+token;
        StringRequest request=new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s= (String) o;
                Log.i("Result", s);
                Message message = new Message();
                if (s.equals("SUCCESS")) {
                    message.what = MyApplication.GET_TOKEN_SUCCESS;
                    handler.sendMessage(message);
                } else {
                    message.what = MyApplication.GET_TOKEN_ERROR;
                    handler.sendMessage(message);
                }
            }
            @Override
            public void tokenouttime() {
                Log.i("Token","token outtime");
                AutologonUtil autologonUtil=new AutologonUtil(MainActivity_setting.this,handler1, null);
                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
                Exit.exit(MainActivity_setting.this);
                Toast.makeText(MainActivity_setting.this,"您的账户已在异地登录",Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity_setting.this,"网络连接出错",Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag("Get_Setting_changImage");
        MyApplication.getQueue().add(request);
    }
    public void volley_Get_Image() {
        final SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/getLogo/token/" + sharedPreferences.getString("token", "");
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putString("imageuri", s);
                editor.commit();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity_setting.this, "网络连接出错", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setTag("volley_Get_Image_login");
        MyApplication.getQueue().add(stringRequest);
    }
    public void initFile() {
        if (fileName.equals("")) {
            boolean sdCardExist = Environment.getExternalStorageState()
                    .equals(android.os.Environment.MEDIA_MOUNTED);
            if (sdCardExist) {
                String path = Environment.getExternalStorageDirectory().getPath() + "/DDkdPhoto";
                //设置文件的存储路径;
                tempFile = new File(path);
                if (!tempFile.exists()) {
                    tempFile.mkdir();
                }
                fileName = path + "/user1_head_photo.png";
                //设置文件的文件名;
                tempFile = new File(fileName);
            } else {
                Toast.makeText(this, "请插入SD卡", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void onDestroy(){
        super.onDestroy();
        MyApplication.getQueue().cancelAll("Get_Setting_changImage");
        MyApplication.getQueue().cancelAll("Get_Setting");
    }
}
