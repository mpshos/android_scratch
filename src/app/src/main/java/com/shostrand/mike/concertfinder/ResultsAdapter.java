package com.shostrand.mike.concertfinder;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mike on 1/15/2018.
 */

public class ResultsAdapter extends RecyclerView.Adapter<ResultsAdapter.ResultViewHolder> {
    private Context mContext;
    private String[][] mData;

    public ResultsAdapter(Context context){
        mContext = context;
        mData = null;
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
        if(mData == null || position >= mData.length || mData[ position ].length < 2){
            return;
        }

        holder.nameTextView.setText(mData[position][0]);
        holder.genreTextView.setText(mData[position][1]);
    }


    @Override
    public int getItemCount() {
        if(mData == null){
            return 0;
        }
        else{
            return mData.length;
        }
    }

    public void swapData(String[][] data){
        mData = data;
        this.notifyDataSetChanged();
    }

    //Inner class for creating ViewHolders
    class ResultViewHolder extends RecyclerView.ViewHolder {
        TextView nameTextView;
        TextView genreTextView;

        public ResultViewHolder(View itemView){
            super(itemView);

            nameTextView = itemView.findViewById(R.id.tv_name);
            genreTextView = itemView.findViewById(R.id.tv_genre);
        }
    }
}
