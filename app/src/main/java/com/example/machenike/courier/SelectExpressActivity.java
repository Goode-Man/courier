package com.example.machenike.courier;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.example.machenike.http.HttpConfig;

public class SelectExpressActivity extends AppCompatActivity {

    WebView select_express_webview;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_express);
        Intent intent=getIntent();
        String result=intent.getStringExtra("http");
        select_express_webview= (WebView) findViewById(R.id.select_express_webview);
        select_express_webview.getSettings().setJavaScriptEnabled(true);
        //WebView加载web资源
        select_express_webview.loadUrl(result);
        //覆盖WebView默认使用第三方或系统默认浏览器打开网页的行为，使网页用WebView打开
        select_express_webview.setWebViewClient(new WebViewClient(){
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, String url) {
                // TODO Auto-generated method stub
                //返回值是true的时候控制去WebView打开，为false调用系统浏览器或第三方浏览器
                view.loadUrl(url);
                return true;
            }
        });
    }
}
