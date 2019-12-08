package com.wrkhalil.learningthroughlistening.View;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wrkhalil.learningthroughlistening.Model.Model;
import com.wrkhalil.learningthroughlistening.Presenter.SignInActivityPresenter;
import com.wrkhalil.learningthroughlistening.R;
import com.wrkhalil.learningthroughlistening.Model.User;
import com.wrkhalil.learningthroughlistening.Model.Video;

import java.util.ArrayList;
import java.util.List;


public class SignInActivity extends AppCompatActivity implements
        View.OnClickListener {

    public SignInActivityPresenter signInActivityPresenter;
    public TextView titleTextView;
    public static TextView scoreTextView;
    public ImageView loginAvatar;
    public static Button startANewGame;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        signInActivityPresenter = new SignInActivityPresenter(this);

        // Views
        titleTextView = findViewById(R.id.titleText);
        scoreTextView = findViewById(R.id.scoreText);
        loginAvatar = findViewById(R.id.loginAvatar);

        //Buttons
        startANewGame = findViewById(R.id.startANewGameButton);
        startANewGame.setEnabled(false);
        startANewGame.setOnClickListener(this);
        findViewById(R.id.signInWithGoogleButton).setOnClickListener(this);
    }


    public void onResume() {
        super.onResume();
        signInActivityPresenter.checkSignInStatus();
        signInActivityPresenter.populateVideoListFromFirebase();
    }

    public void signInWithGoogle() {
        Intent intent = new Intent(this, GoogleSignInActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void startChooseGameActivity() {
        Intent intent = new Intent(this, ChooseGameActivity.class);
        startActivity(intent);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.startANewGameButton) {
            Log.d("Start a new game", "Click received");
            signInActivityPresenter.startANewGame();
        } else if (i == R.id.signInWithGoogleButton) {
            signInWithGoogle();
        }
    }

}
