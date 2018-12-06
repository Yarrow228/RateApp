package com.bite.rateapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class SignUpActivity extends AppCompatActivity {

    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;


    private EditText edtKey, edtEmail, edtPassword, edtPasswordConfirm;
    private Button btnSignUp;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        edtKey = (EditText) findViewById(R.id.edtKey);
        edtEmail = (EditText) findViewById(R.id.edtEmail);
        edtPassword = (EditText) findViewById(R.id.edtPassword);
        edtPasswordConfirm = (EditText) findViewById(R.id.edtPasswordConfirm);
        btnSignUp = (Button) findViewById(R.id.btnSignUp);


        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();


        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String email = edtEmail.getText().toString();
                String key = edtKey.getText().toString();
                String password = edtPassword.getText().toString();
                String passwordConfirm = edtPasswordConfirm.getText().toString();


                if(TextUtils.isEmpty(key)){
                    toastMessage("Enter your key!");
                    return;
                }

                if(TextUtils.isEmpty(email)){
                    toastMessage("Enter your email!");
                    return;
                }

                if(TextUtils.isEmpty(password) || TextUtils.isEmpty(passwordConfirm)){
                    toastMessage("Enter and confirm your password!");
                    return;
                }

                if(password.length() < 6){
                    toastMessage("Password too short(min 6 letters)");
                }

                if(!password.equals(passwordConfirm)){
                    toastMessage("Passwords do not match");
                    return;
                }

                toastMessage("Key:" + key);
                checkKey(key, email, password);
            }
        });

    }



    private void checkKey(final String key, final String email, final String password){

        mDatabase.child("Keys").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                if(dataSnapshot.child(key).exists()){

                    createAccount(email, password, key);

                }else{
                    toastMessage("Wrong key");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    private void createAccount(final String email, String password, final String key){


        mAuth.createUserWithEmailAndPassword(email.trim(), password.trim()).addOnCompleteListener(SignUpActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {

                if(!task.isSuccessful()){
                    toastMessage("Sign Up failed");
                }else{

                    //Call func that add this user to Database
                    FirebaseUser user = mAuth.getCurrentUser();
                    writeNewUser(key, user.getUid(), user.getEmail());
                }
            }
        });
    }


    private void writeNewUser(final String key, final String userID, final String email){

        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                UserInfUtil uInfo = new UserInfUtil();

                //Get name, surname, status from Database - Keys
                uInfo.setName(dataSnapshot.child("Keys").child(key).child("name").getValue().toString());
                uInfo.setSurname(dataSnapshot.child("Keys").child(key).child("surname").getValue().toString());
                uInfo.setStatus(dataSnapshot.child("Keys").child(key).child("status").getValue().toString());
                uInfo.setSchoolId(dataSnapshot.child("Keys").child(key).child("schoolId").getValue().toString());
                uInfo.setEmail(email);


                //Writing name, surname, default achievement status to Database - Users
                mDatabase.child("Users").child(userID).child("name").setValue(uInfo.getName());
                mDatabase.child("Users").child(userID).child("surname").setValue(uInfo.getSurname());
                mDatabase.child("Users").child(userID).child("status").setValue(uInfo.getStatus());
                mDatabase.child("Users").child(userID).child("email").setValue(uInfo.getEmail());
                mDatabase.child("Users").child(userID).child("schoolId").setValue(uInfo.getSchoolId());
                mDatabase.child("Users").child(userID).child("achievements").child("defaultAchievement").child("date").setValue("date");


                toastMessage("Sign Up Success");
                startActivity(new Intent(SignUpActivity.this, MainActivity.class));
                finish();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    // just toasts, nothing interesting
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }

}
