package com.systex.hkspractice;

import android.app.ProgressDialog;
import android.content.Context;
import android.webkit.WebView;
import android.webkit.WebViewClient;


public class SMyWebViewClient extends WebViewClient{
    ProgressDialog dialog;
    Context Context;

    public SMyWebViewClient(Context c,ProgressDialog dialog){
        this.dialog=dialog;
        this.Context=c;
    }

    @Override
    public void onPageFinished(WebView view, String url) {
        super.onPageFinished(view, url);
        this.dialog.cancel();
    }
}
