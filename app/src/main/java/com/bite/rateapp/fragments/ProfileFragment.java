package com.bite.rateapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bite.rateapp.ExampleAdapter;
import com.bite.rateapp.ExampleItem;
import com.bite.rateapp.MainActivity;
import com.bite.rateapp.PostActivity;
import com.bite.rateapp.R;
import com.bite.rateapp.UserInfUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class ProfileFragment extends Fragment {


    //For Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private TextView tvName, tvSurname, tvEmail;
    private Button btnNewPost, btnDeletePost;

    //For Shared Preferences(get and save data)
    private SharedPreferences sharedPrefs;
    SharedPreferences.Editor ed;
    public static final String PREF = "myprefs";
    public static final String NAME_PREF = "namePref";
    public static final String SURNAME_PREF = "surnamePref";
    public static final String EMAIL_PREF = "emailPref";
    public static final String STATUS_PREF = "statusPref";

    //For Recycler View

    private ArrayList<ExampleItem> mExampleList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvName = (TextView) view.findViewById(R.id.tvUserName);
        tvSurname = (TextView) view.findViewById(R.id.tvUserSurname);
        btnNewPost = (Button) view.findViewById(R.id.btnNewPost);
        mRecyclerView = view.findViewById(R.id.rcPostsList);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        loadAndSaveUserInfo(user.getUid());
        loadUserAchievements();

        //Showing name and surname
        sharedPrefs = getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        //tvEmail.setText(sharedPrefs.getString(EMAIL_PREF,""));
        tvName.setText(sharedPrefs.getString(NAME_PREF,"None")); //
        tvSurname.setText(sharedPrefs.getString(SURNAME_PREF, "None"));

        createExampleList();
        buildRecyclerView();


        btnNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comment for new post
                startActivity(new Intent(getActivity(), PostActivity.class));
                //((Activity) getActivity()).overridePendingTransition(0,0);
            }
        });


        return view;
    }


    //createExampleList and buildRecycler view is for recycler view
    public void createExampleList(){
        mExampleList = new ArrayList<>();

    }
    public void buildRecyclerView(){

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }

    /*
    //onCreateOptionsMenu and onOptionsItemSelected is for toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getActivity().getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    */

    private void loadAndSaveUserInfo(String userID){


        mDatabase.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserInfUtil uInfo = new UserInfUtil();

                uInfo.setName(dataSnapshot.child("name").getValue().toString());
                uInfo.setSurname(dataSnapshot.child("surname").getValue().toString());
                uInfo.setEmail(dataSnapshot.child("email").getValue().toString());
                uInfo.setStatus(dataSnapshot.child("status").getValue().toString());

                sharedPrefs =  getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
                ed = sharedPrefs.edit();

                ed.putString(NAME_PREF, uInfo.getName());
                ed.putString(SURNAME_PREF, uInfo.getSurname());
                ed.putString(EMAIL_PREF, uInfo.getEmail());
                ed.apply();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }



    private void loadUserAchievements(){

        FirebaseUser user = mAuth.getCurrentUser();

        mDatabase.child("Users").child(user.getUid()).child("achievements").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int position = 0;


                for(DataSnapshot dsp : dataSnapshot.getChildren()){
                    if(!dsp.child("date").getValue().toString().equals("date")) {


                        if (dsp.child("typeOfEvent").getValue().toString().equals("competition")) {
                            mExampleList.add(position, new ExampleItem(dsp.child("date").getValue().toString(), dsp.child("time").getValue().toString(), dsp.child("comment").getValue().toString(), dsp.child("rateMark").getValue().toString(), dsp.child("typeOfEvent").getValue().toString(), dsp.child("levelOfEvent").getValue().toString()));
                            position += 1;

                        }

                        if (dsp.child("typeOfEvent").getValue().toString().equals("mark")) {
                            mExampleList.add(position, new ExampleItem(dsp.child("date").getValue().toString(), dsp.child("time").getValue().toString(), dsp.child("comment").getValue().toString(), dsp.child("rateMark").getValue().toString(), dsp.child("typeOfEvent").getValue().toString(), dsp.child("markOfEvent").getValue().toString()));
                            position += 1;
                        }

                    }
                }

                mAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void showUserData(){

    }


    /*In future
    public void removeItem(int position){
        if(mAdapter.getItemCount() != 0){
            mExampleList.remove(position);
            mAdapter.notifyDataSetChanged();
        }
    }
    */



    // just toasts, nothing interesting
    private void toastMessage(String message){
        Toast.makeText(getActivity() ,message,Toast.LENGTH_SHORT).show();
    }




}
