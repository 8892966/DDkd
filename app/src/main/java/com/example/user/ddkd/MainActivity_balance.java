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

import com.example.user.ddkd.text.Person;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/4/5.
 */
public class MainActivity_balance extends Activity implements View.OnClickListener {

    private List<Person> list;
    private TextView textView;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_balance);
        TextView exit=(TextView)findViewById(R.id.tv_head_fanghui);
        exit.setOnClickListener(this);

        textView=(TextView)findViewById(R.id.TX);
        textView.setOnClickListener(this);

        ListView viewById = (ListView) findViewById(R.id.listviewbalance);
        Person person=new Person();
        person.setMoney("+1");
        person.setReason("小费");
        person.setAddr("南区三栋");
        Person person2=new Person();
        person2.setMoney("+2");
        person2.setReason("小费");
        person2.setAddr("南区七栋");
        list=new ArrayList<Person>();
        list.add(person);
        list.add(person2);
        viewById.setAdapter(new MyAdapter());
    }

    @Override
    public void onClick(View v) {
        Intent intent;
        switch (v.getId()){
            case R.id.TX:
                intent=new Intent(this,MainActivity_getmoney.class);
                startActivity(intent);
                break;
            case R.id.tv_head_fanghui:
                intent=new Intent(this,MainActivity_main.class);
                startActivity(intent);
                break;
        }
    }

    class MyAdapter extends BaseAdapter{

        @Override
        public int getCount() {
//            Log.i("MainActivity_balance", list.size() + "");
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
                LayoutInflater inflater = MainActivity_balance.this.getLayoutInflater();
                view = inflater.inflate(R.layout.balance_listview, null);
            }else{
                view=convertView;
            }
            TextView view2=(TextView)view.findViewById(R.id.money);
            TextView view1= (TextView) view.findViewById(R.id.reason);
            TextView view3=(TextView)view.findViewById(R.id.addr);

            Person person=list.get(position);
            view1.setText(person.getMoney());
            view2.setText(person.getReason());
            view3.setText(person.getAddr());
            return view;
        }
    }
}
