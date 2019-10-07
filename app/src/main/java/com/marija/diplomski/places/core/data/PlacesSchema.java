package com.marija.diplomski.places.core.data;

public final class PlacesSchema {

    private PlacesSchema() {
    }

    public static class KeywordTable {

        public static final String KEYWORD_TABLE = "keyword";
        public static final String COLUMN_ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_TYPE = "type";
        public static final String COLUMN_ICON = "icon";
        public static final String KEYWORD_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
                + KEYWORD_TABLE
                + " ("
                + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE
                + " TEXT, "
                + COLUMN_TYPE
                + " TEXT, "
                + COLUMN_ICON
                + " TEXT "
                + ")";

        public static final String[] KEYWORD_COLUMNS = new String[]{COLUMN_ID,
                COLUMN_TITLE, COLUMN_TYPE, COLUMN_ICON};

    }

    public static class PlaceTable {

        public static final String PLACE_TABLE = "place";
        public static final String COLUMN_ID = "p_id";
        public static final String COLUMN_PLACE_ID = "place_api_id";
        public static final String COLUMN_NAME = "name";
        public static final String COLUMN_PHOTO = "photo";
        public static final String COLUMN_LATITUDE = "latitude";
        public static final String COLUMN_LONGITUDE = "longitude";
        public static final String COLUMN_ICON = "icon";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String PLACE_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
                + PLACE_TABLE
                + " ("
                + COLUMN_ID
                + " INTEGER PRIMARY KEY, "
                + COLUMN_PLACE_ID
                + " TEXT, "
                + COLUMN_NAME
                + " TEXT, "
                + COLUMN_PHOTO
                + " TEXT, "
                + COLUMN_LATITUDE
                + " REAL, "
                + COLUMN_LONGITUDE
                + " REAL, "
                + COLUMN_ICON
                + " TEXT, "
                + COLUMN_DESCRIPTION
                + " TEXT "
                + ")";

        public static final String[] PLACE_COLUMNS = new String[]{COLUMN_ID, COLUMN_PLACE_ID,
                COLUMN_NAME, COLUMN_PHOTO, COLUMN_LATITUDE, COLUMN_LONGITUDE, COLUMN_ICON, COLUMN_DESCRIPTION};
    }

    public static class FolderTable {
        public static final String FOLDER_TABLE = "folder";
        public static final String COLUMN_ID = "f_id";
        public static final String COLUMN_TITLE = "title";
        public static final String FOLDER_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
                + FOLDER_TABLE
                + " ("
                + COLUMN_ID
                + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COLUMN_TITLE
                + " TEXT "
                + ")";

        public static final String[] FOLDER_COLUMNS = new String[]{COLUMN_ID, COLUMN_TITLE};
    }

    public static class PlaceFolderTable {
        public static final String PLACE_FOLDER_TABLE = "place_folder";
        public static final String COLUMN_PLACE_ID = "place_id";
        public static final String COLUMN_FOLDER_ID = "folder_id";
        public static final String PLACE_FOLDER_TABLE_CREATE = "CREATE TABLE IF NOT EXISTS "
                + PLACE_FOLDER_TABLE
                + " ("
                + COLUMN_PLACE_ID
                + " INTEGER, "
                + COLUMN_FOLDER_ID
                + " INTEGER, "
                + " PRIMARY KEY("
                + COLUMN_PLACE_ID + ", " + COLUMN_FOLDER_ID + "), "
                + " CONSTRAINT FK_FOLDER FOREIGN KEY (" + COLUMN_FOLDER_ID + ") REFERENCES " + FolderTable.FOLDER_TABLE + "("
                + FolderTable.COLUMN_ID + ")"
                + " ON DELETE CASCADE, "
                + " CONSTRAINT FK_USER FOREIGN KEY (" + COLUMN_PLACE_ID + ") REFERENCES " + PlaceTable.PLACE_TABLE + "("
                + PlaceTable.COLUMN_ID + ") "
                + " ON DELETE CASCADE "
                + ")";

        public static final String[] PLACES_FOLDER_COLUMNS = new String[]{COLUMN_PLACE_ID,
                COLUMN_FOLDER_ID};
    }
}
