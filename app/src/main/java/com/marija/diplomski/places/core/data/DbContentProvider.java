package com.marija.diplomski.places.core.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public abstract class DbContentProvider<T> {

    public SQLiteDatabase db;

    public DbContentProvider(SQLiteDatabase db) {
        this.db = db;
    }

    protected abstract T entryToEntity();

    public int delete(String tableName, String selection,
                      String[] selectionArgs) {
        return db.delete(tableName, selection, selectionArgs);
    }

    public long insert(String tableName, ContentValues values) {
        return db.insert(tableName, null, values);
    }

    public Cursor query(String tableName, String[] columns,
                        String selection, String[] selectionArgs, String sortOrder) {

        Cursor cursor = db.query(tableName, columns,
                selection, selectionArgs, null, null, sortOrder);

        return cursor;
    }

    public int update(String tableName, ContentValues values,
                      String selection, String[] selectionArgs) {
        return db.update(tableName, values, selection,
                selectionArgs);
    }

    public Cursor rawQuery(String sql, String[] selectionArgs) {
        return db.rawQuery(sql, selectionArgs);
    }
}