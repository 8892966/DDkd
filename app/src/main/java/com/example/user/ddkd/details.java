package com.example.user.ddkd;

import android.app.Activity;
import android.content.SharedPreferences;
import android.os.Bundle;
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
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.SimpleTimeZone;

/**
 * Created by Administrator on 2016/4/4.
 */
public class details extends Activity implements View.OnClickListener {
    private TextView exit_button;
    private List<DetailsInfo> detailsinfolist;
    private MyAdater myAdater;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_details);
        ListView listView = (ListView) findViewById(R.id.listviewdetails);
        exit_button = (TextView) findViewById(R.id.tv_head_fanghui);
        exit_button.setOnClickListener(this);
        Volley_Get("3");

        detailsinfolist = new ArrayList<DetailsInfo>();
        myAdater = new MyAdater();
        listView.setAdapter(myAdater);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_head_fanghui:
                finish();
                break;
        }
    }

    class MyAdater extends BaseAdapter {
        @Override
        public int getCount() {
            return detailsinfolist.size();
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
            if (convertView == null) {
                LayoutInflater inflater = details.this.getLayoutInflater();
                view = inflater.inflate(R.layout.details_listview, null);
            } else {
                view = convertView;
            }
            TextView orderid = (TextView) view.findViewById(R.id.orderid);
            TextView orderprice = (TextView) view.findViewById(R.id.orderprice);
            TextView username = (TextView) view.findViewById(R.id.username);
            TextView courier = (TextView) view.findViewById(R.id.courier);
            TextView telephone = (TextView) view.findViewById(R.id.userphone);
            TextView addr = (TextView) view.findViewById(R.id.addr);
            TextView date = (TextView) view.findViewById(R.id.date);

            DetailsInfo detailsInfo = detailsinfolist.get(position);
            if (detailsInfo != null) {
                orderid.setText(detailsInfo.getId() + "");
                orderprice.setText(detailsInfo.getPrice() + "");
                username.setText(detailsInfo.getUsername());
                courier.setText(detailsInfo.getExpressCompany());
                telephone.setText(detailsInfo.getPhone() + "");
                Log.e("getView", detailsInfo.getReceiverPlace()+"");
                addr.setText(detailsInfo.getReceiverPlace()+"");
                //回显时间
                date.setText(detailsInfo.getTime());
            } else {
                Log.i("Error","fsdfaffsda");
            }
            return view;
        }
    }

    public void Volley_Get(String Static) {
        SharedPreferences preferences=getSharedPreferences("config",MODE_PRIVATE) ;
        String token=preferences.getString("token",null);
        //Log.i("Get_details_token",token);
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Order/getOrder/state/"+Static+"/token/"+token;
        Log.i("Details",url);
        StringRequest request = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                Log.i("Get_deailes",s);
                Type listv = new TypeToken<LinkedList<DetailsInfo>>() {}.getType();
                Gson gson = new Gson();
                detailsinfolist = gson.fromJson(s, listv);
                SimpleDateFormat dateFormat=new SimpleDateFormat("yyy/mm/dd  HH:mm:ss");
                for(DetailsInfo detailsInfo2:detailsinfolist){
                    detailsInfo2.setTime(dateFormat.format(Long.valueOf(detailsInfo2.getTime())));
                }
                myAdater.notifyDataSetChanged();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.e("111", "content is null");
            }
        });
        request.setTag("abcGet_details");
        MyApplication.getQueue().add(request);
    }
}
