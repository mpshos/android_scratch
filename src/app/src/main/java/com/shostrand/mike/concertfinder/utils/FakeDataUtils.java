package com.shostrand.mike.concertfinder.utils;


/**
 * Created by Mike on 1/15/2018.
 */

public class FakeDataUtils {

    public static String[][] getFakeData(){
        String[][] fakeData = new String[5][];
        fakeData[0] = new String[] {"Red Hot Chili Peppers",    "Rock/Funk"};
        fakeData[1] = new String[] {"The Killers",              "Alt Rock"};
        fakeData[2] = new String[] {"Run the Jewels",           "Rap"};
        fakeData[3] = new String[] {"Atlas Genius",             "Alt Rock"};
        fakeData[4] = new String[] {"Modest Mouse",             "Alt Rock"};

        return fakeData;
    }
}
