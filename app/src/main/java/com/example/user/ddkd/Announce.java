package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
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
public class Announce extends Activity implements View.OnClickListener {
    private ListView announcelistview;
    private List<AnnounceInfo> announcelist;
    private ImageView exitannounce;
    private TextView tongzhi;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
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
        announcelistview = (ListView) findViewById(R.id.announcelistview);
        exitannounce = (ImageView) findViewById(R.id.exit);
        exitannounce.setOnClickListener(this);

        announcelist = new ArrayList<AnnounceInfo>();
        announcelistview.setAdapter(new MyAdapter());
        ExitApplication.getInstance().addActivity(this);
        voll_Get();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.exit:
                finish();
                break;

        }
    }
    class MyAdapter extends BaseAdapter implements View.OnClickListener {
        AnnounceInfo an;
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
            }else{
                view = convertView;
            }
            an = announcelist.get(position);
            TextView announ = (TextView) view.findViewById(R.id.announce);
            if (an!=null){
                announ.setText(an.getTitle());
                announ.setOnClickListener(this);
                tongzhi.setVisibility(View.GONE);
            }else{
                Log.i("Announce_Error","announce is null");
                Toast.makeText(Announce.this,"数据获取失败，请重试",Toast.LENGTH_SHORT).show();
            }
            return view;
        }

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                case R.id.announce:
                    Intent intent = new Intent(Announce.this, WebActivity.class);
                    intent.putExtra("title", "优惠活动");
                    intent.putExtra("url", an.getUrl());
                    startActivity(intent);
                    break;
            }
        }
    }

    public void voll_Get() {
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/GongGao/ShowNotice";
        StringRequest request = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s= (String) o;
                if(!s.equals("")){
                    Type listv = new TypeToken<LinkedList<String>>() {}.getType();
                    Gson gson=new Gson();
                    announcelist=gson.fromJson(s,listv);
                    //****************将list里面的内容倒叙输出******************
                    Collections.reverse(announcelist);
                }else{
                    Toast.makeText(Announce.this,"数据访问出错，请重新进入此页面",Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void tokenouttime() {
                Log.i("Announ_Error","token outtime");
                AutologonUtil autologonUtil=new AutologonUtil(Announce.this,handler,announcelist);
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
        MyApplication.getQueue().cancelAll("abcGet_announce");
    }

}
