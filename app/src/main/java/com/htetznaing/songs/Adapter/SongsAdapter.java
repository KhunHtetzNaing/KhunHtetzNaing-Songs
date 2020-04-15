package com.htetznaing.songs.Adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

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
    private static final int LIST_ITEM = 0;
    private static final int GRID_ITEM = 1;
    boolean isSwitchView = false;

    public SongsAdapter(Activity activity,List<Songs> data) {
        this.data = data;
        this.activity=activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView;
        if (viewType == LIST_ITEM){
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_items,parent,false);
        }else{
            itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.items,parent,false);
        }
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        Glide.with(holder.itemView)
                .applyDefaultRequestOptions(new RequestOptions().placeholder(R.drawable.dev))
                .load(data.get(position).getImg())
                .into(holder.thumb);
        if (isSwitchView){
            holder.artist.setText(data.get(position).getArtists());
            holder.title.setText(data.get(position).getTitle());
        }
        holder.itemView.setOnClickListener(view -> showDialog(holder.itemView.getContext(),data.get(position)));
    }

    @Override
    public int getItemViewType (int position) {
        if (isSwitchView){
            return LIST_ITEM;
        }else{
            return GRID_ITEM;
        }
    }

    public boolean toggleItemViewType () {
        isSwitchView = !isSwitchView;
        return isSwitchView;
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
        TextView title,artist;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            thumb = itemView.findViewById(R.id.thumb);
            if (isSwitchView){
                title = itemView.findViewById(R.id.title);
                artist = itemView.findViewById(R.id.artist);
            }
        }
    }
}
