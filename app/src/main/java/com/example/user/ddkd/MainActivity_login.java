package com.example.user.ddkd;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.utils.BitmaoCache;
import com.example.user.ddkd.utils.MyStringRequest;
import com.tencent.android.tpush.XGIOperateCallback;
import com.tencent.android.tpush.XGPushConfig;
import com.tencent.android.tpush.XGPushManager;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


/**
 * Created by Administrator on 2016/4/2.
 */
public class MainActivity_login extends Activity implements View.OnClickListener {
    private TextView button;
    private EditText userid1;
    private EditText password1;
    private TextView insert;
    private TextView forget;
    private ProgressDialog progressDialog;
    private CheckBox rembpwd;
    private File tempFile;
    private BitmaoCache bitmaoCache;

    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            ExitApplication.getInstance().exit();
            return false;
        }
        return super.onKeyDown(keyCode, event);
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_login);
        button = (TextView) findViewById(R.id.login);
        userid1 = (EditText) findViewById(R.id.userInfo);
        password1 = (EditText) findViewById(R.id.passwordInfo);
        insert = (TextView) findViewById(R.id.insert);
        forget = (TextView) findViewById(R.id.forget);
        rembpwd = (CheckBox) findViewById(R.id.rembpwd);

        SharedPreferences preferences01 = getSharedPreferences("config", MODE_PRIVATE);
        userid1.setText(preferences01.getString("phone", null));
        password1.setText(preferences01.getString("password", null));
        button.setOnClickListener(this);
        insert.setOnClickListener(this);
        forget.setOnClickListener(this);
        ExitApplication.getInstance().addActivity(this);

        //**********点击图标判断当前是否为登录状态**********
        SharedPreferences loginstatic = getSharedPreferences("config", MODE_PRIVATE);
        String nowLoginstatic = loginstatic.getString("loginstatic", "");
        if (nowLoginstatic.equals("1")) {
            // 开启logcat输出，方便debug，发布时请关闭
            XGPushConfig.enableDebug(MainActivity_login.this, true);
            // 如果需要知道注册是否成功，请使用eregisterPush(getApplicationContxt(), XGIOperateCallback)带callback版本
            // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
            // 具体可参考详细的开发指南
            // 传递的参数为ApplicationContext
            Context context = getApplicationContext();
            XGPushManager.registerPush(MainActivity_login.this, new XGIOperateCallback() {
                @Override
                public void onSuccess(Object data, int flag) {
                    Log.d("TPush", "注册成功，设备token为：" + data);
                    SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
                    SharedPreferences.Editor edit = preferences.edit();
                    edit.putString("XGtoken", (String) data);
                    edit.commit();
                }
                @Override
                public void onFail(Object data, int errCode, String msg) {
                    Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
                }
            });
            Intent intent = new Intent(MainActivity_login.this, JieDangActivity.class);
            startActivity(intent);
            finish();
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

    public void volley_Get(final String userid, final String password) {
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/login/phone/" + userid + "/password/" + password;
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.e("Get_login", s);
                closeProgressDialog();//*****关闭加载提示框*****
                if (s.equals("WAIT PASS")) {
                    closeProgressDialog();
                    Toast.makeText(MainActivity_login.this, "正在审核中，请耐心等候...", Toast.LENGTH_SHORT).show();
                } else if (!s.equals("ERROR")) {
                    s = s.substring(1, s.length() - 1);
                    //******************当提交成功以后，后台会返回一个参数来说明是否提交/验证成功******************
                    SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
                    SharedPreferences.Editor edit = sharedPreferences.edit();
                    edit.putString("token", s);
                    //****************保存登录状态，0为离线状态，1为在线状态************************
                    edit.putString("loginstatic", "1");
                    MyApplication.state = 1;
//                    MyApplication.state=1;
                    edit.commit();
                    // 开启logcat输出，方便debug，发布时请关闭
                    XGPushConfig.enableDebug(MainActivity_login.this, true);
                    // 如果需要知道注册是否成功，请使用eregisterPush(getApplicationContxt(), XGIOperateCallback)带callback版本
                    // 如果需要绑定账号，请使用registerPush(getApplicationContext(),account)版本
                    // 具体可参考详细的开发指南
                    // 传递的参数为ApplicationContext
                    Context context = getApplicationContext();
                    XGPushManager.registerPush(MainActivity_login.this, new XGIOperateCallback() {
                        @Override
                        public void onSuccess(Object data, int flag) {
                            Log.d("TPush", "注册成功，设备token为：" + data);
                            SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
                            SharedPreferences.Editor edit = preferences.edit();
                            edit.putString("XGtoken", (String) data);
                            edit.commit();
                        }

                        @Override
                        public void onFail(Object data, int errCode, String msg) {
                            Log.d("TPush", "注册失败，错误码：" + errCode + ",错误信息：" + msg);
                        }
                    });
                    closeProgressDialog();
                    Intent intent = new Intent(MainActivity_login.this, JieDangActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    closeProgressDialog();
                    Toast.makeText(MainActivity_login.this, "您的信息有误", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                closeProgressDialog();
                Toast.makeText(MainActivity_login.this, "网络连接中断", Toast.LENGTH_SHORT).show();
                Log.e("onErrorResponse", "onErrorResponse");
            }
        });
        request.setTag("abcGet_login");
        MyApplication.getQueue().add(request);
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()) {
            case R.id.login:
                //*****启动加载提示框*****
                showProgressDialog();
                //***********判断服务器返回的参数，根据参数来判断验证是否通过**********
                String phone = userid1.getText().toString();
                String password = password1.getText().toString();
                if (rembpwd.isChecked()) {
                    SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
                    SharedPreferences.Editor editor = preferences.edit();
                    editor.putString("phone", phone);
                    editor.putString("password", password);
                    editor.commit();
//                    Log.i("save","保存成功");
                }
                if (!TextUtils.isEmpty(phone)) {
                    if (!TextUtils.isEmpty(password)) {
                        volley_phoExist_GET(phone, password);
                    } else {
                        closeProgressDialog();
                        Toast.makeText(MainActivity_login.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    closeProgressDialog();
                    Toast.makeText(MainActivity_login.this, "账号不能为空", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.insert:
                intent = new Intent(this, ZhuCe1Activity.class);
                startActivity(intent);
                break;
            case R.id.forget:
                intent = new Intent(this, MainActivity_forget.class);
                startActivity(intent);
                break;
        }
    }

    private void showProgressDialog() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(MainActivity_login.this);
            progressDialog.setMessage("正在登陆.......");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    //判断用户是否已注册
    private void volley_phoExist_GET(final String phone, final String password) {
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/phoExist/phone/" + phone;
//        参数一：方法 参数二：地址 参数三：成功回调 参数四：错误回调 。重写getParams 以post参数
        StringRequest request_post = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("volley_phoExist_GET", s);
                if (!"SUCCESS".equals(s)) {
                    volley_Get(phone, password);
                } else {
                    closeProgressDialog();
                    Toast.makeText(MainActivity_login.this, "该用户还没注册！", Toast.LENGTH_SHORT).show();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                closeProgressDialog();
                Toast.makeText(MainActivity_login.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        });
        request_post.setTag("volley_phoExist_GET");
        MyApplication.getQueue().add(request_post);
    }
    public void volley_Get_Image(){
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        String url="http://www.louxiago.com/wc/ddkd/admin.php/User/getLogo/token/"+sharedPreferences.getString("token","");
        StringRequest stringRequest=new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
//                String s= (String) o;
//                bitmaoCache=new BitmaoCache();
//                String imageurl=s;
//                ImageLoader imageLoader=new ImageLoader(MyApplication.getQueue(),bitmaoCache);
//                ImageLoader.ImageListener imageListener=ImageLoader.getImageListener(userimage,R.drawable.personinfo3,R.drawable.personinfo3);
//                imageLoader.get(imageurl,);
////                userimage.getDrawable();
//                Bitmap bm = null;
//                File tmpDir = new File(Environment.getExternalStorageDirectory() + "/DDkdphoto");
//                imageLoader.get(imageurl,);
//                if (!tmpDir.exists()) {
//                    tmpDir.mkdir();
//                }
//                tempFile = new File(tmpDir, System.currentTimeMillis() + ".png");
//                try {
//                    FileOutputStream fos = new FileOutputStream(tempFile);
//                    bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
//                    fos.flush();
//                    fos.close();
//                } catch (FileNotFoundException e) {
//                    e.printStackTrace();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }

            }
            @Override
            public void tokenouttime() {

            }

            @Override
            public void yidiensdfsdf() {

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        stringRequest.setTag("volley_Get_Image_login");
        MyApplication.getQueue().add(stringRequest);
    }
}
