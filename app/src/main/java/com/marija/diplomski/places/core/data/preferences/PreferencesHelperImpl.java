package com.marija.diplomski.places.core.data.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.marija.diplomski.places.core.domain.data.PreferencesHelper;

public class PreferencesHelperImpl implements PreferencesHelper {

    public static final String PREF_KEY_MAP_TYPE = "pref_key_map_type";

    private SharedPreferences preferences;
    private static PreferencesHelperImpl INSTANCE;

    private PreferencesHelperImpl(Context context) {
        preferences = PreferenceManager.getDefaultSharedPreferences(context);
    }

    public static PreferencesHelperImpl getInstance(Context context) {
        if(INSTANCE == null){
            INSTANCE = new PreferencesHelperImpl(context);
        }
        return INSTANCE;
    }

    @Override
    public String getMapType() {
        return preferences.getString(PREF_KEY_MAP_TYPE, "");
    }

}
