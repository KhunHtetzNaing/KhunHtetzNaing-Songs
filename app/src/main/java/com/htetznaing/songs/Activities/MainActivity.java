package com.htetznaing.songs.Activities;

import android.content.Context;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationMenuView;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.htetznaing.songs.API;
import com.htetznaing.songs.AppUpdater.AppUpdater;
import com.htetznaing.songs.R;
import com.htetznaing.songs.Utils.DownloadWithOther;
import com.htetznaing.songs.ui.home.SongsViewModel;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import io.github.inflationx.viewpump.ViewPumpContextWrapper;

public class MainActivity extends AppCompatActivity {
    BottomNavigationView navView;
    Typeface typeface;
    public static SongsViewModel songsViewModel;
    public static MainActivity instance;
    boolean douleClickToExit = false;
    AppUpdater appUpdater;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        instance = this;
        typeface = Typeface.createFromAsset(getAssets(),"Pyidaungsu-2.5.3_Regular.ttf");
        navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_songs, R.id.navigation_downloads, R.id.navigation_about)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);

        songsViewModel = new ViewModelProvider(this).get(SongsViewModel.class);
        appUpdater = new AppUpdater(this,"https://myappupdateserver.blogspot.com/2020/04/khun-htetz-naing-song.html");
        new API().load();
        applyBottomNavFont();
    }

    @Override
    protected void onResume() {
        super.onResume();
        appUpdater.check(false);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(ViewPumpContextWrapper.wrap(newBase));
    }

    private void applyBottomNavFont() {
        // The BottomNavigationView widget doesn't provide a native way to set the appearance of
        // the text views. So we have to hack in to the view hierarchy here.
        for (int i = 0; i < navView.getChildCount(); i++) {
            View child = navView.getChildAt(i);
            if (child instanceof BottomNavigationMenuView) {
                BottomNavigationMenuView menu = (BottomNavigationMenuView) child;
                for (int j = 0; j < menu.getChildCount(); j++) {
                    View item = menu.getChildAt(j);
                    View smallItemText = item.findViewById(R.id.smallLabel);
                    if (smallItemText instanceof TextView) {
                        // Set font here
                        ((TextView) smallItemText).setTypeface(typeface);
                    }
                    View largeItemText = item.findViewById(R.id.largeLabel);
                    if (largeItemText instanceof TextView) {
                        // Set font here
                        ((TextView) largeItemText).setTypeface(typeface);
                    }
                }
            }
        }
    }

    @Override
    public void onBackPressed() {
        if (douleClickToExit) {
            super.onBackPressed();
        }
        douleClickToExit = true;
        Toast.makeText(instance, "ထွက်ရန်နောက်တစ်ကြိမ်ထပ်နှိပ်ပါ။", Toast.LENGTH_SHORT).show();
        new Handler().postDelayed(() -> douleClickToExit = false,2000);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main,menu);
        menu.findItem(R.id.dlOther).setChecked(DownloadWithOther.get());
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.dlOther:
                if (item.isChecked()){
                    item.setChecked(false);
                    DownloadWithOther.set(false);
                }else {
                    item.setChecked(true);
                    DownloadWithOther.set(true);
                }

                break;
            case R.id.checkUpdate:
                appUpdater.check(true);
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}
