package com.wrkhalil.learningthroughlistening.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class User {

    public String firebaseID;
    public String fullName;
    //public String dateOfBirth;
    public String email;
    public int score;

    public User() {
    }

    public User(String fireBaseID, String fullName, String email) {
        this.firebaseID = fireBaseID;
        this.fullName = fullName;
        //this.dateOfBirth = dateOfBirth;
        this.email = email;
        this.score = 0;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("firebaseID", firebaseID);
        result.put("fullName", fullName);
        result.put("email", email);
        result.put("score", score);
        return result;
    }

    private void submitScore(){
        score += score;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/saving-data/fireblog/users");
        final DatabaseReference usersRef = ref.child( Model.operatingUser.firebaseID);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                usersRef.setValue(Model.operatingUser.toMap());
                Log.d("Account Info Updated:", Model.operatingUser.fullName + " " + Model.operatingUser.score);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

}
