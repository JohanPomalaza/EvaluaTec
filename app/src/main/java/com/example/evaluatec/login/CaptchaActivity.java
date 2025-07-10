package com.example.evaluatec.login;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.webkit.JavascriptInterface;
import android.webkit.WebView;

import androidx.appcompat.app.AppCompatActivity;

public class CaptchaActivity extends AppCompatActivity {
    WebView webView;
    Context context;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        webView = new WebView(this);
        setContentView(webView);

        webView.getSettings().setJavaScriptEnabled(true);
        webView.addJavascriptInterface(new WebAppInterface(this), "Android");

        webView.loadUrl("file:///android_asset/captcha.html");
    }

    public class WebAppInterface {
        Context mContext;

        WebAppInterface(Context context) {
            mContext = context;
        }

        @JavascriptInterface
        public void receiveToken(String token) {
            // Cuando el token es recibido desde el HTML
            Intent resultIntent = new Intent();
            resultIntent.putExtra("captchaToken", token);
            setResult(RESULT_OK, resultIntent);
            finish(); // Cierra la actividad y retorna a LoginActivity
        }
    }
}
