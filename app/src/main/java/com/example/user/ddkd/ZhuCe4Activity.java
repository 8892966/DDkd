package com.example.user.ddkd;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.beam.MainMsgInfo;
import com.example.user.ddkd.beam.SignUpInfo;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.ImageFactory;
import com.example.user.ddkd.utils.PostUtil;
import com.example.user.ddkd.utils.UploadUtil;
import com.google.gson.Gson;
import com.lidroid.xutils.HttpUtils;
import com.lidroid.xutils.exception.HttpException;
import com.lidroid.xutils.http.RequestParams;
import com.lidroid.xutils.http.ResponseInfo;
import com.lidroid.xutils.http.callback.RequestCallBack;
import com.lidroid.xutils.http.client.HttpRequest;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 2016-04-03.
 */
public class ZhuCe4Activity extends Activity implements View.OnClickListener {
    public static final int SUCCESS = 1;//提交数据成功：
    public static final int ERROR = 2;//提交数据失败：
    public static final int NEXT = 3;//提交图片成功，进行下一个
    public static int Static = 0;
    //    //放照片文件
//    private File file;
    //放照片的控件
    private ImageView iv_zhuce4_zhaopian1;
    private ImageView iv_zhuce4_zhaopian2;
    private ImageView iv_zhuce4_zhaopian3;
    private TextView textView;
    private File tempFile;
    private Uri uri1;
    private Uri uri2;
    private Uri uri3;
    private Map<String, String> map;
    //自定义dialog
    private ProgressBar progressBar1;
    private ProgressBar progressBar2;
    private AlertDialog show;
    private ProgressDialog progressDialog;
    private ImageFactory imageFactory;

    private String picture;//头像的路径
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    Toast.makeText(getApplication(), "提交成功，请等待审核通过", Toast.LENGTH_SHORT).show();
                    closeProgressDialog();
                    finish();
                    break;
                case ERROR:
                    Static=10;
                    closeProgressDialog();
                    break;
                case NEXT:
                    try {
                        if (Static == 0) {
                            RequestParams requestParams = new RequestParams();
                            requestParams.addBodyParameter("name", "IdCard");
                            requestParams.addBodyParameter("phone", signUpInfo.getPhone());
//                            Log.e("ZhuCe4Activity", getRealFilePath(ZhuCe4Activity.this, uri1));
                            requestParams.addBodyParameter("file", new File(getRealFilePath(ZhuCe4Activity.this, uri1)));
                            new UploadUtil().uploadMethod(requestParams, "http://www.louxiago.com/wc/ddkd/admin.php/User/uploadimage", handler, progressBar2, ZhuCe4Activity.this, null);
                            progressBar1.setProgress(Static);
                        } else if (Static == 1) {
                            RequestParams requestParams = new RequestParams();
                            requestParams.addBodyParameter("name", "IdCardBack");
                            requestParams.addBodyParameter("phone", signUpInfo.getPhone());
                            requestParams.addBodyParameter("file", new File(getRealFilePath(ZhuCe4Activity.this, uri2)));
                            new UploadUtil().uploadMethod(requestParams, "http://www.louxiago.com/wc/ddkd/admin.php/User/uploadimage", handler, progressBar2, ZhuCe4Activity.this, null);
                            progressBar1.setProgress(Static);
                        } else if (Static == 2) {
                            RequestParams requestParams = new RequestParams();
                            requestParams.addBodyParameter("name", "StudentCard");
                            requestParams.addBodyParameter("phone", signUpInfo.getPhone());
                            requestParams.addBodyParameter("file", new File(getRealFilePath(ZhuCe4Activity.this, uri3)));
                            new UploadUtil().uploadMethod(requestParams, "http://www.louxiago.com/wc/ddkd/admin.php/User/uploadimage", handler, progressBar2, ZhuCe4Activity.this, null);
                            progressBar1.setProgress(Static);
                        } else if (Static == 3) {
                            progressBar1.setProgress(Static);
                            volley_ZC_GET(map);
                            Static = 0;
                        }
                    }catch (Exception e){
                        Log.e("Exception", e.getMessage());
                        Toast.makeText(ZhuCe4Activity.this,"信息有误",Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    private SignUpInfo signUpInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce4_activity);

        imageFactory=new ImageFactory();//初始化
//        showProgressDialog(4);
//       initFile();//初始化文件
        //拍照
        TextView tv_button1_paizhao = (TextView) findViewById(R.id.tv_button1_paizhao);
        TextView tv_button2_paizhao = (TextView) findViewById(R.id.tv_button2_paizhao);
        TextView tv_button3_paizhao = (TextView) findViewById(R.id.tv_button3_paizhao);

        textView = (TextView) findViewById(R.id.tv_button_next);
        textView.setOnClickListener(this);

        //放照片的地方
        iv_zhuce4_zhaopian1 = (ImageView) findViewById(R.id.iv_zhuce4_zhaopian1);
        iv_zhuce4_zhaopian2 = (ImageView) findViewById(R.id.iv_zhuce4_zhaopian2);
        iv_zhuce4_zhaopian3 = (ImageView) findViewById(R.id.iv_zhuce4_zhaopian3);

        tv_button1_paizhao.setOnClickListener(this);
        tv_button2_paizhao.setOnClickListener(this);
        tv_button3_paizhao.setOnClickListener(this);

        //返回
        TextView tv_head_fanghui = (TextView) findViewById(R.id.tv_head_fanghui);
        tv_head_fanghui.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_fanghui:
                Intent intent = new Intent(ZhuCe4Activity.this, ZhuCe3Activity.class);
                intent.putExtra("SignUpInfo", getIntent().getSerializableExtra("SignUpInfo"));
                intent.putExtra("picture", getIntent().getSerializableExtra("picture"));
                startActivity(intent);
                finish();
                break;
            case R.id.tv_button1_paizhao:
//                showChooseDialog();
                initFile("IdCard");
                paizhao(100);
                break;
            case R.id.tv_button2_paizhao:
                initFile("IdCardBack");
                paizhao(102);
                break;
            case R.id.tv_button3_paizhao:
                initFile("StudentCard");
                paizhao(103);
                break;
            case R.id.tv_button_next:
                if (uri1 == null || uri2 == null || uri3 == null) {
                    Toast.makeText(ZhuCe4Activity.this, "请把需要的信息填上", Toast.LENGTH_SHORT).show();
                    break;
                } else {
                    signUpInfo = (SignUpInfo) getIntent().getSerializableExtra("SignUpInfo");
                    picture = getIntent().getStringExtra("picture");
//                    Log.e("signUpInfo", signUpInfo.toString());
                    map = new HashMap<String, String>();
                    map.put("class", signUpInfo.getClazz());
                    map.put("college", signUpInfo.getCollege());
                    map.put("number", signUpInfo.getNumber());
                    map.put("password", signUpInfo.getPassword());
                    map.put("IdCardNum", signUpInfo.getId_card());
                    map.put("phone", signUpInfo.getPhone());
                    map.put("sex", signUpInfo.getSex());
                    map.put("shortphone", signUpInfo.getShortnumber());
                    map.put("username", signUpInfo.getUsername());

                    Static=0;
                    showProgressDialog(4);
                    handler.sendEmptyMessage(NEXT);
                }
                break;
        }
    }

    private void paizhao(int code) {
        Intent intent = new Intent(Intent.ACTION_PICK);// 打开相册
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
//        intent.putExtra("output", Uri.fromFile(tempFile));
        startActivityForResult(intent, code);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (data != null) {
                final Uri data1 = data.getData();
                String path = getRealFilePath(this, data1);
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap cameraBitmap = UploadUtil.getBitmap(path, options, iv_zhuce4_zhaopian1.getHeight(), iv_zhuce4_zhaopian1.getWidth());
                if (cameraBitmap != null) {
                    iv_zhuce4_zhaopian1.setImageBitmap(cameraBitmap);
                    showProgressDialog1();
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                imageFactory.compressAndGenImage(getRealFilePath(ZhuCe4Activity.this, data1), tempFile.getPath(), 200, false);
                                closeProgressDialog2();
                            }catch (Exception e) {
                                e.printStackTrace();
                                closeProgressDialog2();
                                Toast.makeText(ZhuCe4Activity.this,"信息有误",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).start();
                    uri1 = Uri.fromFile(tempFile);
                } else {
                    Toast.makeText(ZhuCe4Activity.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == 102) {
            if (data != null) {
                final Uri data1 = data.getData();
//                Log.e("onActivityResult", data1.getPath() + "...." + getRealFilePath(this, data1));
                String path = getRealFilePath(this, data1);
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap cameraBitmap = UploadUtil.getBitmap(path, options, iv_zhuce4_zhaopian2.getHeight(), iv_zhuce4_zhaopian2.getWidth());
                if (cameraBitmap != null) {
                    iv_zhuce4_zhaopian2.setImageBitmap(cameraBitmap);
                    showProgressDialog1();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                imageFactory.compressAndGenImage(getRealFilePath(ZhuCe4Activity.this, data1), tempFile.getPath(),200, false);
                                closeProgressDialog2();
                            }catch (Exception e) {
                                e.printStackTrace();
                                closeProgressDialog2();
                                Toast.makeText(ZhuCe4Activity.this,"信息有误",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).start();
                    uri2 = Uri.fromFile(tempFile);
                } else {
                    Toast.makeText(ZhuCe4Activity.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == 103) {
            if (data != null) {
                final Uri data1 = data.getData();
//                Log.e("onActivityResult", data1.getPath() + "...." + getRealFilePath(this, data1));
                String path = getRealFilePath(this, data1);
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap cameraBitmap = UploadUtil.getBitmap(path, options, iv_zhuce4_zhaopian3.getHeight(), iv_zhuce4_zhaopian3.getWidth());
                if (cameraBitmap != null) {
                    iv_zhuce4_zhaopian3.setImageBitmap(cameraBitmap);
                    showProgressDialog1();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                imageFactory.compressAndGenImage(getRealFilePath(ZhuCe4Activity.this, data1), tempFile.getPath(), 200, false);
                                closeProgressDialog2();
                            }catch (Exception e) {
                                e.printStackTrace();
                                closeProgressDialog2();
                                Toast.makeText(ZhuCe4Activity.this,"信息有误",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).start();
                    uri3 = Uri.fromFile(tempFile);
                } else {
                    Toast.makeText(ZhuCe4Activity.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
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

    private void showProgressDialog(int max) {
        AlertDialog.Builder builder = new AlertDialog.Builder(ZhuCe4Activity.this);
        View view = View.inflate(ZhuCe4Activity.this, R.layout.zhuce_dialog_progress, null);
        progressBar1 = (ProgressBar) view.findViewById(R.id.pb_sum);
        progressBar1.setMax(max);
        progressBar2 = (ProgressBar) view.findViewById(R.id.pb_each);
        builder.setTitle("上传信息中...");
        builder.setView(view);
        builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                show.dismiss();
                Static = 10;
            }
        });
        show = builder.create();
        show.setCanceledOnTouchOutside(false);
        show.show();
    }

    private void closeProgressDialog() {
        if (show != null) {
            show.dismiss();
            Static = 10;
        }
    }

    private void volley_ZC_GET(final Map<String, String> map) {
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/register";
        StringRequest request_post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                if ("SUCCESS".equals(s)) {
                    handler.sendEmptyMessage(SUCCESS);
                } else {
                    handler.sendEmptyMessage(ERROR);
                }
            }

        }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ZhuCe4Activity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map1 = new HashMap<>();
                for (Map.Entry<String, String> entry : map.entrySet()) {
                    map1.put(entry.getKey(), entry.getValue());
                }
                return map1;
            }
        };
        request_post.setTag("volley_ZC_GET");
        MyApplication.getQueue().add(request_post);
    }

//    private void showChooseDialog(){
//        Dialog dialog = new AlertDialog.Builder(this).setIcon(
//                android.R.drawable.btn_star).setTitle("选择").setMessage(
//                "请选择获取图片方式").setPositiveButton("图库",
//                new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        openGallery();
//                    }
//                }).setNegativeButton("拍照", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                openCamera();
//            }
//        }).setNeutralButton("取消", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialog, int which) {
//                dialog.dismiss();
//            }
//        }).create();
//        dialog.show();
//    }
    ////******************************
//    private ImageView iv_user_photo;
//    private String fileName = "";
//    private File tempFile;
//    private int crop = 300;// 裁剪大小
//    private static final int OPEN_CAMERA_CODE = 10;
//    private static final int OPEN_GALLERY_CODE = 11;
//    private static final int CROP_PHOTO_CODE = 12;
//
//    private OnClickListener PopupWindowItemOnClick = new OnClickListener() {
//        @Override
//        public void onClick(View v) {
//            menuWindow.dismiss();
//            switch (v.getId()) {
//                // 拍照
//                case R.id.btn_camera:
//                    initFile();
//                    openCamera();
//                    break;
//                // 相册
//                case R.id.btn_gallery:
//                    initFile();
//                    openGallery();
//                    break;
//                default:
//                    break;
//            }
//        }
//    };
//
//    public void initFile() {
//        if (fileName.equals("")) {
//            boolean sdCardExist = Environment.getExternalStorageState()
//                    .equals(android.os.Environment.MEDIA_MOUNTED);
//            if (sdCardExist) {
//                String path = Environment.getExternalStorageDirectory().getPath()+"/DDkdphoto";
////                FileUtil.mkdir(path);
////                Logger.i("path:" + path);
//                tempFile=new File(path);
//                if(!tempFile.exists()){
//                    tempFile.mkdir();
//                }
//                fileName = path + "/iv_zhuce4_zhaopian1.png";
//                tempFile = new File(fileName);
//            } else {
//                Toast.makeText(this, "请插入SD卡", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }
//
//    /**
//     * 调用相机
//     */
//    public void openCamera() {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);// 打开相机
//        intent.putExtra("output", Uri.fromFile(tempFile));
//        startActivityForResult(intent, OPEN_CAMERA_CODE);
//    }
//
//    /**
//     * 打开相册
//     */
//    public void openGallery() {
//        Intent intent = new Intent(Intent.ACTION_PICK);// 打开相册
//        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
//        intent.putExtra("output", Uri.fromFile(tempFile));
//        startActivityForResult(intent, OPEN_GALLERY_CODE);
//    }
//    /**
//     * 裁剪图片
//     * @param uri
//     */
//    public void cropPhoto(Uri uri) {
//        Intent intent = new Intent("com.android.camera.action.CROP");
//        intent.setDataAndType(uri, "image/*");
//        intent.putExtra("output", Uri.fromFile(tempFile));
//        intent.putExtra("crop", true);
//        intent.putExtra("aspectX", 1);
//        intent.putExtra("aspectY", 1);
//        intent.putExtra("outputX", crop);
//        intent.putExtra("outputY", crop);
//        startActivityForResult(intent, CROP_PHOTO_CODE);
//    }
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (resultCode == 1)
//            return;
//        switch (requestCode) {
//            case OPEN_CAMERA_CODE:
//                cropPhoto(Uri.fromFile(tempFile));
//                break;
//            case OPEN_GALLERY_CODE:
//                cropPhoto(data.getData());
//                break;
//            case CROP_PHOTO_CODE:
//                try {
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    options.inSampleSize = 2;
//                    Bitmap bitmap = BitmapFactory.decodeFile(fileName, options);
//                    if (bitmap != null) {
//                        iv_zhuce4_zhaopian1.setImageBitmap(bitmap);
//                    }
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }
//                break;
//            default:
//                break;
//        }
//
//        super.onActivityResult(requestCode, resultCode, data);
//    }
    ////**************

    /**
     *  * Try to return the absolute file path from the given Uri
     *  *
     *  * @param context
     *  * @param uri
     *  * @return the file path or null
     *  
     */
    public static String getRealFilePath(final Context context, final Uri uri) {
        if (null == uri) return null;
        final String scheme = uri.getScheme();
        String data = null;
        if (scheme == null)
            data = uri.getPath();
        else if (ContentResolver.SCHEME_FILE.equals(scheme)) {
            data = uri.getPath();
        } else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri, new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        data = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return data;
    }

    public void initFile(String TPname) {
        String fileName = "";
        if (fileName.equals("")) {
            boolean sdCardExist = Environment.getExternalStorageState()
                    .equals(android.os.Environment.MEDIA_MOUNTED);
            if (sdCardExist) {
                String path = Environment.getExternalStorageDirectory().getPath() + "/DDkdPhoto";
                tempFile = new File(path);
                if (!tempFile.exists()) {
                    tempFile.mkdir();
                }
                fileName = path + "/" + TPname + ".png";
                tempFile = new File(fileName);
            } else {
                Toast.makeText(this, "请插入SD卡", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ZhuCe4Activity.this, ZhuCe3Activity.class);
        intent.putExtra("SignUpInfo", getIntent().getSerializableExtra("SignUpInfo"));
        intent.putExtra("picture", getIntent().getSerializableExtra("picture"));
        startActivity(intent);
        finish();
    }

    private void showProgressDialog1() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(ZhuCe4Activity.this);
            progressDialog.setMessage("处理数据中.......");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog2() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

}
