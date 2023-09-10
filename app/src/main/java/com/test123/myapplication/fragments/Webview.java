package com.test123.myapplication.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.CookieManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.test123.myapplication.MainActivity;
import com.test123.myapplication.R;

public class Webview extends Fragment {
    private static WebView webview;
    private String url;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        url = getArguments().getString("url");
        Webview.newInstance(url);
        super.onCreate(savedInstanceState);
    }
    public static Webview newInstance(String url) {
        Webview f = new Webview();
        Bundle args = new Bundle();
        args.putString("url", url);
        f.setArguments(args);
        return f;
    }
    public Webview(){
    }
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        if(savedInstanceState!=null)
            webview.restoreState(savedInstanceState);
        else
            webview.loadUrl(url);
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        webview.saveState(outState);
        super.onSaveInstanceState(outState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_webview, container, false);
        webview = view.findViewById(R.id.webview);
        Log.i("URL", url);

        webview.setWebViewClient(new WebViewClient());
        webview.getSettings().setJavaScriptEnabled(true);
        webview.getSettings().setDomStorageEnabled(true);
        webview.getSettings().setJavaScriptCanOpenWindowsAutomatically(true);
        webview.getSettings().setSupportZoom(false);
        webview.getSettings().setLoadWithOverviewMode(true);
        webview.getSettings().setDatabaseEnabled(true);
        webview.getSettings().setAllowFileAccess(true);
        webview.getSettings().setAllowContentAccess(true);
        webview.getSettings().setUseWideViewPort(true);
        webview.getSettings().setAllowContentAccess(true);
        CookieManager cookieManager = CookieManager.getInstance();
        cookieManager.setAcceptCookie(true);

        MainActivity.webViewActive();
        return view;
    }

    public static void backPressed(){
        webview.goBack();
    }
}