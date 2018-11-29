package com.bite.rateapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class ProfileItemAdapter extends RecyclerView.Adapter<ProfileItemAdapter.ExampleViewHolder> {

    private ArrayList<ProfileItem> mExampleList;


    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

        public TextView mPostDate;
        public TextView mPostComment;
        public TextView mPostMark;
        public TextView mPostTime;
        public TextView mPostTypeOfEvent;
        public TextView mPostLevelOfEvent;


        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);

            mPostDate = itemView.findViewById(R.id.postDate);
            mPostComment = itemView.findViewById(R.id.postComment);
            mPostMark = itemView.findViewById(R.id.postMark);
            mPostTime = itemView.findViewById(R.id.postTime);

            mPostTypeOfEvent = itemView.findViewById(R.id.postMarkOrComp);
            mPostLevelOfEvent = itemView.findViewById(R.id.postLevelOfEvent);

        }
    }

    public ProfileItemAdapter(ArrayList<ProfileItem> exampleList){
        mExampleList = exampleList;
    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ProfileItem currentItem = mExampleList.get(position);

        holder.mPostDate.setText(currentItem.getmPostDate());
        holder.mPostComment.setText(currentItem.getmPostComment());
        holder.mPostMark.setText("+" + currentItem.getmPostMark());
        holder.mPostTime.setText(currentItem.getmPostTime());
        holder.mPostTypeOfEvent.setText(currentItem.getmPostTypeOfEvent());
        holder.mPostLevelOfEvent.setText(currentItem.getmPostLevelOfEvent());

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }
}
