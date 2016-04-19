package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
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
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * Created by Administrator on 2016/4/5.
 */
public class MainActivity_balance extends Activity implements View.OnClickListener {
    private List<Payment> paymentslist;
    private TextView textView;
    private MyAdapter myAdapter;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_balance);
        TextView exit=(TextView)findViewById(R.id.tv_head_fanghui);
        exit.setOnClickListener(this);

        textView=(TextView)findViewById(R.id.getmoney);
        textView.setOnClickListener(this);
        ListView viewById = (ListView) findViewById(R.id.listviewbalance);
        paymentslist=new ArrayList<Payment>();
        Volley_Get();
        myAdapter=new MyAdapter();
        viewById.setAdapter(myAdapter);
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
        SharedPreferences sharedPreferences=getSharedPreferences("config",MODE_PRIVATE);
        String token=sharedPreferences.getString("token",null);
        String url="http://www.louxiago.com/wc/ddkd/admin.php/Turnover/takeoutrecord/token/"+token;
        StringRequest request=new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("Json",s);
                //**********从后台返回一个参数来说明数据的获取状况**********
                Type listv=new TypeToken<LinkedList<Payment>>(){}.getType();
                Gson gson=new Gson();
                paymentslist=gson.fromJson(s,listv);
                myAdapter.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("onErrorResponse","onErrorResponse");
            }
        });
        request.setTag("abcGet_balance");
        MyApplication.getQueue().add(request);
    }

    class MyAdapter extends BaseAdapter {

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
            if (convertView == null) {//判断当前的缓存对象是否为空；
                LayoutInflater inflater = MainActivity_balance.this.getLayoutInflater();
                view = inflater.inflate(R.layout.balance_listview, null);
            } else {
                view = convertView;
            }
            TextView view2 = (TextView) view.findViewById(R.id.money);
            TextView view1 = (TextView) view.findViewById(R.id.Tname);
            TextView view3 = (TextView) view.findViewById(R.id.counter);
            TextView view4 = (TextView) view.findViewById(R.id.time1);


            Payment payment = paymentslist.get(position);
            if (payment != null) {
                view1.setText(payment.getTname());
                view2.setText(String.valueOf(payment.getMoney()));
                view3.setText(payment.getCounter());
                view4.setText(String.valueOf(payment.getTime1()));

            }else {
                Log.i("ERROR","payment的内容为空");
            }
            return view;
        }
    }
}
