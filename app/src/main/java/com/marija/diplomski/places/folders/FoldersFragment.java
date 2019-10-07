package com.marija.diplomski.places.folders;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.view.ActionMode;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.marija.diplomski.places.R;
import com.marija.diplomski.places.addselectfolders.adapter.FolderViewHolder;
import com.marija.diplomski.places.base.views.BaseFragment;
import com.marija.diplomski.places.core.domain.model.Folder;
import com.marija.diplomski.places.core.folders.FoldersContract;
import com.marija.diplomski.places.addselectfolders.adapter.FoldersAdapter;
import com.marija.diplomski.places.keywords.views.MainActivity;
import com.marija.diplomski.places.keywords.views.NavigationDrawerViewExtension;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class FoldersFragment extends BaseFragment implements FolderViewHolder.FolderItemListener, FoldersContract.View {

    @BindView(R.id.rv_folders) RecyclerView rvFolders;
    @BindView(R.id.cv_folder) CardView cvFolder;

    private FoldersAdapter foldersAdapter;

    private FoldersContract.Presenter presenter;

    private ActionMode actionMode;

    public FoldersFragment() {
        // Required empty public constructor
    }

    public static FoldersFragment newInstance() {
        return new FoldersFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foldersAdapter = new FoldersAdapter(this, FolderViewHolder.LAYOUT_FOLDER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_folders, container, false);
        ButterKnife.bind(this, root);

        setupRecyclerView();

        return root;
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvFolders.setLayoutManager(linearLayoutManager);
        rvFolders.setAdapter(foldersAdapter);
    }

    @Override
    public void setPresenter(FoldersContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadFolders();
    }

    @Override
    public void showFoldersList(List<Folder> folders) {
        foldersAdapter.setList(folders);
    }

    @OnClick(R.id.cv_folder)
    public void onAllPlacesFolderClick() {
        presenter.openMyMapForAllPlaces();
    }

    @Override
    public void showMyMapUiForAllPlaces(){
        Intent intent = new Intent(getContext(), MainActivity.class);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onFolderClick(int folderId) {
        presenter.openMyMap(folderId);
    }

    @Override
    public void showMyMapUiForFolder(int folderId) {
        Intent intent = new Intent(getContext(), MainActivity.class);
        intent.putExtra(NavigationDrawerViewExtension.EXTRA_FOLDER_ID, folderId);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onFolderLongClick(int position) {
        if (actionMode == null) {
            actionMode = ((AppCompatActivity) getActivity()).startSupportActionMode(actionModeCallback);
        }
        toggleSelection(position);
    }

    private void toggleSelection(int position) {
        foldersAdapter.toggleSelection(position);
        int selectedItemCount = foldersAdapter.getSelectedItemCount();

        if (selectedItemCount == 0) {
            actionMode.finish();
        } else {
            actionMode.setTitle(String.valueOf(selectedItemCount));
            actionMode.invalidate();
        }
    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {

        // Called when the action mode is created; startActionMode() was called
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.cm_folders, menu);
            return true;
        }

        // Called each time the action mode is shown. Always called after onCreateActionMode, but
        // may be called multiple times if the mode is invalidated.
        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            MenuItem renameFolderItem = menu.findItem(R.id.menu_rename_folder);
            if(foldersAdapter.getSelectedItemCount() > 1) {
                renameFolderItem.setVisible(false);
            }else {
                renameFolderItem.setVisible(true);
            }
            return false;
        }

        // Called when the user selects a contextual menu item
        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            switch (item.getItemId()) {
                case R.id.menu_delete_folder:
                    presenter.deleteFolders(foldersAdapter.getSelectedItems());
                    mode.finish(); // Action picked, so close the CAB
                    foldersAdapter.clearSelection();
                    return true;
                case R.id.menu_rename_folder:
                    presenter.openRenameFolderDialog(foldersAdapter.getSelectedItems().get(0));
                    mode.finish(); // Action picked, so close the CAB
                    foldersAdapter.clearSelection();
                    return true;
                default:
                    return false;
            }
        }

        // Called when the user exits the action mode
        @Override
        public void onDestroyActionMode(ActionMode mode) {
            actionMode = null;
        }
    };


    @Override
    public void showRenameFolderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.dialog_title_rename_folder);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_folder_create, null);
        final EditText newFolderName = ButterKnife.findById(view, R.id.txt_new_folder_name);

        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.renameFolder(newFolderName.getText().toString());
                dialog.dismiss();
            }
        });
        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        builder.show();
    }
}