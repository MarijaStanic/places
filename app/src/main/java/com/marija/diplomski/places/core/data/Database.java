package com.marija.diplomski.places.core.data;

import android.content.Context;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.marija.diplomski.places.core.domain.data.FolderDao;
import com.marija.diplomski.places.core.domain.data.KeywordDao;
import com.marija.diplomski.places.core.domain.data.PlaceDao;
import com.marija.diplomski.places.core.domain.model.Keyword;
import com.marija.diplomski.places.core.data.daos.KeywordDaoImpl;
import com.marija.diplomski.places.core.data.daos.FolderDaoImpl;
import com.marija.diplomski.places.core.data.daos.PlaceDaoImpl;

public class Database {

    private static final String DATABASE_NAME = "diplomski";
    private static final int DATABASE_VERSION = 15;

    private DatabaseHelper dbHelper;

    public static KeywordDao keywordDao;
    public static PlaceDao placeDao;
    public static FolderDao folderDao;

    public Database(Context context) {
        dbHelper = new DatabaseHelper(context);
    }

    public void open() throws SQLException {
        keywordDao = new KeywordDaoImpl(dbHelper.getWritableDatabase());
        placeDao = new PlaceDaoImpl(dbHelper.getWritableDatabase());
        folderDao = new FolderDaoImpl(dbHelper.getWritableDatabase());
    }

    public void close() {
        dbHelper.close();
    }

    private class DatabaseHelper extends SQLiteOpenHelper {

        public DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onOpen(SQLiteDatabase db) {
            super.onOpen(db);
            db.execSQL("PRAGMA foreign_keys=ON;");
            db.setForeignKeyConstraintsEnabled(true);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(PlacesSchema.KeywordTable.KEYWORD_TABLE_CREATE);
            db.execSQL(PlacesSchema.PlaceTable.PLACE_TABLE_CREATE);
            db.execSQL(PlacesSchema.FolderTable.FOLDER_TABLE_CREATE);
            db.execSQL(PlacesSchema.PlaceFolderTable.PLACE_FOLDER_TABLE_CREATE);
            keywordDao = new KeywordDaoImpl(db);
            populateKeywordTable();
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion,
                              int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS " + PlacesSchema.KeywordTable.KEYWORD_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PlacesSchema.PlaceTable.PLACE_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PlacesSchema.FolderTable.FOLDER_TABLE);
            db.execSQL("DROP TABLE IF EXISTS " + PlacesSchema.PlaceFolderTable.PLACE_FOLDER_TABLE);
            onCreate(db);

        }

        public void populateKeywordTable(){
            keywordDao.addKeyword(new Keyword(null, "Cafe", "cafe", "ic_coffee"));
            keywordDao.addKeyword(new Keyword(null, "Bakery", "bakery", "ic_bread"));
            keywordDao.addKeyword(new Keyword(null, "Restaurant", "restaurant", "ic_dinner"));
            keywordDao.addKeyword(new Keyword(null, "ATM", "atm", "ic_atm"));
            keywordDao.addKeyword(new Keyword(null, "Bank", "bank", "ic_bank"));
            keywordDao.addKeyword(new Keyword(null, "Airport", "airport", "ic_airplane"));
            keywordDao.addKeyword(new Keyword(null, "Bus Station", "bus_station", "ic_bus"));
            keywordDao.addKeyword(new Keyword(null, "Train Station", "train_station", "ic_train"));
            keywordDao.addKeyword(new Keyword(null, "Gas Station", "gas_station", "ic_gas"));
            keywordDao.addKeyword(new Keyword(null, "Doctor", "doctor", "ic_doctor"));
            keywordDao.addKeyword(new Keyword(null, "Dentist", "dentist", "ic_tooth"));
            keywordDao.addKeyword(new Keyword(null, "Book Store", "book_store", "ic_book"));
            keywordDao.addKeyword(new Keyword(null, "Library", "library", "ic_books"));
            keywordDao.addKeyword(new Keyword(null, "Shopping Mall", "shopping_mall", "ic_mall"));
            keywordDao.addKeyword(new Keyword(null, "Movie Theater", "movie_theater", "ic_tickets"));
            keywordDao.addKeyword(new Keyword(null, "Beauty Salon", "beauty_salon", "ic_mirror"));
            keywordDao.addKeyword(new Keyword(null, "Post Office", "post_office", "ic_stamp"));
            keywordDao.addKeyword(new Keyword(null, "Bar", "bar", "ic_bar"));
            keywordDao.addKeyword(new Keyword(null, "Night Club", "night_club", "ic_club"));
            keywordDao.addKeyword(new Keyword(null, "Car Rental", "car_rental", "ic_car_rental"));
            keywordDao.addKeyword(new Keyword(null, "Car Repair", "car_repair", "ic_car_repair"));
            keywordDao.addKeyword(new Keyword(null, "Car Wash", "car_wash", "ic_car_wash"));
            keywordDao.addKeyword(new Keyword(null, "Electronics Store", "electronics_store", "ic_smartphone_screen"));
            keywordDao.addKeyword(new Keyword(null, "Florist", "florist", "ic_rose"));
            keywordDao.addKeyword(new Keyword(null, "Park", "park", "ic_park"));
            keywordDao.addKeyword(new Keyword(null, "Taxi", "taxi_stand", "ic_taxi"));
            keywordDao.addKeyword(new Keyword(null, "Hospital", "hospital", "ic_hospital"));
            keywordDao.addKeyword(new Keyword(null, "Museum", "museum", "ic_museum"));
            keywordDao.addKeyword(new Keyword(null, "Pharmacy", "pharmacy", "ic_bandage"));
            keywordDao.addKeyword(new Keyword(null, "Convenience Store", "convenience_store", "ic_convenience_store"));
            keywordDao.addKeyword(new Keyword(null, "Subway Station", "subway_station", "ic_subway"));
        }
    }

}