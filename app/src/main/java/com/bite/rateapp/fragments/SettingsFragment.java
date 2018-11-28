package com.bite.rateapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.bite.rateapp.R;
import com.bite.rateapp.StartActivity;
import com.google.firebase.auth.FirebaseAuth;


public class SettingsFragment extends Fragment {


    private FirebaseAuth mAuth;
    private Button btnSignOut;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        mAuth = FirebaseAuth.getInstance();

        View view = inflater.inflate(R.layout.fragment_settings, container, false);

        btnSignOut = (Button) view.findViewById(R.id.btnSignOut);



        btnSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mAuth.signOut();
                toastMessage("Signing out...");
                startActivity(new Intent(getActivity(), StartActivity.class));
            }
        });


        return view;
    }


    // just toasts, nothing interesting
    private void toastMessage(String message){
        Toast.makeText(getActivity() ,message,Toast.LENGTH_SHORT).show();
    }
}

