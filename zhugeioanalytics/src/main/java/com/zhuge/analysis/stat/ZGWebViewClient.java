package com.zhuge.analysis.stat;

import android.graphics.Bitmap;
import android.util.Log;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import java.math.BigDecimal;

public class ZGWebViewClient  extends WebViewClient {

    long _start;
    long _end;
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
        _end = System.currentTimeMillis();
        Log.i("webview end time == ",String.valueOf(System.currentTimeMillis()));

        double diff = new BigDecimal((float)(_end - _start) / 1000).setScale(3, BigDecimal.ROUND_HALF_UP).doubleValue();
        Log.i("ZG time diffrence =",String.valueOf(diff));

    }
}
