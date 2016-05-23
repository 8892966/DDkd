package com.example.user.ddkd;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.DownloadListener;
import android.webkit.WebChromeClient;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.baidu.mobstat.StatService;

/**
 * Created by User on 2016-04-02.
 */
public class WebActivity extends BaseActivity implements View.OnClickListener {
    //返回按钮
    private TextView tv_head_fanghui;
    private WebView wv_web;
    private  String url;
    private ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.web_activity);
        ExitApplication.getInstance().addActivity(this);
        tv_head_fanghui= (TextView)findViewById(R.id.tv_head_fanghui);
        progressBar= (ProgressBar) findViewById(R.id.ProgressBar1);
        tv_head_fanghui.setOnClickListener(this);
        url=getIntent().getStringExtra("url");
        wv_web= (WebView)findViewById(R.id.wv_web);
        wv_web.setWebViewClient(new android.webkit.WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                view.loadUrl(url);
                return true;
            }
        });
        wv_web.getSettings().setBuiltInZoomControls(false);
        wv_web.getSettings().setSupportZoom(false);
        wv_web.getSettings().setJavaScriptEnabled(true);
        wv_web.setWebChromeClient(new WebViewClient());
        wv_web.setDownloadListener(new DownloadListener() {
            @Override
            public void onDownloadStart(String url, String userAgent, String contentDisposition, String mimetype, long contentLength) {
                if (url != null && url.startsWith("http://"))
                    startActivity(new Intent(Intent.ACTION_VIEW,Uri.parse(url)));
            }
        });
        wv_web.loadUrl(url);
        TextView title= (TextView) findViewById(R.id.tv_web_title);
        title.setText(getIntent().getStringExtra("title"));
    }
    private class WebViewClient extends WebChromeClient {
        @Override
        public void onProgressChanged(WebView view, int newProgress) {
            progressBar.setProgress(newProgress);
            if(newProgress==100){
                progressBar.setVisibility(View.GONE);
            }else {
                progressBar.setVisibility(View.VISIBLE);
            }
            super.onProgressChanged(view, newProgress);
        }
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
        StatService.onPageEnd(this,getIntent().getStringExtra("title"));
    }
}
