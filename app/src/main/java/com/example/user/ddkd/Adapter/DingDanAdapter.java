package com.example.user.ddkd.Adapter;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.user.ddkd.R;
import com.example.user.ddkd.beam.OrderInfo;
import java.util.List;

/**
 * Created by User on 2016-05-13.
 */
public class DingDanAdapter extends BaseAdapter {
    private List<OrderInfo> list;
    private Activity activity;
    private int xuanzhe;
    private StaticListener staticListener;

    public PopupWindow getPopupWindow() {
        return popupWindow;
    }

    public void setPopupWindow(PopupWindow popupWindow) {
        this.popupWindow = popupWindow;
    }

    private PopupWindow popupWindow;

    public DingDanAdapter(Activity activity, List<OrderInfo> list, int xuanzhe, StaticListener staticListener){
        this.activity=activity;
        this.list=list;
        this.xuanzhe=xuanzhe;
        this.staticListener=staticListener;
    }
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
        try {
            View view;
            ZhuanTai zhuanTai;
            if (convertView != null) {
                view = convertView;
                zhuanTai = (ZhuanTai) view.getTag();
            } else {
                zhuanTai = new ZhuanTai();
                view = View.inflate(activity, R.layout.dingdan_item, null);

                zhuanTai.button = (TextView) view.findViewById(R.id.tv_dingdang_tuidang);
                //订单的id
                zhuanTai.tv_dingdang_id = (TextView) view.findViewById(R.id.tv_dingdang_id);
                //可以赚到的钱
                zhuanTai.tv_money = (TextView) view.findViewById(R.id.tv_money);
                //拿快递地址
                zhuanTai.tv_kuaidi_dizhi = (TextView) view.findViewById(R.id.tv_kuaidi_dizhi);
                //快递公司
                zhuanTai.tv_dingdang_kuaidi = (TextView) view.findViewById(R.id.tv_dingdang_kuaidi);
                //接收人和电话号码
                zhuanTai.lxr = (TextView) view.findViewById(R.id.lxr);
                //快递联系人和电话
                zhuanTai.dh = (TextView) view.findViewById(R.id.dh);
                //留言
                zhuanTai.ly = (TextView) view.findViewById(R.id.ly);
                //下单的时间
                zhuanTai.tv_dingdang_shijain = (TextView) view.findViewById(R.id.tv_dingdang_shijain);
                //打客户的电话
                zhuanTai.iv_call_phone = (ImageView) view.findViewById(R.id.iv_call_phone);
                //ProgressBar,点击改变状态时出现
                zhuanTai.pb_button = (ProgressBar) view.findViewById(R.id.pb_button);
                //退单理由
                zhuanTai.tuidan = (TextView) view.findViewById(R.id.tuidan);
                //付款状态
                zhuanTai.zhuangtai = (TextView) view.findViewById(R.id.zhuangtai);

                zhuanTai.fahuodizhi = (TextView) view.findViewById(R.id.fahuodizhi);

                zhuanTai.ll9 = (LinearLayout) view.findViewById(R.id.ll9);

                zhuanTai.ll8 = (LinearLayout) view.findViewById(R.id.ll8);
                zhuanTai.ll_xinxin=(LinearLayout) view.findViewById(R.id.ll_xinxin);
                zhuanTai.xx1=(ImageView)view.findViewById(R.id.xx1);
                zhuanTai.xx2=(ImageView)view.findViewById(R.id.xx2);
                zhuanTai.xx3=(ImageView)view.findViewById(R.id.xx3);
                zhuanTai.xx4=(ImageView)view.findViewById(R.id.xx4);
                zhuanTai.xx5=(ImageView)view.findViewById(R.id.xx5);
                view.setTag(zhuanTai);
            }
            OrderInfo info = list.get(position);
            zhuanTai.iv_call_phone.setVisibility(View.VISIBLE);
            zhuanTai.ll_xinxin.setVisibility(View.GONE);
            if (xuanzhe == 1) {
                zhuanTai.button.setEnabled(true);
                zhuanTai.button.setText("已拿件");
                zhuanTai.button.setVisibility(View.VISIBLE);
                if (!info.getPid().equals("0")) {
                    zhuanTai.zhuangtai.setText("已付款");
                } else {
                    zhuanTai.zhuangtai.setText("未付款");
                }
                zhuanTai.ll8.setVisibility(View.GONE);
                zhuanTai.ll9.setVisibility(View.VISIBLE);
            } else if (xuanzhe == 2) {
                zhuanTai.ll8.setVisibility(View.GONE);
                zhuanTai.ll9.setVisibility(View.GONE);
                if (!info.getPid().equals("0")) {
                    zhuanTai.button.setText("完成");
                    zhuanTai.button.setEnabled(true);
                } else {
                    zhuanTai.button.setText("未付款");
                    zhuanTai.button.setEnabled(false);
                }
                zhuanTai.button.setVisibility(View.VISIBLE);
            } else if (xuanzhe == 3) {
                xingxing(Float.valueOf(info.getEvaluate()),zhuanTai.xx1,zhuanTai.xx2,zhuanTai.xx3,zhuanTai.xx4,zhuanTai.xx5);
                zhuanTai.ll_xinxin.setVisibility(View.VISIBLE);
                zhuanTai.iv_call_phone.setVisibility(View.GONE);
                zhuanTai.ll8.setVisibility(View.GONE);
                zhuanTai.ll9.setVisibility(View.GONE);
                zhuanTai.button.setVisibility(View.GONE);
            } else if (xuanzhe == 4) {
                if (!info.getPid().equals("0")) {
                    zhuanTai.zhuangtai.setText("已付款");
                } else {
                    zhuanTai.zhuangtai.setText("未付款");
                }
                zhuanTai.ll8.setVisibility(View.VISIBLE);
                zhuanTai.ll9.setVisibility(View.VISIBLE);
                zhuanTai.button.setVisibility(View.GONE);
            }
            String[] s = info.getReceivePlace().split("/", -2);
            int i = s.length;
            String diz;
            if (3 <= i) {
                diz = s[3].replace(',', ' ');
            } else {
                diz = "";
            }
            if (info.getTip() != null) {
                zhuanTai.tv_dingdang_id.setText("订单：" + info.getId());
                if (info.getTip().equals("0")) {
                    zhuanTai.tv_money.setText(info.getPrice() + "元");
                } else {
                    zhuanTai.tv_money.setText(info.getPrice() + "元" + "+小费" + info.getTip() + "元");
                }
            } else {
                zhuanTai.tv_money.setText("0元");
            }
            zhuanTai.tv_kuaidi_dizhi.setText(info.getAddressee() + "");
            zhuanTai.tv_dingdang_kuaidi.setText(info.getExpressCompany() + "");
            if (0 <= i && i <= i) {
                zhuanTai.lxr.setText(s[0] + "/" + s[1] + "/" + s[2]);
            } else {
                zhuanTai.lxr.setText("");
            }
            zhuanTai.dh.setText(info.getUsername() + "/" + info.getPhone());
            zhuanTai.fahuodizhi.setText(diz + "");
            zhuanTai.ly.setText("留言:" + info.getMessage());
            zhuanTai.tuidan.setText("退单理由:" + info.getReason());
            zhuanTai.tv_dingdang_shijain.setText(info.getOrderTime() + "");
            zhuanTai.iv_call_phone.setOnClickListener(new MyOnClickListener(info, null, null));
            zhuanTai.button.setOnClickListener(new MyOnClickListener(info, zhuanTai.pb_button, zhuanTai.button));
            return view;
        } catch (Exception e) {
            Log.e("Exception", e.getMessage()+"");
            Toast.makeText(activity, "信息有误", Toast.LENGTH_SHORT).show();
        }
        return null;
    }

    //按钮的监听
    class MyOnClickListener implements View.OnClickListener {
        OrderInfo info;
        ProgressBar pb_button;
        TextView button;
        AlertDialog alertDialog;

        public MyOnClickListener(OrderInfo info, ProgressBar pb_button, TextView button) {
            this.info = info;
            this.pb_button = pb_button;
            this.button = button;
        }

        @Override
        public void onClick(View v) {
            try {
                switch (v.getId()) {
                    case R.id.iv_call_phone://打电话
//                        dismissPopuWindow();
                        AlertDialog.Builder builder = new AlertDialog.Builder(activity);
                        View inflate = View.inflate(activity, R.layout.call_phone_dialog, null);
                        TextView shouhuo_call_short_phone = (TextView) inflate.findViewById(R.id.shouhuo_call_short_phone);
                        TextView shouhuo_call_long_phone = (TextView) inflate.findViewById(R.id.shouhuo_call_long_phone);
                        TextView xiadan_call_long_phone = (TextView) inflate.findViewById(R.id.xiadan_call_long_phone);
                        TextView qixiao= (TextView) inflate.findViewById(R.id.qixiao);
                        final String[] s = info.getReceivePlace().split("/", -2);
                        if (s.length >= 2) {
                            if (s[2] != null && !s[2].equals("")) {
                                shouhuo_call_short_phone.setText("收货人短号：" + s[2]);
                                shouhuo_call_short_phone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + s[2]));
                                        activity.startActivity(intent);
//                                        dismissPopuWindow();
                                        alertDialog.dismiss();
                                    }
                                });
                            } else {
                                shouhuo_call_short_phone.setVisibility(View.GONE);
                            }
                        }
                        if (s.length >= 1) {
                            if (s[1] != null && !s[1].equals("")) {
                                shouhuo_call_long_phone.setText("收货人长号：" + s[1]);
                                shouhuo_call_long_phone.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + s[1]));
                                        activity.startActivity(intent);
//                                        dismissPopuWindow();
                                        alertDialog.dismiss();
                                    }
                                });
                            } else {
                                shouhuo_call_long_phone.setVisibility(View.GONE);
                            }
                        }
                        if (info.getPhone() != null && !info.getPhone().equals("")) {
                            xiadan_call_long_phone.setText("下单人长号：" + info.getPhone());
                            xiadan_call_long_phone.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View v) {
                                    Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + info.getPhone()));
                                    activity.startActivity(intent);
//                                    dismissPopuWindow();
                                    alertDialog.dismiss();
                                }
                            });
                        } else {
                            xiadan_call_long_phone.setVisibility(View.GONE);
                        }
                        qixiao.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
//                                dismissPopuWindow();
                                alertDialog.dismiss();
                            }
                        });
                        builder.setView(inflate, 0, 0, 0, 0);
                        builder.setNegativeButton("取消", null);
                        alertDialog = builder.create();
                        alertDialog.show();
//                        popupWindow=new PopupWindow(inflate,-2,-2);
//                        popupWindow.showAtLocation(v, Gravity.CENTER,0,0);
                        break;
                    case R.id.tv_dingdang_tuidang://已拿件按钮事件
                        if (xuanzhe == 2) {
                            staticListener.xuanzhe2Change3(info, pb_button, button);
                        }
                        if (xuanzhe == 1) {

                            staticListener.xuanzhe1Change2(info, pb_button, button);
                        }
                        break;
                }
            }catch (Exception e){
                Log.e("Exception", e.getMessage()+"");
                Toast.makeText(activity, "信息有误", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void dismissPopuWindow() {
        if(popupWindow!=null&&popupWindow.isShowing()){
            popupWindow.dismiss();;
            popupWindow=null;
        }
    }

    class ZhuanTai {
        TextView tv_dingdang_id;//订单号
        TextView tv_money;//钱
        ImageView iv_call_phone;//打电话
        TextView tv_kuaidi_dizhi;//拿快递地址
        TextView tv_dingdang_kuaidi;//快递公司
        TextView lxr;//接收人和电话号码
        TextView dh;//快递联系人和电话
        TextView ly;//留言
        TextView tv_dingdang_shijain;//时间
        TextView button;//按钮
        ProgressBar pb_button;//等待
        TextView tuidan;//退单理由
        TextView zhuangtai;//付款状态
        TextView fahuodizhi;//目的地址
        LinearLayout ll9;//状态栏
        LinearLayout ll8;//退单栏
        //星星
        LinearLayout ll_xinxin;
        ImageView xx1;
        ImageView xx2;
        ImageView xx3;
        ImageView xx4;
        ImageView xx5;
    }

    private void xingxing(float i,ImageView xx1,
                          ImageView xx2,
                          ImageView xx3,
                          ImageView xx4,
                          ImageView xx5){
        try {
            int ii=(int)(i+0.5);
            switch (ii) {
                case 0:
                    xx1.setImageResource(R.drawable.comment_star_gray_icon);
                    xx2.setImageResource(R.drawable.comment_star_gray_icon);
                    xx3.setImageResource(R.drawable.comment_star_gray_icon);
                    xx4.setImageResource(R.drawable.comment_star_gray_icon);
                    xx5.setImageResource(R.drawable.comment_star_gray_icon);
                    break;
                case 1:
                    xx1.setImageResource(R.drawable.comment_star_light_icon);
                    xx2.setImageResource(R.drawable.comment_star_gray_icon);
                    xx3.setImageResource(R.drawable.comment_star_gray_icon);
                    xx4.setImageResource(R.drawable.comment_star_gray_icon);
                    xx5.setImageResource(R.drawable.comment_star_gray_icon);
                    break;
                case 2:
                    xx1.setImageResource(R.drawable.comment_star_light_icon);
                    xx2.setImageResource(R.drawable.comment_star_light_icon);
                    xx3.setImageResource(R.drawable.comment_star_gray_icon);
                    xx4.setImageResource(R.drawable.comment_star_gray_icon);
                    xx5.setImageResource(R.drawable.comment_star_gray_icon);
                    break;
                case 3:
                    xx1.setImageResource(R.drawable.comment_star_light_icon);
                    xx2.setImageResource(R.drawable.comment_star_light_icon);
                    xx3.setImageResource(R.drawable.comment_star_light_icon);
                    xx4.setImageResource(R.drawable.comment_star_gray_icon);
                    xx5.setImageResource(R.drawable.comment_star_gray_icon);
                    break;
                case 4:
                    xx1.setImageResource(R.drawable.comment_star_light_icon);
                    xx2.setImageResource(R.drawable.comment_star_light_icon);
                    xx3.setImageResource(R.drawable.comment_star_light_icon);
                    xx4.setImageResource(R.drawable.comment_star_light_icon);
                    xx5.setImageResource(R.drawable.comment_star_gray_icon);
                    break;
                case 5:
                    xx1.setImageResource(R.drawable.comment_star_light_icon);
                    xx2.setImageResource(R.drawable.comment_star_light_icon);
                    xx3.setImageResource(R.drawable.comment_star_light_icon);
                    xx4.setImageResource(R.drawable.comment_star_light_icon);
                    xx5.setImageResource(R.drawable.comment_star_light_icon);
                    break;
            }
        }catch (Exception e){
            Log.e("Exception", e.getMessage()+"");
            Toast.makeText(activity,"信息有误",Toast.LENGTH_SHORT).show();
        }
    }
    public interface StaticListener{
        void xuanzhe2Change3(OrderInfo info, ProgressBar pb_button, TextView button);
        void xuanzhe1Change2(OrderInfo info, ProgressBar pb_button, TextView button);
    }
}
