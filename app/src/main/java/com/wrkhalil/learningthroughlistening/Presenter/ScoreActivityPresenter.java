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

    private ScoreActivity view;
    private Model model;

    //Instantiate the presenter
    public ScoreActivityPresenter(ScoreActivity view) {
        this.view = view;
        this.model = new Model();
    }

    public void submitScore(int calculatedScore){
        model.submitScore(calculatedScore);
    }

    public void incrementNumberOfPlays(int position){
        model.incrementNumberOfPlays(position);
    }

    // Back to Choose Game Activity
    public void goBackToChooseGameActivity() {
        Intent intent = new Intent(view, SignInActivity.class);
        view.startActivity(intent); //Start the activity
        view.finish();
    }
}
