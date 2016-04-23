package com.example.user.ddkd;

import android.app.Activity;
import android.os.Bundle;
import android.webkit.WebView;

/**
 * Created by Administrator on 2016/4/23.
 */
public class MainActivity_Webview extends Activity{
    private WebView webView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_aboutdd);
        webView= (WebView) findViewById(R.id.webview);

        String url="http://www.baidu.com";
        webView.loadUrl(url);

    }
}
