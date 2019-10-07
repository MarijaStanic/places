package com.marija.diplomski.places.base;

import android.content.Intent;
import android.os.Bundle;

public interface LifecycleDelegate {

    default void onCreate(Bundle savedInstanceState) {
    }

    default void onActivityResult(int requestCode, int resultCode, Intent data) {
    }

    default void onStart() {
    }

    default void onRestoreInstanceState(Bundle savedInstanceState) {
    }

    default void onPostCreate(Bundle savedInstanceState) {
    }

    default void onResume() {
    }

    default void onPause() {
    }

    default void onSaveInstanceState(Bundle outState) {
    }

    default void onStop() {
    }

    default void onDestroy() {
    }

    default void onBackPressed() {
    }

}
