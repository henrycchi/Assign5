package edu.nyu.appsec.assignment5;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

public class MainActivity extends AppCompatActivity {
    private static final String SPELL_CHECK_URL =  "http://appsecclass.report:8080/";
    private static final String KNOWN_HOST = "appsecclass.report";

    private class MyWebViewClient extends WebViewClient {
        /*
        This is never called but it could just be a platform issue.
         */
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
            String url = String.valueOf(request.getUrl());
            String host = Uri.parse(url).getHost();

            if (KNOWN_HOST.equals(host)) {
                return false;
            }

            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
            return true;
        }
    }

    /* Get location data to provide language localization
    *  Supported languages ar-DZ zh-CN en-US en-IN en-AU fr-FR
    */
    /*
    Note
        Disabled location reporting methods
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView view = new WebView(this);
        view.setWebViewClient(new MyWebViewClient());

        WebSettings settings = view.getSettings();
        /*
        TODO
            Disable file access
         */
        settings.setJavaScriptEnabled(true);

        /*
        TODO
            Disable location tracking
         */

        setContentView(view);
        view.loadUrl(SPELL_CHECK_URL + "register");
    }
}