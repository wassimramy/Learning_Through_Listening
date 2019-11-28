package com.wrkhalil.learningthroughlistening;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDataRetriever {

    private String id;
    private int plays;

    FirebaseDataRetriever(String id){
        this.id = id;
        getFirebaseData();
    }

    public int getPlays() {
        return plays;
    }

    private void getFirebaseData() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/saving-data/fireblog/videos");
        DatabaseReference videoRef = ref.child(id+"/plays");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                FirebaseDataRetriever.this.plays  = dataSnapshot.getValue(Integer.class);
                Log.d("This video has been played", plays + " plays");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException());
            }
        };
        videoRef.addValueEventListener(postListener);
    }

}
