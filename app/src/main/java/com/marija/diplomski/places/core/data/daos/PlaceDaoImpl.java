package com.marija.diplomski.places.core.data.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.marija.diplomski.places.core.domain.data.PlaceDao;
import com.marija.diplomski.places.core.domain.model.Folder;
import com.marija.diplomski.places.core.domain.model.Place;
import com.marija.diplomski.places.core.data.CustomCursor;
import com.marija.diplomski.places.core.data.DbContentProvider;
import com.marija.diplomski.places.core.domain.data.PlaceFolderDao;
import com.marija.diplomski.places.core.data.PlacesSchema.PlaceTable;
import com.marija.diplomski.places.core.data.PlacesSchema.PlaceFolderTable;
import com.marija.diplomski.places.core.data.PlacesSchema.FolderTable;

import java.util.ArrayList;
import java.util.List;

public class PlaceDaoImpl extends DbContentProvider<Place> implements PlaceDao {

    private Cursor cursor;
    private ContentValues values;
    private PlaceFolderDao placeFolderDao;

    private CustomCursor customCursor;

    public PlaceDaoImpl(SQLiteDatabase db) {
        super(db);
        placeFolderDao = new PlaceFolderDaoImpl(db);
    }

    @Override
    public List<Place> getAllPlaces() {
        final List<Place> places = new ArrayList<>();

        cursor = query(PlaceTable.PLACE_TABLE, PlaceTable.PLACE_COLUMNS, null, null, PlaceTable.COLUMN_ID);

        while (cursor.moveToNext()) {
            Place place = entryToEntity();
            places.add(place);
        }
        cursor.close();

        return places;
    }

    @Override
    public boolean addPlace(Place place) {
        setContentValue(place);

        db.beginTransaction();
        try {
            long row = super.insert(PlaceTable.PLACE_TABLE, getContentValue());
            for (Folder f : place.getFolders()) {
                placeFolderDao.addPlaceFolder((int) row, f.getId());
            }
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public boolean updatePlace(Place place) {
        setContentValue(place);
        final String selectionArgs[] = {String.valueOf(place.getPlaceId())};
        final String selection = PlaceTable.COLUMN_PLACE_ID + " = ?";

        db.beginTransaction();
        try {
            if(place.getFolders().size() > 0)
                placeFolderDao.deletePlaceFoldersForPlace(place.getId());
            update(PlaceTable.PLACE_TABLE, getContentValue(), selection, selectionArgs);
            for (Folder f : place.getFolders()) {
                placeFolderDao.addPlaceFolder(place.getId(), f.getId());
            }
            db.setTransactionSuccessful();
            return true;
        } catch (Exception e) {
            return false;
        } finally {
            db.endTransaction();
        }
    }

    public Place getPlace(String id) {
        String query = String.format("SELECT p.*, f.* FROM %1$s p OUTER LEFT JOIN %2$s pf " +
                        "ON p.%3$s = pf.%4$s OUTER LEFT JOIN %5$s f ON f.%6$s = pf.%7$s WHERE p.%8$s = ?",
                PlaceTable.PLACE_TABLE, PlaceFolderTable.PLACE_FOLDER_TABLE, PlaceTable.COLUMN_ID, PlaceFolderTable.COLUMN_PLACE_ID,
                FolderTable.FOLDER_TABLE, FolderTable.COLUMN_ID, PlaceFolderTable.COLUMN_FOLDER_ID, PlaceTable.COLUMN_PLACE_ID);
        final String selectionArgs[] = {id};

        Place place = null;
        List<Folder> folders = new ArrayList<>();

        cursor = db.rawQuery(query, selectionArgs);
        customCursor = new CustomCursor(cursor);
        if(cursor.getCount() > 0) {
            cursor.moveToFirst();
            place = entryToEntity();
            if (place != null) {
                while (!cursor.isAfterLast()) {
                    Integer folderId = customCursor.getInteger(cursor.getColumnIndexOrThrow(FolderTable.COLUMN_ID));
                    if (folderId != null) {
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(FolderTable.COLUMN_TITLE));
                        Folder folder = new Folder(folderId, name);
                        folders.add(folder);
                    }
                    cursor.moveToNext();
                    //   Folder f = Database.folderDao.cursorToEntity(cursor);
                }
            }
        }
        cursor.close();
        if (place != null) {
            place.setFolders(folders);
        }
        return place;
    }



    @Override
    public boolean deletePlace(String id) {
        final String selectionArgs[] = {id};
        final String selection = PlaceTable.COLUMN_PLACE_ID + " = ?";

        return super.delete(PlaceTable.PLACE_TABLE, selection, selectionArgs) > 0;
    }

    @Override
    protected Place entryToEntity() {
        int id;
        String placeId, name, photo, icon, description;
        double latitude, longitude;

        id = cursor.getInt(cursor.getColumnIndexOrThrow(PlaceTable.COLUMN_ID));
        placeId = cursor.getString(cursor.getColumnIndexOrThrow(PlaceTable.COLUMN_PLACE_ID));
        name = cursor.getString(cursor.getColumnIndexOrThrow(PlaceTable.COLUMN_NAME));
        photo = cursor.getString(cursor.getColumnIndexOrThrow(PlaceTable.COLUMN_PHOTO));
        latitude = cursor.getDouble(cursor.getColumnIndexOrThrow(PlaceTable.COLUMN_LATITUDE));
        longitude = cursor.getDouble(cursor.getColumnIndexOrThrow(PlaceTable.COLUMN_LONGITUDE));
        icon = cursor.getString(cursor.getColumnIndexOrThrow(PlaceTable.COLUMN_ICON));
        description = cursor.getString(cursor.getColumnIndexOrThrow(PlaceTable.COLUMN_DESCRIPTION));
        Place place = new Place(id, placeId, name, photo, latitude, longitude, icon, description);

        return place;
    }

    private void setContentValue(Place place) {
        values = new ContentValues();

        values.put(PlaceTable.COLUMN_PLACE_ID, place.getPlaceId());
        values.put(PlaceTable.COLUMN_NAME, place.getName());
        values.put(PlaceTable.COLUMN_PHOTO, place.getPhoto());
        values.put(PlaceTable.COLUMN_LATITUDE, place.getLatitude());
        values.put(PlaceTable.COLUMN_LONGITUDE, place.getLongitude());
        values.put(PlaceTable.COLUMN_ICON, place.getIcon());
        values.put(PlaceTable.COLUMN_DESCRIPTION, place.getDescription());
    }

    private ContentValues getContentValue() {
        return values;
    }

    @Override
    public List<Place> getPlacesForFolder(int folderId) {
        final String query = String.format("select distinct * from %1$s p join %2$s pf on pf.%3$s = p.%4$s join %5$s f on f.%6$s = pf.%7$s and f.%8$s = ?",
                PlaceTable.PLACE_TABLE, PlaceFolderTable.PLACE_FOLDER_TABLE, PlaceFolderTable.COLUMN_PLACE_ID, PlaceTable.COLUMN_ID, FolderTable.FOLDER_TABLE, FolderTable.COLUMN_ID,
                PlaceFolderTable.COLUMN_FOLDER_ID, FolderTable.COLUMN_ID);
        final String selectionArgs[] = {String.valueOf(folderId)};

        List<Place> places = new ArrayList<>();

        cursor = db.rawQuery(query, selectionArgs);
        while (cursor.moveToNext()) {
            Place place = entryToEntity();
            places.add(place);
        }
        cursor.close();

        return places;
    }

}
