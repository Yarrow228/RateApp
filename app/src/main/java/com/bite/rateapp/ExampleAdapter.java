package com.bite.rateapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ExampleAdapter extends RecyclerView.Adapter<ExampleAdapter.ExampleViewHolder> {

    private ArrayList<ExampleItem> mExampleList;


    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

        public TextView mPostDate;
        public TextView mPostComment;
        public TextView mPostMark;
        public TextView mPostTime;

        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);

            mPostDate = itemView.findViewById(R.id.postDate);
            mPostComment = itemView.findViewById(R.id.postComment);
            mPostMark = itemView.findViewById(R.id.postMark);
            mPostTime = itemView.findViewById(R.id.postTime);
        }
    }

    public ExampleAdapter(ArrayList<ExampleItem> exampleList){
        mExampleList = exampleList;
    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.example_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ExampleItem currentItem = mExampleList.get(position);

        holder.mPostDate.setText(currentItem.getmPostDate());
        holder.mPostComment.setText(currentItem.getmPostComment());
        holder.mPostMark.setText(currentItem.getmPostMark());
        holder.mPostTime.setText(currentItem.getmPostTime());

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
