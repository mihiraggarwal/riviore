package com.example.riviore;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class DonateActivity extends AppCompatActivity {
    private WebView webView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_donate);

        webView = findViewById(R.id.form_wv);
        webView.setWebViewClient(new WebViewClient());
        webView.loadUrl("https://forms.office.com/Pages/ResponsePage.aspx?id=dqBGjZPQbUGle4aSzeE7-BbD_7g9OEtOuB6Xa-XQCCBUQTlFSklSTEZMVEgzUU9DVTkwSDBIVDE5My4u");
    }
}
