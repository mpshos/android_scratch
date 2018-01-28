package com.shostrand.mike.concertfinder;

import android.content.Intent;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.shostrand.mike.concertfinder.data.BandContract;
import com.shostrand.mike.concertfinder.data.BandResult;

public class MainActivity extends AppCompatActivity {

    private RecyclerView mBandRecyclerView;
    private BandAdapter mAdapter;
    private Cursor mCursor;

    //band query projection
    public static final String[] BAND_PROJECTION = {
            BandContract.BandEntry.COLUMN_BAND_NAME,
            BandContract.BandEntry.COLUMN_GENRE
    };

    public static final int BAND_NAME_IDX = 0;
    public static final int BAND_GENRE_IDX = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up RecyclerView
        mBandRecyclerView = (RecyclerView) findViewById(R.id.rv_bands);
        mBandRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BandAdapter(this, null);
        mBandRecyclerView.setAdapter(mAdapter);

        //set up floating action button onclick handler
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addBandIntent = new Intent(MainActivity.this, AddBandActivity.class);
                startActivity(addBandIntent);
            }
        });

        mCursor = getContentResolver().query(
                BandContract.BandEntry.CONTENT_URI,
                BAND_PROJECTION,
                null,
                null,
                null
        );

        mAdapter.swapData(mCursor);
    }

}
