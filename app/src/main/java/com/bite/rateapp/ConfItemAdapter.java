package com.bite.rateapp;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class ConfItemAdapter extends RecyclerView.Adapter<ConfItemAdapter.ExampleViewHolder> {

    private ArrayList<ConfItem> mConfList;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){mListener = listener;}







    public static class ExampleViewHolder extends RecyclerView.ViewHolder{

        public TextView mConfName;
        public TextView mConfSurname;
        public TextView mConfDate;
        public TextView mConfTime;
        public TextView mConfComment;
        public TextView mConfType;
        public TextView mConfLevel;
        public ImageView mConfCheck;


        public ExampleViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);

            mConfName = itemView.findViewById(R.id.conf_list_card_name);
            mConfSurname = itemView.findViewById(R.id.conf_list_card_surname);
            mConfDate = itemView.findViewById(R.id.conf_list_card_date);
            mConfTime = itemView.findViewById(R.id.conf_list_card_time);
            mConfComment = itemView.findViewById(R.id.conf_list_card_comment);
            mConfType = itemView.findViewById(R.id.conf_list_cardType);
            mConfLevel = itemView.findViewById(R.id.conf_list_levelEvent);


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

    public ConfItemAdapter(ArrayList<ConfItem> exampleList){
        mConfList = exampleList;
    }


    @NonNull
    @Override
    public ExampleViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.conf_list_item, parent, false);
        ExampleViewHolder evh = new ExampleViewHolder(v, mListener);
        return evh;
    }

    @Override
    public void onBindViewHolder(@NonNull ExampleViewHolder holder, int position) {
        ConfItem currentItem = mConfList.get(position);

        holder.mConfName.setText(currentItem.getmConfName());
        holder.mConfSurname.setText(currentItem.getmConfSurname());
        holder.mConfDate.setText(currentItem.getmConfDate());
        holder.mConfTime.setText(currentItem.getmConfTime());
        holder.mConfComment.setText(currentItem.getmConfComment());
        holder.mConfType.setText(currentItem.getmConfType());
        holder.mConfLevel.setText(currentItem.getmConfLevel());
    }

    @Override
    public int getItemCount() {
        return mConfList.size();
    }
}
