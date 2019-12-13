package com.wrkhalil.learningthroughlistening.Presenter;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wrkhalil.learningthroughlistening.Model.Model;
import com.wrkhalil.learningthroughlistening.View.ScoreActivity;
import com.wrkhalil.learningthroughlistening.View.SignInActivity;

public class ScoreActivityPresenter {

    //Attributes
    private ScoreActivity view;
    private Model model;

    //ScoreActivityPresenter Constructor
    public ScoreActivityPresenter(ScoreActivity view) {
        this.view = view; //Fetch view
        this.model = new Model(); //Instantiate model
    }

    //Executed if the user chooses to submit its score
    public void submitScore(int calculatedScore){
        model.submitScore(calculatedScore); //Update the user's score
    }
    //Executed if the user chooses to submit its score
    public void incrementNumberOfPlays(int position){
        model.incrementNumberOfPlays(position); //Increment the song's plays number
    }

    // Back to Choose Game Activity
    public void goBackToSignInActivity() {
        Intent intent = new Intent(view, SignInActivity.class);
        view.startActivity(intent); //Start the activity
        view.finish(); //Destroy teh current activity
    }
}
