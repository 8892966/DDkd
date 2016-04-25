package com.example.user.ddkd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mobstat.StatService;
import com.tencent.android.tpush.XGPushManager;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.Map;

/**
 * Created by Administrator on 2016/4/4.
 */
public class MainActivity_setting extends Activity implements View.OnClickListener {
    private TextView exit;
    private TextView userimage;
    private TextView updatepwd;
    private TextView clime;
    private TextView updateapp;
    private TextView aboutDD;
    private ImageView imageView;
    private RelativeLayout changeimage;
    private final int IMAGE_CODE=0;
    private Uri uri;
    private File tempFile;
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_setting);
        imageView=(ImageView)findViewById(R.id.setExit);
        imageView.setOnClickListener(this);
        exit=(TextView)findViewById(R.id.exit);
        exit.setOnClickListener(this);
        userimage= (TextView) findViewById(R.id.userimage);

        updatepwd= (TextView) findViewById(R.id.updatepwd);
        updatepwd.setOnClickListener(this);
        clime= (TextView) findViewById(R.id.cline);
        clime.setOnClickListener(this);
        updateapp= (TextView) findViewById(R.id.updateApp);
        updateapp.setOnClickListener(this);
        aboutDD= (TextView) findViewById(R.id.aboutDD);
        aboutDD.setOnClickListener(this);
        changeimage= (RelativeLayout) findViewById(R.id.changeimage);
        changeimage.setOnClickListener(this);
        ExitApplication.getInstance().addActivity(this);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
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
//                        Toast.makeText(MainActivity_setting.this,"已启动该选项",Toast.LENGTH_SHORT).show();
                    }
                }).show();

                break;
            case R.id.updatepwd:
                intent=new Intent(MainActivity_setting.this,Activity_updpwd.class);
                startActivity(intent);
                break;
            case R.id.cline:
                intent=new Intent(MainActivity_setting.this,Activity_feedback.class);
                startActivity(intent);
                break;
            case R.id.updateApp:
                new AlertDialog.Builder(this).setTitle("系统提示").setMessage("当前已是最新版本").show();
                break;
            case R.id.aboutDD:
                intent=new Intent(MainActivity_setting.this,WebActivity.class);
                intent.putExtra("title","关于DD快递");
                intent.putExtra("url","http://www.baidu.com");
                startActivity(intent);
                break;
        }

    }
    @Override
    protected void onResume(){
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        StatService.onPause(this);
    }

    DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener()
    {
        public void onClick(DialogInterface dialog, int which)
        {
            switch (which)
            {
                case AlertDialog.BUTTON_POSITIVE:// "确认"按钮退出程序
                    //点击确定退出以后，重新将loginstatic的值设置为“1”
                    SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
                    SharedPreferences.Editor editor=sharedPreferences.edit();
                    editor.putString("loginstatic","0");
//                    MyApplication.state=0;
                    editor.commit();
                    ExitApplication.getInstance().exit();
                    Intent intent=new Intent(MainActivity_setting.this,MainActivity_login.class);
                    startActivity(intent);
                    XGPushManager.unregisterPush(MainActivity_setting.this);
                    finish();
                    Toast.makeText(MainActivity_setting.this,"退出成功",Toast.LENGTH_SHORT).show();
                    break;
                case AlertDialog.BUTTON_NEGATIVE:// "取消"第二个按钮取消对话框
                    break;
                default:
                    break;
            }
        }
    };

    private void getImage(){
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        startActivityForResult(intent,IMAGE_CODE );
    }
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == IMAGE_CODE) {
            Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
            if (cameraBitmap != null) {
                imageView.setImageBitmap(cameraBitmap);
                uri = saveBitmap(cameraBitmap);
            }else{
                Toast.makeText(MainActivity_setting.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private Uri saveBitmap(Bitmap bm) {
        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/photo");
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
    public static String post(String actionUrl, Map<String, File> files) throws IOException {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(5 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false);// 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
        // 首先组拼文本类型的参数
        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        InputStream in = null;
        // 发送文件数据
        if (files != null) {
            for (Map.Entry<String, File> file : files.entrySet()) {
                StringBuilder sb1 = new StringBuilder();
                sb1.append(PREFIX);
                sb1.append(BOUNDARY);
                sb1.append(LINEND);
                // name是post中传参的键 filename是文件的名称
                sb1.append("Content-Disposition: form-data; name=\"file\"; filename=\"" + file.getKey() + "\"" + LINEND);
                sb1.append("Content-Type: application/octet-stream; charset=" + CHARSET + LINEND);
                sb1.append(LINEND);
                outStream.write(sb1.toString().getBytes());
                InputStream is = new FileInputStream(file.getValue());
                byte[] buffer = new byte[1024];
                int len = 0;
                while ((len = is.read(buffer)) != -1) {
                    outStream.write(buffer, 0, len);
                }
                is.close();
                outStream.write(LINEND.getBytes());
            }
            // 请求结束标志
            byte[] end_data = (PREFIX + BOUNDARY + PREFIX + LINEND).getBytes();
            outStream.write(end_data);
            outStream.flush();
            // 得到响应码
            int res = conn.getResponseCode();
            StringBuilder sb2 = new StringBuilder();
            Log.e("ZhuCe4Activity", res + "");
            if (res == 200) {
                in = conn.getInputStream();
                int ch;
                while ((ch = in.read()) != -1) {
                    sb2.append((char) ch);
                }
            } else {
                Log.e("ZhuCe4Activity", "访问出错");
            }
            outStream.close();
            conn.disconnect();
            return sb2.toString();
        }
//         return in.toString();
        return BOUNDARY;
    }

}
