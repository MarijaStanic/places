package com.marija.diplomski.places.core.keywords;

import com.marija.diplomski.places.core.base.view.UiContracts;
import com.marija.diplomski.places.core.domain.model.Keyword;

public interface KeywordsContract {

    interface View extends UiContracts.BaseView<Presenter> {

        void showPlacesMapForKeyword(Keyword keyword);

        void showMyMapForFolder(int folderId);

        void showMyMapForTablet();

        void showKeywordAddedMessage();
    }

    interface Presenter {

        void loadKeywords();
    }
}