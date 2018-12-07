package com.bite.rateapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

import static com.bite.rateapp.fragments.ProfileFragment.PREF;

public class RateListFragment extends Fragment {

    private ArrayList<RateItem> mRateList;
    private RecyclerView mRecyclerView;
    private RateItemAdapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;


    //For Shared Preferences(get and save data)
    private SharedPreferences sharedPrefs;
    SharedPreferences.Editor ed;
    public static final String PREF = "myprefs";
    public static final String NAME_PREF = "namePref";
    public static final String SURNAME_PREF = "surnamePref";
    public static final String EMAIL_PREF = "emailPref";
    public static final String STATUS_PREF = "statusPref";
    public static final String SCHOOL_PREF = "schoolPref";
    public static final String SCHOOL_ID_PREF = "schoolIdPref";


    private DatabaseReference mDatabase;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_rate_list, container, false);
        setHasOptionsMenu(true);

        Toolbar toolbarFragment = (Toolbar)getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setToolbar(toolbarFragment, getResources().getString(R.string.rating_fragment_label));



        mRecyclerView = view.findViewById(R.id.rcRateList);
        mDatabase = FirebaseDatabase.getInstance().getReference();

        createExampleList();
        buildRecyclerView();
        loadDataSchool();



        return view;
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {

        inflater.inflate(R.menu.fragment_rating_toolbar, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case R.id.sub_item1:
                loadDataSchool();
                return true;

            case R.id.sub_item2:
                toastMessage("District");

                loadDataDistrict();
                return true;

            case R.id.sub_item3:
                toastMessage("Region");


                loadDataRegion();
                return true;

            case R.id.sub_item4:
                toastMessage("Republic");
                loadData();
                return true;
        }

        return super.onOptionsItemSelected(item);
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


    private void loadDataSchool(){


        sharedPrefs =  getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mAdapter.clear();


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int position = 0;

                for(DataSnapshot dsp : dataSnapshot.child("Users").getChildren()){

                    String name = dsp.child("name").getValue().toString();

                    //&& schoolId.equals(userSchoolId)
                    if(!name.equals("name")) {

                        String userID = dsp.getKey().toString();
                        String status = dsp.child("status").getValue().toString();
                        String schoolId = dsp.child("schoolId").getValue().toString();
                        String userSchoolId = sharedPrefs.getString("schoolIdPref","");

                        String district = dataSnapshot.child("Schools").child(schoolId).child("district").getValue().toString();
                        String userDistrict = dataSnapshot.child("Schools").child(userSchoolId).child("district").getValue().toString();

                        String region = dataSnapshot.child("Schools").child(schoolId).child("region").getValue().toString();
                        String userRegion = dataSnapshot.child("Schools").child(userSchoolId).child("region").getValue().toString();

                        if(status.equals("0") && region.equals(userRegion)  && district.equals(userDistrict) && schoolId.equals(userSchoolId)){


                            int rating = 0;

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

                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }


    private void loadDataDistrict(){
        sharedPrefs =  getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mAdapter.clear();


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int position = 0;

                for(DataSnapshot dsp : dataSnapshot.child("Users").getChildren()){

                    String name = dsp.child("name").getValue().toString();

                    //&& schoolId.equals(userSchoolId)
                    if(!name.equals("name")) {

                        String userID = dsp.getKey().toString();
                        String status = dsp.child("status").getValue().toString();
                        String schoolId = dsp.child("schoolId").getValue().toString();
                        String userSchoolId = sharedPrefs.getString("schoolIdPref","");
                        String district = dataSnapshot.child("Schools").child(schoolId).child("district").getValue().toString();
                        String userDistrict = dataSnapshot.child("Schools").child(userSchoolId).child("district").getValue().toString();
                        String region = dataSnapshot.child("Schools").child(schoolId).child("region").getValue().toString();
                        String userRegion = dataSnapshot.child("Schools").child(userSchoolId).child("region").getValue().toString();


                        if(status.equals("0") && region.equals(userRegion) && district.equals(userDistrict)){


                            int rating = 0;

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

                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void loadDataRegion(){

        sharedPrefs =  getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mAdapter.clear();


        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int position = 0;

                for(DataSnapshot dsp : dataSnapshot.child("Users").getChildren()){

                    String name = dsp.child("name").getValue().toString();

                    //&& schoolId.equals(userSchoolId)
                    if(!name.equals("name")) {

                        String userID = dsp.getKey().toString();
                        String status = dsp.child("status").getValue().toString();
                        String schoolId = dsp.child("schoolId").getValue().toString();
                        String userSchoolId = sharedPrefs.getString("schoolIdPref","");
                        String region = dataSnapshot.child("Schools").child(schoolId).child("region").getValue().toString();
                        String userRegion = dataSnapshot.child("Schools").child(userSchoolId).child("region").getValue().toString();

                        if(status.equals("0") && region.equals(userRegion)){


                            int rating = 0;

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

                        mAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void loadData(){

        //Should clear Recycler view!!!
        mAdapter.clear();

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


    private void clearRecycylerView(){

    }



    // just toasts, nothing interesting
    private void toastMessage(String message){
        Toast.makeText(getActivity() ,message,Toast.LENGTH_SHORT).show();
    }

}
