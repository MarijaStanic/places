package com.marija.diplomski.places.keywords.adapter;

import android.support.v7.widget.CardView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.marija.diplomski.places.base.adapters.AbstractViewHolder;
import com.marija.diplomski.places.R;
import com.marija.diplomski.places.core.domain.model.Keyword;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;

public class KeywordContentViewHolder extends AbstractViewHolder<Keyword> {

    @BindView(R.id.cv_keyword)
    CardView cvKeyword;
    @BindView(R.id.iv_icon)
    ImageView ivIcon;
    @BindView(R.id.tv_title)
    TextView tvTitle;

    private Keyword item;

    private KeywordItemListener itemListener;

    public KeywordContentViewHolder(View view, KeywordItemListener itemListener) {
        super(view);
        this.itemListener = itemListener;
    }

    @Override
    public void bind(Keyword item) {
        this.item = item;
        ivIcon.setBackgroundResource(ivIcon.getContext().getResources()
                .getIdentifier(item.getIcon(), "drawable", ivIcon.getContext()
                        .getPackageName()));
        //   ImageLoader.loadImage(ivIcon.getContext(), ivIcon.getContext().getResources().getIdentifier(item.getIcon(), "drawable", ivIcon.getContext().getPackageName()), ivIcon);
        tvTitle.setText(item.getTitle());
    }

    @OnClick(R.id.cv_keyword)
    public void onKeywordCardClick() {
        itemListener.onKeywordClick(item);
    }

    @OnLongClick(R.id.cv_keyword)
    public boolean onKeywordLongClick() {
        itemListener.onDeleteKeywordLongClick(item);
        return false;
    }

    public interface KeywordItemListener {
        void onKeywordClick(Keyword keyword);

        void onDeleteKeywordLongClick(Keyword keyword);

        void onMyMapClick();
    }
}
