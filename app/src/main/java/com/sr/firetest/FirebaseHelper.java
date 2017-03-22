package com.sr.firetest;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;

import java.util.ArrayList;

/**
 * Created by sr on 3/22/17.
 */

public class FirebaseHelper {
    DatabaseReference db;





    Boolean saved;
    ArrayList<Spacecraft> spacecrafts=new ArrayList<>();
    /*
 PASS DATABASE REFRENCE
  */



    public FirebaseHelper(DatabaseReference db) {
        this.db = db;
    }
    //WRITE IF NOT NULL
    public Boolean save(Spacecraft spacecraft)
    {
        if(spacecraft==null)
        {
            saved=false;
        }else
        {
            try
            {
                db.child("Spacecraft").push().setValue(spacecraft);
                saved=true;
            }catch (DatabaseException e)
            {
                e.printStackTrace();
                saved=false;
            }
        }
        return saved;
    }
    //IMPLEMENT FETCH DATA AND FILL ARRAYLIST
    private void fetchData(DataSnapshot dataSnapshot)
    {
        spacecrafts.clear();
        for (DataSnapshot ds : dataSnapshot.getChildren())
        {
            Spacecraft spacecraft=ds.getValue(Spacecraft.class);
            spacecrafts.add(spacecraft);
        }
    }
    //RETRIEVE
    public ArrayList<Spacecraft> retrieve()
    {
        db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }
            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }
            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
            }
            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });
        return spacecrafts;
    }
}