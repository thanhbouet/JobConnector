package com.example.jobconnector;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);

        WebView webView = findViewById(R.id.webview);
        webView.loadUrl("https://www.youtube.com/watch?v=1CTced9CMMk");
    }
}