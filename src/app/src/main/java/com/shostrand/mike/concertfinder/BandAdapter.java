package com.shostrand.mike.concertfinder;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.shostrand.mike.concertfinder.data.BandResult;

import java.util.ArrayList;

/**
 * Created by Mike on 1/15/2018.
 */

public class BandAdapter extends RecyclerView.Adapter<BandAdapter.ResultViewHolder> {
    private Context mContext;
    private ArrayList<BandResult> mDataList;
    private Cursor mDataCursor;
    private boolean mUseCursor = false;
    private final BandAdapterOnClickHandler mClickHandler;

    public interface BandAdapterOnClickHandler {
        void onClick(String name, String genre);
    }

    public BandAdapter(Context context, BandAdapterOnClickHandler clickHandler){
        mContext = context;
        mDataList = null;
        mDataCursor = null;
        mClickHandler = clickHandler;
    }

    @Override
    public ResultViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //inflate scroll item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.result_layout, parent, false);
        return new ResultViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ResultViewHolder holder, int position) {
        if(mDataCursor != null) {
            mDataCursor.moveToPosition(position);

            holder.nameTextView.setText(mDataCursor.getString(MainActivity.BAND_NAME_IDX));
            holder.genreTextView.setText(mDataCursor.getString(MainActivity.BAND_GENRE_IDX));
            holder.itemView.setTag(mDataCursor.getInt(MainActivity.BAND_ID_IDX));
        }
        else {
            holder.nameTextView.setText(mDataList.get(position).getName());
            holder.genreTextView.setText(mDataList.get(position).getGenre());
        }
    }


    @Override
    public int getItemCount() {
        if(mDataList == null && mDataCursor == null) {
            return 0;
        }
        else if(mDataList != null) {
            return mDataList.size();
        }
        else {
            return mDataCursor.getCount();
        }
    }

    public void swapData(ArrayList<BandResult> data){
        mDataList = data;
        mDataCursor = null;
        mUseCursor = false;
        this.notifyDataSetChanged();
    }

    public void swapData(Cursor cursor) {
        mDataCursor = cursor;
        mDataList = null;
        mUseCursor = true;
        this.notifyDataSetChanged();
    }

    //Inner class for creating ViewHolders
    class ResultViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView nameTextView;
        TextView genreTextView;

        public ResultViewHolder(View itemView){
            super(itemView);

            nameTextView = itemView.findViewById(R.id.tv_name);
            genreTextView = itemView.findViewById(R.id.tv_genre);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            String name, genre;

            if(mClickHandler == null){
                return;
            }

            if(mDataCursor == null){
                BandResult band =  mDataList.get(getAdapterPosition());
                name = band.getName();
                genre = band.getGenre();
            }
            else{
                mDataCursor.moveToPosition(getAdapterPosition());
                name = mDataCursor.getString(MainActivity.BAND_NAME_IDX);
                genre = mDataCursor.getString(MainActivity.BAND_GENRE_IDX);
            }

            mClickHandler.onClick(name, genre);
        }
    }
}
