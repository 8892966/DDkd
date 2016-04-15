package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.example.user.ddkd.beam.SignUpInfo;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by User on 2016-04-03.
 */
public class ZhuCe4Activity extends Activity implements View.OnClickListener {
    //放照片文件
    private File file;
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

    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.zhuce4_activity);
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
                paizhao(100);
                break;
            case R.id.tv_button2_paizhao:
                paizhao(102);
                break;
            case R.id.tv_button3_paizhao:
                paizhao(103);
                break;
            case R.id.tv_button_next:
                SignUpInfo signUpInfo = (SignUpInfo) getIntent().getSerializableExtra("SignUpInfo");
                map = new HashMap<String, String>();
                map.put("", signUpInfo.getCollege());
                map.put("", signUpInfo.getNumber());
                map.put("", signUpInfo.getPassword());
                map.put("", signUpInfo.getId_card());
                map.put("", signUpInfo.getPhone());
                map.put("", signUpInfo.getSex());
                map.put("", signUpInfo.getShortnumber());
                map.put("", signUpInfo.getUsername());
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        File file1 = new File(uri1.getPath());
                        File file2 = new File(uri2.getPath());
                        File file3 = new File(uri3.getPath());
                        Map<String, File> mapfile = new HashMap<String, File>();
                        mapfile.put("",file1);
                        mapfile.put("",file2);
                        mapfile.put("",file3);
                        try {
                            post("", map,mapfile);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        //把图片缓存删除
                        file1.delete();
                        file2.delete();
                        file3.delete();
//                        Log.i("ZhuCe4Activity",signUpInfo.toString());
                        Toast.makeText(ZhuCe4Activity.this, "提交成功，请登录", Toast.LENGTH_SHORT).show();
                        Intent intent1 = new Intent(ZhuCe4Activity.this, MainActivity_login.class);
                        startActivity(intent1);
                        finish();
                    }
                }).start();
                textView.setEnabled(false);
                textView.setText("正在传送数据，请等一下");
                break;
        }
    }

    private void paizhao(int code) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        intent.putExtra("return-data", true);
        startActivityForResult(intent, code);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 100) {
            if (data != null) {
                Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
                if (cameraBitmap != null) {
                    iv_zhuce4_zhaopian1.setImageBitmap(cameraBitmap);
                    uri1 = saveBitmap(cameraBitmap);
                } else {
                    Toast.makeText(ZhuCe4Activity.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == 102) {
            if (data != null) {
                Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
                if (cameraBitmap != null) {
                    iv_zhuce4_zhaopian2.setImageBitmap(cameraBitmap);
                    uri2 = saveBitmap(cameraBitmap);
                } else {
                    Toast.makeText(ZhuCe4Activity.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
            }
        } else if (requestCode == 103) {
            if (data != null) {
                Bitmap cameraBitmap = (Bitmap) data.getExtras().get("data");
                if (cameraBitmap != null) {
                    iv_zhuce4_zhaopian3.setImageBitmap(cameraBitmap);
                    uri3 = saveBitmap(cameraBitmap);
                } else {
                    Toast.makeText(ZhuCe4Activity.this, "获取图片出错，请再次获取", Toast.LENGTH_SHORT).show();
                }
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

    /**
     * 通过拼接的方式构造请求内容，实现参数传输以及文件传输
     *
     * @param actionUrl 访问的服务器URL
     * @param params    普通参数
     * @param files     文件参数
     * @return
     * @throws IOException
     */
    public static String post(String actionUrl, Map<String, String> params, Map<String, File> files) throws IOException {
        String BOUNDARY = java.util.UUID.randomUUID().toString();
        String PREFIX = "--", LINEND = "\r\n";
        String MULTIPART_FROM_DATA = "multipart/form-data";
        String CHARSET = "UTF-8";
        URL uri = new URL(actionUrl);
        HttpURLConnection conn = (HttpURLConnection) uri.openConnection();
        conn.setReadTimeout(5 * 1000); // 缓存的最长时间
        conn.setDoInput(true);// 允许输入
        conn.setDoOutput(true);// 允许输出
        conn.setUseCaches(false); // 不允许使用缓存
        conn.setRequestMethod("POST");
        conn.setRequestProperty("connection", "keep-alive");
        conn.setRequestProperty("Charsert", "UTF-8");
        conn.setRequestProperty("Content-Type", MULTIPART_FROM_DATA + ";boundary=" + BOUNDARY);
        // 首先组拼文本类型的参数
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, String> entry : params.entrySet()) {
            sb.append(PREFIX);
            sb.append(BOUNDARY);
            sb.append(LINEND);
            sb.append("Content-Disposition: form-data; name=\"" + entry.getKey() + "\"" + LINEND);
            sb.append("Content-Type: text/plain; charset=" + CHARSET + LINEND);
            sb.append("Content-Transfer-Encoding: 8bit" + LINEND);
            sb.append(LINEND);
            sb.append(entry.getValue());
            sb.append(LINEND);
        }
        DataOutputStream outStream = new DataOutputStream(conn.getOutputStream());
        outStream.write(sb.toString().getBytes());
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
            if (res == 200) {
                in = conn.getInputStream();
                int ch;
                StringBuilder sb2 = new StringBuilder();
                while ((ch = in.read()) != -1) {
                    sb2.append((char) ch);
                }
            }
            outStream.close();
            conn.disconnect();
        }
        // return in.toString();
        return BOUNDARY;
    }
}
