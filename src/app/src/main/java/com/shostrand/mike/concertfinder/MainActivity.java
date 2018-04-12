package com.shostrand.mike.concertfinder;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;
import android.support.v7.app.AppCompatActivity;
import android.support.design.widget.FloatingActionButton;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.View;

import com.shostrand.mike.concertfinder.data.BandContract;

public class MainActivity extends AppCompatActivity implements LoaderManager.LoaderCallbacks<Cursor> {

    private static final int LOADER_BAND_ID = 44;

    private RecyclerView mBandRecyclerView;
    private BandAdapter mAdapter;
    private Cursor mCursor;

    //band query projection
    public static final String[] BAND_PROJECTION = {
            BandContract.BandEntry._ID,
            BandContract.BandEntry.COLUMN_BAND_NAME,
            BandContract.BandEntry.COLUMN_GENRE
    };

    public static final int BAND_ID_IDX     = 0;
    public static final int BAND_NAME_IDX   = 1;
    public static final int BAND_GENRE_IDX  = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //set up RecyclerView
        mBandRecyclerView = (RecyclerView) findViewById(R.id.rv_bands);
        mBandRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new BandAdapter(this, null);
        mBandRecyclerView.setAdapter(mAdapter);

        //Create new ItemTouchHelper to handle swipe to delete
        new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0,ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                int id = (int) viewHolder.itemView.getTag();

                Uri deleteUri = BandContract.BandEntry.CONTENT_URI.buildUpon().appendPath(Integer.toString(id)).build();

                getContentResolver().delete(deleteUri, null, null);

            }
        }).attachToRecyclerView(mBandRecyclerView);

        //set up floating action button onclick handler
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent addBandIntent = new Intent(MainActivity.this, AddBandActivity.class);
                startActivity(addBandIntent);
            }
        });

        // Set up cursor loader
        getSupportLoaderManager().initLoader(LOADER_BAND_ID, null, this);

    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        mAdapter.swapCursor(data);
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        switch(id){
            case LOADER_BAND_ID:
                return new CursorLoader(this, BandContract.BandEntry.CONTENT_URI, BAND_PROJECTION, null, null, null);

            default:
                throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        getSupportLoaderManager().restartLoader(LOADER_BAND_ID, null, this);
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }
}
