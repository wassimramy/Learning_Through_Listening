package com.wrkhalil.learningthroughlistening.Presenter;

import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import com.wrkhalil.learningthroughlistening.Model.Model;
import com.wrkhalil.learningthroughlistening.View.ChooseDifficultyActivity;
import com.wrkhalil.learningthroughlistening.View.ChooseGameActivity;

public class ChooseGameActivityPresenter {

    //Attributes
    private  ChooseGameActivity view;
    private Model model;

    //ChooseGameActivityPresenter Constructor
    public ChooseGameActivityPresenter(ChooseGameActivity view) {
        this.view = view; //Fetch view
        this.model = new Model(); //Instantiate model
    }

    //Called to display the items stored in the database in the recyclerView
    public void updateRecyclerView (RecyclerView recyclerView){
        VideoAdapter videoAdapter = new VideoAdapter(view, model.videoList, position -> startGameActivity(position));
        recyclerView.setAdapter(videoAdapter); //Update the recyclerView
    }

    //Executed to start the  ChooseDifficultyActivity for the user to choose teh difficulty level
    private void startGameActivity(int position){
        Intent intent = new Intent(view, ChooseDifficultyActivity.class);
        intent.putExtra("Position", position); //Sends the video's id to the PlayGameActivity
        view.startActivity(intent); //Start the activity
        view.finish(); //Destroy the current activity
    }

}
