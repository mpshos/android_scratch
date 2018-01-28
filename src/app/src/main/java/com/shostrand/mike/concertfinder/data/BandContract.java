package com.shostrand.mike.concertfinder.data;

import android.net.Uri;
import android.provider.BaseColumns;

/**
 * Created by Mike on 1/13/2018.
 */

public class BandContract {

    public static final String CONTENT_AUTHORITY = "com.shostrand.mike.concertfinder";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final String BAND_PATH = "band";

    public static final class BandEntry implements BaseColumns {

        //Band table content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon().appendPath(BAND_PATH).build();

        //Band DB structure
        public static final String TABLE_NAME = "bands";
        public static final String COLUMN_BAND_NAME = "bandName";
        public static final String COLUMN_GENRE = "genre";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
