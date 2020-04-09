package com.htetznaing.songs.Utils;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import androidx.appcompat.app.AlertDialog;

/**
 * Created by HtetzNaing on 1/16/2018.
 */

public class CheckInternet {
    Activity context;
    public CheckInternet(Activity context){
        this.context = context;
    }

    public final boolean isInternetOn(boolean showDialog) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo netInfo = cm.getActiveNetworkInfo();
        boolean how = netInfo != null && netInfo.isConnectedOrConnecting();
        if (!how && showDialog){
            AlertDialog.Builder builder = new AlertDialog.Builder(context)
                    .setTitle("ဝမ်းနည်းပါတယ်!")
                    .setMessage("အင်တာနက်ရှိရန်လိုအပ်ပါသည်။\nသင့်အင်တာနက်ကွန်ယက်အားဖွင့်ပါ။\nသို့မဟုတ်\nဝိုင်ဖိုင်ကွန်ယက်တစ်ခုခုနှင့်ချိတ်ဆက်ပါ။")
                    .setPositiveButton("ထပ်ကြိုးစားမည်", (dialogInterface, i) -> {
                        context.startActivity(new Intent(context,context.getClass()).setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK).setFlags(Intent.FLAG_ACTIVITY_NEW_TASK));
                        context.finishAffinity();
                    })
                    .setNegativeButton("ပိတ်မည်", (dialogInterface, i) -> context.finishAffinity());
            builder.show();
        }
        return how;
    }
}
