package com.marija.diplomski.places.core.data;

import android.database.Cursor;
import android.database.CursorWrapper;

public class CustomCursor extends CursorWrapper {

    public CustomCursor(Cursor cursor) {
        super(cursor);
    }

    public Integer getInteger(int columnIndex) {
        if (super.isNull(columnIndex)){
            return null;
        }else{
            return super.getInt(columnIndex);
        }
    }
}
