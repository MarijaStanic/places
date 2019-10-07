package com.marija.diplomski.places.base.adapters;

import android.support.v7.util.SortedList;
import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

import java.util.Comparator;
import java.util.List;

public abstract class SortedAdapter<T> extends RecyclerView.Adapter<AbstractViewHolder> {

    public static final int VIEW_TYPE_HEADER = 0;
    public static final int VIEW_TYPE_CONTENT = 1;
    public static final int VIEW_TYPE_FOOTER = 2;

    private SortedList<T> sortedList;
    private boolean hasHeader;
    private boolean hasFooter;

    public SortedAdapter(Class<T> itemClass, final Comparator<T> comparator) {

        sortedList = new SortedList<>(itemClass, new SortedList.Callback<T>() {
            @Override
            public int compare(T o1, T o2) {
                return comparator.compare(o1, o2);
            }

            @Override
            public void onChanged(int position, int count) {
                notifyItemRangeChanged(position + getHeaderCount(), count);
            }

            @Override
            public boolean areContentsTheSame(T oldItem, T newItem) {
                return oldItem.equals(newItem);
            }

            @Override
            public boolean areItemsTheSame(T item1, T item2) {
                return item1.equals(item2);
            }

            @Override
            public void onInserted(int position, int count) {
                notifyItemRangeInserted(position + getHeaderCount(), count);
            }

            @Override
            public void onRemoved(int position, int count) {
                notifyItemRangeRemoved(position + getHeaderCount(), count);
            }

            @Override
            public void onMoved(int fromPosition, int toPosition) {
                notifyItemMoved(fromPosition + getHeaderCount(), toPosition + getHeaderCount());
            }
        });

    }

    public void add(T model) {
        sortedList.add(model);
    }

    public void remove(T model) {
        sortedList.remove(model);
    }

    public void replaceAll(List<T> models) {
        sortedList.beginBatchedUpdates();
        for (int i = sortedList.size() - 1; i >= 0; i--) {
            final T model = sortedList.get(i);
            if (!models.contains(model)) {
                sortedList.remove(model);
            }
        }
        addAll(models);
        sortedList.endBatchedUpdates();
    }

    public void removeAll() {
        sortedList.clear();
    }

    public void addAll(List<T> models) {
        sortedList.addAll(models);
    }

    @Override
    public AbstractViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == VIEW_TYPE_HEADER) {
            return onCreateHeaderItemViewHolder(parent, viewType);
        } else if (viewType == VIEW_TYPE_CONTENT) {
            return onCreateContentItemViewHolder(parent, viewType);
        } else {
            return onCreateFooterItemViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(AbstractViewHolder holder, int position) {
        if (getItemViewType(position) == VIEW_TYPE_HEADER) {
            onBindHeaderItemViewHolder(holder, position);
        } else if (getItemViewType(position) == VIEW_TYPE_FOOTER) {
            onBindFooterItemViewHolder(holder, position);
        } else {
            holder.bind(sortedList.get(position - getHeaderCount()));
        }
    }

    @Override
    public int getItemCount() {
        return sortedList != null ? sortedList.size() + getFooterAndHeaderCount() : getFooterAndHeaderCount();
    }

    @Override
    public int getItemViewType(int position) {
        if (position < getHeaderCount()) {
            return VIEW_TYPE_HEADER;
        } else {
            int itemCount = sortedList.size();
            if (position < getHeaderCount() + itemCount) {
                return VIEW_TYPE_CONTENT;
            } else return VIEW_TYPE_FOOTER;
        }
    }

    private int getFooterAndHeaderCount() {
        int count = 0;
        count += getHeaderCount();
        count += getFooterCount();
        return count;
    }

    private int getHeaderCount() {
        return hasHeader ? 1 : 0;
    }

    private int getFooterCount() {
        return hasFooter ? 1 : 0;
    }

    public void setHasHeader(boolean hasHeader) {
        this.hasHeader = hasHeader;
    }

    public void setHasFooter(boolean hasFooter) {
        this.hasFooter = hasFooter;
    }

    protected AbstractViewHolder onCreateHeaderItemViewHolder(ViewGroup parent, int headerViewType) {
        return null;
    }

    protected abstract AbstractViewHolder onCreateContentItemViewHolder(ViewGroup parent, int contentViewType);

    protected AbstractViewHolder onCreateFooterItemViewHolder(ViewGroup parent, int footerViewType) {
        return null;
    }

    protected void onBindHeaderItemViewHolder(AbstractViewHolder headerViewHolder, int position) {
    }


    protected void onBindFooterItemViewHolder(AbstractViewHolder footerViewHolder, int position) {
    }

}
