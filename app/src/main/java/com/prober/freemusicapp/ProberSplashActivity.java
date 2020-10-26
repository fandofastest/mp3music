package com.prober.freemusicapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.os.SystemClock;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.facebook.ads.Ad;
import com.facebook.ads.AdError;
import com.facebook.ads.AudienceNetworkAds;
import com.facebook.ads.InterstitialAdExtendedListener;
import com.facebook.ads.InterstitialAdListener;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class ProberSplashActivity extends AppCompatActivity {

    ImageView gambaranimasi;
    Button tombolmulai;
    TextView splashTitle;
    ProgressBar splashProgress;
    Animation anim;
    SharedPreference bagikan;

    static String NAMA_PACKAGE;
    public static String id_inter;
    public static String id_banner;
    public static String ads_sett = "";
    public static String availability;
    public static String statususer="";
    public static String sc = "";
    static String moving_link;
    static String json;
    public static String ruang;
    public static boolean online;
     ProgressDialog pgstart;
    public InterstitialAd mInterstitialAd;
    public com.facebook.ads.InterstitialAd interstitialFb;
    private InterstitialAd interstitialAd;
    Handler handler = new Handler();
    static int PERCODE = 1212;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        pgstart = new ProgressDialog(ProberSplashActivity.this);
        pgstart.setIndeterminate(false);
        pgstart.setCancelable(false);
        pgstart.setMessage("Loading...");
        pgstart.show();

//        createInterstitial();

        if (Build.VERSION.SDK_INT >= 21) {
            getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            Window w = getWindow();
            w.setFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS, WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1214);
            }
        }

        gambaranimasi = findViewById(R.id.splashImg);
        tombolmulai = findViewById(R.id.splashButton);
        tombolmulai.setVisibility(View.GONE);


        anim = AnimationUtils.loadAnimation(ProberSplashActivity.this, R.anim.fadein);

        ruang = Environment.getExternalStorageDirectory() + File.separator + "Download";
        bagikan = new SharedPreference(this);
        NAMA_PACKAGE = getApplicationContext().getPackageName();
        json = getResources().getString(R.string.json);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1111);
            }
        }
        if (CheckConnection()) {
            new CallData().execute();
            SystemClock.sleep(2500);
        } else {
            WarningBox();
        }
        if (bagikan.getApp_runFirst().equals("FIRST")) {
            showPolicy();
        } else {

        }

        startApp();

    }

    public void loadInter() {
        final ProgressDialog progressInter = new ProgressDialog(this);
        progressInter.setIndeterminate(false);
        progressInter.setCancelable(false);
        progressInter.setMessage("Loading...");
        progressInter.show();

        if (ads_sett.equals("fb")) {
            AudienceNetworkAds.initialize(this);
            interstitialFb = new com.facebook.ads.InterstitialAd(this, id_inter);

            interstitialFb.loadAd(
                    interstitialFb.buildLoadAdConfig()
                            .withAdListener(new InterstitialAdListener() {
                                @Override
                                public void onInterstitialDisplayed(Ad ad) {

                                }

                                @Override
                                public void onInterstitialDismissed(Ad ad) {
                                    progressInter.dismiss();
                                    Intent i = new Intent(ProberSplashActivity.this, ProberMainActivity.class);
                                    startActivity(i);
                                    finish();

                                }

                                @Override
                                public void onError(Ad ad, AdError adError) {
                                    Log.e("fanzzz", "onError: "+adError.getErrorMessage() );
                                    progressInter.dismiss();
                                    Intent i = new Intent(ProberSplashActivity.this, ProberMainActivity.class);
                                    startActivity(i);
                                    finish();

                                }

                                @Override
                                public void onAdLoaded(Ad ad) {
                                    interstitialFb.show();
                                }

                                @Override
                                public void onAdClicked(Ad ad) {

                                }

                                @Override
                                public void onLoggingImpression(Ad ad) {

                                }
                            })
                            .build());
        } else {
            mInterstitialAd = new InterstitialAd(getApplicationContext());
            mInterstitialAd.setAdUnitId(ProberSplashActivity.id_inter);
            mInterstitialAd.loadAd(new AdRequest.Builder().build());
            mInterstitialAd.setAdListener(new AdListener() {
                @Override
                public void onAdLoaded() {
                    super.onAdLoaded();
                    progressInter.dismiss();
                    mInterstitialAd.show();

                }

                @Override
                public void onAdFailedToLoad(int errorCode){
                    progressInter.dismiss();
                    Intent i = new Intent(ProberSplashActivity.this, ProberMainActivity.class);
                    startActivity(i);
                    finish();
                }

                @Override
                public void onAdOpened() {
                    // Code to be executed when the ad is displayed.
                }

                @Override
                public void onAdClicked() {
                    // Code to be executed when the user clicks on an ad.
                }

                @Override
                public void onAdLeftApplication() {
                    // Code to be executed when the user has left the app.
                }

                @Override
                public void onAdClosed() {
                    Intent i = new Intent(ProberSplashActivity.this, ProberMainActivity.class);
                    startActivity(i);
                    finish();
                }
            });
        }
    }
    @SuppressLint("StaticFieldLeak")
    private class CallData extends AsyncTask<Void, Void, Void> {

        String data = "";

        @Override
        protected Void doInBackground(Void... voids) {

            try {
                URL url = new URL(json);
                HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
                InputStream inputStream = httpURLConnection.getInputStream();
                BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
                String line = "";
                while (line != null) {
                    line = bufferedReader.readLine();
                    data = data + line;
                }
                try {

                    JSONObject getParam = new JSONObject(data);


                    if (NAMA_PACKAGE.equals(getParam.getString("packagename"))) {
                        ads_sett = getParam.getString("ads_sett");
                        availability = getParam.getString("avail");
                        moving_link = getParam.getString("linkps");
                        statususer=getParam.getString("statususer");
                        System.out.println(statususer);
                        sc = getParam.getString("sckey");

                        if (ads_sett.equals("fb")) {
                            id_inter = getParam.getString("fb_inter");
                            id_banner = getParam.getString("fb_banner");
                        } else {
                            id_inter = getParam.getString("ad_inter");
                            id_banner = getParam.getString("ad_banner");
                        }
                    }
                    pgstart.dismiss();
                    tombolmulai.setVisibility(View.VISIBLE);
//                    JSONArray getParam = json.getJSONArray("kampreters");
//                    for (int i = 0; i < getParam.length(); i++) {
//
//                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            } catch (MalformedURLException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);

        }
    }
    public void startApp(){
        if (CheckConnection()){
            if (isOnline()) {
                if (availability.equals("y")) {
                    tombolmulai.setVisibility(View.VISIBLE);
                    tombolmulai.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            tombolmulai.setVisibility(View.INVISIBLE);
                            loadInter();
                        }
                    });

                } else {
                    MoveApps();
                }
            }
        } else {
            WarningBox();
        }
    }
    public boolean CheckConnection() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        if (netInfo != null && netInfo.isConnectedOrConnecting()) {
            return true;
        } else {
            return false;
        }
    }

    public Boolean isOnline() {

        try {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
            HttpURLConnection urlc = (HttpURLConnection) (new URL("https://www.google.com").openConnection());
            urlc.setRequestProperty("User-Agent", "Test");
            urlc.setRequestProperty("Connection", "close");
            urlc.setConnectTimeout(500); //choose your own timeframe
            urlc.setReadTimeout(500); //choose your own timeframe
            urlc.connect();
            int networkcode2 = urlc.getResponseCode();
            return (networkcode2 == 200);
        } catch (IOException e) {
            return false;  //connectivity exists, but no internet.
        }

    }

    public void showPolicy(){
        String txt;
        try {
            InputStream is = getAssets().open("policy.txt");
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            txt = new String(buffer);
        } catch (IOException ex) {
            return;
        }

        AlertDialog alertDialogPolicy = new AlertDialog.Builder(ProberSplashActivity.this).create();
        alertDialogPolicy.setTitle("Privacy Policy");
        alertDialogPolicy.setIcon(R.mipmap.ic_launcher);
        alertDialogPolicy.setMessage(txt);
        alertDialogPolicy.setButton(AlertDialog.BUTTON_POSITIVE, "Accept",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        bagikan.setApp_runFirst("NO");


                    }
                });
        alertDialogPolicy.setButton(AlertDialog.BUTTON_NEGATIVE, "Decline",
                new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        warningpolicy();

                    }
                });
        alertDialogPolicy.setOnDismissListener(new DialogInterface.OnDismissListener() {
            @Override
            public void onDismiss(DialogInterface dialog) {
                warningpolicy();
            }
        });
        alertDialogPolicy.show();
    }

    public void warningpolicy() {
        if (bagikan.getApp_runFirst().equals("FIRST")) {
            AlertDialog alertDialogPolicy = new AlertDialog.Builder(ProberSplashActivity.this).create();
            alertDialogPolicy.setTitle("Policy Warning !");
            alertDialogPolicy.setIcon(R.mipmap.ic_launcher);
            alertDialogPolicy.setMessage("Please accept our policy before use this App.\nThank You.");
            alertDialogPolicy.setButton(AlertDialog.BUTTON_POSITIVE, "Restart",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent i = getBaseContext().getPackageManager()
                                    .getLaunchIntentForPackage(getBaseContext().getPackageName());
                            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(i);
                            finish();
                        }
                    });
            alertDialogPolicy.setButton(AlertDialog.BUTTON_NEGATIVE, "Quit",
                    new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            System.exit(1);
                            finish();
                        }
                    });
            alertDialogPolicy.show();
        }
    }
    public void btn_rate(){
        Uri uri = Uri.parse("market://details?id=" + getPackageName());
        Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
        try {
            startActivity(goToMarket);
        } catch (ActivityNotFoundException e) {
            startActivity(new Intent(
                    Intent.ACTION_VIEW,
                    Uri.parse("http://play.google.com/store/apps/details?id="
                            + getPackageName())));
        }
    }
    public void MoveApps() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(ProberSplashActivity.this)
                        .setTitle("Apps Maintenance")
                        .setMessage("This App is on maintenance,\nPlease go to our new apps with new feature and new experience.")
                        .setIcon(R.mipmap.ic_launcher)
                        .setCancelable( false )
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                startActivity(new Intent(Intent.ACTION_VIEW,
                                        Uri.parse("market://details?id="
                                                + moving_link)));
                                finish();
                            }
                        })
                        .show();
            }
        });
    }

    public void WarningBox() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                new AlertDialog.Builder(ProberSplashActivity.this)
                        .setTitle("No Connection")
                        .setMessage("Please check your internet connection\nThis app running well with good connection")
                        .setCancelable(false)
                        .setIcon(R.mipmap.ic_launcher)
                        .setPositiveButton("Quit", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                System.exit(1);
                                finish();
                            }
                        })
                        .show();
            }
        });
    }
}

