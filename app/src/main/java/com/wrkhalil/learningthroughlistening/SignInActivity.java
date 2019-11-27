package com.wrkhalil.learningthroughlistening;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class SignInActivity extends AppCompatActivity implements
        View.OnClickListener {

    private TextView titleTextView;
    private ImageView loginAvatar;
    private Button startANewGame;
    public static List<Video> videoList = new ArrayList<>();


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        titleTextView = findViewById(R.id.titleText);
        loginAvatar = findViewById(R.id.loginAvatar);

        //Buttons
        startANewGame = findViewById(R.id.startANewGameButton);
        startANewGame.setOnClickListener(this);
        findViewById(R.id.signInWithGoogleButton).setOnClickListener(this);
    }

    public void checkSignInStatus() {

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
            MainActivity.operatingUser = null;
            updateUIForLoggedOutUser();

        }
        else{
            Log.d("Sign In", "A user is logged in");
            updateUIForLoggedInUser(currentUser);
            fetchUserDataFromFirebase(currentUser);
        }
    }

    public void updateUIForLoggedOutUser() {
        findViewById(R.id.startANewGameButton).setVisibility(View.GONE);
        titleTextView.setText("Sign-in using Google");
    }

    public void updateUIForLoggedInUser(FirebaseUser currentUser) {
        findViewById(R.id.startANewGameButton).setVisibility(View.VISIBLE);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.login_vector);
        requestOptions.error(R.drawable.login_vector);
        requestOptions.circleCrop();

        Glide
                .with(this)
                .setDefaultRequestOptions(requestOptions)
                .load(currentUser.getPhotoUrl())
                .into(loginAvatar);

        titleTextView.setText("Welcome " + currentUser.getDisplayName() + "!");
    }

    public void fetchUserDataFromFirebase(FirebaseUser currentUser) {
        MainActivity.operatingUser = new User(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getEmail());
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/saving-data/fireblog/users");
        DatabaseReference usersRef = ref.child(currentUser.getUid());

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                MainActivity.operatingUser  = dataSnapshot.getValue(User.class);
                Log.d("Operating User Score", MainActivity.operatingUser.score + "");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                //Log.w(TAG, "loadPost:onCancelled", databaseError.toException());
            }
        };
        usersRef.addValueEventListener(postListener);
    }

    public void onResume() {
        super.onResume();
        checkSignInStatus();
        populateVideoListFromFirebase();

    }

    public void signInWithGoogle() {
        Intent intent = new Intent(this, GoogleSignInActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void startANewGame() {
        Intent intent = new Intent(this, ChooseGameActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.startANewGameButton) {
            Log.d("Start a new game", "Click received");
            startANewGame();
        } else if (i == R.id.signInWithGoogleButton) {
            signInWithGoogle();
        }
    }

    public void populateVideoListFromFirebase() {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/saving-data/fireblog/videos");
        //DatabaseReference usersRef = ref.child();

        if (videoList.size()>1){
            videoList = new ArrayList<>();
            findViewById(R.id.startANewGameButton).setEnabled(false);
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
                findViewById(R.id.startANewGameButton).setEnabled(true);
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
                Log.w("loadPost:onCancelled", databaseError.toException());
            }
        };
        ref.addChildEventListener(childEventListener);
    }
}
