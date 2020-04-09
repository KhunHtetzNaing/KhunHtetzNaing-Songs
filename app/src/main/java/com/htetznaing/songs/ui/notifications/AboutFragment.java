package com.htetznaing.songs.ui.notifications;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.htetznaing.songs.Constants;
import com.htetznaing.songs.R;

public class AboutFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_about, container, false);
        setupClicks(root);
        return root;
    }

    private void openLink(String link){
        startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(link)));
    }

    private void setupClicks(View view) {
        view.findViewById(R.id.profile_image).setOnClickListener(view1 -> {
            Constants.openDevFB(getContext());
        });

        view.findViewById(R.id.fb).setOnClickListener(view12 -> {
            Constants.openDevFB(getContext());
        });

        view.findViewById(R.id.github).setOnClickListener(view13 -> {
            openLink("https://github.com/KhunHtetzNaing/mmCovid19");
        });

        view.findViewById(R.id.youtube).setOnClickListener(view14 -> {
            openLink("https://www.youtube.com/c/KhunHtetzNaingX");
        });

        view.findViewById(R.id.web).setOnClickListener(view15 -> {
            openLink("https://khunhtetznaing.com");
        });

        view.findViewById(R.id.app_info).setOnClickListener(view16 -> {
            Constants.openDevFB(getContext());
        });

        view.findViewById(R.id.desc).setOnClickListener(view17 -> {
            Constants.openDevFbPage(getContext());
        });

        view.findViewById(R.id.fbpage).setOnClickListener(view19 ->{
            Constants.openDevFbPage(getContext());
        });
    }
}
