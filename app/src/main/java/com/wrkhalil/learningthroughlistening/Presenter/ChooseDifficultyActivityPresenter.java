package com.wrkhalil.learningthroughlistening.Presenter;

import android.content.Intent;
import android.net.Uri;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.wrkhalil.learningthroughlistening.Model.Model;
import com.wrkhalil.learningthroughlistening.R;
import com.wrkhalil.learningthroughlistening.View.ChooseDifficultyActivity;
import com.wrkhalil.learningthroughlistening.View.PlayGameActivity;
import com.wrkhalil.learningthroughlistening.View.SignInActivity;

import static com.wrkhalil.learningthroughlistening.Model.Model.fetchingAudio;
import static com.wrkhalil.learningthroughlistening.Model.Model.fetchingTranscript;
import static com.wrkhalil.learningthroughlistening.View.ChooseDifficultyActivity.easyGameButton;
import static com.wrkhalil.learningthroughlistening.View.ChooseDifficultyActivity.hardGameButton;
import static com.wrkhalil.learningthroughlistening.View.ChooseDifficultyActivity.mediumGameButton;

public class ChooseDifficultyActivityPresenter {

    //Attributes
    private ChooseDifficultyActivity view;
    private Model model;

    //ChooseDifficultyActivityPresenter Constructor
    public ChooseDifficultyActivityPresenter(ChooseDifficultyActivity view) {
        this.view = view; //Fetch View
        this.model = new Model(); //Instantiate the model
    }

    public void updateFields(int position){
        RequestOptions requestOptions = new RequestOptions(); //Set the options of for the displayed picture
        requestOptions.placeholder(R.drawable.ic_launcher_background); //Picture displayed when the app is fetching the picture
        requestOptions.error(R.drawable.ic_launcher_background); //Picture displayed when the picture is not fetched
        requestOptions.circleCrop(); //Display the picture in a circle view
        requestOptions.override(600, 600); //Set the resolution of the picture
        Glide.with(view)
                .load(Uri.parse(model.videoList.get(position).getThumbnailURL())) // Uri of the picture
                .apply(requestOptions) // Set the options
                .into(view.songThumbnail); // The container where the picture is displayed

        view.txtSongTitle.setText(model.videoList.get(position).getTitle()); //Display the song's title

        settingButtonsStatus(); //Determine the state of the buttons depending on the data retrieval

        model.videoList.get(position).generateClosedCaption(); //Generate new .srt file for the song
        model.videoList.get(position).downloadAudioFile(); //Download the audio file temporarily
    }

    //Check whether the buttons need to be disabled or enabled depending on the data retrieval status
    public static void settingButtonsStatus(){

        String loadingStatement = "Loading Data";
        if (fetchingTranscript && fetchingAudio){ //Enter if all data is retrieved
            easyGameButton.setEnabled(true); //Enable the easyGameButton
            mediumGameButton.setEnabled(true); //Enable the mediumGameButton
            hardGameButton.setEnabled(true);//Enable the hardGameButton
            easyGameButton.setText("Easy"); //Set the text to Easy for the easyGameButton instead of the loadingStatement
            mediumGameButton.setText("Medium"); //Set the text to Medium for the mediumGameButton instead of the loadingStatement
            hardGameButton.setText("Hard"); //Set the text to Hard for the hardGameButton instead of the loadingStatement
        }
        else{
            easyGameButton.setEnabled(false);//Disable the easyGameButton
            mediumGameButton.setEnabled(false);//Disable the mediumGameButton
            hardGameButton.setEnabled(false); //Disable the hardGameButton
            easyGameButton.setText(loadingStatement); //Set the text to the loadingStatement for the easyGameButton
            mediumGameButton.setText(loadingStatement); //Set the text to the loadingStatement for the mediumGameButton
            hardGameButton.setText(loadingStatement); //Set the text to the loadingStatement for the hardGameButton
        }
    }

    //Executed to start the PlayGameActivity for the user to play
    public void startGameActivity(int position, String difficulty){
        Intent intent = new Intent(view, PlayGameActivity.class);
        intent.putExtra("Position", position); //Sends the video's id to the PlayGameActivity
        intent.putExtra("Difficulty", difficulty); //Sends the difficulty level to the PlayGameActivity
        view.startActivity(intent); //Start the activity
        view.finish(); //Destroy the current activity
    }
}
