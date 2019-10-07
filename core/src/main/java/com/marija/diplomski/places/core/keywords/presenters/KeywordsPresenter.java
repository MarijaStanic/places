package com.marija.diplomski.places.core.keywords.presenters;

import com.marija.diplomski.places.core.domain.usecase.UseCase;
import com.marija.diplomski.places.core.domain.usecase.UseCaseHandler;
import com.marija.diplomski.places.core.domain.model.Keyword;
import com.marija.diplomski.places.core.keywords.KeywordsContract;
import com.marija.diplomski.places.core.keywords.NavigationDrawerContract;
import com.marija.diplomski.places.core.keywords.listener.DialogListener;
import com.marija.diplomski.places.core.keywords.usecase.AddKeyword;
import com.marija.diplomski.places.core.keywords.usecase.DeleteKeyword;
import com.marija.diplomski.places.core.keywords.usecase.GetKeywords;

import java.util.ArrayList;
import java.util.List;

public class KeywordsPresenter implements KeywordsContract.Presenter,
        NavigationDrawerContract.NavigationDrawerViewExtensionDelegate {

    public static final String NEW_PLACE_ICON = "ic_new_place";

    private UseCaseHandler useCaseHandler;
    private KeywordsContract.View keywordsView;
    private GetKeywords getKeywords;
    private DeleteKeyword deleteKeyword;
    private List<Keyword> keywords;
    private AddKeyword addKeyword;
    private NavigationDrawerContract.NavigationDrawerViewExtensionContract drawerView;

    public KeywordsPresenter(UseCaseHandler useCaseHandler,
                             KeywordsContract.View keywordsView,
                             GetKeywords getKeywords,
                             DeleteKeyword deleteKeyword,
                             AddKeyword addKeyword,
                             NavigationDrawerContract.NavigationDrawerViewExtensionContract drawerView) {
        this.useCaseHandler = useCaseHandler;
        this.keywordsView = keywordsView;
        this.getKeywords = getKeywords;
        this.deleteKeyword = deleteKeyword;
        this.drawerView = drawerView;
        this.drawerView.setEventsDelegate(this);
        this.addKeyword = addKeyword;
        this.keywordsView.setPresenter(this);
    }

    @Override
    public void loadKeywords() {
        useCaseHandler.execute(getKeywords, null, new UseCase.UseCaseCallback<GetKeywords.ResponseValue>() {
            @Override
            public void onSuccess(GetKeywords.ResponseValue response) {
                keywords = response.getKeywords();
                processKeywords(keywords);
            }

            @Override
            public void onError() {

            }
        });
    }

    private void processKeywords(List<Keyword> keywords) {
        if (!keywords.isEmpty()) {
            drawerView.showKeywords(keywords);
        }
    }

    @Override
    public void deleteKeyword(final Keyword keyword) {
        drawerView.showAlertDialog("Delete", "Are you sure you want to delete " + keyword.getTitle() + "?", "Delete", new DialogListener() {
            @Override
            public void confirm() {
                useCaseHandler.execute(deleteKeyword, new DeleteKeyword.RequestValues(keyword), new UseCase.UseCaseCallback<DeleteKeyword.ResponseValue>() {

                    @Override
                    public void onSuccess(DeleteKeyword.ResponseValue response) {
                        keywords.remove(keyword);
                        drawerView.showUpdatedAdapter(keyword);
                    }

                    @Override
                    public void onError() {

                    }
                });
            }

            @Override
            public void cancel() {
            }
        });
    }

    private void addKeyword(final Keyword keyword) {
        useCaseHandler.execute(addKeyword, new AddKeyword.RequestValues(keyword), new UseCase.UseCaseCallback<AddKeyword.ResponseValue>() {
            @Override
            public void onSuccess(AddKeyword.ResponseValue response) {
                keywords.add(keyword);
                keywordsView.showKeywordAddedMessage();
            }

            @Override
            public void onError() {

            }
        });
    }

    @Override
    public void openPlacesMapForKeyword(Keyword keyword) {
        keywordsView.showPlacesMapForKeyword(keyword);
    }

    @Override
    public void openMyMapForFolder(int folderId) {
        keywordsView.showMyMapForFolder(folderId);
    }

    @Override
    public void openMyMapForTablet() {
        keywordsView.showMyMapForTablet();
    }

    @Override
    public void filterKeywordsForQueriedTitle(String newTitle) {
        if (newTitle.isEmpty()) {
            refreshNavigationDrawer(true, keywords);
        } else {
            boolean setSearchButtonImage = false;
            final String lowerCaseQuery = newTitle.toLowerCase();
            final List<Keyword> filteredKeywords = new ArrayList<>();
            for (Keyword keyword : keywords) {
                final String title = keyword.getTitle().toLowerCase();
                if (title.contains(lowerCaseQuery)) {
                    filteredKeywords.add(keyword);
                    if (title.equals(lowerCaseQuery)) {
                        setSearchButtonImage = true;
                    }
                }
            }
            refreshNavigationDrawer(setSearchButtonImage, filteredKeywords);
        }
    }

    private void refreshNavigationDrawer(boolean setSearchButtonImage, List<Keyword> filteredKeywords) {
        drawerView.setSearchButtonImage(setSearchButtonImage);
        processKeywords(filteredKeywords);
    }

    @Override
    public void checkSubmittedKeywordTitleQuery(String title) {
        if (!keywordExistsWithTitle(title)) {
            Keyword keyword = new Keyword(title, NEW_PLACE_ICON);
            addKeyword(keyword);
            openPlacesMapForKeyword(keyword);
        }
    }

    private boolean keywordExistsWithTitle(String title) {
        for (Keyword keyword : keywords) {
            if (keyword.getTitle().equalsIgnoreCase(title)) {
                openPlacesMapForKeyword(keyword);
                return true;
            }
        }
        return false;
    }
}
