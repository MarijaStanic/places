package com.marija.diplomski.places.settings;

import android.os.Bundle;

import com.marija.diplomski.places.R;
import com.marija.diplomski.places.base.views.BaseActivity;

import butterknife.BindString;

public class PreferenceActivity extends BaseActivity {

    @BindString(R.string.menu_settings)
    String toolbarTitle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setupUpNavigation();
        getToolbar().setTitle("Settings");
        getFragmentManager().beginTransaction()
                .replace(R.id.content, new SettingsFragment())
                .commit();

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_preference;
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }
}
