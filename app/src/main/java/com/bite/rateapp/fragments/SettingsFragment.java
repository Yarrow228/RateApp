package com.bite.rateapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bite.rateapp.MainActivity;
import com.bite.rateapp.R;
import com.bite.rateapp.StartActivity;
import com.bite.rateapp.UserInfUtil;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class SettingsFragment extends Fragment {

    //For Firebase
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private Button btnSignOut, btnAccountSettings;
    private TextView tvName, tvSurname;


    //For Shared Preferences(get and save data)
    private SharedPreferences sharedPrefs;
    SharedPreferences.Editor ed;
    public static final String PREF = "myprefs";
    public static final String STATUS_PREF = "statusPref";


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();




        View view = inflater.inflate(R.layout.fragment_settings, container, false);


        Toolbar toolbarFragment = (Toolbar)getActivity().findViewById(R.id.toolbar);
        ((MainActivity)getActivity()).setToolbar(toolbarFragment, getResources().getString(R.string.settings_fragment_label));


        tvName = (TextView) view.findViewById(R.id.tvSettingsUserName);
        tvSurname = (TextView) view.findViewById(R.id.tvSettingsUserSurname);
        btnSignOut = (Button) view.findViewById(R.id.btnSignOut);
        btnAccountSettings = (Button) view.findViewById(R.id.btnAccountSettings);


        FirebaseUser user = mAuth.getCurrentUser();

        loadUserInfo(user.getUid());


        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                deleteUserData();

                toastMessage(sharedPrefs.getString(STATUS_PREF, ""));

                mAuth.signOut();

                toastMessage("Signing out...");

                startActivity(new Intent(getActivity(), StartActivity.class));
                getActivity().finish();
            }
        });


        btnAccountSettings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toastMessage("Work in progress");
            }
        });

        return view;
    }



    private void loadUserInfo(final String userId){

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserInfUtil uInfo = new UserInfUtil();

                uInfo.setName(dataSnapshot.child("Users").child(userId).child("name").getValue().toString());
                uInfo.setSurname(dataSnapshot.child("Users").child(userId).child("surname").getValue().toString());
                uInfo.setEmail(dataSnapshot.child("Users").child(userId).child("email").getValue().toString());

                tvName.setText(uInfo.getName());
                tvSurname.setText(uInfo.getSurname());
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }


    private void deleteUserData(){

        sharedPrefs =  getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
        ed = sharedPrefs.edit();

        ed.putString(STATUS_PREF, "");
        ed.apply();

    }

    // just toasts, nothing interesting
    private void toastMessage(String message){
        Toast.makeText(getActivity() ,message,Toast.LENGTH_SHORT).show();
    }
}

