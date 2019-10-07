package com.marija.diplomski.places.core.infrastructure.network.exception;

import android.content.Context;

import com.marija.diplomski.places.R;
import com.marija.diplomski.places.core.data.network.exception.NetworkConnectionException;

public class NetworkExceptionFactory {

    private NetworkExceptionFactory() {
    }

    public static String create(Context context, Exception exception) {
        String message = context.getString(R.string.exception_message_general);
        if (exception instanceof NetworkConnectionException) {
            message = context.getString(R.string.exception_message_no_connection);
        }
        return message;
    }
}
