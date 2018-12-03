package com.bite.rateapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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

    private Button btnSignOut;


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

        //checkStatus();



        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        btnSignOut = (Button) view.findViewById(R.id.btnSignOut);

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

        return view;
    }

    private void checkStatus(){

        FirebaseUser user = mAuth.getCurrentUser();

        mDatabase.child("Users").child(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserInfUtil uInfo = new UserInfUtil();
                uInfo.setStatus(dataSnapshot.child("status").getValue().toString());

                sharedPrefs =  getActivity().getSharedPreferences(PREF, Context.MODE_PRIVATE);
                ed = sharedPrefs.edit();

                ed.putString(STATUS_PREF, uInfo.getStatus());
                ed.apply();
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

