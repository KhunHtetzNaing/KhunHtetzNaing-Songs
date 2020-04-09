package com.htetznaing.songs.Utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

public class DownloadWithOther {
    private static SharedPreferences sharedPreferences;
    private static String KEY = "dl";

    public static void init(Context context){
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static boolean get(){
        return sharedPreferences.getBoolean(KEY,false);
    }

    public static void set(boolean how){
        sharedPreferences.edit().putBoolean(KEY,how).apply();
    }
}
