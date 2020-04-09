package com.htetznaing.songs;

import android.os.AsyncTask;

import com.google.gson.Gson;
import com.htetznaing.songs.Activities.MainActivity;
import com.htetznaing.songs.Model.Songs;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;
import java.util.Arrays;

public class API {
    public void load(){
        new AsyncTask<Void,Void,String>() {
            @Override
            protected String doInBackground(Void... voids) {
                String json_url = "https://myappupdateserver.blogspot.com/p/my-song-api.html";
                String s = null;
                try {
                    Document document = Jsoup.connect(json_url).get();
                    if (document.hasClass("post-body entry-content")) {
                        //Blogspot Link
                        try {
                            s = document.getElementsByClass("post-body entry-content").get(0).text();
                        } catch (Exception e) {
                            s = document.getElementsByTag("body").get(0).text();
                        }
                    } else {
                        s = document.text();
                    }
                    assert s != null;
                    int start = s.indexOf("[");
                    int end = s.lastIndexOf("]") + 1;
                    s = s.substring(start, end);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return s;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
                if (s!=null){
                    MainActivity.songsViewModel.getData().setValue(Arrays.asList(new Gson().fromJson(s, Songs[].class)));
                }
            }
        }.execute();
    }
}
