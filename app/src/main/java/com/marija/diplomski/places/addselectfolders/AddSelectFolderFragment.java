package com.marija.diplomski.places.addselectfolders;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.marija.diplomski.places.R;
import com.marija.diplomski.places.addselectfolders.adapter.FolderViewHolder;
import com.marija.diplomski.places.addeditplace.views.AddEditPlaceActivity;
import com.marija.diplomski.places.addeditplace.views.AddEditPlaceFragment;
import com.marija.diplomski.places.base.views.BaseFragment;
import com.marija.diplomski.places.core.addeditfolder.AddEditFolderContract;
import com.marija.diplomski.places.core.domain.model.Folder;
import com.marija.diplomski.places.addselectfolders.adapter.FoldersAdapter;

import static com.marija.diplomski.places.core.utils.ActivityUtil.isTablet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddSelectFolderFragment extends BaseFragment implements AddEditFolderContract.View, FolderViewHolder.FolderItemListener {

    @BindView(R.id.btn_create_folder)
    Button btnCreateFolder;
    @BindView(R.id.rv_folders)
    RecyclerView rvFolders;

    private AddEditFolderContract.Presenter presenter;

    private FoldersAdapter foldersAdapter;
    private List<Folder> folders;

    public AddSelectFolderFragment() {
    }

    public static AddSelectFolderFragment newInstance() {
        return new AddSelectFolderFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        foldersAdapter = new FoldersAdapter(this, FolderViewHolder.LAYOUT_CHOOSE_FOLDER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_edit_folder, container, false);
        ButterKnife.bind(this, root);

        setupRecyclerView();

        showOptionsMenuIfNeeded();

        return root;
    }

    private void setupRecyclerView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvFolders.setLayoutManager(linearLayoutManager);
        rvFolders.setAdapter(foldersAdapter);
    }

    private void showOptionsMenuIfNeeded() {
        if (!isTablet(getActivity())) {
            setHasOptionsMenu(true);
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.om_add_edit_place, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_confirm:
                presenter.openAddEditPlace();
                break;
        }
        return false;
    }

    @OnClick(R.id.btn_create_folder)
    void onButtonCreateFolderClick() {
        presenter.openAddFolder();
    }

    @Override
    public void setPresenter(AddEditFolderContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showAddFolderDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle(R.string.dialog_title_new_folder);

        View view = LayoutInflater.from(getContext()).inflate(R.layout.dialog_folder_create, null);

        final EditText input = ButterKnife.findById(view, R.id.txt_new_folder_name);

        builder.setView(view);

        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                presenter.addFolder(input.getText().toString());
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

    @Override
    public void showFoldersList(List<Folder> folders) {
        this.folders = folders;
        foldersAdapter.setList(folders);
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadFolders();
    }

    @Override
    public void showAddEditPlaceUi(List<Folder> folders) {
        Intent intent = new Intent(getContext(), AddEditPlaceActivity.class);
        intent.putExtra(AddEditPlaceFragment.EXTRA_SELECTED_FOLDERS, (ArrayList) folders);
        getActivity().setResult(Activity.RESULT_OK, intent);
        getActivity().finish();
    }

    @Override
    public void onFolderClick(int folderId) {
        presenter.markClickedFolder(folderId);
    }

    @Override
    public void onFolderLongClick(int position) {

    }
}
