package com.htetznaing.songs;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;

import androidx.core.content.FileProvider;

import java.io.File;

public class Constants {

    public static void openDevFB(Context context) {
        openFb("100011402500763",context,false);
    }

    public static void openDevFbPage(Context context) {
        openFb("2227883043913863",context,true);
    }

    private static void openFb(String id,Context context,boolean isPage){
        Intent intent = new Intent(Intent.ACTION_VIEW);
        try {
            intent.setData(Uri.parse(isPage ? "fb://page/"+id : "fb://profile/"+id));
            context.startActivity(intent);
        } catch (Exception e) {
            intent.setData(Uri.parse("https://facebook.com/"+id));
            context.startActivity(intent);
        }
    }

    public static Uri uriFromFile(Context context, File file) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            return FileProvider.getUriForFile(context, context.getPackageName()+ ".provider", file);
        } else {
            return Uri.fromFile(file);
        }
    }
}
