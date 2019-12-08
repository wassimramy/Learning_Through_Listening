package com.wrkhalil.learningthroughlistening.Presenter;

import android.content.Intent;
import android.view.View;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.auth.FirebaseUser;
import com.wrkhalil.learningthroughlistening.Model.Model;
import com.wrkhalil.learningthroughlistening.R;
import com.wrkhalil.learningthroughlistening.View.ChooseGameActivity;
import com.wrkhalil.learningthroughlistening.View.GoogleSignInActivity;
import com.wrkhalil.learningthroughlistening.View.SignInActivity;

import static com.wrkhalil.learningthroughlistening.View.SignInActivity.startANewGame;

public class SignInActivityPresenter {
    private SignInActivity view;
    private Model model;

    //Instantiate the presenter
    public SignInActivityPresenter(SignInActivity view) {
        this.view = view;
        this.model = new Model();
    }

    public void populateVideoListFromFirebase() {
        model.populateVideoListFromFirebase();
    }

    public void checkSignInStatus() {
        FirebaseUser currentUser = model.checkSignInStatus();
        if (currentUser != null){
            updateUIForLoggedInUser(currentUser);
        }
        else{
            updateUIForLoggedOutUser();
        }
        view.scoreTextView.setText("Total Score: " + Model.operatingUser.score);
    }

    public static void setStartANewGameStatus(boolean status){
        if (status){
            startANewGame.setText("Start a New Game");
        }
        else{
            startANewGame.setText("Loading List");
        }
        startANewGame.setEnabled(status);
    }

    public void startANewGame() {
        if (!model.checkForNullValues()){
            startChooseGameActivity();
        }
    }

    private void updateUIForLoggedOutUser() {
        view.findViewById(R.id.startANewGameButton).setVisibility(View.GONE);
        view.titleTextView.setText("Sign-in using Google");
    }

    private void updateUIForLoggedInUser(FirebaseUser currentUser) {
        view.findViewById(R.id.startANewGameButton).setVisibility(View.VISIBLE);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.login_vector);
        requestOptions.error(R.drawable.login_vector);
        requestOptions.circleCrop();

        Glide
                .with(view)
                .setDefaultRequestOptions(requestOptions)
                .load(currentUser.getPhotoUrl())
                .into(view.loginAvatar);

        view.titleTextView.setText("Welcome " + currentUser.getDisplayName() + "!");
    }

    public static void printScore(int score){
        SignInActivity.scoreTextView.setText("Score: " + score);
    }

    public void signInWithGoogle() {
        Intent intent = new Intent(view, GoogleSignInActivity.class);
        view.startActivity(intent);
        view.finish();
    }

    public void startChooseGameActivity() {
        Intent intent = new Intent(view, ChooseGameActivity.class);
        view.startActivity(intent);
    }
}
