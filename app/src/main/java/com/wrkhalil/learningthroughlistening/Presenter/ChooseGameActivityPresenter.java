package com.wrkhalil.learningthroughlistening.Presenter;

import android.content.Intent;

import androidx.recyclerview.widget.RecyclerView;

import com.wrkhalil.learningthroughlistening.Model.Model;
import com.wrkhalil.learningthroughlistening.View.ChooseDifficultyActivity;
import com.wrkhalil.learningthroughlistening.View.ChooseGameActivity;

public class ChooseGameActivityPresenter {
    private  ChooseGameActivity view;
    private Model model;

    //Instantiate the presenter
    public ChooseGameActivityPresenter(ChooseGameActivity view) {
        this.view = view;
        this.model = new Model();
    }

    //Called to display the items stored in the database in the recyclerView
    public void updateRecyclerView (RecyclerView recyclerView){
        VideoAdapter videoAdapter = new VideoAdapter(view, model.videoList, position -> startGameActivity(position));
        recyclerView.setAdapter(videoAdapter); //Update the recyclerView
    }

    private void startGameActivity(int position){
        Intent intent = new Intent(view, ChooseDifficultyActivity.class);
        intent.putExtra("Position", position); //Sends the URI value to the ShowPictureActivity to fetch the picture
        view.startActivity(intent); //Start the activity
        view.finish();
    }

}
