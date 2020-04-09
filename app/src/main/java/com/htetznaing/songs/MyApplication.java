package com.htetznaing.songs;

import android.app.Application;

import com.htetznaing.songs.Utils.DownloadWithOther;
import com.onesignal.OneSignal;

import io.github.inflationx.calligraphy3.CalligraphyConfig;
import io.github.inflationx.calligraphy3.CalligraphyInterceptor;
import io.github.inflationx.viewpump.ViewPump;

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();

        DownloadWithOther.init(this);
        ViewPump.init(ViewPump.builder()
                .addInterceptor(new CalligraphyInterceptor(
                        new CalligraphyConfig.Builder()
                                .setDefaultFontPath("Pyidaungsu-2.5.3_Regular.ttf")
                                .setFontAttrId(R.attr.fontPath)
                                .build()))
                .build());

        OneSignal.startInit(this)
                .inFocusDisplaying(OneSignal.OSInFocusDisplayOption.Notification)
                .unsubscribeWhenNotificationsAreDisabled(true)
                .init();
    }
}
