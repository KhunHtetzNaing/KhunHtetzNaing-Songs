package com.htetznaing.songs.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.gson.Gson;
import com.htetznaing.songs.API;
import com.htetznaing.songs.Activities.MainActivity;
import com.htetznaing.songs.Adapter.SongsAdapter;
import com.htetznaing.songs.Constants;
import com.htetznaing.songs.Model.Songs;
import com.htetznaing.songs.R;
import com.htetznaing.songs.Utils.CheckInternet;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SongsFragment extends Fragment {
    public static SongsAdapter adapter;
    public static RecyclerView recyclerView;
    private List<Songs> data = new ArrayList<>();
    private TextView no_net;
    private SwipeRefreshLayout swipeRefresh;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_songs, container, false);
        swipeRefresh = root.findViewById(R.id.swipeRefresh);
        swipeRefresh.setRefreshing(true);
        no_net = root.findViewById(R.id.no_net);
        adapter = new SongsAdapter(getActivity(),data);
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager manager = new GridLayoutManager(getContext(),2);
        recyclerView.setLayoutManager(manager);
        recyclerView.setAdapter(adapter);

        if (new CheckInternet(getActivity()).isInternetOn(false)){
            no_net.setVisibility(View.VISIBLE);
            no_net.setText("သီချင်းများရယူနေပါသည်...");
        }

        final Observer<List<Songs>> live = temp -> {
            swipeRefresh.setRefreshing(false);
            if (temp != null) {
                data.clear();
                data.addAll(temp);
                adapter.notifyDataSetChanged();
                no_net.setVisibility(View.GONE);
            }else {
                no_net.setText(getString(R.string.no_internet));
                no_net.setVisibility(View.VISIBLE);
            }
        };
        if (MainActivity.instance!=null && MainActivity.songsViewModel!=null) {
            MainActivity.songsViewModel.getData().observe(MainActivity.instance, live);
        }

        swipeRefresh.setOnRefreshListener(() -> {
            if (new CheckInternet(getActivity()).isInternetOn(true)) {
                new API().load();
            }
        });
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        new API().load();
    }
}
