package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.baidu.mobstat.StatService;
import com.example.user.ddkd.beam.OrderInfo;
import com.example.user.ddkd.text.DetailsInfo;
import com.example.user.ddkd.utils.AutologonUtil;
import com.example.user.ddkd.utils.Exit;
import com.example.user.ddkd.utils.MyStringRequest;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/4.
 */
public class details extends Activity implements View.OnClickListener {
    private TextView exit_button;
    private List<DetailsInfo> detailsinfolist;
    private MyAdater myAdater;
    private TextView tongzhi;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MyApplication.GET_TOKEN_SUCCESS:
                    Object[] objects = (Object[]) msg.obj;
                    DetailsInfo detailsInfo = (DetailsInfo) objects[0];
                    String Static = (String) objects[1];
                    Volley_Get(detailsInfo, Static);
                    break;
                case MyApplication.GET_TOKEN_ERROR:

                    break;
            }
        }
    };

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_details);
        ListView listView = (ListView) findViewById(R.id.listviewdetails);
        tongzhi = (TextView) findViewById(R.id.tongzhi);
        exit_button = (TextView) findViewById(R.id.tv_head_fanghui);
        exit_button.setOnClickListener(this);
        DetailsInfo detailsInfo = new DetailsInfo();
        Volley_Get(detailsInfo, "3");

        detailsinfolist = new ArrayList<DetailsInfo>();
        myAdater = new MyAdater();
        listView.setAdapter(myAdater);
//        ExitApplication.getInstance().addActivity(this);
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
            RatingBar ratingBar= (RatingBar) view.findViewById(R.id.ratingbar);

            DetailsInfo detailsInfo = detailsinfolist.get(position);
            if (detailsInfo != null) {
                orderid.setText(detailsInfo.getId() + "");
                orderprice.setText(detailsInfo.getPrice() + "");
                username.setText(detailsInfo.getUsername());
                courier.setText(detailsInfo.getExpressCompany());
                telephone.setText(detailsInfo.getPhone() + "");
                addr.setText(detailsInfo.getReceivePlace());
                //回显时间
                date.setText(detailsInfo.getTime());
                if(detailsInfo.getEvaluate()==""){
                    ratingBar.setRating(0);
                }else{
                    ratingBar.setRating(Float.valueOf(detailsInfo.getEvaluate()));
                }
            } else {
                Log.i("Error", "fsdfaffsda");
            }
            return view;
        }
    }

    public void Volley_Get(final DetailsInfo detailsInfo, final String Static) {
        SharedPreferences preferences = getSharedPreferences("config", MODE_PRIVATE);
        String token = preferences.getString("token", null);
        //Log.i("Get_details_token",token);
        String url = "http://www.louxiago.com/wc/ddkd/admin.php/Order/getOrder/state/" + Static + "/token/" + token;
        Log.i("Details", url);
        StringRequest request=new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {
                String s= (String) o;
                if (!s.equals("ERROR")) {
                    Type listv = new TypeToken<LinkedList<DetailsInfo>>() {
                    }.getType();
                    Gson gson = new Gson();
                    detailsinfolist = gson.fromJson(s, listv);
                    //转化时间轴
                    SimpleDateFormat dateFormat = new SimpleDateFormat("yyy/mm/dd  HH:mm:ss");
                    for (DetailsInfo detailsInfo2 : detailsinfolist) {
                        detailsInfo2.setTime(dateFormat.format(Long.valueOf(detailsInfo2.getTime())));
                    }
                    tongzhi.setVisibility(View.GONE);
                    myAdater.notifyDataSetChanged();
                } else {
                    Log.i("OrderError","Failed to get the order information");
                }
            }

            @Override
            public void tokenouttime() {
                Log.e("Main_balance", "token outtime");
                Object[] objects = {detailsInfo, Static};
                AutologonUtil autologonUtil = new AutologonUtil(details.this, handler, objects);
                autologonUtil.volley_Get_TOKEN();
            }
            @Override
            public void yidiensdfsdf() {
                Exit.exit(details.this);
                Toast.makeText(details.this, "您的账户已在异地登录", Toast.LENGTH_SHORT).show();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                Log.i("Error_details","faile to get order information");
            }
        });
        request.setTag("abcGet_details");
        MyApplication.getQueue().add(request);
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
        MyApplication.getQueue().cancelAll("abcGet_details");
    }
}

