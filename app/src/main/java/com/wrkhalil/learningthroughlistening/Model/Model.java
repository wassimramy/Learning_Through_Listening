package com.wrkhalil.learningthroughlistening.Model;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wrkhalil.learningthroughlistening.Presenter.SignInActivityPresenter;

import java.util.ArrayList;
import java.util.List;

import static com.wrkhalil.learningthroughlistening.Presenter.SignInActivityPresenter.setStartANewGameStatus;

public class Model {

    //Attributes
    public static User operatingUser;
    public static List<Video> videoList = new ArrayList<>();
    public static boolean fetchingTranscript, fetchingAudio = false;

    //Check if any attribute was not fetched correctly
    public boolean checkForNullValues(){
        for (int i = 0 ; i < videoList.size() ; i++){
            if (videoList.get(i).getThumbnailURL() == null ||
                    videoList.get(i).getTitle() == null){
                return true;
            }
        }
        return false;
    }

    //Executed to increment the number of plays when the user submits its score
    public void incrementNumberOfPlays(int position){
        videoList.get(position).incrementNumberOfPlays();
    }

    //Obfuscate the passed word with underscores
    public String generateUnderscores(String target){
        String result;
        result = "_";
        for (int i = 1; i < target.length() ; i++){
            result += "_";
        }
        return result;
    }

    //Executed to add the earned score when the user submits its score
    public void submitScore(int calculatedScore){
        operatingUser.submitScore(calculatedScore);
    }

    //Retrieve all the videos information
    public void populateVideoListFromFirebase() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/saving-data/fireblog/videos");
        setStartANewGameStatus(false);

        if (videoList.size()>1){
            videoList = new ArrayList<>(); //Re-instantiate the videoList to prevent duplication
        }
        ChildEventListener childEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("Message From Firebase", s + ""); //Displayed for testing purposes
                Video video = dataSnapshot.getValue(Video.class); //Retrieve the Video object
                Video videoParsed = new Video(video.id, video.plays); //Parse the returned Video object
                videoList.add(videoParsed); //Add the Video object to the videoList
                for (int i = 0 ; i < videoList.size() ; i++){
                    Log.d("videoList[" + i +"]", videoList.get(i).id + " "); //Displayed for testing purposes
                }
                setStartANewGameStatus(true); //Enable startANewGameButton Status
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                setStartANewGameStatus(false);  //Disable startANewGameButton Status
                Log.w("loadPost:onCancelled", databaseError.toException());
            }

        };
        ref.addChildEventListener(childEventListener); //Start the transaction request
    }

    //Executed to fetch the user's information
    public void  fetchUserDataFromFirebase(FirebaseUser currentUser) {
        Model.operatingUser = new User(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getEmail());
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/saving-data/fireblog/users");
        DatabaseReference usersRef = ref.child(currentUser.getUid());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Model.operatingUser  = dataSnapshot.getValue(User.class);
                SignInActivityPresenter.printScore(Model.operatingUser.score); //Retrieve the score value stored in Firebase
                Log.d("Operating User Score", Model.operatingUser.score + ""); //Displayed for testing purposes
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("Operating User Score", "loadPost:onCancelled", databaseError.toException()); //Displayed for testing purposes
            }
        };
        usersRef.addValueEventListener(postListener); //Start the transaction request
    }

    //Executed to check the signin status of the user in the app
    public FirebaseUser checkSignInStatus() {

        // [START declare_auth]
        FirebaseAuth mAuth;
        // [END declare_auth]

        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null){
            Log.d("Sign In", "No user is logged in"); //Displayed for testing purposes
            Model.operatingUser = null; //Set the user variable to null if the user is not logged in

        }
        else{
            Log.d("Sign In", "A user is logged in"); //Displayed for testing purposes
            fetchUserDataFromFirebase(currentUser); //Fetch the user information if a user is logged in
        }
        return currentUser;
    }
}
