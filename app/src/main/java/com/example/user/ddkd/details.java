package com.example.user.ddkd;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/4.
 */
public class details extends Activity{
    private List<DetailsInfo> list;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_details);
        ListView listView=(ListView)findViewById(R.id.listviewdetails);
        DetailsInfo detailsinfo=new DetailsInfo();

        detailsinfo.setDetailsid(131110191);
        detailsinfo.setDetailsMoney("2.5元");
        detailsinfo.setUsername("刘嘉文");
        detailsinfo.setCourier("省通快递");
        detailsinfo.setTelphone(188139174);
        detailsinfo.setAddr("南区七栋");
        list=new ArrayList<DetailsInfo>();
        list.add(detailsinfo);
        listView.setAdapter(new MyAdater());
    }
    class MyAdater extends BaseAdapter{
        @Override
        public int getCount() {
            return list.size();
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
            if(convertView==null){
                LayoutInflater inflater=details.this.getLayoutInflater();
                view=inflater.inflate(R.layout.details_listview,null);
            }else{
                view=convertView;
            }
            TextView orderid=(TextView)view.findViewById(R.id.orderid);
            TextView orderprice=(TextView)view.findViewById(R.id.orderprice);
            TextView username=(TextView)view.findViewById(R.id.username);
            TextView courier=(TextView)view.findViewById(R.id.courier);
            TextView telephone=(TextView)view.findViewById(R.id.userphone);
            TextView addr=(TextView)view.findViewById(R.id.addr);

            DetailsInfo detailsInfo=list.get(position);
            if(orderid==null){
                Log.i("dfsf","fdsff");
            }else {
                orderid.setText(detailsInfo.getDetailsid()+"");
            }

            orderprice.setText(detailsInfo.getDetailsMoney());
            username.setText(detailsInfo.getUsername());
            courier.setText(detailsInfo.getCourier());
            telephone.setText(detailsInfo.getTelphone()+"");
            addr.setText(detailsInfo.getAddr());

            return view;
        }
    }
}
