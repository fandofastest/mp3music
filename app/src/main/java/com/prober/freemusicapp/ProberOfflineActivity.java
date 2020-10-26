package com.prober.freemusicapp;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.provider.MediaStore;
import android.provider.Settings;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;
import android.widget.TextView;

import com.prober.freemusicapp.adapter.TrackAdapterOffline;
import com.prober.freemusicapp.model.TrackOffline;
import com.facebook.ads.AdSize;
import com.google.android.gms.ads.AdRequest;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static com.prober.freemusicapp.ProberSplashActivity.PERCODE;

public class ProberOfflineActivity extends AppCompatActivity {

    SearchView searchView;
    RecyclerView recView;
    RelativeLayout banner;
    TrackAdapterOffline mAdapterOffline;
    List<Object> listOffline;
    ProgressDialog mProgressDialog;
    TextView noSong;

    com.google.android.gms.ads.AdView adView;
    com.facebook.ads.AdView fbView;

    String search_query, cat;
//    public static String DB_PATH = Environment.getExternalStorageDirectory() + File.separator + "Downloads";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_offline);

        searchView = findViewById(R.id.searchViewOffline);
        recView = findViewById(R.id.recView);
        banner = findViewById(R.id.banner);
        noSong = findViewById(R.id.noSong);
        setTitle("Downloaded Songs");

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                search_query = query;
                Intent i = new Intent(ProberOfflineActivity.this, ProberOnlineActivity.class);
                i.putExtra("query", search_query);
                startActivity(i);
                return true;

            }

            @Override
            public boolean onQueryTextChange(String query) {

                return false;
            }
        });

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE}, 1111);
            }
        }


        if (ProberSplashActivity.ads_sett.equals("fb")) {
            fbView = new com.facebook.ads.AdView(this, ProberSplashActivity.id_banner, AdSize.BANNER_HEIGHT_50);
            banner.addView(fbView);
            fbView.loadAd();
        } else {
            adView = new com.google.android.gms.ads.AdView(this);
            adView.setAdUnitId(ProberSplashActivity.id_banner);
            adView.setAdSize(com.google.android.gms.ads.AdSize.SMART_BANNER);
            AdRequest dareq = new AdRequest.Builder().build();
            banner.addView(adView);
            adView.loadAd(dareq);
        }


        LinearLayoutManager linlayout = new LinearLayoutManager(this);
        linlayout.setOrientation(RecyclerView.VERTICAL);
        recView.setLayoutManager(linlayout);
        recView.setHasFixedSize(true);
        recView.setItemAnimator(new DefaultItemAnimator());

        listOffline = new ArrayList<>();
        mAdapterOffline = new TrackAdapterOffline(this, listOffline);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (!Settings.System.canWrite(this)) {
                requestPermissions(new String[]{
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                }, PERCODE);

            } else {
                getMusic();
            }
        } else {
            getMusic();

        }

        recView.setAdapter(mAdapterOffline);
        getMusic();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERCODE) {

        }
    }




    public void getMusic(){

        listOffline.clear();
        recView.removeAllViews();


        Uri allSongsUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        String selection = MediaStore.Audio.Media.IS_MUSIC + " != 0";
        Cursor cursor =  getApplicationContext().getContentResolver().query(allSongsUri, null, null, null, selection);
        if (cursor != null) {
            if (cursor.moveToFirst()) {
                do {


                    TrackOffline modalClass = new TrackOffline(cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE)),cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.DATA)));
                       listOffline.add(modalClass);




                } while (cursor.moveToNext());
            }
            cursor.close();
        }

        if (listOffline.size() < 1) {
            listOffline.clear();
            noSong.setVisibility(View.VISIBLE);
        } else {
            noSong.setVisibility(View.GONE);
        }

        mAdapterOffline.notifyDataSetChanged();
    }


    public ArrayList<String> GetFiles(String directorypath) {
        ArrayList<String> Myfiles = new ArrayList<String>();
        File f = new File(directorypath);

        File[] files = f.listFiles();
        for (int i = 0; i < files.length; i++) {

            String filename = files[i].getName();
            String ext = filename.substring(filename.lastIndexOf('.') + 1, filename.length());
            if (ext.equals("MP3") || ext.equals("mp3")) {
                Myfiles.add(files[i].getName());
            }
        }

        return Myfiles;
    }

    public void Exit() {
        new AlertDialog.Builder(ProberOfflineActivity.this)
                .setTitle("Exit")
                .setMessage("Are you sure ?")
                .setIcon(R.mipmap.ic_launcher)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        System.exit(1);
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                })
                .show();
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        super.onKeyDown(keyCode, event);

        if (keyCode == KeyEvent.KEYCODE_BACK && !searchView.isIconified()) {
            searchView.setIconified(true);
            searchView.setIconified(true);
        } else if (keyCode == KeyEvent.KEYCODE_BACK) {
            onBackPressed();
        }

        return false;
    }
}
