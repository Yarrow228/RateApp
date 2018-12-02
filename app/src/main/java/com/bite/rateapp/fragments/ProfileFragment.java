package com.bite.rateapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bite.rateapp.ConfItem;
import com.bite.rateapp.ConfItemAdapter;
import com.bite.rateapp.ProfileItemAdapter;
import com.bite.rateapp.ProfileItem;
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

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


public class ProfileFragment extends Fragment {


    //For Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private TextView tvName, tvSurname, tvEmail, tvRating;
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

    private ArrayList<ProfileItem> mExampleList;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;
    private int rating = 0;


    //For teacher account
    private ArrayList<ConfItem> mConfList;
    private RecyclerView mConfRecyclerView;
    private ConfItemAdapter mConfAdapter;
    private RecyclerView.LayoutManager mConfLayoutManager;


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        sharedPrefs = getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        //FirebaseUser user = mAuth.getCurrentUser();

    }



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        FirebaseUser user = mAuth.getCurrentUser();


        checkStatus(user.getUid());


        toastMessage(sharedPrefs.getString(STATUS_PREF, ""));


        if (sharedPrefs.getString(STATUS_PREF, "").equals("")){
            refreshFragment(container);

        }

        if (sharedPrefs.getString(STATUS_PREF, "").equals("0")){
            View view = inflater.inflate(R.layout.fragment_profile, container, false);

            tvRating = (TextView) view.findViewById(R.id.tvUserRating);
            tvName = (TextView) view.findViewById(R.id.tvUserName);
            tvSurname = (TextView) view.findViewById(R.id.tvUserSurname);
            btnNewPost = (Button) view.findViewById(R.id.btnNewPost);


            //Showing name and surname
            loadAndSaveUserInfo(user.getUid());
            tvName.setText(sharedPrefs.getString(NAME_PREF,"None")); //
            tvSurname.setText(sharedPrefs.getString(SURNAME_PREF, "None"));


            mRecyclerView = view.findViewById(R.id.rcPostsList);
            //Showing student achievements
            loadUserAchievements();
            createExampleList();
            buildRecyclerView();

            btnNewPost.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //Comment for new post
                    startActivity(new Intent(getActivity(), PostActivity.class));
                }
            });



            //refreshFragment(container);

            return view;
        }

        if (sharedPrefs.getString(STATUS_PREF,"").equals("1")){
            View view = inflater.inflate(R.layout.fragment_profile_teacher, container, false);

            tvName = (TextView) view.findViewById(R.id.tvUserName);
            tvSurname = (TextView) view.findViewById(R.id.tvUserSurname);

            //Showing name and surname
            loadAndSaveUserInfo(user.getUid());
            tvName.setText(sharedPrefs.getString(NAME_PREF,"None")); //
            tvSurname.setText(sharedPrefs.getString(SURNAME_PREF, "None"));


            mConfRecyclerView = view.findViewById(R.id.rcConfPostsList);
            loadNotConfirmedAchievements();
            createConfList();
            buildConfList();

            //refreshFragment(container);

            return view;
        }

        //refreshFragment(container);



        return null;
    }


    //check user status
    private void checkStatus(String userId){



        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserInfUtil uInfo = new UserInfUtil();
                uInfo.setStatus(dataSnapshot.child("status").getValue().toString());

                toastMessage("LOL");

                sharedPrefs =  getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
                ed = sharedPrefs.edit();

                ed.putString(STATUS_PREF, uInfo.getStatus());
                ed.apply();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });



    }

    private void refreshFragment(ViewGroup container){

        //Refresh fragment
        FragmentManager manager = getActivity().getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        Fragment newFragment = this;
        this.onDestroy();
        fragmentTransaction.replace(container.getId(), newFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

    }

    //createExampleList and buildRecycler view is for recycler view
    public void createExampleList(){
        mExampleList = new ArrayList<>();

    }
    public void buildRecyclerView(){

        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mAdapter = new ProfileItemAdapter(mExampleList);

        mRecyclerView.setLayoutManager(mLayoutManager);
        mRecyclerView.setAdapter(mAdapter);
    }



    //For status 1("teacher") list if achievements
    private void createConfList(){
        mConfList = new ArrayList<>();
    }
    private void buildConfList(){

        mConfRecyclerView.setHasFixedSize(true);
        mConfLayoutManager = new LinearLayoutManager(getActivity());
        mConfAdapter = new ConfItemAdapter(mConfList);

        mConfRecyclerView.setLayoutManager(mConfLayoutManager);
        mConfRecyclerView.setAdapter(mConfAdapter);


        mConfAdapter.setOnItemClickListener(new ConfItemAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                mConfList.get(position);
                toastMessage("Its work");
            }
        });
    }




    private void loadAndSaveUserInfo(String userId){

        mDatabase.child("Users").child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
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
                ed.putString(STATUS_PREF, uInfo.getStatus());
                ed.apply();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }



    private void loadUserAchievements(){

        mDatabase.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {


                int position = 0;
                FirebaseUser user = mAuth.getCurrentUser();

                for(DataSnapshot dsp : dataSnapshot.child("Users").child(user.getUid()).child("achievements").getChildren()){

                    String date = dsp.child("date").getValue().toString();

                    if(!date.equals("date")) {

                        String typeOfEvent = dsp.child("typeOfEvent").getValue().toString();
                        String timeOfPost = dsp.child("time").getValue().toString();

                        if (typeOfEvent.equals("competition")) {

                            String levelOfEvent = dsp.child("levelOfEvent").getValue().toString();
                            String reward = dataSnapshot.child("AchievementsTypes").child(typeOfEvent).child(levelOfEvent).getValue().toString();

                            mExampleList.add(position, new ProfileItem(date, timeOfPost, dsp.child("comment").getValue().toString(), reward, typeOfEvent, levelOfEvent));
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

                            mExampleList.add(position, new ProfileItem(date, timeOfPost, dsp.child("comment").getValue().toString(), reward, typeOfEvent, markOfEvent));
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
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }


    private void loadNotConfirmedAchievements(){

        mDatabase.child("Users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int position = 0;

                for(DataSnapshot dsp : dataSnapshot.getChildren()){

                    if (!dsp.child("status").equals("1")){


                        for (DataSnapshot insideDsp : dsp.child("achievements").getChildren()){

                            if (!insideDsp.child("date").getValue().toString().equals("date")){
                                if (insideDsp.child("confirmed").getValue().toString().equals("0")){

                                    String name = dsp.child("name").getValue().toString();
                                    String surname = dsp.child("surname").getValue().toString();
                                    String date = insideDsp.child("date").getValue().toString();
                                    String time = insideDsp.child("time").getValue().toString();
                                    String comment = insideDsp.child("comment").getValue().toString();
                                    String type = insideDsp.child("typeOfEvent").getValue().toString();


                                    if(type.equals("competition")){

                                        String level = insideDsp.child("levelOfEvent").getValue().toString();
                                        mConfList.add(position, new ConfItem(name, surname, date, time, comment, type, level));
                                        position += 1;
                                    }

                                    if(type.equals("mark")){

                                        String level = insideDsp.child("markOfEvent").getValue().toString();
                                        mConfList.add(position, new ConfItem(name, surname, date, time, comment, type, level));
                                        position += 1;
                                    }


                                }
                            }
                        }
                    }
                }

                mConfAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });









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
