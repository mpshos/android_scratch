package com.shostrand.mike.concertfinder.utils;

import com.shostrand.mike.concertfinder.data.BandResult;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by Mike on 1/17/2018.
 */

public class JsonUtils {

    private static final String OBJ_PERFORMERS = "performers";
    private static final String ARR_PERFORMERS = "performer";
    private static final String STR_NAME = "name";
    private static final String STR_SHORT_BIO = "short_bio";

    //Parse json data from Eventful API
    public static ArrayList<BandResult> parseEventfulResponse(String jsonResponse)
            throws JSONException{
        ArrayList<BandResult> results;
        JSONObject performerData;

        if(jsonResponse == null || jsonResponse.length() == 0){
            return null;
        }

        JSONObject resultObj = new JSONObject(jsonResponse);
        JSONObject performersObj = resultObj.optJSONObject(OBJ_PERFORMERS);

        if(performersObj == null){
            //no results
            return null;
        }

        JSONArray performersAry = performersObj.optJSONArray(ARR_PERFORMERS);

        //Performers will be an object or an array depending on result number
        if(performersAry != null){
            //multiple results found
            results = new ArrayList<BandResult>(performersAry.length());
            for(int i = 0; i < performersAry.length(); i++){
                performerData = performersAry.getJSONObject(i);
                results.add(new BandResult(performerData.getString(STR_NAME), performerData.getString(STR_SHORT_BIO)));
            }

            return results;
        }
        else{
            //one result found
            performerData = performersObj.getJSONObject(ARR_PERFORMERS);
            results = new ArrayList<BandResult>(1);
            results.add(new BandResult(performerData.getString(STR_NAME), performerData.getString(STR_SHORT_BIO)));
            return results;
        }

    } //function parseEventfulResponse

}// class JsonUtils
