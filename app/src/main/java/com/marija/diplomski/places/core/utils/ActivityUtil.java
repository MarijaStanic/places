package com.marija.diplomski.places.core.utils;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.view.View;

import com.marija.diplomski.places.BuildConfig;
import com.marija.diplomski.places.R;

import butterknife.ButterKnife;

public final class ActivityUtil {

    private ActivityUtil() {
    }

    public static boolean isTablet(Context context) {
        return context.getResources().getBoolean(R.bool.isTablet);
    }

    public static void showSnackBarPermissionMessage(final Activity activity, int message) {
        CoordinatorLayout coordinatorLayout = ButterKnife.findById(activity, R.id.coordinator_layout);
        Snackbar snackbar = Snackbar.make(coordinatorLayout, activity.getString(message), Snackbar.LENGTH_LONG)
                .setAction(activity.getString(R.string.snack_bar_settings), new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(android.provider.Settings
                                .ACTION_APPLICATION_DETAILS_SETTINGS);
                        intent.setData(Uri.parse("package:" + BuildConfig.APPLICATION_ID));
                        activity.startActivity(intent);
                    }
                });
        snackbar.show();
    }

}
