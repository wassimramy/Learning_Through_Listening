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
        this.view = view; //Fetch view
        this.model = new Model(); //Fetch model
    }

    //Fetch the videos entries from Firebase
    public void populateVideoListFromFirebase() {
        model.populateVideoListFromFirebase();
    }

    //Check teh sign in status of the user
    public void checkSignInStatus() {
        FirebaseUser currentUser = model.checkSignInStatus();
        if (currentUser != null){ //Enters if no user is logged in
            updateUIForLoggedInUser(currentUser); //Update the UI for logged in user
        }
        else{
            updateUIForLoggedOutUser(); //Update the UI for logged out user
        }

        if (Model.operatingUser != null) {
            view.scoreTextView.setText("Total Score: " + Model.operatingUser.score); //Display the score if a user is logged in
        }
        else{
            view.scoreTextView.setText(""); //No score to display for logged out users
        }
    }

    //Change the text on the startANewGameButton to reflect the status of fetching the songs
    public static void setStartANewGameStatus(boolean status){
        if (status){ //Enters if all songs are fetched
            startANewGame.setText("Start a New Game"); //Change the text to "Start A New Game" when the list is fetched
        }
        else{
            startANewGame.setText("Loading List"); //Change the text to "Loading List" when the list is still in the process of getting fetched
        }
        startANewGame.setEnabled(status); //Enable the button depending on the status of the data retrieval
    }

    //Called when the user taps on startANewGameButton
    public void startANewGame() {
        if (!model.checkForNullValues()){
            startChooseGameActivity(); //Start ChooseGameActivity
        }
    }

    //Modify the UI if the user is logged out
    private void updateUIForLoggedOutUser() {
        view.findViewById(R.id.startANewGameButton).setVisibility(View.GONE); //Hide startANewGameButton
        view.titleTextView.setText("Sign-in using Google"); //Show this "Sign-in using Google" instead of the greeting message
    }

    //Modify the UI if teh user is logged in
    private void updateUIForLoggedInUser(FirebaseUser currentUser) {
        //Show the startANewGameButton if the user is logged in
        view.findViewById(R.id.startANewGameButton).setVisibility(View.VISIBLE);

        //Project the user logo to the view.loginAvatar
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.placeholder(R.drawable.login_vector);
        requestOptions.error(R.drawable.login_vector);
        requestOptions.circleCrop();

        Glide
                .with(view)
                .setDefaultRequestOptions(requestOptions)
                .load(currentUser.getPhotoUrl())
                .into(view.loginAvatar);

        //Display the greeting message on titleTextView
        view.titleTextView.setText("Welcome " + currentUser.getDisplayName() + "!");
    }

    //Display the score on the scoreTextView
    public static void printScore(int score){
        SignInActivity.scoreTextView.setText("Score: " + score);
    }

    //Start GoogleSignInActivity when the "Google" button is pressed
    public void signInWithGoogle() {
        Intent intent = new Intent(view, GoogleSignInActivity.class);
        view.startActivity(intent);
        view.finish(); //Destroy this activity so the previous login state won't be fetched
    }

    //Start ChooseGameActivity when the "Start a new game" is pressed
    private void startChooseGameActivity() {
        Intent intent = new Intent(view, ChooseGameActivity.class);
        view.startActivity(intent);
    }
}
