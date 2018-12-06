package com.bite.rateapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bite.rateapp.MainActivity;
import com.bite.rateapp.PostActivity;
import com.bite.rateapp.ProfileBlankActivity;
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
    private RateItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    private DatabaseReference mDatabase;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rate_list, container, false);


        Toolbar toolbarFragment = (Toolbar)getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setToolbar(toolbarFragment, getResources().getString(R.string.rating_fragment_label));



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

        mAdapter.setOnItemClickListener(new RateItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mRateList.get(position);
                toastMessage(String.valueOf(position));

                rateItemClicked(position);
            }
        });
    }




    private void loadData(){

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                //Should check if its a teacher!!!!
                int position = 0;


                for(DataSnapshot dsp : dataSnapshot.child("Users").getChildren()){

                    if(!dsp.child("name").getValue().toString().equals("name") && !dsp.child("status").getValue().toString().equals("1")) {

                        int rating = 0;
                        String userID = dsp.getKey().toString();
                        //toastMessage(userID);

                        for (DataSnapshot inDsp : dsp.child("achievements").getChildren()){

                            if (!inDsp.child("date").getValue().toString().equals("date")){


                                if (String.valueOf(inDsp.child("confirmed").getValue()).equals("1")) {

                                    String typeOfEvent = inDsp.child("typeOfEvent").getValue().toString();


                                    if (typeOfEvent.equals("competition")) {

                                        String levelOfEvent = inDsp.child("levelOfEvent").getValue().toString();

                                        rating += Integer.parseInt(dataSnapshot.child("AchievementsTypes").child(typeOfEvent).child(levelOfEvent).getValue().toString());
                                    }


                                    if (typeOfEvent.equals("mark")){

                                        String markOfEvent = inDsp.child("markOfEvent").getValue().toString();

                                        rating += Integer.parseInt(dataSnapshot.child("AchievementsTypes").child(typeOfEvent).child(markOfEvent).getValue().toString());
                                    }
                                }
                            }
                        }


                        mRateList.add(position, new RateItem(dsp.child("name").getValue().toString(),dsp.child("surname").getValue().toString(), String.valueOf(rating), userID));
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



    private void rateItemClicked(int position){
        RateItem item = mRateList.get(position);


        //Will be remade in future(at the moment using crutch)
        String userId = item.getmUserId();


        Intent intent = new Intent(getActivity(), ProfileBlankActivity.class);
        intent.putExtra("EXTRA_ID", userId);
        startActivity(intent);
    }





    // just toasts, nothing interesting
    private void toastMessage(String message){
        Toast.makeText(getActivity() ,message,Toast.LENGTH_SHORT).show();
    }

}
