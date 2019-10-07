package com.marija.diplomski.places.keywords.views;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Gravity;
import android.view.View;
import android.widget.ImageView;
import android.widget.SearchView;

import com.marija.diplomski.places.R;
import com.marija.diplomski.places.base.LifecycleDelegate;
import com.marija.diplomski.places.base.views.BaseActivity;
import com.marija.diplomski.places.core.domain.model.Keyword;
import com.marija.diplomski.places.core.keywords.listener.DialogListener;
import com.marija.diplomski.places.core.keywords.NavigationDrawerContract;
import com.marija.diplomski.places.core.mymap.PlacesInFolderPresenter;
import com.marija.diplomski.places.folders.FoldersActivity;
import com.marija.diplomski.places.keywords.adapter.KeywordContentViewHolder;
import com.marija.diplomski.places.keywords.adapter.KeywordsAdapter;
import com.marija.diplomski.places.core.utils.ActivityUtil;

import java.util.Comparator;
import java.util.List;

import butterknife.ButterKnife;

public class NavigationDrawerViewExtension implements
        NavigationDrawerContract.NavigationDrawerViewExtensionContract, LifecycleDelegate {

    public static final int REQUEST_FOLDER = 0;
    public static final String EXTRA_FOLDER_ID = "EXTRA_FOLDER_ID";

    private BaseActivity activity;
    private DrawerLayout drawerLayout;
    private NavigationView nvKeywords;
    private ActionBarDrawerToggle drawerToggle;
    private SearchView svKeyword;
    private Toolbar toolbar;
    private ImageView searchButtonImage;

    private KeywordsAdapter keywordsAdapter;
    private RecyclerView rvKeywords;

    private static final Comparator<Keyword> ALPHABETICAL_COMPARATOR = new Comparator<Keyword>() {
        @Override
        public int compare(Keyword o1, Keyword o2) {
            return o1.getTitle().compareToIgnoreCase(o2.getTitle());
        }
    };

    private NavigationDrawerContract.NavigationDrawerViewExtensionDelegate eventsDelegate;

    public NavigationDrawerViewExtension(BaseActivity activity) {
        this.activity = activity;
    }

    public void setViews(DrawerLayout drawerLayout, NavigationView navigationView, Toolbar toolbar, RecyclerView rvKeywords) {
        this.drawerLayout = drawerLayout;
        this.nvKeywords = navigationView;
        this.toolbar = toolbar;
        this.rvKeywords = rvKeywords;
        createDrawerToggle();
        setupRecyclerView();
        setupSearchView();
    }

    private void createDrawerToggle() {
        drawerToggle = new ActionBarDrawerToggle(
                activity, drawerLayout, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(drawerToggle);
    }

    private void setupRecyclerView() {
        keywordsAdapter = new KeywordsAdapter(ALPHABETICAL_COMPARATOR, keywordListener);
        keywordsAdapter.setHasHeader(true);
        final GridLayoutManager gridLayoutManager = new GridLayoutManager(activity, 3);
        gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
            @Override
            public int getSpanSize(int position) {
                if (position == 0) {
                    return 3;
                }
                return 1;
            }
        });
        rvKeywords.setLayoutManager(gridLayoutManager);
        rvKeywords.setAdapter(keywordsAdapter);
    }

    private KeywordContentViewHolder.KeywordItemListener keywordListener = new KeywordContentViewHolder.KeywordItemListener() {
        @Override
        public void onKeywordClick(Keyword keyword) {
            drawerLayout.closeDrawer(Gravity.LEFT);
            eventsDelegate.openPlacesMapForKeyword(keyword);
        }

        @Override
        public void onDeleteKeywordLongClick(Keyword keyword) {
            eventsDelegate.deleteKeyword(keyword);
        }

        @Override
        public void onMyMapClick() {
            if (ActivityUtil.isTablet(activity)) {
                eventsDelegate.openMyMapForTablet();
                return;
            }
            Intent intent = new Intent(activity, FoldersActivity.class);
            activity.startActivityForResult(intent, REQUEST_FOLDER);
        }
    };

    private void setupSearchView() {
        View headerView = nvKeywords.getHeaderView(0);
        svKeyword = ButterKnife.findById(headerView, R.id.sv_keyword);
        svKeyword.setOnQueryTextListener(queryTextListener);
        svKeyword.setSubmitButtonEnabled(true);
        int searchGoButtonId = activity.getResources().getIdentifier("android:id/search_go_btn", null, null);
        searchButtonImage = ButterKnife.findById(svKeyword, searchGoButtonId);
        setSearchButtonImage(true);
    }

    @Override
    public void setSearchButtonImage(boolean set) {
        if (set) {
            searchButtonImage.setImageResource(R.drawable.ic_right_arrow);
        } else {
            searchButtonImage.setImageResource(R.drawable.ic_add);
        }
    }

    private SearchView.OnQueryTextListener queryTextListener = new SearchView.OnQueryTextListener() {
        @Override
        public boolean onQueryTextSubmit(String query) {
            eventsDelegate.checkSubmittedKeywordTitleQuery(query);
            drawerLayout.closeDrawer(Gravity.LEFT);
            return false;
        }

        @Override
        public boolean onQueryTextChange(String newText) {
            if (svKeyword.getWidth() > 0) {
                eventsDelegate.filterKeywordsForQueriedTitle(newText);
            }
            return false;
        }
    };

    @Override
    public void showKeywords(List<Keyword> keywords) {
        keywordsAdapter.replaceAll(keywords);
    }

    @Override
    public void showAlertDialog(String title, String message, String positiveButton, DialogListener listener) {
        activity.showAlertDialog(title, message, positiveButton, listener);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case REQUEST_FOLDER: {
                if (resultCode == activity.RESULT_OK) {
                    eventsDelegate.openMyMapForFolder(data.getIntExtra(EXTRA_FOLDER_ID, PlacesInFolderPresenter.LOAD_ALL_PLACES));
                    drawerLayout.closeDrawer(Gravity.LEFT);
                }
            }
        }
    }

    @Override
    public void showUpdatedAdapter(Keyword keyword) {
        keywordsAdapter.remove(keyword);
    }

    @Override
    public void onPostCreate(Bundle savedInstanceState) {
        drawerToggle.syncState(); // Sync the toggle state after onRestoreInstanceState has occurred
    }

    @Override
    public void setEventsDelegate(
            NavigationDrawerContract.NavigationDrawerViewExtensionDelegate eventsDelegate) {
        this.eventsDelegate = eventsDelegate;
    }
}
