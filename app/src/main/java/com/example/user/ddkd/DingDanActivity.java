package com.example.user.ddkd;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

public class DingDanActivity extends AppCompatActivity implements View.OnClickListener {
    //放订单列表
    private ListView listView;
    private MyBaseAdapter baseAdapter;

    private TextView tv_button_yijie;
    private TextView tv_button_daisong;
    private TextView tv_button_wangchen;
    private TextView tv_button_quxiao;
    private  TextView tv_head_fanghui;

    private int xuanzhe;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.order_details_activity);

        listView= (ListView) findViewById(R.id.lv_order_details);
        tv_button_yijie= (TextView) findViewById(R.id.tv_button_yijie);
        tv_button_daisong= (TextView) findViewById(R.id.tv_button_daisong);
        tv_button_wangchen= (TextView) findViewById(R.id.tv_button_wangchen);
        tv_button_quxiao= (TextView) findViewById(R.id.tv_button_quxiao);

        tv_head_fanghui= (TextView) findViewById(R.id.tv_head_fanghui);

        xuanzhe=1;

        tv_button_yijie.setOnClickListener(this);
        tv_button_daisong.setOnClickListener(this);
        tv_button_wangchen.setOnClickListener(this);
        tv_button_quxiao.setOnClickListener(this);

        tv_head_fanghui.setOnClickListener(this);

        baseAdapter=new MyBaseAdapter();
        listView.setAdapter(baseAdapter);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_button_yijie:
                xuanzhe=1;
                baseAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_button_daisong:
                xuanzhe=2;
                baseAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_button_wangchen:
                xuanzhe=3;
                baseAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_button_quxiao:
                xuanzhe=4;
                baseAdapter.notifyDataSetChanged();
                break;
            case R.id.tv_head_fanghui:
                finish();
                break;
        }
    }
    class MyBaseAdapter extends BaseAdapter{
        @Override
        public int getCount() {
            return 10-xuanzhe;
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
            if(convertView!=null){
                view=convertView;
                zhuanTai= (ZhuanTai) view.getTag();
            }else {
                zhuanTai=new ZhuanTai();
                view = View.inflate(DingDanActivity.this, R.layout.dingdan_item, null);
                zhuanTai.textbutton = (TextView) view.findViewById(R.id.tv_dingdang_yina);
                zhuanTai.button = (TextView) view.findViewById(R.id.tv_dingdang_tuidang);
                view.setTag(zhuanTai);
            }
                if(xuanzhe==1){
                    zhuanTai.textbutton.setText("已拿件");
                    zhuanTai.textbutton.setVisibility(View.VISIBLE);
                    zhuanTai.button.setVisibility(View.VISIBLE);
                }else if(xuanzhe==2){
                    zhuanTai.textbutton.setText("完成");
                    zhuanTai.textbutton.setVisibility(View.VISIBLE);
                    zhuanTai.button.setVisibility(View.VISIBLE);
                }else if(xuanzhe==3){
                    zhuanTai.textbutton.setVisibility(View.GONE);
                    zhuanTai.button.setVisibility(View.VISIBLE);
                }else if(xuanzhe==4){
                    zhuanTai.textbutton.setVisibility(View.GONE);
                    zhuanTai.button.setVisibility(View.GONE);
                }
            return view;
        }
        class ZhuanTai{
            TextView textbutton;
            TextView button;
        }
    }
}
