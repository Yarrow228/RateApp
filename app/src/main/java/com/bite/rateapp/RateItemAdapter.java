package com.bite.rateapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;

public class RateItemAdapter extends RecyclerView.Adapter<RateItemAdapter.ExampleViewHolder>{


    private ArrayList<RateItem> mRateList;
    private OnItemClickListener mListener;


    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }


    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

        public TextView mUserName;
        public TextView mUserSurname;
        public TextView mUserRating;


        public ExampleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mUserName = itemView.findViewById(R.id.rate_list_card_name);
            mUserSurname = itemView.findViewById(R.id.rate_list_card_surname);
            mUserRating = itemView.findViewById(R.id.rate_list_card_rating);


            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(listener != null){

                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onItemClick(position);
                        }
                    }
                }
            });

        }
    }


    public RateItemAdapter(ArrayList<RateItem> exampleList){
        mRateList = exampleList;
    }


    @NonNull
    @Override
    public RateItemAdapter.ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.rate_list_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull RateItemAdapter.ExampleViewHolder holder, int position) {
        RateItem currentItem = mRateList.get(position);

        holder.mUserName.setText(currentItem.getmUserName());
        holder.mUserSurname.setText(currentItem.getmUserSurname());
        holder.mUserRating.setText(currentItem.getmUserRating());


    }

    @Override
    public int getItemCount() {
            return mRateList.size();
        }


    public void clear(){
        final int size = mRateList.size();
        mRateList.clear();
        notifyItemRangeRemoved(0,size);
    }

}
