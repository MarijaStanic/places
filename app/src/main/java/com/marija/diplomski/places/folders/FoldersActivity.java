package com.marija.diplomski.places.folders;

import android.os.Bundle;

import com.marija.diplomski.places.base.views.BaseActivity;
import com.marija.diplomski.places.R;
import com.marija.diplomski.places.core.infrastructure.UseCaseThreadPoolScheduler;
import com.marija.diplomski.places.core.domain.usecase.UseCaseHandler;
import com.marija.diplomski.places.core.addeditfolder.usecase.GetFolders;
import com.marija.diplomski.places.core.folders.FoldersPresenter;
import com.marija.diplomski.places.core.folders.usecase.DeleteFolder;
import com.marija.diplomski.places.core.folders.usecase.UpdateFolder;
import com.marija.diplomski.places.core.data.Database;

public class FoldersActivity extends BaseActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setupUpNavigation();
        getToolbar().setTitle(R.string.toolbar_title_folders);

        FoldersFragment foldersFragment = (FoldersFragment) getSupportFragmentManager().findFragmentById(R.id.content);

        if (foldersFragment == null) {
            foldersFragment = FoldersFragment.newInstance();
            addFragmentToActivity(foldersFragment, R.id.content);
        }

        new FoldersPresenter(UseCaseHandler.getInstance(UseCaseThreadPoolScheduler.getInstance()), foldersFragment, new GetFolders(Database.folderDao),
                new UpdateFolder(Database.folderDao), new DeleteFolder(Database.folderDao));

    }

    @Override
    protected int getLayoutId() {
        return R.layout.activity_folders;
    }

}
