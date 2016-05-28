package com.example.user.ddkd;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.ddkd.utils.ImageFactory;
import com.example.user.ddkd.utils.UploadUtilNew;

import java.io.File;

import static com.example.user.ddkd.ZhuCeActivity.getRealFilePath;

/**
 * Created by User on 2016-04-19.
 */
public class ZhuCe4Fragment extends Fragment implements View.OnClickListener {

    private ImageView iv_zhuce4_zhaopian1;
    private ImageView iv_zhuce4_zhaopian2;
    private ImageView iv_zhuce4_zhaopian3;
    private File tempFile;
    public Uri uri1;
    public Uri uri2;
    public Uri uri3;
    private ProgressDialog progressDialog;
    private ImageFactory imageFactory;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.zhuce4, container, false);
        imageFactory=new ImageFactory();//初始化
        TextView tv_button1_paizhao = (TextView) view.findViewById(R.id.tv_button1_paizhao);
        TextView tv_button2_paizhao = (TextView) view.findViewById(R.id.tv_button2_paizhao);
        TextView tv_button3_paizhao = (TextView) view.findViewById(R.id.tv_button3_paizhao);
        //放照片的地方
        iv_zhuce4_zhaopian1 = (ImageView) view.findViewById(R.id.iv_zhuce4_zhaopian1);
        iv_zhuce4_zhaopian2 = (ImageView) view.findViewById(R.id.iv_zhuce4_zhaopian2);
        iv_zhuce4_zhaopian3 = (ImageView) view.findViewById(R.id.iv_zhuce4_zhaopian3);
        tv_button1_paizhao.setOnClickListener(this);
        tv_button2_paizhao.setOnClickListener(this);
        tv_button3_paizhao.setOnClickListener(this);
        return view;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_button1_paizhao:
                initFile("IdCard");
                Log.e("ZhuCe4Fragment","IdCard");
                paizhao(100);
                break;
            case R.id.tv_button2_paizhao:
                initFile("IdCardBack");
                Log.e("ZhuCe4Fragment", "IdCardBack");
                paizhao(102);
                break;
            case R.id.tv_button3_paizhao:
                initFile("StudentCard");
                Log.e("ZhuCe4Fragment", "StudentCard");
                paizhao(103);
                break;
        }
    }

    public boolean next() {
        if (uri1 == null || uri2 == null || uri3 == null) {
            Toast.makeText(getActivity(), "请把需要的信息填上", Toast.LENGTH_SHORT).show();
            return false;
        } else {

            return true;
        }
    }


    public void initFile(String TPname) {
        String fileName = "";
        if (fileName.equals("")) {
            boolean sdCardExist = Environment.getExternalStorageState()
                    .equals(Environment.MEDIA_MOUNTED);
            if (sdCardExist) {
                String path = Environment.getExternalStorageDirectory().getPath() + "/DDkdPhoto";
                tempFile = new File(path);
                if (!tempFile.exists()) {
                    tempFile.mkdir();
                }
                fileName = path + "/" + TPname + ".png";
                tempFile = new File(fileName);
            } else {
                Toast.makeText(getActivity(), "请插入SD卡", Toast.LENGTH_SHORT).show();
            }
        }
    }
    private void paizhao(int code) {
        Intent intent = new Intent(Intent.ACTION_PICK);// 打开相册
        intent.setDataAndType(MediaStore.Images.Media.INTERNAL_CONTENT_URI, "image/*");
//        intent.putExtra("output", Uri.fromFile(tempFile));
        startActivityForResult(intent, code);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (data != null) {
                final Uri data1 = data.getData();
                String path = getRealFilePath(getActivity(), data1);
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap cameraBitmap = UploadUtilNew.getBitmap(path, options, iv_zhuce4_zhaopian1.getHeight(), iv_zhuce4_zhaopian1.getWidth());
                if (cameraBitmap != null) {
                    iv_zhuce4_zhaopian1.setImageBitmap(cameraBitmap);
                    showProgressDialog1();
                    new Thread(new Runnable(){
                        @Override
                        public void run() {
                            try {
                                imageFactory.compressAndGenImage(getRealFilePath(getContext(), data1), tempFile.getPath(), 200, false);
                                closeProgressDialog2();
                            }catch (Exception e) {
                                e.printStackTrace();
                                closeProgressDialog2();
                                Toast.makeText(getContext(),"信息有误",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).start();
                    uri1 = Uri.fromFile(tempFile);
                } else {
                    Toast.makeText(getContext(), "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == 102) {
            if (data != null) {
                final Uri data1 = data.getData();
//                Log.e("onActivityResult", data1.getPath() + "...." + getRealFilePath(this, data1));
                String path = getRealFilePath(getContext(), data1);
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap cameraBitmap = UploadUtilNew.getBitmap(path, options, iv_zhuce4_zhaopian2.getHeight(), iv_zhuce4_zhaopian2.getWidth());
                if (cameraBitmap != null) {
                    iv_zhuce4_zhaopian2.setImageBitmap(cameraBitmap);
                    showProgressDialog1();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                imageFactory.compressAndGenImage(getRealFilePath(getContext(), data1), tempFile.getPath(),200, false);
                                closeProgressDialog2();
                            }catch (Exception e) {
                                e.printStackTrace();
                                closeProgressDialog2();
                                Toast.makeText(getContext(),"信息有误",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).start();
                    uri2 = Uri.fromFile(tempFile);
                } else {
                    Toast.makeText(getContext(), "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == 103) {
            if (data != null) {
                final Uri data1 = data.getData();
//                Log.e("onActivityResult", data1.getPath() + "...." + getRealFilePath(this, data1));
                String path = getRealFilePath(getContext(), data1);
                BitmapFactory.Options options = new BitmapFactory.Options();
                Bitmap cameraBitmap = UploadUtilNew.getBitmap(path, options, iv_zhuce4_zhaopian3.getHeight(), iv_zhuce4_zhaopian3.getWidth());
                if (cameraBitmap != null) {
                    iv_zhuce4_zhaopian3.setImageBitmap(cameraBitmap);
                    showProgressDialog1();
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            try {
                                imageFactory.compressAndGenImage(getRealFilePath(getContext(), data1), tempFile.getPath(), 200, false);
                                closeProgressDialog2();
                            }catch (Exception e) {
                                e.printStackTrace();
                                closeProgressDialog2();
                                Toast.makeText(getContext(),"信息有误",Toast.LENGTH_SHORT).show();
                            }
                        }
                    }).start();
                    uri3 = Uri.fromFile(tempFile);
                } else {
                    Toast.makeText(getContext(), "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
    private void showProgressDialog1() {
        if (progressDialog == null) {
            progressDialog = new ProgressDialog(getActivity());
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
