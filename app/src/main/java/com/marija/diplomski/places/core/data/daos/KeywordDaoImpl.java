package com.marija.diplomski.places.core.data.daos;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.marija.diplomski.places.core.domain.data.KeywordDao;
import com.marija.diplomski.places.core.domain.model.Keyword;
import com.marija.diplomski.places.core.data.DbContentProvider;
import com.marija.diplomski.places.core.data.PlacesSchema.KeywordTable;

import java.util.ArrayList;
import java.util.List;

public class KeywordDaoImpl extends DbContentProvider implements KeywordDao {

    private Cursor cursor;
    private ContentValues values;

    public KeywordDaoImpl(SQLiteDatabase db) {
        super(db);
    }

    public List<Keyword> getAllKeywords() {
        final List<Keyword> keywords = new ArrayList<>();

        cursor = query(KeywordTable.KEYWORD_TABLE, KeywordTable.KEYWORD_COLUMNS, null, null, KeywordTable.COLUMN_ID);

        while (cursor.moveToNext()) {
            Keyword keyword = entryToEntity();
            keywords.add(keyword);
        }
        cursor.close();

        return keywords;
    }

    public boolean addKeyword(Keyword keyword) {
        setContentValue(keyword);

        return insert(KeywordTable.KEYWORD_TABLE, getContentValue()) > 0;
    }

    public boolean deleteKeyword(Keyword keyword) {
        String selection = KeywordTable.COLUMN_TITLE + "=?";
        String selectionArgs[] = {keyword.getTitle()};

        return delete(KeywordTable.KEYWORD_TABLE, selection, selectionArgs) > 0;
    }

    protected Keyword entryToEntity() {
        int id;
        String title, type, icon;

        id = cursor.getInt(cursor.getColumnIndexOrThrow(KeywordTable.COLUMN_ID));
        title = cursor.getString(cursor.getColumnIndexOrThrow(KeywordTable.COLUMN_TITLE));
        type = cursor.getString(cursor.getColumnIndexOrThrow(KeywordTable.COLUMN_TYPE));
        icon = cursor.getString(cursor.getColumnIndexOrThrow(KeywordTable.COLUMN_ICON));
        Keyword keyword = new Keyword(id, title, type, icon);

        return keyword;
    }

    private void setContentValue(Keyword keyword) {
        values = new ContentValues();
        values.put(KeywordTable.COLUMN_TITLE, keyword.getTitle());
        values.put(KeywordTable.COLUMN_TYPE, keyword.getType());
        values.put(KeywordTable.COLUMN_ICON, keyword.getIcon());
    }

    private ContentValues getContentValue() {
        return values;
    }

}
