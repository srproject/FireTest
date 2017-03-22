package com.sr.firetest;

import android.app.Dialog;
import android.content.Context;
import android.media.AudioManager;
import android.media.ToneGenerator;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MainActivity extends AppCompatActivity {

    private EditText ename,eage;
    private Button eabu;

    FirebaseDatabase firebaseDatabase;
    DatabaseReference db;
    FirebaseHelper helper;
    CustomAdapter adapter;
    ListView lv;
    EditText nameEditTxt, propTxt, descTxt;
    Context context;

    private SwipeRefreshLayout swipeRefreshLayout;



    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                     return true;
                case R.id.navigation_dashboard:
                     return true;
                case R.id.navigation_notifications:
                     return true;
            }
            return false;
        }

    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lv = (ListView) findViewById(R.id.lv);
        //INITIALIZE FIREBASE DB
        db = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper(db);
        //ADAPTER
        adapter = new CustomAdapter(this, helper.retrieve());
        lv.setAdapter(adapter);


        db.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                lv.setAdapter(adapter);
                ToneGenerator toneG = new ToneGenerator(AudioManager.STREAM_MUSIC, 100);
                toneG.startTone(ToneGenerator.TONE_SUP_RINGTONE, 200);


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                lv.setAdapter(adapter);

            }
        });



/*
        lv = (ListView) findViewById(R.id.lv);
        //INITIALIZE FIREBASE DB
        db = FirebaseDatabase.getInstance().getReference();
        helper = new FirebaseHelper(db);
        //ADAPTER
        adapter = new CustomAdapter(this, helper.retrieve());
        lv.setAdapter(adapter);
*/


        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                displayInputDialog();
            }
        });




/*
        ename=(EditText)findViewById(R.id.ename);
        eage=(EditText)findViewById(R.id.eage);
        eabu=(Button) findViewById(R.id.eabu);




        eabu.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {

                                        db.child("name").push().setValue(ename.getText().toString());


                                    }
                                });

*/

                BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
    }

    //DISPLAY INPUT DIALOG
    private void displayInputDialog() {
        Dialog d = new Dialog(this);
        d.setTitle("Send To Firebase");
        d.setContentView(R.layout.inputdialog);
        nameEditTxt = (EditText) d.findViewById(R.id.nameEditText);
        propTxt = (EditText) d.findViewById(R.id.propellantEditText);
        descTxt = (EditText) d.findViewById(R.id.descEditText);
        Button saveBtn = (Button) d.findViewById(R.id.saveBtn);
        //SAVE
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //GET DATA
                String name = nameEditTxt.getText().toString();
                String propellant = propTxt.getText().toString();
                String desc = descTxt.getText().toString();
                //SET DATA
                Spacecraft s = new Spacecraft();
                s.setName(name);
                s.setPropellant(propellant);
                s.setDescription(desc);
                //SIMPLE VALIDATION
                if (name != null && name.length() > 0) {
                    //THEN SAVE
                    if (helper.save(s)) {
                        //IF SAVED CLEAR EDITXT
                        nameEditTxt.setText("");
                        propTxt.setText("");
                        descTxt.setText("");
                        adapter = new CustomAdapter(MainActivity.this, helper.retrieve());
                        lv.setAdapter(adapter);
                    }
                } else {
                    Toast.makeText(MainActivity.this, "Name Must Not Be Empty", Toast.LENGTH_SHORT).show();
                }
            }
        });
        d.show();
    }




}
