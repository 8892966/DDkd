package com.example.user.ddkd;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.baidu.mobstat.StatService;

/**
 * Created by User on 2016-04-02.
 */
public class WebActivity extends Activity implements View.OnClickListener {
    //返回按钮
    private TextView tv_head_fanghui;
    private WebView wv_web;
    private  String url;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        tv_head_fanghui= (TextView) findViewById(R.id.tv_head_fanghui);
        tv_head_fanghui.setOnClickListener(this);
        url=getIntent().getStringExtra("url");
        wv_web= (WebView) findViewById(R.id.wv_web);
        wv_web.loadUrl(url);
        wv_web.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
        TextView title= (TextView) findViewById(R.id.tv_web_title);
        title.setText(getIntent().getStringExtra("title"));
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.tv_head_fanghui:
                finish();
                break;
        }
    }
    @Override
    protected void onResume(){
        super.onResume();
        StatService.onPageStart(this,getIntent().getStringExtra("title"));
    }

    @Override
    protected void onPause(){
        super.onPause();
        StatService.onPageStart(this,getIntent().getStringExtra("title"));
    }
}
