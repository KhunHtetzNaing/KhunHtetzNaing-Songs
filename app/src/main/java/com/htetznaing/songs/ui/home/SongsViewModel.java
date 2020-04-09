package com.htetznaing.songs.ui.home;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.htetznaing.songs.Model.Songs;

import java.util.List;

public class SongsViewModel extends ViewModel {
    private MutableLiveData<List<Songs>> data;

    public MutableLiveData<List<Songs>> getData() {
        if (data == null) {
            data = new MutableLiveData<>();
        }
        return data;
    }
}
