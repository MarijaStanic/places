package com.marija.diplomski.places.core.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

public final class ImageLoaderUtil {

    private ImageLoaderUtil() {
    }

    public static void loadImage(Context context, String url, ImageView iv, int errorResource) {
        Picasso.with(context).load(url).error(errorResource).into(iv);
    }

    public static void loadImage(Context context, String url, ImageView iv) {
        Picasso.with(context).load(url).into(iv);
    }

    public static void loadImage(Context context, int resourceId, ImageView iv) {
        Picasso.with(context).load(resourceId).into(iv);
    }

    public static Bitmap getBitmapFromView(View view) {
        view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
        view.layout(0, 0, view.getMeasuredWidth(), view.getMeasuredHeight());
        view.buildDrawingCache();
        Bitmap returnedBitmap = Bitmap.createBitmap(view.getMeasuredWidth(), view.getMeasuredHeight(),
                Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(returnedBitmap);
        canvas.drawColor(Color.WHITE, PorterDuff.Mode.SRC_IN);
        Drawable drawable = view.getBackground();
        if (drawable != null)
            drawable.draw(canvas);
        view.draw(canvas);
        return returnedBitmap;
    }

}