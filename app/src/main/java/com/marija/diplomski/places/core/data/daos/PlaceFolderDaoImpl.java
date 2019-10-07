package com.marija.diplomski.places.core.data.daos;

import android.content.ContentValues;

import android.database.sqlite.SQLiteDatabase;

import com.marija.diplomski.places.core.domain.data.PlaceFolderDao;
import com.marija.diplomski.places.core.domain.model.PlaceFolder;
import com.marija.diplomski.places.core.data.DbContentProvider;
import com.marija.diplomski.places.core.data.PlacesSchema.PlaceFolderTable;

public class PlaceFolderDaoImpl extends DbContentProvider<PlaceFolder> implements PlaceFolderDao {

    private ContentValues values;

    @Override
    protected PlaceFolder entryToEntity() {
        return null;
    }

    public PlaceFolderDaoImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public boolean addPlaceFolder(int placeId, int folderId) {
        setContentValue(placeId, folderId);
        boolean num = insert(PlaceFolderTable.PLACE_FOLDER_TABLE, getContentValue()) > 0;
        return num;
    }

    public boolean deletePlaceFoldersForPlace(int id) {
        final String selectionArgs[] = {String.valueOf(id)};
        final String selection = PlaceFolderTable.COLUMN_PLACE_ID + " = ?";

        boolean num = delete(PlaceFolderTable.PLACE_FOLDER_TABLE, selection, selectionArgs) > 0;
        return num;
    }

    private void setContentValue(int placeId, int folderId) {
        values = new ContentValues();
        values.put(PlaceFolderTable.COLUMN_PLACE_ID, placeId);
        values.put(PlaceFolderTable.COLUMN_FOLDER_ID, folderId);
    }

    private ContentValues getContentValue() {
        return values;
    }
}
