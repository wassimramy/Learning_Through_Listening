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
    private ChooseDifficultyActivity view;
    private Model model;

    //Instantiate the presenter
    public ChooseDifficultyActivityPresenter(ChooseDifficultyActivity view) {
        this.view = view;
        this.model = new Model();
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

        view.txtSongTitle.setText(model.videoList.get(position).getTitle());

        settingButtonsStatus();

        model.videoList.get(position).generateClosedCaption();
        model.videoList.get(position).downloadAudioFile();
    }

    public static void settingButtonsStatus(){

        String loadingStatement = "Loading Data";
        if (fetchingTranscript && fetchingAudio){
            easyGameButton.setEnabled(true);
            mediumGameButton.setEnabled(true);
            hardGameButton.setEnabled(true);
            easyGameButton.setText("Easy");
            mediumGameButton.setText("Medium");
            hardGameButton.setText("Hard");
        }
        else{
            easyGameButton.setEnabled(false);
            mediumGameButton.setEnabled(false);
            hardGameButton.setEnabled(false);
            easyGameButton.setText(loadingStatement);
            mediumGameButton.setText(loadingStatement);
            hardGameButton.setText(loadingStatement);
        }
    }

    public void startGameActivity(int position, String difficulty){
        Intent intent = new Intent(view, PlayGameActivity.class);
        intent.putExtra("Position", position); //Sends the URI value to the ShowPictureActivity to fetch the picture
        intent.putExtra("Difficulty", difficulty); //Sends the URI value to the ShowPictureActivity to fetch the picture
        view.startActivity(intent); //Start the activity
        view.finish();
    }
}
