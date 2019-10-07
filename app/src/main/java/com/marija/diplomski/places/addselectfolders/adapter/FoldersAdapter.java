package com.marija.diplomski.places.addselectfolders.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.marija.diplomski.places.base.adapters.AbstractViewHolder;
import com.marija.diplomski.places.base.adapters.SelectableAdapter;
import com.marija.diplomski.places.core.domain.model.Folder;

import java.util.List;

public class FoldersAdapter extends SelectableAdapter<AbstractViewHolder> {

    private List<Folder> items;
    private FolderViewHolder.FolderItemListener folderItemListener;
    private int layoutId;

    public FoldersAdapter(FolderViewHolder.FolderItemListener folderItemListener, int layoutId) {
        this.folderItemListener = folderItemListener;
        this.layoutId = layoutId;
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(layoutId, parent, false);
        return new FolderViewHolder(view, folderItemListener);
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        holder.bind(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items != null ? items.size() : 0;
    }

    public void setList(List<Folder> folders){
        this.items = folders;
        notifyDataSetChanged();
    }

}