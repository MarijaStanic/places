package com.marija.diplomski.places.base.views;

import android.app.ProgressDialog;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;

import com.marija.diplomski.places.core.base.view.UiContracts;

public class BaseFragment extends Fragment implements UiContracts.MvpView {

    private ProgressDialog progressDialog;

    public void showMessage(String message) {
        Snackbar.make(getView(), message, Snackbar.LENGTH_LONG).show();
    }

    @Override
    public void showProgress(String message) {
        if (getActivity() != null) {
            dismissProgress();
            progressDialog = ProgressDialog.show(getContext(), null, message);
        }
    }

    @Override
    public void dismissProgress() {
        if (getActivity() != null) {
            if (progressDialog != null && progressDialog.isShowing())
                progressDialog.dismiss();
            progressDialog = null;
        }
    }

}
