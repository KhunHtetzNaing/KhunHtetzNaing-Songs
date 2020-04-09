package com.htetznaing.songs.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.htetznaing.songs.Activities.DetailsActivity;
import com.htetznaing.songs.Model.Songs;
import com.htetznaing.songs.R;
import com.htetznaing.songs.Utils.CheckInternet;

import java.util.List;

public class SongsAdapter extends RecyclerView.Adapter<SongsAdapter.ViewHolder> {
    private List<Songs> data;
    private Activity activity;

    public SongsAdapter(Activity activity,List<Songs> data) {
        this.data = data;
        this.activity=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(holder.itemView)
                .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.dev))
                .load(data.get(position).getImg())
                .into(holder.thumb);
        holder.itemView.setOnClickListener(view -> showDialog(holder.itemView.getContext(),data.get(position)));
    }

    private void showDialog(Context context,Songs songs) {
        if (new CheckInternet(activity).isInternetOn(true)) {
            context.startActivity(new Intent(context, DetailsActivity.class).putExtra("data", songs));
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView thumb;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
        }
    }
}
