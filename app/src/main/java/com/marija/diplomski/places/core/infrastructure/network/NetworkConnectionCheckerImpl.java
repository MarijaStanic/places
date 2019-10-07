package com.marija.diplomski.places.core.infrastructure.network;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.marija.diplomski.places.core.data.network.NetworkConnectionChecker;

public class NetworkConnectionCheckerImpl implements NetworkConnectionChecker {

    private Context context;

    public NetworkConnectionCheckerImpl(Context context) {
        this.context = context;
    }

    public boolean isConnectedToInternet() {
        ConnectivityManager cm =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null && activeNetwork.isConnectedOrConnecting();
    }
}
