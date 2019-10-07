package com.marija.diplomski.places.core.data.network.exception;

import java.io.IOException;

/**
 * Created by Marija on 2017-06-13.
 */

public class NetworkConnectionException extends IOException {

    public NetworkConnectionException(String s) {
        super(s);
    }

    public NetworkConnectionException() {

    }
}
