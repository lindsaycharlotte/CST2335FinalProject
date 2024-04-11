package com.example.cst2335finalproject;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Build;

import androidx.fragment.app.Fragment;

public class MyOpener extends SQLiteOpenHelper {
    protected final static String DATABASE_NAME = "FavouritesDB";
    protected final static int VERSION_NUM = 1;
    public final static String TABLE_NAME = "Favourites";
    public final static String COL_ARTICLE = "ARTICLE";
    public final static String COL_CATEGORY = "CATEGORY";
    public final static String COL_URL = "URL";
    public final static String COL_ID = "_id";

    /**
     *
     * @param ctx
     */

    public MyOpener(Context ctx) {
        super(ctx, DATABASE_NAME, null, VERSION_NUM);
    }

    /**
     *
     * @param db The database.
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL_ARTICLE + " text,"
                + COL_CATEGORY + " text,"
                + COL_URL + " text);");
    }

    /**
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }

    /**
     *
     * @param db The database.
     * @param oldVersion The old database version.
     * @param newVersion The new database version.
     */
    @Override
    public void onDowngrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        onCreate(db);
    }


}
