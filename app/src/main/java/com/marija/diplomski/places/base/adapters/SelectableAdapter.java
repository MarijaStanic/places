package com.marija.diplomski.places.base.adapters;

import android.support.v7.widget.RecyclerView;
import android.util.SparseBooleanArray;

import java.util.ArrayList;
import java.util.List;

public abstract class SelectableAdapter<VH extends RecyclerView.ViewHolder> extends RecyclerView.Adapter<VH> {

    private SparseBooleanArray selectedItems;

    public SelectableAdapter() {
        selectedItems = new SparseBooleanArray();
    }

    public void toggleSelection(int position) {
        if (selectedItems.get(position)) {
            selectedItems.delete(position);
        } else {
            selectedItems.put(position, true);
        }
    }

    public void clearSelection() {
        selectedItems.clear();
    }

    public int getSelectedItemCount() {
        return selectedItems.size();
    }

    public List<Integer> getSelectedItems() {
        List<Integer> items = new ArrayList<>(selectedItems.size());
        for (int i = 0; i < selectedItems.size(); i++) {
            items.add(selectedItems.keyAt(i));
        }
        return items;
    }
}
