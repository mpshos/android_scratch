package com.shostrand.mike.concertfinder;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.EditText;

import com.shostrand.mike.concertfinder.utils.FakeDataUtils;

public class AddBandActivity extends AppCompatActivity {

    private EditText mSearchEditText;
    private ResultsAdapter mAdapter;
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
        mAdapter = new ResultsAdapter(this);
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

    public class BandSearchTask extends AsyncTask<String, Void, String[][]> {
        @Override
        protected String[][] doInBackground(String... strings) {
            return FakeDataUtils.getFakeData();
        }

        @Override
        protected void onPostExecute(String[][] strings) {
            super.onPostExecute(strings);
            mAdapter.swapData(strings);
            displayResults(strings != null);
        }
    }
}
