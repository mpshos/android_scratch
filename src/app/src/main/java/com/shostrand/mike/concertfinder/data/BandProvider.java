package com.shostrand.mike.concertfinder.data;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.CancellationSignal;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Created by michaelshostrand on 1/24/18.
 */

public class BandProvider extends ContentProvider {

    private static final int CODE_BAND = 100;
    private static final int CODE_BAND_W_ID = 101;

    private BandDbHelper mOpenDbHelper;

    private static final UriMatcher sUriMatcher = buildUriMatcher();

    /**
     *
     * Create a new uri matcher for accepted content uris
     */
    public static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);

        matcher.addURI(BandContract.CONTENT_AUTHORITY, BandContract.BAND_PATH, CODE_BAND);
        matcher.addURI(BandContract.CONTENT_AUTHORITY, BandContract.BAND_PATH + "/#", CODE_BAND_W_ID);

        return matcher;
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int numRowsDeleted = 0;

        switch(sUriMatcher.match(uri)){

            case CODE_BAND_W_ID:
                String id = uri.getLastPathSegment();

                numRowsDeleted = mOpenDbHelper.getWritableDatabase().delete(
                        BandContract.BandEntry.TABLE_NAME,
                        BandContract.BandEntry._ID + "=?" + id,
                        null
                );
                break;

            default:
                throw new UnsupportedOperationException("Delete doesn't support this uri: " + uri.toString());
        }

        return numRowsDeleted;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        throw new RuntimeException("Not implementing getType at this time");
    }

    @Override
    public boolean onCreate() {
        mOpenDbHelper = new BandDbHelper(getContext());
        return true;
    }

    @Override
    public Uri insert(@NonNull Uri uri, ContentValues values) {
        Uri returnUri;

        switch(sUriMatcher.match(uri)){
            case CODE_BAND:
                final SQLiteDatabase db = mOpenDbHelper.getWritableDatabase();
                long id = db.insert(BandContract.BandEntry.TABLE_NAME, null, values);
                if ( id > 0 ) {
                    returnUri = ContentUris.withAppendedId(BandContract.BandEntry.CONTENT_URI, id);

                    //Notify the resolver that the uri has been updated
                    getContext().getContentResolver().notifyChange(uri, null);
                } else {
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                }
                break;

            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri.toString());
        }

        return returnUri;
    }

    @Override
    public Cursor query(@NonNull Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        switch(sUriMatcher.match(uri)){
            case CODE_BAND_W_ID:
                return null;

            case CODE_BAND:
            default:
                //Just pass through as SQLite query
                return( mOpenDbHelper.getReadableDatabase().query(
                        BandContract.BandEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                ));
        }
    }

    @Override
    public int update(@NonNull Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        throw new RuntimeException("We are not implementing update at this time");
    }
}
