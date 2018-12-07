package com.bite.rateapp;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class ProfileItemAdapter extends RecyclerView.Adapter<ProfileItemAdapter.ExampleViewHolder> {

    private ArrayList<ProfileItem> mExampleList;


    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

        public TextView mPostDate;
        public TextView mPostComment;
        public TextView mPostMark;
        public TextView mPostTime;
        public TextView mPostTypeOfEvent;
        public TextView mPostLevelOfEvent;
        public TextView mPostPlaceOfEvent;



        public ExampleViewHolder(@NonNull View itemView) {
            super(itemView);

            mPostDate = itemView.findViewById(R.id.postDate);
            mPostComment = itemView.findViewById(R.id.postComment);
            mPostMark = itemView.findViewById(R.id.postMark);
            mPostTime = itemView.findViewById(R.id.postTime);

            mPostTypeOfEvent = itemView.findViewById(R.id.postMarkOrComp);
            mPostLevelOfEvent = itemView.findViewById(R.id.postLevelOfEvent);


            mPostPlaceOfEvent = itemView.findViewById(R.id.postPlaceOfEvent);
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
        holder.mPostMark.setText(currentItem.getmPostMark());
        holder.mPostTime.setText(currentItem.getmPostTime());
        holder.mPostTypeOfEvent.setText(currentItem.getmPostTypeOfEvent());
        holder.mPostLevelOfEvent.setText(currentItem.getmPostLevelOfEvent());
        holder.mPostPlaceOfEvent.setText(currentItem.getmPlaceOfEvent());


        if (currentItem.getmPostConfirm().equals("1")){
            holder.mPostMark.setText("+" + currentItem.getmPostMark());
            holder.mPostMark.setTextColor(Color.parseColor("#3F51B5"));
        }


        if (checkDate(currentItem.getmPostDate())){
            holder.mPostMark.setTextColor(Color.parseColor("#E0E0E0"));
        }

    }

    @Override
    public int getItemCount() {
        return mExampleList.size();
    }


    private boolean checkDate(String date){

        int preLastNum = Character.getNumericValue(date.charAt(date.length()-2));
        int lastNum = Character.getNumericValue(date.charAt(date.length()-1)) + 1;
        int num = preLastNum*10 + lastNum;


        String valid_until;
        if(num < 10){
            valid_until = date.substring(0, date.length()-1) + String.valueOf(num);
        }else{
            valid_until = date.substring(0, date.length()-2) + String.valueOf(num);
        }

        valid_until = valid_until.replace(".","/");

        SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yy");


        try{
            Date dateOld = sdf.parse(date);
            Date dateNew = sdf.parse(valid_until);

            if(new Date().after(dateNew)){
                return true;
            }
            return false;
        }catch(ParseException e){
            e.printStackTrace();
        }
        return false;
    }


    public void clear(){
        final int size = mExampleList.size();
        mExampleList.clear();
        notifyItemRangeRemoved(0,size);
    }

}
