package com.shostrand.mike.concertfinder.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.shostrand.mike.concertfinder.data.BandContract.*;

/**
 * Created by Mike on 1/13/2018.
 */

public class BandDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "concert_finder.db";
    private static final int DB_REV = 1;

    public BandDbHelper(Context context) {
        super(context, DB_NAME, null, DB_REV);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        //Create band table
        final String SQL_CREATE_BAND_TABL = "CREATE TABLE " + BandEntry.TABLE_NAME + "( " +
                BandEntry._ID + " INTEGER NON NULL AUTOINCREMENT, " +
                BandEntry.COLUMN_BAND_NAME + " TEXT NOT NULL, " +
                BandEntry.COLUMN_GENRE + " TEXT NOT NULL, " +
                BandEntry.COLUMN_TIMESTAMP + " TIMESTAMP DEFAULT CURRENT_TIMESTAMP" +
                "); ";

        db.execSQL(SQL_CREATE_BAND_TABL);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //Delete old table a create a new one with new definition
        //Could be improved by preserving data through upgrade process
        db.execSQL("DROP TABLE IF EXISTS " + BandEntry.TABLE_NAME);
        onCreate(db);
    }

}
