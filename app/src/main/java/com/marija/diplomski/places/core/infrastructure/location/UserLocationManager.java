package com.marija.diplomski.places.core.infrastructure.location;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.marija.diplomski.places.R;
import com.marija.diplomski.places.base.LifecycleDelegate;
import com.marija.diplomski.places.core.infrastructure.permission.PermissionAction;
import com.marija.diplomski.places.core.infrastructure.permission.PermissionChecker;
import com.marija.diplomski.places.core.domain.infrastructure.LocationService;
import com.marija.diplomski.places.core.utils.ActivityUtil;

public class UserLocationManager implements LocationService, LifecycleDelegate {

    private static final String TAG = UserLocationManager.class.getSimpleName();

    // Keys for storing activity state in the Bundle.
    private final static String KEY_REQUESTING_LOCATION_UPDATES = "requestingLocationUpdates";
    private final static String KEY_LOCATION = "location";

    private static final int REQUEST_CHECK_SETTINGS = 0x1;

    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    private Activity activity;
    private PermissionChecker permissionChecker;

    private static UserLocationManager INSTANCE;
    private Location currentLocation;

    private FusedLocationProviderClient fusedLocationClient;
    private SettingsClient settingsClient;
    private LocationCallback locationCallback;
    private LocationSettingsRequest locationSettingsRequest;
    private LocationRequest locationRequest;
    private boolean requestingLocationUpdates = true;

    private UserLocationManager(Activity activity, PermissionChecker permissionChecker) {
        this.activity = activity;
        this.permissionChecker = permissionChecker;
        permissionChecker.addPermissionListener(permissionListener);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(activity);
        settingsClient = LocationServices.getSettingsClient(activity);
        createLocationCallback();
        createLocationRequest();
        buildLocationSettingsRequest();
    }

    public static UserLocationManager getInstance(Activity activity, PermissionChecker permissionChecker) {
        if (INSTANCE == null) {
            INSTANCE = new UserLocationManager(activity, permissionChecker);
        }
        return INSTANCE;
    }

    /**
     * Creates a callback for receiving location events.
     */
    private void createLocationCallback() {
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                currentLocation = locationResult.getLastLocation();
                Log.i(TAG, currentLocation.getLatitude() + "");
            }
        };
    }

    private void createLocationRequest() {
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(UPDATE_INTERVAL_IN_MILLISECONDS)
                .setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
    }

    /**
     * Uses a {@link com.google.android.gms.location.LocationSettingsRequest.Builder} to build
     * a {@link com.google.android.gms.location.LocationSettingsRequest} that is used for checking
     * if a device has the needed location settings.
     */
    private void buildLocationSettingsRequest() {
        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
        builder.addLocationRequest(locationRequest);
        locationSettingsRequest = builder.build();
    }

    @Override
    public void onRestoreInstanceState(Bundle savedInstanceState) {
        if (savedInstanceState.keySet().contains(KEY_REQUESTING_LOCATION_UPDATES)) {
            requestingLocationUpdates = savedInstanceState.getBoolean(
                    KEY_REQUESTING_LOCATION_UPDATES);
        }
        if (savedInstanceState.keySet().contains(KEY_LOCATION)) {
            currentLocation = savedInstanceState.getParcelable(KEY_LOCATION);
        }

    }

    public void startLocationUpdates() {
        settingsClient.checkLocationSettings(locationSettingsRequest)
                .addOnSuccessListener(activity, new OnSuccessListener<LocationSettingsResponse>() {
                    @Override
                    public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                        Log.i(TAG, "All location settings are satisfied.");
                        //noinspection MissingPermission
                        fusedLocationClient.requestLocationUpdates(locationRequest,
                                locationCallback, Looper.myLooper());
                    }
                }).addOnFailureListener(activity, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                int statusCode = ((ApiException) e).getStatusCode();
                switch (statusCode) {
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        Log.i(TAG, "Location settings are not satisfied. Attempting to upgrade " +
                                "location settings ");
                        try {
                            // Show the dialog by calling startResolutionForResult(), and check the
                            // result in onActivityResult().
                            ResolvableApiException resolvable = (ResolvableApiException) e;
                            resolvable.startResolutionForResult(activity, REQUEST_CHECK_SETTINGS);
                        } catch (IntentSender.SendIntentException sie) {
                            Log.i(TAG, "PendingIntent unable to execute request.");
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        String errorMessage = "Location settings are inadequate, and cannot be " +
                                "fixed here. Fix in Settings.";
                        Log.e(TAG, errorMessage);
                        Toast.makeText(activity, errorMessage, Toast.LENGTH_LONG).show();
                        requestingLocationUpdates = false;
                }
            }
        });
    }

    private void stopLocationUpdates() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    PermissionChecker.PermissionListener permissionListener = new PermissionChecker.PermissionListener() {
        @Override
        public void permissionAccepted(@PermissionAction.PermissionActionCode int actionCode) {
            if (actionCode == PermissionAction.CODE_GET_LOCATION) {
                startLocationUpdates();
            }
            Log.i(TAG, "permissionAccepted");
        }

        @Override
        public void permissionDenied(@PermissionAction.PermissionActionCode int actionCode) {
            requestingLocationUpdates = false;
        }

        @Override
        public void showRationale(@PermissionAction.PermissionActionCode int actionCode) {
            Log.i(TAG, "showRationale");
            com.marija.diplomski.places.core.utils.ActivityUtil.showSnackBarPermissionMessage(activity, R.string.snack_bar_get_location);
        }
    };

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult()");
        switch (requestCode) {
            // Check for the integer request code originally supplied to startResolutionForResult().
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Log.i(TAG, "User agreed to make required location settings changes.");
                        // Nothing to do. startLocationupdates() gets called in onResume again.
                        break;
                    case Activity.RESULT_CANCELED:
                        Log.i(TAG, "User chose not to make required location settings changes.");
                        requestingLocationUpdates = false;
                        break;
                }
                break;
        }
    }

    @Override
    public void onResume() {
        if (requestingLocationUpdates)
            checkPermissions();
    }

    private void checkPermissions() {
        permissionChecker.checkAndRequestPermission(PermissionAction.GET_LOCATION);
    }

    @Override
    public void onPause() {
        if (requestingLocationUpdates)
            stopLocationUpdates();
        Log.i(TAG, "onPause" + requestingLocationUpdates);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(KEY_REQUESTING_LOCATION_UPDATES, requestingLocationUpdates);
        outState.putParcelable(KEY_LOCATION, currentLocation);
    }

    @Override
    public void onDestroy() {
        INSTANCE = null;
        activity = null;
        permissionChecker = null;
    }

    public double getLongitude() {
        if (currentLocation != null)
            return currentLocation.getLongitude();
        return 0.0;
    }

    public double getLatitude() {
        if (currentLocation != null)
            return currentLocation.getLatitude();
        return 0.0;
    }

}
