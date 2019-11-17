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

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;


public class SignInActivity extends AppCompatActivity {

    private TextView titleTextView;
    private ImageView loginAvatar;
    private Button startANewGame;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Views
        titleTextView = findViewById(R.id.titleText);
        loginAvatar = findViewById(R.id.loginAvatar);

        //Buttons
        startANewGame = findViewById(R.id.startANewGameButton);
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
        startANewGame.setVisibility(View.GONE);
        titleTextView.setText("Sign-in using Google");
    }

    public void updateUIForLoggedInUser(FirebaseUser currentUser) {
        startANewGame.setVisibility(View.VISIBLE);
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
        Video video = new Video ("YOfa1xGWJCc");
    }

    public void signInWithGoogle(View view) {
        Intent intent = new Intent(this, GoogleSignInActivity.class);
        startActivity(intent);
        this.finish();
    }
}
