package com.wrkhalil.learningthroughlistening.View;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import com.wrkhalil.learningthroughlistening.Model.Model;
import com.wrkhalil.learningthroughlistening.Presenter.ScoreActivityPresenter;
import com.wrkhalil.learningthroughlistening.R;

public class ScoreActivity extends Activity implements View.OnClickListener {

    private int position, score;
    private ScoreActivityPresenter scoreActivityPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);

        scoreActivityPresenter = new ScoreActivityPresenter(this);
        Intent intent = getIntent();
        position = intent.getIntExtra ("Position", 0); //get the position (song's id) value from the PlayGameActivity to increment the # of plays
        score = intent.getIntExtra ("Score", 0); //get the score value from the PlayGameActivity to add the earned score to the user's score

        //Views declaration
        ImageView songThumbnail = findViewById(R.id.songThumbnail);
        TextView txtSongTitle = findViewById(R.id.txtSongTitle);
        TextView txtScore = findViewById(R.id.txtScore);

        //Glide declaration to project the thumbnail on the songThumbnail ImageView
        RequestOptions requestOptions = new RequestOptions(); //Set the options of for the displayed picture
        requestOptions.placeholder(R.drawable.ic_launcher_background); //Picture displayed when the app is fetching the picture
        requestOptions.error(R.drawable.ic_launcher_background); //Picture displayed when the picture is not fetched
        requestOptions.circleCrop(); //Display the picture in a circle view
        requestOptions.override(600, 600); //Set the resolution of the picture
        Glide.with(this)
                .load(Uri.parse(Model.videoList.get(position).getThumbnailURL())) // Uri of the picture
                .apply(requestOptions) // Set the options
                .into(songThumbnail); // The container where the picture is displayed

        txtSongTitle.setText(Model.videoList.get(position).getTitle()); //Display the song's title
        txtScore.setText("Score: " + score); //Display the earned score


        //Buttons Declaration
        Button backButton = findViewById(R.id.backButton);
        Button submitScoreButton = findViewById(R.id.submitScoreButton);

        //Buttons Listeners
        backButton.setOnClickListener(this);
        submitScoreButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.backButton) {
            scoreActivityPresenter.goBackToSignInActivity(); //Go back to the SignInActivity
        }
        else if (i == R.id.submitScoreButton){
            scoreActivityPresenter.submitScore(score); //Add the score to the current user's score
            scoreActivityPresenter.incrementNumberOfPlays(position); //Increment the # of plays
            scoreActivityPresenter.goBackToSignInActivity(); //Go back to the SignInActivity
        }
    }
}
