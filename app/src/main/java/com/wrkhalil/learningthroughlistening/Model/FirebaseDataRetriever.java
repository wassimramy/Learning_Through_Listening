package com.wrkhalil.learningthroughlistening.Model;

import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class FirebaseDataRetriever {

    //Attributes
    private String id;
    private int plays;

    //FirebaseDataRetriever Constructor
    FirebaseDataRetriever(String id){
        this.id = id; //Fetch id
        getFirebaseData(); //Executed to retrieve the # of plays
    }

    //Executed to retrieve the video's id and # of plays
    private void getFirebaseData() {
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/saving-data/fireblog/videos");
        DatabaseReference videoRef = ref.child(id+"/plays");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                FirebaseDataRetriever.this.plays  = dataSnapshot.getValue(Integer.class); //Retrieve the # of plays, parse it, and store it in the plays class variable
                Log.d("This video has been played", plays + " plays"); //Notify the developer about the retrieval state for testing purposes
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException()); //Notify the developer about the retrieval state for testing purposes
            }
        };
        videoRef.addValueEventListener(postListener); //Send the request
    }

    //Getters
    public int getPlays() {
        return plays;
    }
}
