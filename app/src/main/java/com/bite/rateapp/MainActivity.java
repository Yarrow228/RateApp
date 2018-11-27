package com.bite.rateapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;


public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {


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




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        tvName = (TextView) findViewById(R.id.tvUserName);
        tvSurname = (TextView) findViewById(R.id.tvUserSurname);
        //tvEmail = (TextView) findViewById(R.id.tvUserEmail);
        btnNewPost = (Button) findViewById(R.id.btnNewPost);

        //For Firebase
        mDatabase = FirebaseDatabase.getInstance().getReference();



        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


        btnNewPost.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Comment for new post
                startActivity(new Intent(MainActivity.this, PostActivity.class));
            }
        });
    }


    @Override
    protected void onStart() {
        super.onStart();

        //Loading name & surname
        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        loadAndSaveUserInfo(user.getUid());

        //For Recycler View
        loadUserAchievements();
        createExampleList();
        buildRecyclerView();


        showUserData();
    }



    //createExampleList and buildRecycler view is for recycler view
    public void createExampleList(){
        mExampleList = new ArrayList<>();

    }
    public void buildRecyclerView(){
        mRecyclerView = findViewById(R.id.rcPostsList);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mAdapter = new ExampleAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }



    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }



    //onCreateOptionsMenu and onOptionsItemSelected is for toolbar
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        /*if (id == R.id.action_settings) {
            return true;
        }
        */
        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_settings) {

        } else if (id == R.id.nav_rating) {

        } else if (id == R.id.nav_share) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    private void loadAndSaveUserInfo(String userID){


        mDatabase.child("Users").child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserInfUtil uInfo = new UserInfUtil();

                uInfo.setName(dataSnapshot.child("name").getValue().toString());
                uInfo.setSurname(dataSnapshot.child("surname").getValue().toString());
                uInfo.setEmail(dataSnapshot.child("email").getValue().toString());
                uInfo.setStatus(dataSnapshot.child("status").getValue().toString());



                sharedPrefs = getSharedPreferences(PREF, Context.MODE_PRIVATE);
                ed = sharedPrefs.edit();

                ed.putString(NAME_PREF, uInfo.getName());
                ed.putString(SURNAME_PREF, uInfo.getSurname());
                ed.putString(EMAIL_PREF, uInfo.getEmail());
                ed.apply();

                toastMessage(sharedPrefs.getString(NAME_PREF, "None"));

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
                    if(!dsp.child("date").getValue().toString().equals("date")){
                        mExampleList.add(position, new ExampleItem(dsp.child("date").getValue().toString(), dsp.child("time").getValue().toString(), dsp.child("comment").getValue().toString(), dsp.child("mark").getValue().toString()));
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




    private void showUserData(){
        sharedPrefs = getSharedPreferences(PREF, Context.MODE_PRIVATE);

        //tvEmail.setText(sharedPrefs.getString(EMAIL_PREF,""));
        tvName.setText(sharedPrefs.getString(NAME_PREF,"None"));
        tvSurname.setText(sharedPrefs.getString(SURNAME_PREF, "None"));
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
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
