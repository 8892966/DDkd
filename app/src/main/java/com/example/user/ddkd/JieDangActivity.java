package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

/**
 * Created by User on 2016-04-02.
 */
public class JieDangActivity extends Activity implements View.OnClickListener {
    private ListView listView;

    private TextView tv_ddzhinang;

    private TextView tv_jianlihuodong;

    private TextView tv_to_dingdang;

    private  TextView but_jiedang;

    //测试用的
    boolean i = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.jiedang_activity);
        listView = (ListView) findViewById(R.id.lv_jiedang);

        tv_ddzhinang= (TextView) findViewById(R.id.tv_ddzhinang);
        tv_jianlihuodong= (TextView) findViewById(R.id.tv_jianlihuodong);
        tv_to_dingdang= (TextView) findViewById(R.id.tv_to_dingdang);
        but_jiedang= (TextView) findViewById(R.id.but_jiedang);

        tv_ddzhinang.setOnClickListener(this);
        tv_jianlihuodong.setOnClickListener(this);
        tv_to_dingdang.setOnClickListener(this);
        but_jiedang.setOnClickListener(this);

        listView.setVisibility(View.GONE);
        listView.setAdapter(new MyBaseAdapter());
    }
    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.tv_ddzhinang:
                intent=new Intent(this,WebActivity.class);
                intent.putExtra("title","DD指南");
                intent.putExtra("url","http://www.baidu.com");
                startActivity(intent);
                break;
            case R.id.tv_jianlihuodong:
                intent=new Intent(this,WebActivity.class);
                intent.putExtra("title","奖励活动");
                intent.putExtra("url","http://www.baidu.com");
                startActivity(intent);
                break;
            case R.id.tv_to_dingdang:
                intent=new Intent(this,DingDanActivity.class);
                startActivity(intent);
                break;
            case R.id.but_jiedang:
                if (i) {
                    i=false;
                    listView.setVisibility(View.VISIBLE);
                    but_jiedang.setText("休息");
                    but_jiedang.setBackgroundResource(R.drawable.yuan_selected);
                } else {
                    i=true;
                    listView.setVisibility(View.GONE);
                    but_jiedang.setText("听单");
                    but_jiedang.setBackgroundResource(R.drawable.yuan_color_gray);
                }
                break;
        }
    }
    class MyBaseAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return 10;
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
            View v;
            if (convertView != null) {
                v=convertView;
            }else {
                v=View.inflate(JieDangActivity.this,R.layout.dialog_view, null);
            }
            return v;
        }
    }

}
