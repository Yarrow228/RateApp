package com.bite.rateapp.fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bite.rateapp.ProfileItem;
import com.bite.rateapp.ProfileItemAdapter;
import com.bite.rateapp.R;
import com.bite.rateapp.RateItem;
import com.bite.rateapp.RateItemAdapter;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class RateListFragment extends Fragment {

    private ArrayList<RateItem> mRateList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private DatabaseReference mDatabase;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rate_list, container, false);

        mRecyclerView = view.findViewById(R.id.rcRateList);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        createExampleList();
        buildRecyclerView();
        loadData();

        return view;
    }


    public void createExampleList(){
        mRateList = new ArrayList<>();

    }
    public void buildRecyclerView(){

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new RateItemAdapter(mRateList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    private void loadData(){

        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Should check if its a teacher!!!!


                int position = 0;

                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    if(!dsp.child("name").getValue().toString().equals("name")) {

                        mRateList.add(position, new RateItem(dsp.child("name").getValue().toString(),dsp.child("surname").getValue().toString(),"100"));
                        position += 1;
                    }
                }


                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    // just toasts, nothing interesting
    private void toastMessage(String message){
        Toast.makeText(getActivity() ,message,Toast.LENGTH_SHORT).show();
    }

}
