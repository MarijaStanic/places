package com.marija.diplomski.places.core.infrastructure.permission;

import android.app.Activity;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

public class PermissionChecker {

    private Activity activity;
    private List<PermissionListener> permissionListeners;

    public PermissionChecker(Activity activity) {
        this.activity = activity;
        permissionListeners = new ArrayList<>();
    }

    public boolean hasSelfPermission(String permission) {
        return ContextCompat.checkSelfPermission(activity, permission) == PackageManager.PERMISSION_GRANTED;
    }

    // returns true if the app has requested this permission previously and the user denied the request
    public boolean shouldShowRequestPermissionRationale(String permission) {
        return ActivityCompat.shouldShowRequestPermissionRationale(activity, permission);
    }

    public void requestPermission(String permission, int requestCode) {
        ActivityCompat.requestPermissions(activity, new String[]{permission}, requestCode);
    }

    public void checkAndRequestPermission(PermissionAction action) {
        if (hasSelfPermission(action.getPermission())) {
            permissionListeners.forEach(permissionListener ->
                    permissionListener.permissionAccepted(action.getActionCode()));
        } else if (shouldShowRequestPermissionRationale(action.getPermission())) {
            permissionListeners.forEach(permissionListener ->
                    permissionListener.showRationale(action.getActionCode()));
        } else {
            requestPermission(action.getPermission(), action.getActionCode());
        }
    }

    public void checkGrantedPermission(int[] grantResults, int requestCode) {
        if (verifyGrantedPermission(grantResults)) {
            permissionListeners.forEach(permissionListener ->
                    permissionListener.permissionAccepted(requestCode));
        } else {
            permissionListeners.forEach(permissionListener ->
                    permissionListener.permissionDenied(requestCode));
        }
    }

    private boolean verifyGrantedPermission(int[] grantResults) {
        for (int result : grantResults) {
            if (result != PackageManager.PERMISSION_GRANTED) {
                return false;
            }
        }
        return true;
    }

    public void addPermissionListener(PermissionListener permissionListener) {
        this.permissionListeners.add(permissionListener);
    }

    public interface PermissionListener {
        void permissionAccepted(@PermissionAction.PermissionActionCode int actionCode);

        void permissionDenied(@PermissionAction.PermissionActionCode int actionCode);

        void showRationale(@PermissionAction.PermissionActionCode int actionCode);
    }

}
