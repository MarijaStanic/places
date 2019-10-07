package com.marija.diplomski.places.core.base.view;

public class UiContracts {

    public interface MvpView {
        void showProgress(String message);

        void dismissProgress();
    }

    public interface BaseView<T> extends MvpView {
        void setPresenter(T presenter);
    }

    public interface EventsDelegate {

    }

    public interface BaseViewExtension<D extends EventsDelegate> {
        void setEventsDelegate(D eventsDelegate);
    }
}
