package com.htetznaing.songs.Utils;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.widget.Toast;

import androidx.core.content.FileProvider;

import com.htetznaing.songs.Constants;
import com.htetznaing.songs.R;

import java.io.File;

public class Utils {
    public static void whatsApp(File input, Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            Uri file = Constants.uriFromFile(activity,input);
            intent.setPackage("com.whatsapp");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String hashtag = activity.getString(R.string.app_name);
            if (hashtag.contains(" ")){
                hashtag = hashtag.replace(" ","");
            }
            hashtag = "#"+hashtag;
            intent.putExtra(Intent.EXTRA_TEXT, hashtag);
            intent.putExtra(Intent.EXTRA_STREAM, file);
            intent.setType("video/*");
            activity.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "You have not installed WhatsApp", Toast.LENGTH_SHORT).show();
        }
    }

    public static void messenger(File input, Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            Uri file = Constants.uriFromFile(activity,input);
            intent.setPackage("com.facebook.orca");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String hashtag = activity.getString(R.string.app_name);
            if (hashtag.contains(" ")){
                hashtag = hashtag.replace(" ","");
            }
            hashtag = "#"+hashtag;
            intent.putExtra(Intent.EXTRA_TEXT, hashtag);
            intent.putExtra(Intent.EXTRA_STREAM, file);
            intent.setType("video/*");
            activity.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "You have not installed Messenger", Toast.LENGTH_SHORT).show();
        }
    }

    public static void facebook(File input, Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            Uri file = Constants.uriFromFile(activity,input);
            intent.setPackage("com.facebook.katana");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String hashtag = activity.getString(R.string.app_name);
            if (hashtag.contains(" ")){
                hashtag = hashtag.replace(" ","");
            }
            hashtag = "#"+hashtag;
            intent.putExtra(Intent.EXTRA_TEXT, hashtag);
            intent.putExtra(Intent.EXTRA_STREAM, file);
            intent.setType("video/*");
            activity.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "You have not installed Facebook", Toast.LENGTH_SHORT).show();
        }
    }

    public static void instagram(File input, Activity activity) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            Uri file = Constants.uriFromFile(activity,input);
            intent.setPackage("com.instagram.android");
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String hashtag = activity.getString(R.string.app_name);
            if (hashtag.contains(" ")){
                hashtag = hashtag.replace(" ","");
            }
            hashtag = "#"+hashtag;
            intent.putExtra(Intent.EXTRA_TEXT, hashtag);
            intent.putExtra(Intent.EXTRA_STREAM, file);
            intent.setType("video/*");
            activity.startActivity(intent);
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(activity, "You have not installed Facebook", Toast.LENGTH_SHORT).show();
        }
    }

    public static void others(File input, Activity activity) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_SEND);
            Uri file = Constants.uriFromFile(activity,input);
            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
            String hashtag = activity.getString(R.string.app_name);
            if (hashtag.contains(" ")){
                hashtag = hashtag.replace(" ","");
            }
            hashtag = "#"+hashtag;
            intent.putExtra(Intent.EXTRA_TEXT, hashtag);
            intent.putExtra(Intent.EXTRA_STREAM, file);
            intent.setType("video/*");
            activity.startActivity(Intent.createChooser(intent,"Share Via..."));
    }
}
