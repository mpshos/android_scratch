package com.shostrand.mike.concertfinder;

import android.content.ContentValues;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.shostrand.mike.concertfinder.data.BandContract;
import com.shostrand.mike.concertfinder.data.BandResult;
import com.shostrand.mike.concertfinder.utils.JsonUtils;
import com.shostrand.mike.concertfinder.utils.NetworkUtils;

import java.net.URL;
import java.util.ArrayList;

public class AddBandActivity extends AppCompatActivity implements BandAdapter.BandAdapterOnClickHandler {

    private EditText mSearchEditText;
    private BandAdapter mAdapter;
    private RecyclerView mResRecyclerView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_band);

        //Get references to UI elements
        mSearchEditText = (EditText) findViewById(R.id.et_search_text);

        //set up RecyclerView
        mResRecyclerView = (RecyclerView) findViewById(R.id.rv_band_results);
        mResRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BandAdapter(this, this);
        mResRecyclerView.setAdapter(mAdapter);

    }

    public void searchForBand(View view){
        if(mSearchEditText.getText().length() == 0){
            return;
        }

        String keyword = mSearchEditText.getText().toString();

        //fire off query
        BandSearchTask searchTask = new BandSearchTask();
        searchTask.execute(keyword);

    }

    public void displayResults(boolean resultsFound){
        if(resultsFound){
            mResRecyclerView.setVisibility(View.VISIBLE);
        }
        else{
            mResRecyclerView.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onClick(String name, String genre) {
        //Add insert parameters to new ContentValues object
        ContentValues values = new ContentValues();
        values.put(BandContract.BandEntry.COLUMN_BAND_NAME, name);
        values.put(BandContract.BandEntry.COLUMN_GENRE, genre);

        getContentResolver().insert(BandContract.BandEntry.CONTENT_URI, values);
    }

    public class BandSearchTask extends AsyncTask<String, Void, ArrayList<BandResult>>{
        @Override
        protected ArrayList<BandResult> doInBackground(String... strings) {
            URL searchUrl = NetworkUtils.getUrl(mSearchEditText.getText().toString());

            if(searchUrl != null){
                try{
                    String jsonResult = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                    Log.v(AddBandActivity.class.getSimpleName(), jsonResult);

                    //parse result
                    ArrayList<BandResult> results = JsonUtils.parseEventfulResponse(jsonResult);
                    return results;

                } catch( Exception e){
                    e.printStackTrace();
                }
            }

            return null;
        }

        @Override
        protected void onPostExecute(ArrayList<BandResult> result) {
            super.onPostExecute(result);
            mAdapter.swapData(result);
            displayResults(result != null);
        }
    }
}
