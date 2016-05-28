package com.example.user.ddkd;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.example.user.ddkd.beam.ProblemInfo;
import com.example.user.ddkd.utils.MyStringRequest;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/5/26.
 */
public class Activity_problem extends BaseActivity implements View.OnClickListener {
    private ListView problem_listview;
    private List<ProblemInfo> prob_list = new ArrayList<ProblemInfo>();
    private String[] content = {"problem 1", "problem 2", "problem 3", "problem 4", "problem 5"};
    //    private ProblemInfo problemInfo=new ProblemInfo();
    private MyAdapter myAdapter = new MyAdapter();
    private RelativeLayout zhuyi;
    private RelativeLayout huodong;
    private RelativeLayout jiangli;
    private RelativeLayout kefu;
    private ImageView exit;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_problem);
        problem_listview = (ListView) findViewById(R.id.problem_listview);

        zhuyi = (RelativeLayout) findViewById(R.id.zhuyi);
        zhuyi.setOnClickListener(this);
        huodong = (RelativeLayout) findViewById(R.id.huodong);
        huodong.setOnClickListener(this);
        jiangli = (RelativeLayout) findViewById(R.id.jiangli);
        jiangli.setOnClickListener(this);
        kefu = (RelativeLayout) findViewById(R.id.kefu);
        kefu.setOnClickListener(this);
        exit = (ImageView) findViewById(R.id.tv_head_fanghui);
        exit.setOnClickListener(this);
        for (int i = 0; i < content.length; i++) {
            prob_list.add(new ProblemInfo(content[i]));
        }
        problem_listview.setAdapter(myAdapter);
        myAdapter.notifyDataSetChanged();
    }

    /**
     * 问题专区的点击监听
     *
     * @param v
     */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.kefu:
                final CharSequence[] phone = new CharSequence[]{"客服热线：18813974694"};
                new AlertDialog.Builder(this).setItems(phone, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        String[] array = String.valueOf(phone[which]).split(":");
                        switch (which) {
                            case 0:
                                Intent intent = new Intent("android.intent.action.CALL", Uri.parse("tel:" + array[1]));
                                startActivity(intent);
                                break;
                            case 1:

                                break;
                        }
                    }
                }).show();
                break;
            case R.id.huodong:

                break;
            case R.id.jiangli:

                break;
            case R.id.zhuyi:

                break;
            case R.id.tv_head_fanghui:

                break;
        }
    }


    class ItemOnclickListener implements View.OnClickListener {
        private int position;

        public ItemOnclickListener(int position) {
            this.position = position;
        }

        /**
         * 点击相应的item之后跳转到对应的webActivity
         *
         * @param v
         */
        @Override
        public void onClick(View v) {
            String info = prob_list.get(position).getContext();
            Toast.makeText(Activity_problem.this, "点击了" + info, Toast.LENGTH_SHORT).show();

        }
    }

    class MyAdapter extends BaseAdapter {
        @Override
        public int getCount() {
            return prob_list.size();
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
                LayoutInflater inflater = Activity_problem.this.getLayoutInflater();
                view = inflater.inflate(R.layout.problem_item, null);
            } else {
                view = convertView;
            }
            TextView prob_text = (TextView) view.findViewById(R.id.prob_text);
            RelativeLayout problem = (RelativeLayout) view.findViewById(R.id.problem);
            problem.setOnClickListener(new ItemOnclickListener(position));
            if (prob_list.get(position) != null) {
                prob_text.setText(prob_list.get(position).getContext());
            }
            return view;
        }
    }

    /**
     * 获取到常见的所有URL，按照id排列好
     */
    public void Get_prob_content() {
        String url = "";
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url, new MyStringRequest() {
            @Override
            public void success(Object o) {

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

            }
        });
        stringRequest.setTag("Get_prob_content");
        MyApplication.getQueue().add(stringRequest);
    }

    /**
     * 将需要返回的访问的Id返回给后台
     */
    public void Get_item_id(int index) {
        String url = "";
    }

    protected void onDestroy() {
        super.onDestroy();
        MyApplication.getQueue().cancelAll("Get_prob_content");
    }
}
