package com.shostrand.mike.concertfinder.data;

import android.provider.BaseColumns;

/**
 * Created by Mike on 1/13/2018.
 */

public class BandContract {

    public static final class BandEntry implements BaseColumns {
        public static final String TABLE_NAME = "bands";
        public static final String COLUMN_BAND_NAME = "bandName";
        public static final String COLUMN_GENRE = "genre";
        public static final String COLUMN_TIMESTAMP = "timestamp";
    }
}
