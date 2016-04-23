package com.example.user.ddkd;

import android.app.Activity;
import android.media.Image;
import android.os.Bundle;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/14.
 */
public class Announce extends Activity implements View.OnClickListener {
    private ListView announcelistview;
    private List<String> announcelist;
    private ImageView exitannounce;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_announce);

        announcelistview= (ListView) findViewById(R.id.announcelistview);
        exitannounce= (ImageView)findViewById(R.id.exit);
        exitannounce.setOnClickListener(this);
        announcelist=new ArrayList<>();
        String gonggao1="楼下购开业大优惠";
        String gonggao2="楼下购开业大优惠";
        String gonggao3="楼下购开业大优惠";
        announcelist.add(gonggao1);
        announcelist.add(gonggao2);
        announcelist.add(gonggao3);
        announcelistview.setAdapter(new MyAdapter());
        ExitApplication.getInstance().addActivity(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.exit:
                finish();
                return;
        }
    }

    class MyAdapter extends BaseAdapter{
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
            if(convertView==null){
                LayoutInflater inflater=Announce.this.getLayoutInflater();
                view=inflater.inflate(R.layout.announce_listview,null);
            }else{
                view=convertView;
            }

            String An=announcelist.get(position);
            TextView announ=(TextView)view.findViewById(R.id.announce);
            announ.setText(An);
            return view;
        }
    }
    public void voll_Get(){
        String url="";
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //*****************解析后台传递进来的公告信息*********************
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.setTag("abcGet");
        MyApplication.getQueue().add(request);
    }

    @Override
    protected void onResume(){
        super.onResume();
        StatService.onResume(this);
    }

    @Override
    protected void onPause(){
        super.onPause();
        StatService.onPause(this);
    }

}
