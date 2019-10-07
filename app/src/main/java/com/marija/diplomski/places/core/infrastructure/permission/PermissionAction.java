package com.marija.diplomski.places.core.infrastructure.permission;

import android.Manifest;
import android.support.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class PermissionAction {

    @IntDef({CODE_GET_LOCATION})
    @Retention(RetentionPolicy.SOURCE)
    public @interface PermissionActionCode {}

    public static final int CODE_GET_LOCATION = 0;

    private int actionCode;
    private String permission;

    public static final PermissionAction GET_LOCATION = new PermissionAction(CODE_GET_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION);

    private PermissionAction(@PermissionActionCode int actionCode, String permission) {
        this.actionCode = actionCode;
        this.permission = permission;
    }

    @PermissionActionCode
    public int getActionCode() {
        return actionCode;
    }

    public String getPermission() {
        return permission;
    }
}
