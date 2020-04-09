package com.htetznaing.songs.Utils;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.widget.Toast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import static android.content.Context.DOWNLOAD_SERVICE;

public class XDownloader {
    private Activity activity;
    private String mBaseFolderPath;
    private DownloadManager mDownloadManager;
    private long mDownloadedFileID;
    private DownloadManager.Request mRequest;
    private OnDownloadFinished onDownloadFinished;

    public XDownloader(Activity activity){
        this.activity = activity;
        mDownloadManager = (DownloadManager) activity.getSystemService(DOWNLOAD_SERVICE);
        mBaseFolderPath = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/";
    }

    public void download(String fileName,String url){
        try {
            if (!new File(mBaseFolderPath).exists()) {
                new File(mBaseFolderPath).mkdir();
            }
            String mFilePath = "file://" + mBaseFolderPath + fileName;
            Uri downloadUri = Uri.parse(url);
            mRequest = new DownloadManager.Request(downloadUri);
            mRequest.setDestinationUri(Uri.parse(mFilePath));
            mRequest.setMimeType("video/*");
            mRequest.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED);
            mDownloadedFileID = mDownloadManager.enqueue(mRequest);
            IntentFilter downloaded = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
            activity.registerReceiver(downloadCompletedReceiver, downloaded);
            Toast.makeText(activity, "Starting Download : " + fileName, Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            try {
                intent.setDataAndType(Uri.parse(URLDecoder.decode(url, "UTF-8")), "video/mp4");
                activity.startActivity(Intent.createChooser(intent, "Download with..."));
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }
        }
    }

    private BroadcastReceiver downloadCompletedReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Uri uri = mDownloadManager.getUriForDownloadedFile(mDownloadedFileID);
            onDownloadFinished.onCompleted(getRealPathFromURI(uri));
        }
    };

    private String getRealPathFromURI (Uri contentUri) {
        String path = null;
        String[] proj = { MediaStore.MediaColumns.DATA };
        Cursor cursor = activity.getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.MediaColumns.DATA);
            path = cursor.getString(column_index);
        }
        if (cursor != null) {
            cursor.close();
        }
        return path;
    }

    public void OnDownloadFinishedListerner(OnDownloadFinished onDownloadFinished){
        this.onDownloadFinished=onDownloadFinished;
    }

    public interface OnDownloadFinished {
        void onCompleted(String path);
    }
}
