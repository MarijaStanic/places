package com.marija.diplomski.places;

import android.support.multidex.MultiDexApplication;

import com.marija.diplomski.places.core.data.Database;
import com.squareup.leakcanary.LeakCanary;

public class PlacesApplication extends MultiDexApplication {

    @Override public void onCreate() {
        super.onCreate();

        new Database(getApplicationContext()).open();

        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        LeakCanary.install(this);
        // Normal app init code...
    }
}
