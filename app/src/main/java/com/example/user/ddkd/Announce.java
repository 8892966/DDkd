package com.example.user.ddkd;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.beam.AnnounceInfo;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.MyStringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/14.
 */
public class Announce extends BaseActivity implements View.OnClickListener {
    private ListView announcelistview;
    private List<AnnounceInfo> announcelist = new ArrayList<AnnounceInfo>();
    private ImageView exitannounce;
    private TextView tongzhi;
    private MyAdapter myAdapter = new MyAdapter();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyApplication.GET_TOKEN_SUCCESS:
                    voll_Get();
                    break;
                case MyApplication.GET_TOKEN_ERROR:

                    break;
            }
        }
    };

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_announce);
        tongzhi = (TextView) findViewById(R.id.tongzhi);
        voll_Get();
        announcelistview = (ListView) findViewById(R.id.announcelistview);
        exitannounce = (ImageView) findViewById(R.id.exit);
        exitannounce.setOnClickListener(this);

        ExitApplication.getInstance().addActivity(this);

    }

    class ItemOnclickListener implements View.OnClickListener {
        public int position;

        public ItemOnclickListener(int position) {
            this.position = position;
        }

        @Override
        public void onClick(View v) {
            volley_Get_Id(announcelist.get(position).getId());
            Log.i("ID", announcelist.get(position).getId());
            Intent intent = new Intent(Announce.this, WebActivity.class);
            intent.putExtra("title", "DD讯息");
            intent.putExtra("url", announcelist.get(position).getUrl());
            startActivity(intent);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit:
                finish();
                break;
        }
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return announcelist.size();
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
            //********************获取的内容回显到ListView中*************************
            View view;
            if (convertView == null) {
                LayoutInflater inflater = Announce.this.getLayoutInflater();
                view = inflater.inflate(R.layout.announce_listview, null);
            } else {
                view = convertView;
            }
//            Log.i("fdsfsdf","sfsdsd");
            TextView id = (TextView) view.findViewById(R.id.id);
            TextView title = (TextView) view.findViewById(R.id.title);
            TextView time = (TextView) view.findViewById(R.id.time);
            RelativeLayout announce = (RelativeLayout) view.findViewById(R.id.announce);
            announce.setOnClickListener(new ItemOnclickListener(position));
            if (announcelist.get(position) != null) {
                title.setText(announcelist.get(position).getTiltlele());
                id.setText(announcelist.get(position).getId());
                time.setText(announcelist.get(position).getTime());
                tongzhi.setVisibility(View.GONE);
            } else {
                Log.i("Announce_Error", "announce is null");
                Toast.makeText(Announce.this, "数据获取失败，请重试", Toast.LENGTH_SHORT).show();
            }
            return view;
        }
    }

    public void voll_Get() {

        String url = "http://www.louxiago.com/wc/ddkd/admin.php/PassGongGao/index";
        StringRequest request = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                try {
                    String s = (String) o;
                    Log.i("Announ_Info", s);
                    if (!"".equals(s)) {
                        Type listv = new TypeToken<LinkedList<AnnounceInfo>>() {
                        }.getType();
                        Gson gson = new Gson();
                        announcelist = gson.fromJson(s, listv);
                        //****************将list里面的内容倒叙输出******************
                        Collections.reverse(announcelist);
                        announcelistview.setAdapter(myAdapter);
                        myAdapter.notifyDataSetChanged();
                    } else {
                        Toast.makeText(Announce.this, "数据访问出错，请重新进入此页面", Toast.LENGTH_SHORT).show();
                    }
                } catch (Exception e) {
                    Log.e("Exception", e.getMessage());
//                    Toast.makeText(Announce.this, "信息有误", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void tokenouttime() {
                Log.i("Announ_Error", "token outtime");
                AutologonUtil autologonUtil = new AutologonUtil(Announce.this, handler, announcelist);
                autologonUtil.volley_Get_TOKEN();
            }

            @Override
            public void yidiensdfsdf() {
                Exit.exit(Announce.this);
                Toast.makeText(Announce.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Toast.makeText(Announce.this, "网络连接出错", Toast.LENGTH_SHORT).show();
            }
        });
        request.setTag("abcGet_announce");
        MyApplication.getQueue().add(request);
    }

    public void volley_Get_Id(String id) {
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/GongGao/ShowNotice/id/" + id;
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                //无返回值；
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
                Toast.makeText(Announce.this, "网络连接中断", Toast.LENGTH_SHORT).show();
            }
        });
        stringRequest.setTag("Get_announce_id");
        MyApplication.getQueue().add(stringRequest);
    }

//    @Override
//    protected void onResume() {
//        super.onResume();
//        StatService.onResume(this);
//    }
//
//    @Override
//    protected void onPause() {
//        super.onPause();
//        StatService.onPause(this);
//    }

    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getQueue().cancelAll("abcGet_announce");
        MyApplication.getQueue().cancelAll("Get_announce_id");
    }

}
