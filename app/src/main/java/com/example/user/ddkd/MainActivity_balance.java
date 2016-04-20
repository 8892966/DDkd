package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.example.user.ddkd.text.DetailsInfo;
import com.example.user.ddkd.text.Payment;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public class MainActivity_balance extends Activity implements View.OnClickListener {
    private List<Payment> paymentslist;
    private TextView textView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_balance);
        TextView exit=(TextView)findViewById(R.id.tv_head_fanghui);
        exit.setOnClickListener(this);

        textView=(TextView)findViewById(R.id.getmoney);
        textView.setOnClickListener(this);

        Volley_Get();

        ListView viewById = (ListView) findViewById(R.id.listviewbalance);
        Payment person=new Payment();
        person.setMoney("+1");
        person.setReason("小费");
        person.setAddr("南区三栋");
        Payment person2=new Payment();
        person2.setMoney("+2");
        person2.setReason("小费");
        person2.setAddr("南区七栋");
        paymentslist=new ArrayList<Payment>();
        paymentslist.add(person);
        paymentslist.add(person2);
        viewById.setAdapter(new MyAdapter());
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.getmoney:
                intent=new Intent(this,MainActivity_getmoney.class);
                startActivity(intent);
                break;
            case R.id.tv_head_fanghui:
                finish();
                break;
        }
    }

    public void Volley_Get(){
        String url="";
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                //**********从后台返回一个参数来说明数据的获取状况**********
                Type listv=new TypeToken<LinkedList<DetailsInfo>>(){}.getType();
                Gson gson=new Gson();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {

            }
        });
        request.setTag("abcGet_balance");
        MyApplication.getQueue().add(request);
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
            return paymentslist.size();
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
            if(convertView==null){//判断当前的缓存对象是否为空；
                LayoutInflater inflater = MainActivity_balance.this.getLayoutInflater();
                view = inflater.inflate(R.layout.balance_listview, null);
            }else{
                view=convertView;
            }
            TextView view2=(TextView)view.findViewById(R.id.money);
            TextView view1= (TextView) view.findViewById(R.id.reason);
            TextView view3=(TextView)view.findViewById(R.id.addr);

            Payment person=paymentslist.get(position);
            view1.setText(person.getMoney());
            view2.setText(person.getReason());
            view3.setText(person.getAddr());
            return view;
        }
    }
}
