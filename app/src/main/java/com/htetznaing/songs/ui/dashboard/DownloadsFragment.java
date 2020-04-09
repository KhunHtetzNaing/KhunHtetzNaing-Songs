package com.htetznaing.songs.ui.dashboard;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.htetznaing.songs.Adapter.DownloadsListAdapter;
import com.htetznaing.songs.Adapter.SongsAdapter;
import com.htetznaing.songs.Constants;
import com.htetznaing.songs.Model.Songs;
import com.htetznaing.songs.R;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;

public class DownloadsFragment extends Fragment {
    TextView tvNoHave;
    ArrayList<File> files = new ArrayList<>();
    GridLayoutManager layoutManager;
    DownloadsListAdapter adapter;
    RecyclerView recyclerView;
    ProgressBar progrssBar;
    public static DownloadsFragment instance;
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_downloads, container, false);

        instance = this;
        tvNoHave = root.findViewById(R.id.tvNoHave);
        recyclerView = root.findViewById(R.id.recyclerView);
        progrssBar = root.findViewById(R.id.progrssBar);


        layoutManager =new GridLayoutManager(getActivity(), 1);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(layoutManager);


        adapter = new DownloadsListAdapter(getActivity(),files);

        recyclerView.setAdapter(adapter);
        return root;
    }

    @Override
    public void onStart() {
        super.onStart();
        loadRefresh();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==1000){
            if (grantResults.length > 0&& grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                loadRefresh();
            } else {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext())
                        .setTitle("အသိပေးချက်!")
                        .setMessage("ဆက်လက်လုပ်ဆောင်ရန် Allow နှိပ်ပြီးခွင့်ပြုပေးရန်လိုအပ်ပါသည်။")
                        .setPositiveButton("ဟုတ်ပြီ", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                checkPermissions();
                            }
                        })
                        .setNegativeButton("မလုပ်တော့ပါ",null);
                builder.show();
            }
        }
    }

    private boolean checkPermissions() {
        if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1000);
            return false;
        }return true;
    }

    public void loadRefresh(){
        if (checkPermissions()) {
            ArrayList<File> tempFiles = new ArrayList<>();
            String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/";
            File[] file = new File(path).listFiles();
            if (file != null) {
                Arrays.sort(file, new Comparator() {
                    public int compare(Object o1, Object o2) {
                        if (((File) o1).lastModified() > ((File) o2).lastModified()) {
                            return -1;
                        } else if (((File) o1).lastModified() < ((File) o2).lastModified()) {
                            return +1;
                        } else {
                            return 0;
                        }
                    }
                });

                for (File value : file) {
                    String temp = value.toString();
                    if (temp.endsWith(getString(R.string.id_for_check_file)+".mp4") || temp.endsWith(getString(R.string.id_for_check_file)+".mp3")) {
                        tempFiles.add(value);
                    }
                }
            }

            files.clear();
            files.addAll(tempFiles);
            adapter.notifyDataSetChanged();
            if (files.size() > 0) {
                tvNoHave.setVisibility(View.GONE);
            } else {
                tvNoHave.setVisibility(View.VISIBLE);
            }
            progrssBar.setVisibility(View.GONE);
        }
    }
}
