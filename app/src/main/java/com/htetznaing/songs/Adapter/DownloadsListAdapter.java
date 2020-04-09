package com.htetznaing.songs.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.content.FileProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.htetznaing.songs.Constants;
import com.htetznaing.songs.R;
import com.htetznaing.songs.Utils.Utils;
import com.htetznaing.songs.ui.dashboard.DownloadsFragment;

import java.io.File;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class DownloadsListAdapter extends RecyclerView.Adapter<DownloadsListAdapter.MyViewHolder>  {
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View photoView = inflater.inflate(R.layout.downloads_item, parent, false);
        MyViewHolder viewHolder = new MyViewHolder(photoView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goNext(data.get(position));
            }
        });
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                showOptions(data.get(position));
                return true;
            }
        });

        holder.textViewVideoTitle.setText(getNameFromFile(data.get(position)));
        holder.textViewVideoModified.setText(getModifiedTimeFromFile(data.get(position)));
        holder.textViewVideoSize.setText(getFileSize(data.get(position).length()));

        Glide.with(mContext)
                .load(data.get(position))
                .apply(new RequestOptions().placeholder(R.drawable.ic_launcher_background))
                .into(holder.thumbnailImageView);
        if (data.get(position).getName().endsWith(".mp3")){
            holder.playVid.setVisibility(View.GONE);
            holder.playAud.setVisibility(View.VISIBLE);
        }else {
            holder.playVid.setVisibility(View.VISIBLE);
            holder.playAud.setVisibility(View.GONE);
        }

        holder.buttonShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Utils.others(data.get(position),mContext);
            }
        });
    }

    public void showOptions(final File file){
        CharSequence[] list = {"Messenger မှတဆင့်မျှဝေမည်","WhatsApp မှတဆင့်မျှဝေမည်","Instagram မှတဆင့်မျှဝေမည်","Facebook မှတဆင့်မျှဝေမည်","အခြား App များမှတဆင့်မျှဝေမည်","ဖျက်မည်","အသေးစိတ်"};
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setItems(list, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which){
                            case 0:Utils.messenger(file,mContext);break;
                            case 1:Utils.whatsApp(file,mContext);break;
                            case 2:Utils.instagram(file,mContext);break;
                            case 3:Utils.facebook(file,mContext);break;
                            case 4:Utils.others(file,mContext);break;
                            case 5:deleteVideo(file);break;
                            case 6:showDetails(file);break;
                        }
                    }
                });
        builder.show();
    }

    public String getFileType(File url) {
        if (url.getName().endsWith(".mp3")){
            return "MP3";
        }else if (url.getName().endsWith("mp4")){
            return "MP4";
        }
        Uri uri = Constants.uriFromFile(mContext,url);
        String mime = mContext.getContentResolver().getType(uri);
        return MimeTypeMap.getSingleton().getMimeTypeFromExtension(mime);
    }

    public static String getFileSize(long size) {
        if(size <= 0) return "0";
        final String[] units = new String[] { "B", "kB", "MB", "GB", "TB" };
        int digitGroups = (int) (Math.log10(size)/ Math.log10(1024));
        return new DecimalFormat("#,##0.#").format(size/ Math.pow(1024, digitGroups)) + " " + units[digitGroups];
    }

    public void showDetails(File file){
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle("ဖိုင်အသေးစိတ်အချက်အလက်")
                .setMessage("အမည်: "+getNameFromFile(file)+"\nအမျိုးအစား: "+getFileType(file)+"\nရက်စွဲ: "+getModifiedDateFromFile(file)+"\nဖိုင်ဆိုဒ်: "+getFileSize(file.length())+"\nလမ်းကြောင်း: "+file.getAbsolutePath())
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void deleteVideo(final File file) {
        AlertDialog.Builder builder = new AlertDialog.Builder(mContext)
                .setTitle("Attention!")
                .setMessage("Do you want to delete\n"+getNameFromFile(file)+" ?")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (file.delete()){
                            Toast.makeText(mContext, "Deleted!", Toast.LENGTH_SHORT).show();
                        }else {
                            Toast.makeText(mContext, "Cannot delete this video :(", Toast.LENGTH_SHORT).show();
                        }
                        DownloadsFragment.instance.loadRefresh();
                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
        builder.show();
    }

    private void goNext(File url) {
        // Get URI and MIME type of file
        Uri uri = Constants.uriFromFile(mContext,url);
        String mime = mContext.getContentResolver().getType(uri);
        // Open file with user selected app
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        intent.setDataAndType(uri, mime);
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        mContext.startActivity(Intent.createChooser(intent,"Open with.."));
    }


    @Override
    public int getItemCount() {
        return data.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        ImageView thumbnailImageView,playVid,playAud;
        TextView textViewVideoTitle,textViewVideoModified,textViewVideoSize;
        Button buttonShare;
        MyViewHolder(View itemView) {
            super(itemView);
            thumbnailImageView = itemView.findViewById(R.id.thumbnailImageView);
            textViewVideoTitle = itemView.findViewById(R.id.textViewVideoTitle);
            buttonShare = itemView.findViewById(R.id.buttonShare);
            textViewVideoModified = itemView.findViewById(R.id.textViewVideoModified);
            textViewVideoSize = itemView.findViewById(R.id.textViewVideoSize);
            playVid = itemView.findViewById(R.id.playVideo);
            playAud = itemView.findViewById(R.id.playAudio);
        }
    }

    private ArrayList<File> data;
    private Activity mContext;

    public DownloadsListAdapter(Activity context, ArrayList<File> data) {
        mContext = context;
        this.data = data;
    }

    private String getNameFromFile(File file){
        return file.getName();
    }

    private String getModifiedDateFromFile(File file){
        SimpleDateFormat dateFormat= new SimpleDateFormat("dd/MM/yyyy");
        Date lastModified = new Date(file.lastModified());
        return dateFormat.format(lastModified);
    }

    private String getModifiedTimeFromFile(File file){
        Date lastModified = new Date(System.currentTimeMillis());
        if (file.exists()) {
            lastModified = new Date(file.lastModified());
        }
        return lastModified.toLocaleString();
    }
}