package com.zhuge.zhugeiodemo.activity;

import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;

import com.zhuge.zhugeiodemo.R;
import android.os.Bundle;
import android.util.Log;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.math.BigDecimal;

public class WebViewActivity extends AppCompatActivity {

    WebView webView;

    long _start;
    long _end;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);

        setupWebView();


    }

    private void setupWebView() {

        webView = (WebView) findViewById(R.id.web_view);

        //声明WebSettings子类
        WebSettings webSettings = webView.getSettings();

//如果访问的页面中要与Javascript交互，则webview必须设置支持Javascript
        webSettings.setJavaScriptEnabled(true);

//设置自适应屏幕，两者合用
        webSettings.setUseWideViewPort(true); //将图片调整到适合webview的大小
        webSettings.setLoadWithOverviewMode(true); // 缩放至屏幕的大小

//缩放操作
        webSettings.setSupportZoom(true); //支持缩放，默认为true。是下面那个的前提。
        webSettings.setBuiltInZoomControls(true); //设置内置的缩放控件。若为false，则该WebView不可缩放
        webSettings.setDisplayZoomControls(false); //隐藏原生的缩放控件

//其他细节操作
        webSettings.setCacheMode(WebSettings.LOAD_CACHE_ELSE_NETWORK); //关闭webview中缓存
        webSettings.setAllowFileAccess(true); //设置可以访问文件
        webSettings.setJavaScriptCanOpenWindowsAutomatically(true); //支持通过JS打开新窗口
        webSettings.setLoadsImagesAutomatically(true); //支持自动加载图片
        webSettings.setDefaultTextEncodingName("utf-8");//设置编码格式

//        ZhugeSDK.ZhugeJS js = new ZhugeSDK.ZhugeJS();
//        webView.addJavascriptInterface(js,"zhugeTracker");

        webView.loadUrl("file:///android_asset/H5Demo.html");
//        webView.loadUrl("https://www.baidu.com");


        webView.setWebViewClient(new WebViewClient(){
            // 页面打开时想做的事情
            @Override
            public void onPageStarted(WebView view, String url, Bitmap favicon) {
                super.onPageStarted(view, url, favicon);
                _start = System.currentTimeMillis();
                Log.i("webview start time == ",String.valueOf(_start));

            }
            // 页面加载完成后想做的事情
            @Override
            public void onPageFinished(WebView view, String url) {
                super.onPageFinished(view, url);
                Log.i("webview  == ",url);
                _end = System.currentTimeMillis();
                Log.i("webview end time == ",String.valueOf(System.currentTimeMillis()));
                double diff = new BigDecimal((float)(_end - _start) / 1000).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
                Log.i("time diffrence =",String.valueOf(diff));
            }
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                view.loadUrl(request.getUrl().toString());
                return super.shouldOverrideUrlLoading(view, request);
            }
        });

    }



}