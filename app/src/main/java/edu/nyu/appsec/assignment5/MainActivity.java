package edu.nyu.appsec.assignment5;

//import android.Manifest;   mrh583 - Comment out unused import. Would normally remove.
//import android.content.Context; mrh583 - Comment out unused import. Would normally remove.
import android.content.Intent;
//import android.content.pm.PackageManager; mrh583 - Comment out unused import. Would normally remove.
//import android.location.Location;          mrh583 - Location services not used
//import android.location.LocationListener;  mrh583 - Location services not used
//import android.location.LocationManager;   mrh583 - Location services not used
import android.net.Uri;
//import android.net.http.SslError; mrh583 - Comment out unused import. Would normally remove.
//import android.support.v4.app.ActivityCompat; mrh583 - Comment out unused import. Would normally remove.
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
//import android.webkit.SslErrorHandler; mrh583 - Comment out unused import. Would normally remove.
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

//import java.io.BufferedInputStream; mrh583 - Comment out unused import. Would normally remove.
//import java.io.IOException; mrh583 - Comment out unused import. Would normally remove.
//import java.io.SerializablePermission; mrh583 - Comment out unused import. Would normally remove.
//import java.net.HttpURLConnection; mrh583 - Comment out unused import. Would normally remove.
//import java.net.MalformedURLException; mrh583 - Comment out unused import. Would normally remove.
//import java.net.URL; mrh583 - Comment out unused import. Would normally remove.

// mrh - Remove location listener, this app does not require location services. See comments below
//public class MainActivity extends AppCompatActivity implements LocationListener {
public class MainActivity extends AppCompatActivity {
    private static final String SPELL_CHECK_URL = "http://appsecclass.report:8080/";
    private static final String KNOWN_HOST = "appsecclass.report";

    private class MyWebViewClient extends WebViewClient {

        // mrh583 - this code is ok. verifies loading whitelisted host before loading
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
    mrh583 - Commenting out onLocationChanged with theme of removing location services. First,
    the comment indicates this is to support multiple languages. I tried this by manually setting
    lat/long to Paris but no change. Furthermore, if there was a desire to use locations to support
    language services, that should only need to be captured once on login as a parameter to login
    or the initial page access. This method captures the current location each time it changes and
    the onLocationChanged callback is invoked. This is essentially acting as  tracking service and
    sending current location on a regular change periodic and sending to the web service to be
    stored under /metrics. It is unclear if metrics tracks the logged in user and stores the location
    information, but it could and therefore is a privacy concern.
    @Override
    public void onLocationChanged(Location location) {
        URL url = null;
        try {
            url = new URL(SPELL_CHECK_URL + "metrics"
                    +"?lat="
                    +location.getLatitude()+"&long=" + location.getLongitude()
            );
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return;
        }
        HttpURLConnection urlConnection = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.disconnect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    */

    /**
     * mrh583 - comment out since Loc Listener no longer implemented.
     */
    /*
    /*
    /* Necessary to implement the LocationListener interface
    */
    /**
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {}
    @Override
    public void onProviderEnabled(String s) {}
    @Override
    public void onProviderDisabled(String s) {}
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        WebView view = new WebView(this);
        view.setWebViewClient(new MyWebViewClient());

        WebSettings settings = view.getSettings();

        /**
         * mrh583 - Change  setAllowFileAccessFromFileURLs to false. Based on Android
         * API, this appears to deal with Javascript based file access and is potentially
         * a vulnerability. The API indicates in ICE CREAM SANDWICH MR1 and earlier, set
         * to false to prevent possible violations of same origin policy. While I could just
         * comment this out, it is important to note the API indicates prior to JELLYBEAN,
         * the default was true, and has been changed to false. So if a decision is made to
         * take this app code and compile it for earlier Android releases, by NOT explicitly
         * setting this to false, it may default to true and be a vulnerability. The API also
         * says this is ignored if getAllowUniversalAccessFromFileURLs() is set to true (which
         * it is a couple of lines down - but might not be after i am done with it.
         */
        settings.setAllowFileAccessFromFileURLs(false);
        // settings.setAllowFileAccessFromFileURLs(true);

        /*
         * mrh583 - Considered changing setting to disable javascript to mitigate
         * risk of injection type attacks, however, menu pages rely on javascript
         * and therefore cannot turn this off for the app to work. Verified injection
         * attacks do not occur (attempted to reflect <script>) based on server-
         * side protections
         */
        settings.setJavaScriptEnabled(true);

        /*
           mrh583 - Setting setAllowUniversalAccessFromFileURLs to false. Per API
           description: "To prevent possible violation of same domain policy you...
           should explicitly set this value to false. This defaults to false and since
           there is no file access this app needs to perform on the client side,
           changing setting from true to false. Note i am setting to false
           explicitly. Just commenting out the true setting would have the same result,
           however, because this API call default changed from true to false around
           ICR CREAM SANDWICH MR1, it is safest to explicitly toggle.
         */
        //settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(false);

        /*
         mrh583 - Commenting out location block. While permissions should also be modified not to require
         location services, this app has no legitimate need to acquire location so commenting out the
         code below that attempts to access device location services.
         */
        /*
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        if (!(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)) {
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
        }
        */


        setContentView(view);
        view.loadUrl(SPELL_CHECK_URL + "register");
    }
}