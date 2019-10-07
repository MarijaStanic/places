package com.marija.diplomski.places.core.keywords;

import com.marija.diplomski.places.core.base.view.UiContracts;
import com.marija.diplomski.places.core.domain.model.Keyword;
import com.marija.diplomski.places.core.keywords.listener.DialogListener;

import java.util.List;

public interface NavigationDrawerContract {

    interface NavigationDrawerViewExtensionContract extends
            UiContracts.BaseViewExtension<NavigationDrawerViewExtensionDelegate> {

        void setSearchButtonImage(boolean set);

        void showAlertDialog(String title, String message, String positiveButton, final DialogListener listener);

        void showUpdatedAdapter(Keyword keyword);

        void showKeywords(List<Keyword> keywords);
    }

    interface NavigationDrawerViewExtensionDelegate extends UiContracts.EventsDelegate {

        void deleteKeyword(Keyword keyword);

        void openPlacesMapForKeyword(Keyword keyword);

        void openMyMapForFolder(int folderId);

        void openMyMapForTablet();

        void filterKeywordsForQueriedTitle(String newTitle);

        void checkSubmittedKeywordTitleQuery(String title);
    }
}
