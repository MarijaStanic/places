package com.marija.diplomski.places.base.views;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;

import com.marija.diplomski.places.R;
import com.marija.diplomski.places.base.LifecycleDelegate;
import com.marija.diplomski.places.core.base.view.UiContracts;
import com.marija.diplomski.places.core.keywords.listener.DialogListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseActivity extends AppCompatActivity implements UiContracts.MvpView {

    private Toolbar toolbar;

    private List<LifecycleDelegate> lifecycleDelegates;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        lifecycleDelegates = new ArrayList<>();
        setContentView(getLayoutId());
        setupToolbar();
        bindViews();
        initView();
    }

    protected abstract int getLayoutId();

    public void setupToolbar() {
        toolbar = ButterKnife.findById(this, R.id.toolbar);
        if (toolbar != null) {
            toolbar.setTitle("");
            setSupportActionBar(toolbar);
        }
    }

    private void bindViews() {
        ButterKnife.bind(this);
    }

    public void initView() {
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        for (LifecycleDelegate delegate : lifecycleDelegates) {
            delegate.onActivityResult(requestCode, resultCode, data);
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        for (LifecycleDelegate delegate : lifecycleDelegates) {
            delegate.onStart();
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        for (LifecycleDelegate delegate : lifecycleDelegates) {
            delegate.onRestoreInstanceState(savedInstanceState);
        }
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        for (LifecycleDelegate delegate : lifecycleDelegates) {
            delegate.onPostCreate(savedInstanceState);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        for (LifecycleDelegate delegate : lifecycleDelegates) {
            delegate.onResume();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        for (LifecycleDelegate delegate : lifecycleDelegates) {
            delegate.onPause();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        for (LifecycleDelegate delegate : lifecycleDelegates) {
            delegate.onSaveInstanceState(outState);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        for (LifecycleDelegate delegate : lifecycleDelegates) {
            delegate.onStop();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        for (LifecycleDelegate delegate : lifecycleDelegates) {
            delegate.onDestroy();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        for (LifecycleDelegate delegate : lifecycleDelegates) {
            delegate.onBackPressed();
        }
    }

    public void setupUpNavigation() {
        ActionBar ab = getSupportActionBar();
        ab.setDisplayHomeAsUpEnabled(true);
        ab.setTitle("");
        ab.setHomeAsUpIndicator(R.drawable.ic_left_arrow);
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    public void addMainLifecycleDelegate(LifecycleDelegate lifecycleDelegate) {
        this.lifecycleDelegates.add(lifecycleDelegate);
    }

    public Toolbar getToolbar() {
        return toolbar;
    }

    @Override
    public void showProgress(String message) {

    }

    @Override
    public void dismissProgress() {

    }

    protected void addFragmentToActivity(Fragment fragment, int containerViewId) {
        getSupportFragmentManager().beginTransaction()
                .replace(containerViewId, fragment)
                .commit();
    }

    public void showMessage(String message) {
        Snackbar.make(getToolbar(), message, Snackbar.LENGTH_LONG).show();
    }

    public void showAlertDialog(String title, String message, String positiveButton, final DialogListener listener) {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setTitle(title).setMessage(message);
        dialog.setPositiveButton(positiveButton, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.confirm();
                dialogInterface.dismiss();
            }
        }).setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                listener.cancel();
                dialogInterface.dismiss();
            }
        }).show();
    }


}
