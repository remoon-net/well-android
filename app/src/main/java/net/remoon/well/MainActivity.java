package net.remoon.well;

import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    private WebView webView;
    private static final int REQ_VPN = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = VpnService.prepare(this);
        if (intent != null) {
            startActivityForResult(intent, REQ_VPN);
        }

        webView = new WebView(this);

        WebSettings settings = webView.getSettings();
        settings.setJavaScriptEnabled(true);
        settings.setDomStorageEnabled(true);

        webView.setWebViewClient(new WebViewClient());
        String addr = "http://"+WellApplication.listenAddr+"/";
        webView.loadUrl(addr);

        setContentView(webView);
    }

    @Override
    public void onBackPressed() {
        if (webView != null && webView.canGoBack()) {
            webView.goBack();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onDestroy() {
        if (webView != null) {
            webView.destroy();
        }
        super.onDestroy();
    }
}