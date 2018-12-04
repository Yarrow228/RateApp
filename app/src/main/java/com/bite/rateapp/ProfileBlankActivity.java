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
import android.support.v7.widget.RecyclerView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bite.rateapp.fragments.ProfileFragment;
import com.bite.rateapp.fragments.RateListFragment;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class ProfileBlankActivity extends AppCompatActivity {


    //For Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    //Stuff
    private TextView tvName, tvSurname, tvEmail, tvRating;
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
        mRecyclerView = findViewById(R.id.rcPostsList);



        String userId = getIntent().getStringExtra("EXTRA_ID");
        toastMessage(userId);

        //loadUserInfo(userId);

    }



    //Not working
    @Override
    public void onBackPressed() {
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, new RateListFragment()).commit();
        finish();
    }




    private void loadUserInfo(String userId){

        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserInfUtil uInfo = new UserInfUtil();

                uInfo.setName(dataSnapshot.child("name").getValue().toString());
                uInfo.setSurname(dataSnapshot.child("surname").getValue().toString());
                uInfo.setEmail(dataSnapshot.child("email").getValue().toString());
                uInfo.setStatus(dataSnapshot.child("status").getValue().toString());


                //tvName.setText(uInfo.getName());
                //tvSurname.setText(uInfo.getSurname());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });




    }




    // just toasts, nothing interesting
    private void toastMessage(String message){
        Toast.makeText(this ,message,Toast.LENGTH_SHORT).show();
    }
}
