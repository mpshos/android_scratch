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
import android.widget.ProgressBar;
import android.widget.TextView;

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
    private TextView mNoResultsTextView;
    private ProgressBar mLoadingProgress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_band);

        //Get references to UI elements
        mSearchEditText = findViewById(R.id.et_search_text);
        mNoResultsTextView = findViewById(R.id.tv_no_results);
        mLoadingProgress = findViewById(R.id.pb_loading_indicator);

        //set up RecyclerView
        mResRecyclerView = findViewById(R.id.rv_band_results);
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

        // Display progress bar
        mResRecyclerView.setVisibility(View.GONE);
        mNoResultsTextView.setVisibility(View.GONE);
        mLoadingProgress.setVisibility(View.VISIBLE);

    }

    public void displayResults(boolean resultsFound){
        if(resultsFound){
            // Display results
            mResRecyclerView.setVisibility(View.VISIBLE);
            mLoadingProgress.setVisibility(View.GONE);
            mNoResultsTextView.setVisibility(View.GONE);
        }
        else{
            // Display error message
            mResRecyclerView.setVisibility(View.GONE);
            mLoadingProgress.setVisibility(View.GONE);
            mNoResultsTextView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(String name, String genre) {
        //Add insert parameters to new ContentValues object
        ContentValues values = new ContentValues();
        values.put(BandContract.BandEntry.COLUMN_BAND_NAME, name);
        values.put(BandContract.BandEntry.COLUMN_GENRE, genre);

        getContentResolver().insert(BandContract.BandEntry.CONTENT_URI, values);

        onBackPressed();
    }

    public class BandSearchTask extends AsyncTask<String, Void, ArrayList<BandResult>>{
        @Override
        protected ArrayList<BandResult> doInBackground(String... strings) {
            // Validate keyword
            if(strings.length == 0 || strings[0].isEmpty() ){
                return null;
            }

            // Fetch results
            URL searchUrl = NetworkUtils.getUrl(strings[0]);

            if(searchUrl != null){
                try{
                    String jsonResult = NetworkUtils.getResponseFromHttpUrl(searchUrl);
                    Log.v(AddBandActivity.class.getSimpleName(), jsonResult);

                    // Parse result to BandResult
                    return JsonUtils.parseEventfulResponse(jsonResult);

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
