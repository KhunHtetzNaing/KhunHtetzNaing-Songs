package com.htetznaing.songs.AppUpdater;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresPermission;
import androidx.appcompat.app.AlertDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

public class AppUpdater {
    private Activity activity;
    private String download = null;
    private String versionName = null;
    private boolean uninstall = false,force=true;
    private String json_url;
    private AlertDialog.Builder builder;
    private AlertDialog dialog;
    private boolean showMessage;
    private ProgressDialog progressDialog;
    @RequiresPermission(Manifest.permission.INTERNET)
    public AppUpdater(Activity activity,String json_url) {
        this.json_url = json_url;
        this.activity = activity;

        progressDialog = new ProgressDialog(activity);
        progressDialog.setMessage("စစ်ဆေးနေပါသည်...");

        builder = new AlertDialog.Builder(activity);
    }

    public void check(boolean showMessage){
        this.showMessage=showMessage;
        checkUpdate();
    }

    private void checkUpdate() {
        new AsyncTask<Void,Void,String>(){

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                if (showMessage){
                    progressDialog.show();
                }
            }

            @Override
            protected String doInBackground(Void... voids) {
                String s = null;
                try {
                    Document document = Jsoup.connect(json_url).get();
                    if (json_url.contains("blogspot.com") || document.hasClass("post-body entry-content")) {
                        //Blogspot Link
                        try{
                            s = document.getElementsByClass("post-body entry-content").get(0).text();
                        }catch (Exception e) {
                            s = document.getElementsByTag("body").get(0).text();
                        }
                    }else {
                        s = document.text();
                    }
                    assert s != null;
                    int start = s.indexOf("{");
                    int end = s.lastIndexOf("}")+1;
                    s = s.substring(start,end);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return s;
            }


            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (showMessage){
                    progressDialog.dismiss();
                }
                if (s!=null){
                    letCheck(s);
                }
            }
        }.execute();
    }

    private String query(String key,JSONObject object){
        if (object.has(key)){
            try {
                return object.getString(key);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    private void letCheck(String response){
        UpdateModel data = new UpdateModel();
        if (response!=null) {
            try {
                JSONObject object = new JSONObject(response);
                data.setVersionName(query("versionName",object));
                data.setTitle(query("title",object));
                data.setMessage(query("message",object));
                data.setDownload(query("download",object));
                data.setPlaystore(query("playstore",object));
                data.setUninstall(object.getBoolean("uninstall"));
                data.setVersionCode(object.getInt("versionCode"));
                data.setForce(object.getBoolean("force"));

                if (object.has("what")) {
                    JSONObject what = object.getJSONObject("what");
                    data.setAll(what.getBoolean("all"));
                    JSONArray versions = what.getJSONArray("version");

                    int v_int [] = new int[versions.length()];
                    for (int i=0;i<versions.length();i++){
                        v_int[i] = versions.getInt(i);
                    }
                    data.setVersions(v_int);

                    JSONArray models = what.getJSONArray("model");
                    String models_string [] = new String[models.length()];
                    for (int i=0;i<models.length();i++){
                        models_string[i] = models.getString(i);
                    }
                    data.setModels(models_string);
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }


        final String title = data.getTitle();
        final String message = data.getMessage();
        final String playStore = data.getPlaystore();
        uninstall = data.isUninstall();
        int versionCode = data.getVersionCode();
        versionName = data.getVersionName();
        download = data.getDownload();
        force = data.isForce();

        PackageManager manager = activity.getPackageManager();
        PackageInfo info;
        int currentVersion = 0;
        try {
            info = manager.getPackageInfo(activity.getPackageName(), 0);
            currentVersion = info.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        if (versionCode ==currentVersion || versionCode <currentVersion){
            if (showMessage) {
                if (dialog!=null){
                    dialog.dismiss();
                }
                builder.setTitle("ဂုဏ်ယူပါတယ်!")
                        .setMessage("သင်အသုံးပြုနေတာနောက်ဆုံးဗားရှင်းဖြစ်ပါတယ်။")
                        .setPositiveButton("ဟုတ်ပြီ",null);
                dialog = builder.create();
                dialog.show();
            }
        }else{
            if (data.isAll()){
                letUpdate(title,message,playStore);
            }else {
                String my_model = Build.MANUFACTURER.toLowerCase();
                int my_version = Build.VERSION.SDK_INT;
                boolean match_model = false,match_version=false;

                for (String string:data.getModels()){
                    if (my_model.equalsIgnoreCase(string)){
                        match_model = true;
                    }
                }


                for (int i:data.getVersions()){
                    if (my_version==i){
                        match_version = true;
                    }
                }

                if (match_model && match_version){
                    letUpdate(title,message,playStore);
                }
            }
        }
    }

    private void letUpdate(final String title, final String message, final String playStore){
        activity.runOnUiThread(() -> {

            if (dialog!=null){
                dialog.dismiss();
            }

            builder.setTitle(title)
                    .setMessage(message)
                    .setCancelable(!force);
            if (playStore!=null && !playStore.isEmpty()){
                builder.setPositiveButton("Update Now", (dialogInterface, i) -> {
                    final String appPackageName = playStore;
                    if (uninstall){
                        uninstall();
                    }
                    try {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("https://play.google.com/store/apps/details?id=" + appPackageName)));
                    } catch (android.content.ActivityNotFoundException anfe) {
                        activity.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=" + appPackageName)));
                    }
                });
            }
            dialog = builder.create();
            dialog.show();
        });
    }

    private void uninstall(){
        Intent intent = new Intent(Intent.ACTION_DELETE);
        intent.setData(Uri.parse("package:"+activity.getPackageName()));
        activity.startActivity(intent);
    }
}