package com.marija.diplomski.places.core.data.network;


import com.marija.diplomski.places.core.data.network.exception.NetworkConnectionException;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class ConnectivityInterceptor implements Interceptor {

    private NetworkConnectionChecker networkConnectionChecker;

    public ConnectivityInterceptor(NetworkConnectionChecker networkConnectionChecker) {
        this.networkConnectionChecker = networkConnectionChecker;
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        if(!networkConnectionChecker.isConnectedToInternet()){
          throw new NetworkConnectionException();
        }
        Request request = chain.request();
        return chain.proceed(request);
    }


}
