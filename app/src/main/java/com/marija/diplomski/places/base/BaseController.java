package com.marija.diplomski.places.base;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

import com.marija.diplomski.places.R;

import java.util.List;

import static com.marija.diplomski.places.core.utils.ActivityUtil.isTablet;

public class BaseController {

    private FragmentActivity fragmentActivity;

    public FragmentManager getSupportFragmentManager() {
        return fragmentActivity.getSupportFragmentManager();
    }

    public void showFragment(int containerId, Fragment toShow, String tag) {
        getSupportFragmentManager().beginTransaction()
                .setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(containerId, toShow, tag)
                .addToBackStack(null)
                .commit();
    }

    public void showFragments(Fragment toShowLeft, Fragment toShowRight, String tagLeft, String tagRight, boolean addToBackStack) {
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        ft.setCustomAnimations(android.R.anim.fade_in, android.R.anim.fade_out)
                .replace(R.id.left_frame, toShowLeft, tagLeft)
                .replace(R.id.right_frame, toShowRight, tagRight);
        if (addToBackStack) {
            ft.addToBackStack(null);
        }
        ft.commit();
    }

    public void addFragmentToActivity(int frameId, Fragment fragment, String tag) {
        getSupportFragmentManager()
                .beginTransaction()
                .add(frameId, fragment, tag)
                .commit();
    }

    public <T extends Fragment> T findOrCreateFragment(int containerId, String fragmentTag, Class<T> fragmentClass, boolean toAdd) {
        T fragment = (T) getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if (fragment == null) {
            try {
                fragment = fragmentClass.newInstance();
                if (toAdd) {
                    addFragmentToActivity(containerId, fragment, fragmentTag);
                } else {
                    if (!isTablet(getActivity())) {
                        showFragment(containerId, fragment, fragmentTag);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            if (!fragment.isAdded() && !fragmentTag.equals("myMap") && !isTablet(getActivity())) {
                getSupportFragmentManager().beginTransaction().attach(fragment).commit();
            }
        }
        return fragment;
    }

    public boolean isFragmentVisited(String fragmentTag) {
        Fragment fragment = getSupportFragmentManager().findFragmentByTag(fragmentTag);
        if (fragment != null) {
            return true;
        }
        return false;

    }

    public Fragment getVisibleFragment(){
        FragmentManager fragmentManager = getSupportFragmentManager();
        List<Fragment> fragments = fragmentManager.getFragments();
        if(fragments != null){
            for(Fragment fragment : fragments){
                if(fragment != null && fragment.isVisible())
                    return fragment;
            }
        }
        return null;
    }

    public String getVisibleFragmentTag() {
        Fragment currentFragment = getVisibleFragment();
        if(currentFragment != null) {
            return currentFragment.getTag();
        }
        return null;
    }

    public FragmentActivity getActivity() {
        return fragmentActivity;
    }

    public void setFragmentActivity(FragmentActivity fragmentActivity) {
        this.fragmentActivity = fragmentActivity;
    }
}