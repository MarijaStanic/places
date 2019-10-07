package com.marija.diplomski.places.core.data.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.TextUtils;

import com.marija.diplomski.places.core.domain.data.FolderDao;
import com.marija.diplomski.places.core.domain.model.Folder;
import com.marija.diplomski.places.core.data.DbContentProvider;
import com.marija.diplomski.places.core.data.PlacesSchema.FolderTable;

import java.util.ArrayList;
import java.util.List;

public class FolderDaoImpl extends DbContentProvider<Folder> implements FolderDao {

    private Cursor cursor;
    private ContentValues values;

    public FolderDaoImpl(SQLiteDatabase db) {
        super(db);
    }

    @Override
    public List<Folder> getAllFolders() {
        final List<Folder> folders = new ArrayList<>();

        cursor = query(FolderTable.FOLDER_TABLE, FolderTable.FOLDER_COLUMNS, null, null, FolderTable.COLUMN_ID);

        while (cursor.moveToNext()) {
            Folder folder = entryToEntity();
            folders.add(folder);
        }
        cursor.close();

        return folders;
    }

    @Override
    public Folder addFolder(Folder folder) {
        setContentValue(folder);
        long rowId = insert(FolderTable.FOLDER_TABLE, getContentValue());
        if (rowId < 0) {
            return null;
        }
        folder.setId((int) rowId);
        return folder;
    }

    @Override
    public boolean deleteFolders(List<Integer> folderIds) {
        final String selectionArgs = TextUtils.join(",", folderIds.toArray());
        final String selection = FolderTable.COLUMN_ID + " IN ( " + selectionArgs + " )";

        return delete(FolderTable.FOLDER_TABLE, selection, null) > 0;
    }

    @Override
    public boolean updateFolder(Folder folder) {
        setContentValue(folder);

        return update(FolderTable.FOLDER_TABLE, getContentValue(), FolderTable.COLUMN_ID + "=?", new String[]{String.valueOf(folder.getId())}) > 0;
    }

    @Override
    protected Folder entryToEntity() {
        int id;
        String name;

        id = cursor.getInt(cursor.getColumnIndexOrThrow(FolderTable.COLUMN_ID));
        name = cursor.getString(cursor.getColumnIndexOrThrow(FolderTable.COLUMN_TITLE));
        Folder folder = new Folder(id, name);

        return folder;
    }

    private void setContentValue(Folder folder) {
        values = new ContentValues();
        values.put(FolderTable.COLUMN_TITLE, folder.getName());
    }

    private ContentValues getContentValue() {
        return values;
    }
}
