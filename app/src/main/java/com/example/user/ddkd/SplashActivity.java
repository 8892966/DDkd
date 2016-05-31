package com.example.user.ddkd;

import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.content.DialogInterface.OnClickListener;
import android.view.animation.AlphaAnimation;
import android.widget.TextView;
import android.widget.Toast;
import android.content.DialogInterface.OnCancelListener;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.SendStrategyEnum;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.MyStringRequest;
import com.example.user.ddkd.utils.StreamTools;
import net.tsz.afinal.FinalHttp;
import net.tsz.afinal.http.AjaxCallBack;

import org.json.JSONException;
import org.json.JSONObject;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class SplashActivity extends BaseActivity {
    protected static final int ENTER_HOME = 0;
    protected static final int SHOW_UPDATE_DIALOG = 1;
    protected static final int URL_ERROR = 2;
    protected static final int NETWORK_ERROR = 3;
    protected static final int JSON_ERROR = 4;
    protected static final String TAG = "SplashActivity";
    private TextView tv;
    private String description;
    private String apkurl;
    private TextView jindu;
    private SharedPreferences preferences;
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case ENTER_HOME:
                    if(!isFinishing()) {
                        enterhome();
                    }
                    break;
                case SHOW_UPDATE_DIALOG:
                    if(!isFinishing()) {
                        Log.i(TAG, "显示升级的对话框");
                        showupdateDialog();
                    }
                    break;
                case URL_ERROR:
                    if(!isFinishing()) {
                        enterhome();
                        Log.i(TAG, "url错误");
                    }
                    break;
                case NETWORK_ERROR:
                    if(!isFinishing()) {
                        enterhome();
                        Log.i(TAG, "网络异常");
                    }
                    break;
                case JSON_ERROR:
                    if(!isFinishing()) {
                        enterhome();
                        Log.i(TAG, "json解析错误");
                    }
                    break;
            }
        }
    };
    private Builder builder;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
        try {
            if (MyApplication.state == 0) {
                setContentView(R.layout.activity_splash);
                StatService.setLogSenderDelayed(10);
                StatService.setSendLogStrategy(this, SendStrategyEnum.APP_START,
                        1, false);
                StatService.setSessionTimeOut(0);
                tv = (TextView) findViewById(R.id.tv_splash_verson);
                tv.setText("版本号:" + getVersonName());
                preferences = getSharedPreferences("config", MODE_PRIVATE);
                boolean boolean1 = preferences.getBoolean("update", true);
                if (boolean1) {
                    CheckUpdate();
                } else {
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            enterhome();
                        }
                    }, 2000);
                }
                AlphaAnimation animation = new AlphaAnimation(0.2f, 1.0f);
                animation.setDuration(500);
                findViewById(R.id.rl_root_splash).setAnimation(animation);
                jindu = (TextView) findViewById(R.id.tv_splash_jindu);
            } else {
                enterhome();
            }
        }catch (Exception e){
            enterhome();
        }
    }

    protected void showupdateDialog(){
        try {
            // TODO Auto-generated method stub
            builder = new Builder(this);
            builder.setTitle("提醒升级");
            builder.setMessage(description);
            builder.setOnCancelListener(new OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    enterhome();
                    dialog.dismiss();
                }
            });
            builder.setPositiveButton("立即升级", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Intent intent = new Intent();
                    intent.setAction("android.intent.action.VIEW");
                    Uri uri = Uri.parse("http://www.louxiago.com/app/index.php?name=DDKD");
                    intent.setData(uri);
//                startActivity(intent);
                    startActivityForResult(intent, 0);
                }
            });
            builder.setNegativeButton("下次再说", new OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                    enterhome();
                }
            });
            builder.create().show();
        }catch (Exception e){
            Log.e("Exception", e.getMessage());
            enterhome();
            Toast.makeText(SplashActivity.this,"信息有误",Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 检查更新
     */
    private void CheckUpdate() {
        new Thread(new Runnable() {
            long start=java.lang.System.currentTimeMillis();
            @Override
            public void run() {
                Message message=Message.obtain();
                try {
                    URL url=new URL("http://www.louxiago.com/wc/ddkd/admin.php/User/update");
                    HttpURLConnection connection=(HttpURLConnection) url.openConnection();
                    connection.setRequestMethod("GET");
                    connection.setConnectTimeout(3000);
                    connection.setReadTimeout(3000);
                    int code = connection.getResponseCode();
                    Log.i(TAG, code+"");
                    if(code==200) {
                        InputStream inputStream = connection.getInputStream();
                        String readFromStream = StreamTools.readFromStream(inputStream);
                        Log.i(TAG, "联网成功！" + readFromStream);
                        //json解析
                        if (readFromStream == null) {

                        } else {
                            if (getVersonName().equals(readFromStream)) {
                                //相同版本号
                                Log.i(TAG, "版本相同");
                                message.what = ENTER_HOME;
                            } else {
                                //有新版本号,弹出对话框
                                message.what = SHOW_UPDATE_DIALOG;
                            }
                        }
                    }
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                    message.what=URL_ERROR;
                } catch (IOException e) {
                    e.printStackTrace();
                    message.what=NETWORK_ERROR;
                }finally{
                    long end=java.lang.System.currentTimeMillis();
                    long iii=end-start;
                    if(iii<2000){
                        try {
                            Thread.sleep(2000-iii);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    handler.sendMessage(message);
                }
            }
        }).start();
    }

    protected void enterhome() {
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        Boolean b=sharedPreferences.getBoolean("KYYMA", false);
        if(b) {
            Intent intent = new Intent(getApplicationContext(), MainActivity_login.class);
            startActivity(intent);
            finish();
        }else {
            Intent intent = new Intent(getApplicationContext(), WelcomeActivity.class);
            startActivity(intent);
            finish();
        }
    }

    /**
     * 得到版本号
     * @return
     */
    private String getVersonName() {
        PackageManager manager = getPackageManager();
        try {
            PackageInfo packageInfo = manager.getPackageInfo(getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        builder.create().dismiss();
        enterhome();
        super.onActivityResult(requestCode, resultCode, data);
    }
}
