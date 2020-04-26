package com.zsh.jxunosvideo.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.zsh.jxunosvideo.R;


public class PPtFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v= inflater.inflate(R.layout.fragment_ppt, null, false);
        initView(v);
        return v;
    }

    private void initView(View v) {
        WebView webView=v.findViewById(R.id.ppt_web_view);
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new WebViewClient());
        String readweb="http://view.officeapps.live.com/op/view.aspx?src=";
        webView.loadUrl(readweb+"http://video.ch9.ms/build/2011/slides/TOOL-532T_Sutter.pptx");
    }
}
