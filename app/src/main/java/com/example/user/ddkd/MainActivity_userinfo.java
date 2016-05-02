package com.example.user.ddkd;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import com.example.user.ddkd.beam.SignUpInfo;
import com.example.user.ddkd.text.UserInfo;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.BitmaoCache;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.MyStringRequest;
import com.example.user.ddkd.utils.UploadUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.http.RequestParams;
import com.squareup.picasso.Picasso;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;

import static com.example.user.ddkd.ZhuCe4Activity.getRealFilePath;

/**
 * Created by Administrator on 2016/4/5.
 */
public class MainActivity_userinfo extends Activity implements View.OnClickListener {
    public static final int REPLY = 10;
    private TextView textView;
    private TextView username;
    private TextView collage;
    private TextView number;
    private TextView phone;
    private RelativeLayout changeimage;
    private TextView shortphone;
    private TextView level;
    private ImageView userimage;
    private UserInfo userInfo;
    private Uri uri;
    private File tempFile;
    private String fileName = "";
    private Bitmap cameraBitmap;
    private Handler handler2 = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            UserInfo userInfo = (UserInfo) msg.obj;
            Voley_Get(userInfo);
        }
    };
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyApplication.GET_TOKEN_SUCCESS:
                    userimage.setImageBitmap(cameraBitmap);
                    Toast.makeText(MainActivity_userinfo.this, "图片修改成功", Toast.LENGTH_SHORT).show();
                    volley_Get_Image();
                    break;
                case MyApplication.GET_TOKEN_ERROR:
                    Toast.makeText(MainActivity_userinfo.this, "图片修改失败，请重试", Toast.LENGTH_SHORT).show();
                    break;
                case REPLY:
                    String s = (String) msg.obj;
                    if (s.equals("SUCCESS")) {
                        SharedPreferences sharedPreferences1=getSharedPreferences("config",MODE_PRIVATE);
                        volley_change_Get(sharedPreferences1.getString("phone", ""), sharedPreferences1.getString("token", ""));
                    } else {
                        Log.i("Image_Get", s);
                    }
                    break;
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user_userinfo);
        initFile();
        textView = (TextView) findViewById(R.id.tv_head_fanghui);
        textView.setOnClickListener(this);
        username = (TextView) findViewById(R.id.username);
        collage = (TextView) findViewById(R.id.collage);
        number = (TextView) findViewById(R.id.number);
        phone = (TextView) findViewById(R.id.phone);
        changeimage = (RelativeLayout) findViewById(R.id.changeimage);
        changeimage.setOnClickListener(this);
        shortphone = (TextView) findViewById(R.id.shortphone);
        level = (TextView) findViewById(R.id.level);
        userimage = (ImageView) findViewById(R.id.userimage);
        Voley_Get(userInfo);
        ExitApplication.getInstance().addActivity(this);
        SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String url = sharedPreferences.getString("imageuri", "");
        Log.i("URL", url);
        if (url.equals("")) {
            volley_Get_Image();
        }
        Picasso.with(MainActivity_userinfo.this).load(url).into(userimage);
    }

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_fanghui:
                finish();
                break;
            case R.id.changeimage:
                new AlertDialog.Builder(this).setPositiveButton("更换头像", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getImage();
                    }
                }).show();
                break;
        }
    }

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
        intent.putExtra("outputX", 300);
        intent.putExtra("outputY", 300);
        startActivityForResult(intent, 10);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 10) {
            if (data != null) {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;//图片的保存比例
                cameraBitmap = toRoundBitmap(BitmapFactory.decodeFile(tempFile.getPath(), options));//设置图片的保存路径;
                SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
                if (cameraBitmap != null) {
                    uri = saveBitmap(cameraBitmap);
                    RequestParams requestParams = new RequestParams();
                    requestParams.addBodyParameter("name", "touxiang");
                    requestParams.addBodyParameter("phone", sharedPreferences.getString("phone",""));
                    requestParams.addBodyParameter("file", new File(getRealFilePath(MainActivity_userinfo.this, uri)));
                    new UploadUtil().uploadMethod(requestParams, "http://www.louxiago.com/wc/ddkd/admin.php/User/uploadimage", null, null, MainActivity_userinfo.this, handler);
                } else {
                    Toast.makeText(MainActivity_userinfo.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
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
        Log.e("px2dip",(int) (pxValue / scale + 0.5f)+"");
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

    //************************根据后台的返回结果来判断图片上传是否成功********************
    public void volley_change_Get(String phone, String token) {
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/updateLogo/phone/" + phone + "/token/" + token;
        StringRequest request = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s = (String) o;
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
                Log.i("Token", "token outtime");
                AutologonUtil autologonUtil = new AutologonUtil(MainActivity_userinfo.this, handler, null);
                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
                Exit.exit(MainActivity_userinfo.this);
                Toast.makeText(MainActivity_userinfo.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity_userinfo.this, "网络连接出错", Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag("Get_Setting_changImage");
        MyApplication.getQueue().add(request);
    }

    //***************获取用户的基本信息******************
    public void Voley_Get(final UserInfo userInfo) {
        final SharedPreferences sharedPreferences = getSharedPreferences("config", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Turnover/center/token/" + token;
        StringRequest request = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s = (String) o;
                if (!s.equals("ERROR")) {
                    Gson gson = new Gson();
                    UserInfo userInfo = gson.fromJson(s, UserInfo.class);
                    if (userInfo != null) {
                        boolean network = isNetworkConnected();
                        if (network) {
                            //**********当网络连接存在时，从后台获取用户信息***********
                            Log.i("SUCCESS", "SUCCESS");
                            //*****************根据Json中的数据回显用户的信息********************
                            username.setText(userInfo.getUsername());
                            collage.setText(userInfo.getCollege());
                            number.setText(userInfo.getNumber() + "");
                            phone.setText(userInfo.getPhone() + "");
                            shortphone.setText(userInfo.getShortphone() + "");
                            String name = getLevel(userInfo.getLevel());
                            level.setText(name);
                        } else {
                            //当网络连接不存在时，从手机的内存中获取用户信息
                            Log.i("ERROR", "ERROR");
                            SharedPreferences sharedPreferences1 = getSharedPreferences("user", MODE_PRIVATE);
                            username.setText(sharedPreferences1.getString("username", ""));
                            collage.setText(sharedPreferences1.getString("collage", ""));
                            number.setText(sharedPreferences1.getString("number", ""));
                            phone.setText(sharedPreferences1.getString("phone", ""));
                            shortphone.setText(sharedPreferences1.getString("shortphone", ""));
                            String name = getLevel(sharedPreferences1.getString("shortphone", ""));
                            level.setText(name);
                        }
                    } else {
                        Log.i("Error", "List is null");
                    }
                } else {
                    Toast.makeText(MainActivity_userinfo.this, "网络连接出错", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void tokenouttime() {
                Log.e("MainActivity_userinfo", "token outtime");
                AutologonUtil autologonUtil = new AutologonUtil(MainActivity_userinfo.this, handler2, userInfo);
                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
                Exit.exit(MainActivity_userinfo.this);
                Toast.makeText(MainActivity_userinfo.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(MainActivity_userinfo.this, "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag("abcPost_userinfo");
        MyApplication.getQueue().add(request);
    }

    //********************保存图片路径*******************
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
                Toast.makeText(MainActivity_userinfo.this, "网络连接出错", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setTag("volley_Get_Image");
        MyApplication.getQueue().add(stringRequest);
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

    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getQueue().cancelAll("Get_Setting_changImage");
        MyApplication.getQueue().cancelAll("volley_Get_Image");
        MyApplication.getQueue().cancelAll("abcPost_userinfo");
    }

    public String getLevel(String level) {
        String dengji = level;
        if (dengji.equals("0")) {
            return dengji = "铁牌快递员";
        } else if (dengji.equals("1")) {
            return dengji = "铜牌快递员";
        } else if (dengji.equals("2")) {
            return dengji = "银牌快递员";
        } else {
            return dengji = "金牌快递员";
        }
    }

    /**
     * 检测网络是否可用
     *
     * @return
     */
    public boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo ni = cm.getActiveNetworkInfo();
        if (ni != null && ni.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }
}
