package com.wrkhalil.learningthroughlistening.View;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import com.wrkhalil.learningthroughlistening.Presenter.SignInActivityPresenter;
import com.wrkhalil.learningthroughlistening.R;

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

        //Views Declaration
        titleTextView = findViewById(R.id.titleText);
        scoreTextView = findViewById(R.id.scoreText);
        loginAvatar = findViewById(R.id.loginAvatar);

        //Buttons Declaration
        startANewGame = findViewById(R.id.startANewGameButton);
        startANewGame.setEnabled(false);
        startANewGame.setOnClickListener(this);
        findViewById(R.id.signInWithGoogleButton).setOnClickListener(this);
    }


    public void onResume() {
        super.onResume();
        signInActivityPresenter.checkSignInStatus(); //Check if a user is signed in
        signInActivityPresenter.populateVideoListFromFirebase(); //Retrieve all the videos stored on Firebase database
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.startANewGameButton) {
            Log.d("Start a new game", "Click received");
            signInActivityPresenter.startANewGame(); //Starts the ChooseGameActivity to choose a song to play
        } else if (i == R.id.signInWithGoogleButton) {
            signInActivityPresenter.signInWithGoogle(); //Starts the GoogleSignInActivity to either login or logout
        }
    }

}
