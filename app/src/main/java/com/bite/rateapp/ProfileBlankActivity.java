package com.bite.rateapp;

/*This is class will be used as page for another user
* Maybe it should be fragment?!
*
*
* */

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bite.rateapp.fragments.ProfileFragment;
import com.bite.rateapp.fragments.RateListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class ProfileBlankActivity extends AppCompatActivity {


    //For Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    //Stuff
    private TextView tvName, tvSurname, tvEmail, tvRating, tvSchool;
    private Button btnNewPost, btnDeletePost;


    //For Recycler View
    private ArrayList<ProfileItem> mExampleList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int rating = 0;
    private int mExampleListLen = 0;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile_blank);

        tvName = (TextView) findViewById(R.id.tvUserName);
        tvSurname = (TextView) findViewById(R.id.tvUserSurname);
        tvRating = (TextView) findViewById(R.id.tvUserRating);
        tvSchool = (TextView) findViewById(R.id.tvUserSchool);
        mRecyclerView = findViewById(R.id.rcPostsList);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();



        String userId = getIntent().getStringExtra("EXTRA_ID");
        toastMessage(userId);

        loadUserInfo(userId);
        loadUserAchievements(userId);
        createExampleList();
        buildRecyclerView();


    }



    //Not working
    @Override
    public void onBackPressed() {
        Intent intent = new Intent(ProfileBlankActivity.this, MainActivity.class);
        startActivity(intent);
    }




    private void loadUserInfo(final String userId){

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserInfUtil uInfo = new UserInfUtil();

                uInfo.setName(dataSnapshot.child("Users").child(userId).child("name").getValue().toString());
                uInfo.setSurname(dataSnapshot.child("Users").child(userId).child("surname").getValue().toString());
                uInfo.setEmail(dataSnapshot.child("Users").child(userId).child("email").getValue().toString());
                uInfo.setStatus(dataSnapshot.child("Users").child(userId).child("status").getValue().toString());
                uInfo.setSchoolId(dataSnapshot.child("Users").child(userId).child("schoolId").getValue().toString());

                String school = dataSnapshot.child("Schools").child(uInfo.getSchoolId()).child("name").getValue().toString();

                tvName.setText(uInfo.getName());
                tvSurname.setText(uInfo.getSurname());
                tvSchool.setText(school);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    private void loadUserAchievements(final String userId){


        /*
        if (mExampleListLen != 0){

            for (int i = 0; i < mExampleListLen; i++){
                removeConfItem(i);
            }
            rating = 0;
        }
           */

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                int position = 0;
                FirebaseUser user = mAuth.getCurrentUser();

                for(DataSnapshot dsp : dataSnapshot.child("Users").child(userId).child("achievements").getChildren()){

                    String date = dsp.child("date").getValue().toString();

                    if(!date.equals("date")) {

                        String typeOfEvent = dsp.child("typeOfEvent").getValue().toString();
                        String timeOfPost = dsp.child("time").getValue().toString();
                        String confirm = dsp.child("confirmed").getValue().toString();


                        String[] typesOfEvent = getResources().getStringArray(R.array.typesOfEvent);
                        String[] levelsOfEvent = getResources().getStringArray(R.array.levelOfEvent);
                        String type;



                        if (typeOfEvent.equals("competition")) {

                            type = typesOfEvent[1];
                            String level= "";


                            String levelOfEvent = dsp.child("levelOfEvent").getValue().toString();
                            String reward = dataSnapshot.child("AchievementsTypes").child(typeOfEvent).child(levelOfEvent).getValue().toString();


                            //Crutch
                            if (levelOfEvent.equals("school")){
                                level = levelsOfEvent[1];
                            }else if (levelOfEvent.equals("district")){
                                level = levelsOfEvent[2];
                            }else if (levelOfEvent.equals("region")){
                                level = levelsOfEvent[3];
                            }else if (levelOfEvent.equals("republic")){
                                level = levelsOfEvent[4];
                            }



                            mExampleList.add(position, new ProfileItem(date, timeOfPost, dsp.child("comment").getValue().toString(), reward, type, level, confirm));
                            position += 1;



                            //Check if its confirmed and not out of time
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                            String nowDate = simpleDateFormat.format(new Date()).replace("/",".");

                            int preLastNum = Character.getNumericValue(date.charAt(date.length()-2));
                            int lastNum = Character.getNumericValue(date.charAt(date.length()-1)) + 1;
                            int num = preLastNum*10 + lastNum;

                            String newDate;
                            if(num < 10){
                                newDate = date.substring(0, date.length()-1) + String.valueOf(num);
                            }else{
                                newDate = date.substring(0, date.length()-2) + String.valueOf(num);
                            }

                            if(!nowDate.equals(newDate) && !dsp.child("confirmed").getValue().toString().equals("0")){
                                rating += Integer.parseInt(reward);
                            }
                        }

                        if (typeOfEvent.equals("mark")) {

                            String markOfEvent = dsp.child("markOfEvent").getValue().toString();
                            String reward = dataSnapshot.child("AchievementsTypes").child(typeOfEvent).child(markOfEvent).getValue().toString();


                            type = typesOfEvent[2];



                            mExampleList.add(position, new ProfileItem(date, timeOfPost, dsp.child("comment").getValue().toString(), reward, type, markOfEvent, confirm));
                            position += 1;



                            //Check if its confirmed and not out of time
                            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                            String nowDate = simpleDateFormat.format(new Date()).replace("/",".");

                            int preLastNum = Character.getNumericValue(date.charAt(date.length()-2));
                            int lastNum = Character.getNumericValue(date.charAt(date.length()-1)) + 1;
                            int num = preLastNum*10 + lastNum;

                            String newDate;
                            if(num < 10){
                                newDate = date.substring(0, date.length()-1) + String.valueOf(num);
                            }else{
                                newDate = date.substring(0, date.length()-2) + String.valueOf(num);
                            }

                            if(!nowDate.equals(newDate) && !dsp.child("confirmed").getValue().toString().equals("0")){
                                rating += Integer.parseInt(reward);

                            }
                        }
                    }
                }



                tvRating.setText(String.valueOf(rating));
                mAdapter.notifyDataSetChanged();
                mExampleListLen = position;
                toastMessage(String.valueOf(mExampleListLen));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }



    //createExampleList and buildRecycler view is for recycler view
    public void createExampleList(){
        mExampleList = new ArrayList<>();

    }
    public void buildRecyclerView(){

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ProfileItemAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }








    // just toasts, nothing interesting
    private void toastMessage(String message){
        Toast.makeText(this ,message,Toast.LENGTH_SHORT).show();
    }
}
