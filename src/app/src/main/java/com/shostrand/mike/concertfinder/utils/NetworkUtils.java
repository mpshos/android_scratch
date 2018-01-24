package com.shostrand.mike.concertfinder.utils;

import android.net.Uri;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

/**
 * Created by michaelshostrand on 1/23/18.
 * NetworkUtils class for handling api interaction
 */

public class NetworkUtils {

    private static final String TAG = NetworkUtils.class.getSimpleName();

    private static final String API_SEARCH_URL =
            "http://api.eventful.com/json/performers/search?app_key=TpFJnpt4D3CF4qGm";

    private static final String KEYWORD_PARAM = "keywords";

    /**
     * Returns a URL given a search keyword
     * @param keyword The search keyword.
     * @return URL to search for
     */
    public static URL getUrl(String keyword){
        Uri searchUri = Uri.parse(API_SEARCH_URL).buildUpon()
                .appendQueryParameter(KEYWORD_PARAM, keyword).build();

        try {
            URL weatherQueryUrl = new URL(searchUri.toString());
            Log.v(TAG, "URL: " + weatherQueryUrl);
            return weatherQueryUrl;
        } catch (MalformedURLException e) {
            e.printStackTrace();
            return null;
        }

    }


    /**
     * This method returns the entire result from the HTTP response.
     *
     * @param url The URL to fetch the HTTP response from.
     * @return The contents of the HTTP response, null if no response
     * @throws IOException Related to network and stream reading
     */
    public static String getResponseFromHttpUrl(URL url) throws IOException {
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        try {
            InputStream in = urlConnection.getInputStream();

            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            String response = null;
            if (hasInput) {
                response = scanner.next();
            }
            scanner.close();
            return response;
        } finally {
            urlConnection.disconnect();
        }
    }
}
