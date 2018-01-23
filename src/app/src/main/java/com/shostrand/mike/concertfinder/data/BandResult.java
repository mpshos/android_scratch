package com.shostrand.mike.concertfinder.data;

/**
 * Created by Mike on 1/17/2018.
 */

public class BandResult {
        private String mName;
        private String mGenre;

        public BandResult(String name, String genre){
            mName = new String(name);
            mGenre = new String(genre);

        }

        public String getName() {
            return mName;
        }

        public String getGenre(){ return mGenre; }
}
