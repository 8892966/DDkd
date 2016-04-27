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
import com.example.user.ddkd.utils.PostUtil;
import com.google.gson.Gson;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
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
    public static final int ERROR = 2;//提交数据成功：
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
    private String picture;//头像的路径
    private ProgressDialog progressDialog;//注册等待
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case SUCCESS:
                    Toast.makeText(getApplication(), "提交成功，请登录", Toast.LENGTH_SHORT).show();
                    closeProgressDialog();
                    finish();
                    break;
                case ERROR:
                    Toast.makeText(getApplication(), "提交失败，请重新提交", Toast.LENGTH_SHORT).show();
                    closeProgressDialog();
                    break;
                case NEXT:
                    if (Static == 0) {
                        Static++;
                        progressDialog.setProgress(Static);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                File file1 = new File(getRealFilePath(ZhuCe4Activity.this, uri1));
                                Map<String, File> mapfile2 = new HashMap<String, File>();
                                mapfile2.put("IdCard", file1);
                                Map<String, String> map2 = new HashMap<String, String>();
                                map2.put("name", "IdCard");
                                map2.put("phone", signUpInfo.getPhone());
                                Message message = new Message();
                                String msg2 = null;
                                try {
                                    msg2 = PostUtil.post("http://www.louxiago.com/wc/ddkd/admin.php/User/uploadimage/name/IdCard/phone/" + signUpInfo.getPhone(), map2, mapfile2);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    handler.sendEmptyMessage(ERROR);
                                }
                                Log.e("msg2", msg2);
                                message.what = NEXT;
                                handler.sendMessage(message);
                            }
                        }).start();
                    }else
                    if (Static == 1) {
                        Static++;
                        progressDialog.setProgress(Static);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                File file2 = new File(getRealFilePath(ZhuCe4Activity.this, uri2));
                                Map<String, File> mapfile3 = new HashMap<String, File>();
                                mapfile3.put("IdCardBack", file2);
                                Map<String, String> map3 = new HashMap<String, String>();
                                map3.put("name", "IdCardBack");
                                map3.put("phone", signUpInfo.getPhone());
                                String msg3 = null;
                                try {
                                    msg3 = PostUtil.post("http://www.louxiago.com/wc/ddkd/admin.php/User/uploadimage/name/IdCardBack/phone/" + signUpInfo.getPhone(), map3, mapfile3);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    handler.sendEmptyMessage(ERROR);
                                }
                                Log.e("msg3", msg3);
                                message.what = NEXT;
                                handler.sendMessage(message);
                            }
                        }).start();

                    }else
                    if (Static == 2) {
                        Static++;
                        progressDialog.setProgress(Static);
                        new Thread(new Runnable() {
                            @Override
                            public void run() {
                                Message message = new Message();
                                File file3 = new File(getRealFilePath(ZhuCe4Activity.this, uri3));
                                Map<String, File> mapfile4 = new HashMap<String, File>();
                                mapfile4.put("StudentCard", file3);
                                Map<String, String> map4 = new HashMap<String, String>();
                                map4.put("name", "StudentCard");
                                map4.put("phone", signUpInfo.getPhone());
                                String msg4 = null;
                                try {
                                    msg4 = PostUtil.post("http://www.louxiago.com/wc/ddkd/admin.php/User/uploadimage/name/StudentCard/phone/" + signUpInfo.getPhone(), map4, mapfile4);
                                } catch (Exception e) {
                                    e.printStackTrace();
                                    handler.sendEmptyMessage(ERROR);
                                }
                                Log.e("msg4", msg4);
                                message.what = NEXT;
                                handler.sendMessage(message);
                            }
                        }).start();
                    }else if(Static == 3){
                        progressDialog.setProgress(Static++);
                        volley_ZC_GET(map);
                        handler.sendEmptyMessage(SUCCESS);
                        Static=0;
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

//        initFile();//初始化文件
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
                signUpInfo = (SignUpInfo) getIntent().getSerializableExtra("SignUpInfo");
                picture = getIntent().getStringExtra("picture");
                map = new HashMap<String, String>();
//                Log.e("map", signUpInfo.getClazz());
                map.put("class", signUpInfo.getClazz());
                map.put("college", signUpInfo.getCollege());
                map.put("number", signUpInfo.getNumber());
                map.put("password", signUpInfo.getPassword());
                map.put("IdCardNum", signUpInfo.getId_card());
                map.put("phone", signUpInfo.getPhone());
                map.put("sex", signUpInfo.getSex());
                map.put("shortphone", signUpInfo.getShortnumber());
                map.put("username", signUpInfo.getUsername());
                showProgressDialog(4);
                handler.sendEmptyMessage(NEXT);
//                new Thread(new Runnable() {
//                    @Override
//                    public void run() {
//                        int i = 0;
////                        File file = new File(picture);
////                        File file1 = new File(uri1.getPath());
////                        File file2 = new File(uri2.getPath());
////                        File file3 = new File(uri3.getPath());
//                        File file1 = new File(getRealFilePath(ZhuCe4Activity.this, uri1));
//                        Map<String, File> mapfile2 = new HashMap<String, File>();
//                        mapfile2.put("IdCard", file1);
//                        Map<String, String> map2 = new HashMap<String, String>();
//                        map2.put("name", "IdCard");
//                        map2.put("phone", signUpInfo.getPhone());
//
//                        File file2 = new File(getRealFilePath(ZhuCe4Activity.this, uri2));
//                        Map<String, File> mapfile3 = new HashMap<String, File>();
//                        mapfile3.put("IdCardBack", file2);
//                        Map<String, String> map3 = new HashMap<String, String>();
//                        map3.put("name", "IdCardBack");
//                        map3.put("phone", signUpInfo.getPhone());
//
//                        File file3 = new File(getRealFilePath(ZhuCe4Activity.this, uri3));
//                        Map<String, File> mapfile4 = new HashMap<String, File>();
//                        mapfile4.put("StudentCard", file3);
//                        Map<String, String> map4 = new HashMap<String, String>();
//                        map4.put("name", "StudentCard");
//                        map4.put("phone", signUpInfo.getPhone());
//
//                        try {
////                            String msg1 = PostUtil.post("http://www.louxiago.com/wc/ddkd/admin.php/User/uploadimage/name/touxiang/phone/" + signUpInfo.getPhone(), map1, mapfile1);
////                            Log.e("msg1", msg1);
//                            Message message = new Message();
////                            message.arg1 = i;
////                            message.what = NEXT;
////                            handler.sendMessage(message);
////                            i++;
//                            String msg2 = PostUtil.post("http://www.louxiago.com/wc/ddkd/admin.php/User/uploadimage/name/IdCard/phone/" + signUpInfo.getPhone(), map2, mapfile2);
//                            Log.e("msg2", msg2);
//                            message.arg1 = i;
//                            message.what = NEXT;
//                            handler.sendMessage(message);
//                            i++;
//                            String msg3 = PostUtil.post("http://www.louxiago.com/wc/ddkd/admin.php/User/uploadimage/name/IdCardBack/phone/" + signUpInfo.getPhone(), map3, mapfile3);
//                            Log.e("msg3", msg3);
//                            message.arg1 = i;
//                            message.what = NEXT;
//                            handler.sendMessage(message);
//                            i++;
//                            String msg4 = PostUtil.post("http://www.louxiago.com/wc/ddkd/admin.php/User/uploadimage/name/StudentCard/phone/" + signUpInfo.getPhone(), map4, mapfile4);
//                            Log.e("msg4", msg4);
//                            message.arg1 = i;
//                            message.what = NEXT;
//                            handler.sendMessage(message);

//                        } catch (IOException e) {
//                            e.printStackTrace();
//                            handler.sendEmptyMessage(ERROR);
//                            Log.e("ZhuCe4Activity", "出错");
//                        }
//                    }
//                }
//                ).start();
                break;
        }
    }

    private void paizhao(int code) {
//        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
//        intent.addCategory(Intent.CATEGORY_OPENABLE);
//        intent.setType("image/*");
//        intent.putExtra("return-data", true);
//        startActivityForResult(intent, code);
        Intent intent = new Intent(Intent.ACTION_PICK);// 打开相册
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
        intent.putExtra("output", Uri.fromFile(tempFile));
        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 100) {
            if (data != null) {
                Uri data1 = data.getData();
                Log.e("onActivityResult", data1.getPath() + "...." + getRealFilePath(this, data1));
                String path = getRealFilePath(this, data1);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                Bitmap cameraBitmap = BitmapFactory.decodeFile(path, options);
//                Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
                if (cameraBitmap != null) {
                    iv_zhuce4_zhaopian1.setImageBitmap(cameraBitmap);
                    uri1 = data1;
                } else {
                    Toast.makeText(ZhuCe4Activity.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == 102) {
            if (data != null) {
                Uri data1 = data.getData();
                Log.e("onActivityResult", data1.getPath() + "...." + getRealFilePath(this, data1));
                String path = getRealFilePath(this, data1);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                Bitmap cameraBitmap = BitmapFactory.decodeFile(path, options);
//                Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
                if (cameraBitmap != null) {
                    iv_zhuce4_zhaopian2.setImageBitmap(cameraBitmap);
                    uri2 = data1;
                } else {
                    Toast.makeText(ZhuCe4Activity.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == 103) {
            if (data != null) {
                Uri data1 = data.getData();
                Log.e("onActivityResult", data1.getPath() + "...." + getRealFilePath(this, data1));
                String path = getRealFilePath(this, data1);
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inSampleSize = 1;
                Bitmap cameraBitmap = BitmapFactory.decodeFile(path, options);
//                Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
                if (cameraBitmap != null) {
                    iv_zhuce4_zhaopian3.setImageBitmap(cameraBitmap);
                    uri3 = data1;
                } else {
                    Toast.makeText(ZhuCe4Activity.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
//    private Uri saveBitmap(Bitmap bm) {
//        File tmpDir = new File(Environment.getExternalStorageDirectory() + "/DDkdphoto");
//        if (!tmpDir.exists()) {
//            tmpDir.mkdir();
//        }
//        tempFile = new File(tmpDir, System.currentTimeMillis() + ".png");
//        try {
//            FileOutputStream fos = new FileOutputStream(tempFile);
//            bm.compress(Bitmap.CompressFormat.PNG, 85, fos);
//            fos.flush();
//            fos.close();
//            return Uri.fromFile(tempFile);
//        } catch (FileNotFoundException e) {
//            e.printStackTrace();
//            return null;
//        } catch (IOException e) {
//            e.printStackTrace();
//            return null;
//        }
//    }

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
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(this);
            progressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressDialog.setMax(max);
            progressDialog.setMessage("正在提交.......");
            progressDialog.setCanceledOnTouchOutside(false);
        }
        progressDialog.show();
    }

    private void closeProgressDialog() {
        if (progressDialog != null) {
            progressDialog.dismiss();
        }
    }

    private void volley_ZC_GET(final Map<String, String> map) {
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/User/register";
        StringRequest request_post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
//                Log.e("volley_OrderState_GET", s);
                Log.e("msg5", s);

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(ZhuCe4Activity.this, "网络异常", Toast.LENGTH_SHORT).show();
            }
        }) {
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
//                FileUtil.mkdir(path);
//                Logger.i("path:" + path);
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
}
