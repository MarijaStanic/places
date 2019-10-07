package com.marija.diplomski.places.addselectfolders.adapter;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import com.marija.diplomski.places.base.adapters.AbstractViewHolder;
import com.marija.diplomski.places.R;
import com.marija.diplomski.places.core.domain.model.Folder;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class FolderViewHolder extends AbstractViewHolder<Folder> {

    @LayoutRes
    public static final int LAYOUT_FOLDER = R.layout.list_row_folder;
    @LayoutRes
    public static final int LAYOUT_CHOOSE_FOLDER = R.layout.list_row_choose_folder;

    @BindView(R.id.tv_folder_name)
    TextView tvFolderName;
    @BindView(R.id.cv_folder)
    CardView cvFolder;
    @Nullable
    @BindView(R.id.cb_folder)
    CheckBox cbFolder;

    private Folder item;
    private FolderItemListener folderListener;

    public FolderViewHolder(View view, FolderItemListener folderListener) {
        super(view);
        this.folderListener = folderListener;
    }

    @Override
    public void bind(Folder item) {
        this.item = item;
        tvFolderName.setText(item.getName());
        if (cbFolder != null) {
            cbFolder.setOnCheckedChangeListener(null);
            cbFolder.setChecked(item.isSelected());
        }
    }

    @OnClick(R.id.cv_folder)
    public void onFolderCardClick() {
        folderListener.onFolderClick(item.getId());
        if (cbFolder != null) {
            cbFolder.setChecked(item.isSelected());
        }
    }

    @OnLongClick(R.id.cv_folder)
    public boolean onFolderCardLongClick() {
        item.setSelected(!item.isSelected());
        folderListener.onFolderLongClick(item.getId());
        return true;
    }

    public interface FolderItemListener {
        void onFolderClick(int folderId);

        void onFolderLongClick(int position);
    }

}