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
    public static User operatingUser;
    public static List<Video> videoList = new ArrayList<>();
    public static boolean fetchingTranscript, fetchingAudio = false;

    public boolean checkForNullValues(){
        for (int i = 0 ; i < videoList.size() ; i++){
            if (videoList.get(i).getThumbnailURL() == null ||
                    videoList.get(i).getTitle() == null){
                return true;
            }
        }
        return false;
    }

    public void populateVideoListFromFirebase() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/saving-data/fireblog/videos");
        setStartANewGameStatus(false);

        if (videoList.size()>1){
            videoList = new ArrayList<>();
        }
        ChildEventListener childEventListener = new ChildEventListener() {

            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d("Message From Firebase", s + "");
                Video video = dataSnapshot.getValue(Video.class);
                Video videoParsed = new Video(video.id, video.plays);
                videoList.add(videoParsed);
                for (int i = 0 ; i < videoList.size() ; i++){
                    Log.d("videoList[" + i +"]", videoList.get(i).id + " ");
                }
                setStartANewGameStatus(true);
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
                setStartANewGameStatus(false);
                Log.w("loadPost:onCancelled", databaseError.toException());
            }

        };
        ref.addChildEventListener(childEventListener);
    }

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
                SignInActivityPresenter.printScore(Model.operatingUser.score);
                Log.d("Operating User Score", Model.operatingUser.score + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        usersRef.addValueEventListener(postListener);
    }

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
            Log.d("Sign In", "No user is logged in");
            Model.operatingUser = null;

        }
        else{
            Log.d("Sign In", "A user is logged in");
            fetchUserDataFromFirebase(currentUser);
        }
        return currentUser;
    }
}
