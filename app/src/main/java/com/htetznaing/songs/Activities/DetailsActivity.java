package com.htetznaing.songs.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.htetznaing.songs.Constants;
import com.htetznaing.songs.R;
import com.htetznaing.songs.Model.Songs;
import com.htetznaing.songs.Utils.DownloadWithOther;
import com.htetznaing.songs.Utils.XDownloader;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class DetailsActivity extends AppCompatActivity {
    YouTubePlayerView youTubePlayerView;
    TextView info;
    Songs data = null;
    XDownloader xDownloader;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        if (getSupportActionBar()!=null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }
        data = (Songs) getIntent().getSerializableExtra("data");
        if (data==null){
            finish();
        }
        setTitle(data.getTitle());
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTubePlayer) {
                String videoId = data.getYoutube();
                youTubePlayer.loadVideo(videoId, 0);
            }
        });

        info = findViewById(R.id.info);
        info.setText(data.getInfo());
        xDownloader = new XDownloader(this);
        xDownloader.OnDownloadFinishedListerner(path -> {

        });
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    public void downloadMP4(View view) {
        letDownload(getFileName(false),data.getVideo(),"MP4");
    }

    public void open(String url,String title) {
        try {
            Intent intent = new Intent();
            intent.setAction(Intent.ACTION_VIEW);
            intent.setDataAndType(Uri.parse(URLDecoder.decode(url, "UTF-8")), url.contains(".mp3") ? "audio/mpeg" : "video/mp4");
            startActivity(Intent.createChooser(intent, title));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

    private void letDownload(final String file, final String url, String type){
        if (new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS)+"/"+file).exists()){
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("သတိပြုရန်!")
                    .setMessage("ယခုသီချင်းသည်သင့်ဖုန်းအတွင်း\nရှိပြီးသားဟုခန့်မှန်းရပါသည်။")
                    .setPositiveButton("ထပ်ဒေါင်းမယ်", (dialogInterface, i) -> {
                        xDownloader.download(file, url);
                    })
                    .setNegativeButton("မဒေါင်းတော့ပါ", null);;
            if (DownloadWithOther.get()) {
                builder.setNegativeButton("အခြား App များဖြင့်ဒေါင်းမည်", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        open(url, file);
                    }
                });
                builder.setNeutralButton("မဒေါင်းတော့ပါ", null);
            }

            builder.show();
        }else {
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle(data.getTitle())
                    .setMessage(type+" ကိုဒေါင်းမှာလား ?")
                    .setPositiveButton("ဒေါင်းမည်", (dialogInterface, i) -> {
                        xDownloader.download(file, url);
                    })
                    .setNegativeButton("မဒေါင်းပါ",null);
            if (DownloadWithOther.get()) {
                builder.setNegativeButton("အခြား App များဖြင့်ဒေါင်းမည်", (dialogInterface, i) -> open(url, file));
                builder.setNeutralButton("မဒေါင်းပါ", null);
            }
            builder.show();
        }
    }

    private String getFileName(boolean isMP3){
        if (isMP3){
            return FilenameUtils.getName(data.getAudio()).replace(".mp3",getString(R.string.id_for_check_file)+".mp3");
        }
        return FilenameUtils.getName(data.getVideo()).replace(".mp4",getString(R.string.id_for_check_file)+".mp4");
    }

    public void downloadMP3(View view) {
        letDownload(getFileName(true),data.getAudio(),"MP3");
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.details,menu);
        menu.findItem(R.id.joox).setVisible(false);
        if (data.getJoox()!=null) {
            if (data.getJoox().length() > 1) {
                menu.findItem(R.id.joox).setVisible(true);
            }
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId()==R.id.share){
            share();
        }else if (item.getItemId()==R.id.joox){
            startActivity(new Intent(Intent.ACTION_VIEW).setData(Uri.parse(data.getJoox())));
        }
        return super.onOptionsItemSelected(item);
    }

    public void openDeveloperFB(View view) {
        Constants.openDevFB(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    private void share(){
        String shareBody = data.getTitle()+"\n"+data.getInfo()+"\n\nYoutube \uD83D\uDC49\uD83D\uDC49 https://youtu.be/"+data.getYoutube()+" \uD83D\uDC48\uD83D\uDC48\n\nStream/Download free in Khun Htetz Naing Song App => https://play.google.com/store/apps/details?id="+getPackageName()+"\n\n#KhunHtetzNaing #HtetzNaing";
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, data.getTitle());
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
        startActivity(Intent.createChooser(sharingIntent, "Share with.."));
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
