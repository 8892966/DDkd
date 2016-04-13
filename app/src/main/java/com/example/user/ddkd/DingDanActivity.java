package com.example.user.ddkd;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.beam.OrderInfo;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.apache.commons.codec.digest.DigestUtils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DingDanActivity extends Activity implements View.OnClickListener {
    //放订单列表
    private ListView listView;
    private MyBaseAdapter baseAdapter;
    private TextView tv_button_yijie;
    private TextView tv_button_daisong;
    private TextView tv_button_wangchen;
    private TextView tv_button_quxiao;
    private TextView tv_head_fanghui;
    private List<OrderInfo> list;

    private int xuanzhe;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_activity);
        listView = (ListView) findViewById(R.id.lv_order_details);
        tv_button_yijie = (TextView) findViewById(R.id.tv_button_yijie);
        tv_button_daisong = (TextView) findViewById(R.id.tv_button_daisong);
        tv_button_wangchen = (TextView) findViewById(R.id.tv_button_wangchen);
        tv_button_quxiao = (TextView) findViewById(R.id.tv_button_quxiao);

        tv_head_fanghui = (TextView) findViewById(R.id.tv_head_fanghui);

        xuanzhe = 1;

        tv_button_yijie.setOnClickListener(this);
        tv_button_daisong.setOnClickListener(this);
        tv_button_wangchen.setOnClickListener(this);
        tv_button_quxiao.setOnClickListener(this);

        tv_head_fanghui.setOnClickListener(this);
        baseAdapter = new MyBaseAdapter();
        listView.setAdapter(baseAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_button_yijie:
                xuanzhe = 1;
                volley_getOrder_POST("","1","");
                baseAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_button_daisong:
                xuanzhe = 2;
                volley_getOrder_POST("","2","");
                baseAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_button_wangchen:
                xuanzhe = 3;
                volley_getOrder_POST("","3","");
                baseAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_button_quxiao:
                xuanzhe = 4;
//                volley_getOrder_POST("","4","");
                baseAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_head_fanghui:
                finish();
                break;
        }
    }
    class MyBaseAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return 10 - xuanzhe;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view;
            ZhuanTai zhuanTai;
            if (convertView != null) {
                view = convertView;
                zhuanTai = (ZhuanTai) view.getTag();
            } else {
                zhuanTai = new ZhuanTai();
                view = View.inflate(DingDanActivity.this, R.layout.dingdan_item, null);
                //已拿件/完成的按钮
                zhuanTai.textbutton = (TextView) view.findViewById(R.id.tv_dingdang_yina);
                //退单的按钮
                zhuanTai.button = (TextView) view.findViewById(R.id.tv_dingdang_tuidang);
                //订单的id
                zhuanTai.tv_dingdang_id = (TextView) view.findViewById(R.id.tv_dingdang_id);
                //可以赚到的钱
                zhuanTai.tv_money = (TextView) view.findViewById(R.id.tv_money);
                //快递的地址和所属的快递公司
                zhuanTai.tv_dingdang_kuaidi_dizhi = (TextView) view.findViewById(R.id.tv_dingdang_kuaidi_dizhi);
                //客户名字和电话号码
                zhuanTai.tv_dingdang_kehu = (TextView) view.findViewById(R.id.tv_dingdang_kehu);
                //目的地的地址
                zhuanTai.tv_dingdang_nudi_dizhi = (TextView) view.findViewById(R.id.tv_dingdang_nudi_dizhi);
                //客户的留言
                zhuanTai.tv_dingdang_liuyan = (TextView) view.findViewById(R.id.tv_dingdang_liuyan);
                //下单的时间
                zhuanTai.tv_dingdang_shijain = (TextView) view.findViewById(R.id.tv_dingdang_shijain);
                //打客户的电话
                zhuanTai.iv_call_phone = (ImageView) view.findViewById(R.id.iv_call_phone);
                //退单的理由
                zhuanTai.tv_dingdang_liyou = (TextView) view.findViewById(R.id.tv_dingdang_liyou);

                view.setTag(zhuanTai);
            }
            if (xuanzhe == 1) {
                zhuanTai.textbutton.setText("已拿件");
                zhuanTai.textbutton.setVisibility(View.VISIBLE);
                zhuanTai.button.setVisibility(View.VISIBLE);
            } else if (xuanzhe == 2) {
                zhuanTai.textbutton.setText("完成");
                zhuanTai.textbutton.setVisibility(View.VISIBLE);
                zhuanTai.button.setVisibility(View.VISIBLE);
            } else if (xuanzhe == 3) {
                zhuanTai.textbutton.setVisibility(View.GONE);
                zhuanTai.button.setVisibility(View.VISIBLE);
            } else if (xuanzhe == 4) {
                zhuanTai.textbutton.setVisibility(View.GONE);
                zhuanTai.button.setVisibility(View.GONE);
            }
                zhuanTai.iv_call_phone.setOnClickListener(new MyOnClickListener("18813974653"));

            return view;
        }
//按钮的监听
        class MyOnClickListener implements View.OnClickListener {
            String phone;
            public MyOnClickListener(String phone) {
                this.phone = phone;
            }
            @Override
            public void onClick(View v) {
                switch (v.getId()) {
                    case R.id.iv_call_phone:
                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone));
                    //noinspection ResourceType
                    startActivity(intent);
                        break;
                }
            }
        }
        class ZhuanTai {
            TextView textbutton;
            TextView button;
            TextView tv_dingdang_id;
            TextView tv_money;
            TextView tv_dingdang_kuaidi_dizhi;
            TextView tv_dingdang_kehu;
            TextView tv_dingdang_nudi_dizhi;
            TextView tv_dingdang_liuyan;
            TextView tv_dingdang_shijain;
            TextView tv_dingdang_liyou;
            ImageView iv_call_phone;
        }
    }
    private void volley_getOrder_POST(final String token, final String State,String url) {
//        参数一：方法 参数二：地址 参数三：成功回调 参数四：错误回调 。重写getParams 以post参数
        StringRequest request_post = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Gson gson=new Gson();
                list=gson.fromJson(s, new TypeToken<List<OrderInfo>>(){}.getType());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                list=null;
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> map = new HashMap<>();
                map.put("token", token);
                map.put("State",State);
                return map;
            }
        };
        MyApplication.getQueue().add(request_post);
    }
}
