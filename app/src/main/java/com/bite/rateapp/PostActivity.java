package com.bite.rateapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class PostActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener{

    final Random random = new Random();
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    private EditText edComment;
    private Spinner typeOfEvent, levelOfEvent, markOfEvent, placeOfEvent;

    private boolean levelOfEventbool = false;
    private boolean markOfEventbool = false;

    private int typePos, markPos, levelPos, placePos;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_post);


        Toolbar toolbar = (Toolbar) findViewById(R.id.new_post_toolbar);
        setSupportActionBar(toolbar);

        edComment = (EditText) findViewById(R.id.edComment);


        typeOfEvent = (Spinner) findViewById(R.id.spType);
        ArrayAdapter<CharSequence> adapter1 = ArrayAdapter.createFromResource(this, R.array.typesOfEvent, android.R.layout.simple_spinner_item);
        adapter1.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        typeOfEvent.setAdapter(adapter1);
        typeOfEvent.setOnItemSelectedListener(this);


        levelOfEvent = (Spinner) findViewById(R.id.spLevel);
        ArrayAdapter<CharSequence> adapter2 = ArrayAdapter.createFromResource(this, R.array.levelOfEvent, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        levelOfEvent.setAdapter(adapter2);
        levelOfEvent.setOnItemSelectedListener(this);


        markOfEvent = (Spinner) findViewById(R.id.spMark);
        ArrayAdapter<CharSequence> adapter3 = ArrayAdapter.createFromResource(this, R.array.markOfEvent, android.R.layout.simple_spinner_item);
        adapter2.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        markOfEvent.setAdapter(adapter3);
        markOfEvent.setOnItemSelectedListener(this);



        placeOfEvent = (Spinner) findViewById(R.id.spPlace);
        ArrayAdapter<CharSequence> adapter4 = ArrayAdapter.createFromResource(this, R.array.placeOfEvent, android.R.layout.simple_spinner_item);
        adapter4.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        placeOfEvent.setAdapter(adapter4);
        placeOfEvent.setOnItemSelectedListener(this);




        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.activity_post_toobar, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        String comment = edComment.getText().toString();

        switch (item.getItemId()){
            case R.id.action_check:
                if(!TextUtils.isEmpty(comment) && typePos != 0){

                    SimpleDateFormat simpleTimeFormat = new SimpleDateFormat("HH:mm");
                    String strTime = simpleTimeFormat.format(new Date());
                    SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                    String strDate = simpleDateFormat.format(new Date());
                    String timeAndDate = strTime.replace(":","") + strDate.replace("/","") + String.valueOf(random.nextInt(89)+10);

                    FirebaseUser user = mAuth.getCurrentUser();

                    toastMessage(Integer.toString(typePos));





                    if (levelPos != 0 && levelOfEvent.isEnabled() && placePos != 0 && placeOfEvent.isEnabled()){  //Competition


                        mDatabase.child("Users").child(user.getUid()).child("achievements").child(timeAndDate).child("date").setValue(strDate.replace("/","."));
                        mDatabase.child("Users").child(user.getUid()).child("achievements").child(timeAndDate).child("time").setValue(strTime);
                        mDatabase.child("Users").child(user.getUid()).child("achievements").child(timeAndDate).child("comment").setValue(comment);
                        mDatabase.child("Users").child(user.getUid()).child("achievements").child(timeAndDate).child("confirmed").setValue("0");



                        toastMessage(levelOfEvent.getItemAtPosition(levelPos).toString());
                        mDatabase.child("Users").child(user.getUid()).child("achievements").child(timeAndDate).child("typeOfEvent").setValue("competition");
                        mDatabase.child("Users").child(user.getUid()).child("achievements").child(timeAndDate).child("levelOfEvent").setValue(levelOfEvent.getItemAtPosition(levelPos).toString().toLowerCase());
                        mDatabase.child("Users").child(user.getUid()).child("achievements").child(timeAndDate).child("placeOfEvent").setValue(placeOfEvent.getItemAtPosition(placePos).toString());
                    }
                    if (markPos != 0 && markOfEvent.isEnabled()){


                        mDatabase.child("Users").child(user.getUid()).child("achievements").child(timeAndDate).child("date").setValue(strDate.replace("/","."));
                        mDatabase.child("Users").child(user.getUid()).child("achievements").child(timeAndDate).child("time").setValue(strTime);
                        mDatabase.child("Users").child(user.getUid()).child("achievements").child(timeAndDate).child("comment").setValue(comment);
                        mDatabase.child("Users").child(user.getUid()).child("achievements").child(timeAndDate).child("confirmed").setValue("0");



                        toastMessage(markOfEvent.getItemAtPosition(markPos).toString());
                        mDatabase.child("Users").child(user.getUid()).child("achievements").child(timeAndDate).child("typeOfEvent").setValue("mark");
                        mDatabase.child("Users").child(user.getUid()).child("achievements").child(timeAndDate).child("markOfEvent").setValue(markOfEvent.getItemAtPosition(markPos).toString());
                    }





                    //startActivity(new Intent(PostActivity.this, MainActivity.class));
                }else{
                    toastMessage("Write your comment");
                }
            case R.id.action_back:
                startActivity(new Intent(PostActivity.this, MainActivity.class));
                finish();
        }

        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {


        //toastMessage(parent.getItemAtPosition(position).toString());
        String[] type = getResources().getStringArray(R.array.typesOfEvent);
        String[] level = getResources().getStringArray(R.array.levelOfEvent);

        if(parent.getItemAtPosition(position).toString().equals(type[0])){
            markOfEvent.setEnabled(false);
            levelOfEvent.setEnabled(false);
            placeOfEvent.setEnabled(false);
        }


        if(parent.getItemAtPosition(position).toString().equals(type[1])){
            markOfEvent.setEnabled(false);
            levelOfEvent.setEnabled(true);
        }

        if(parent.getItemAtPosition(position).toString().equals(level[1]) || parent.getItemAtPosition(position).toString().equals(level[2]) || parent.getItemAtPosition(position).toString().equals(level[3]) || parent.getItemAtPosition(position).toString().equals(level[4])){
            placeOfEvent.setEnabled(true);
        }

        if(parent.getItemAtPosition(position).toString().equals(type[2])){
            levelOfEvent.setEnabled(false);
            placeOfEvent.setEnabled(false);
            markOfEvent.setEnabled(true);

        }


        if (levelOfEvent.isEnabled() && placeOfEvent.isEnabled()){
            typePos = typeOfEvent.getSelectedItemPosition();
            levelPos = levelOfEvent.getSelectedItemPosition();
            placePos = placeOfEvent.getSelectedItemPosition();
        }

        if (markOfEvent.isEnabled()){
            typePos = typeOfEvent.getSelectedItemPosition();
            markPos = markOfEvent.getSelectedItemPosition();
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }


    // just toasts, nothing interesting
    private void toastMessage(String message){
        Toast.makeText(this,message,Toast.LENGTH_SHORT).show();
    }


}
