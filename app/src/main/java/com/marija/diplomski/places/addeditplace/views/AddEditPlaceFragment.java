package com.marija.diplomski.places.addeditplace.views;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.marija.diplomski.places.R;
import com.marija.diplomski.places.addselectfolders.AddSelectFolderActivity;
import com.marija.diplomski.places.base.views.BaseFragment;
import com.marija.diplomski.places.core.addeditplace.AddEditPlaceContract;
import com.marija.diplomski.places.core.domain.model.Folder;
import static com.marija.diplomski.places.core.utils.ActivityUtil.isTablet;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddEditPlaceFragment extends BaseFragment implements AddEditPlaceContract.View {

    public static final String EXTRA_SELECTED_FOLDERS = "EXTRA_SELECTED_FOLDERS";
    public static final int REQUEST_ADD_FOLDER = 1;

    @BindView(R.id.txt_place_description)
    TextView txtPlaceDescription;
    @BindView(R.id.txt_add_to_folder)
    TextView txtAddToFolder;
    @BindView(R.id.ll_place_folders)
    ViewGroup llPlaceFolders;

    private AddEditPlaceContract.Presenter presenter;

    private List<Folder> folders;

    public static AddEditPlaceFragment newInstance() {
        return new AddEditPlaceFragment();
    }

    public AddEditPlaceFragment() {
        folders = new ArrayList<>();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_add_edit_place, container, false);
        ButterKnife.bind(this, root);

        if(!isTablet(getContext())){
            llPlaceFolders.setVisibility(View.VISIBLE);
        }

        setHasOptionsMenu(true);
        setRetainInstance(true);

        return root;
    }

    @Override
    public void onResume() {
        super.onResume();
        presenter.loadPlace();
    }

    @OnClick(R.id.txt_add_to_folder)
    public void onAddToFolderClick() {
        presenter.openFolders();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.om_add_edit_place, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_confirm:
                presenter.savePlace(txtPlaceDescription.getText().toString(), folders);
                break;
        }
        return false;
    }

    @Override
    public void setPresenter(AddEditPlaceContract.Presenter presenter) {
        this.presenter = presenter;
    }

    @Override
    public void showPlaceDetails() {
        getActivity().setResult(Activity.RESULT_OK);
        getActivity().finish();
    }

    @Override
    public void showDescription(String description) {
        txtPlaceDescription.setText(description);
    }

    @Override
    public void showFolders(List<Folder> folders) {
        for (Folder folder : folders) {
            View customFolderNameView = LayoutInflater.from(getContext()).inflate(R.layout.view_folder, llPlaceFolders, false);

            TextView txtFolderName = ButterKnife.findById(customFolderNameView, R.id.txt_folder_name);
            txtFolderName.setText(folder.getName());
            llPlaceFolders.addView(customFolderNameView);
        }
    }

    @Override
    public void showFoldersUi(List<Integer> placeFolderIds) {
        Intent intent = new Intent(getContext(), AddSelectFolderActivity.class);
        intent.putExtra(AddSelectFolderActivity.EXTRA_PLACE_FOLDER_IDS, (ArrayList<Integer>) placeFolderIds);
        startActivityForResult(intent, REQUEST_ADD_FOLDER);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_ADD_FOLDER && resultCode == getActivity().RESULT_OK) {
            folders.addAll((List<Folder>) data.getSerializableExtra(EXTRA_SELECTED_FOLDERS));
        }
    }
}
