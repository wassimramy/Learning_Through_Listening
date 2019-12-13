package com.wrkhalil.learningthroughlistening.View;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.wrkhalil.learningthroughlistening.Model.Model;
import com.wrkhalil.learningthroughlistening.Presenter.PlayGameActivityPresenter;
import com.wrkhalil.learningthroughlistening.R;

import java.util.Locale;

public class PlayGameActivity extends AppCompatActivity implements MediaPlayer.OnTimedTextListener, View.OnClickListener, MediaPlayer.OnCompletionListener {
    private static Handler handler = new Handler();
    public TextView txtDisplay, txtScore;
    private String videoTrackPath;
    public MediaPlayer player;
    public Button pauseButton, firstChoiceButton, secondChoiceButton, thirdChoiceButton, fourthChoiceButton;
    private int position;
    private PlayGameActivityPresenter playGameActivityPresenter;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);

        playGameActivityPresenter = new PlayGameActivityPresenter(this);

        Intent intent = getIntent();
        position = intent.getIntExtra ("Position", 0); //get the URI value from the previous activity
        playGameActivityPresenter.position = position;

        videoTrackPath = Model.videoList.get(position).getTrackPath();

        //Views Declaration
        txtDisplay = findViewById(R.id.txtDisplay);
        txtScore = findViewById(R.id.txtScore);

        //Buttons Declaration
        firstChoiceButton = findViewById(R.id.firstChoiceButton);
        secondChoiceButton = findViewById(R.id.secondChoiceButton);
        thirdChoiceButton = findViewById(R.id.thirdChoiceButton);
        fourthChoiceButton = findViewById(R.id.fourthChoiceButton);
        pauseButton = findViewById(R.id.pauseButton);

        //Buttons Listeners
        firstChoiceButton.setOnClickListener(this);
        secondChoiceButton.setOnClickListener(this);
        thirdChoiceButton.setOnClickListener(this);
        fourthChoiceButton.setOnClickListener(this);
        findViewById(R.id.quitGameButton).setOnClickListener(this);
        findViewById(R.id.restartButton).setOnClickListener(this);
        pauseButton.setOnClickListener(this);

        //Disable all choice buttons
        playGameActivityPresenter.setChoicesButtons(false);

        //Parse the URL of the audio file and convert it to a URI
        Uri trackFileUri = Uri.parse(videoTrackPath);

        //Player instantiation
        player = MediaPlayer.create(this, trackFileUri);
        try {
            player.addTimedTextSource(Model.videoList.get(position).getClosedCaptionPath() ,
                    MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP);
            int textTrackIndex = findTrackIndexFor(
                    MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT, player.getTrackInfo());
            if (textTrackIndex >= 0) {
                player.selectTrack(textTrackIndex);
            } else {
                Log.w("Subtitle Status", "Cannot find text track!");
            }
            player.setOnTimedTextListener(this); //To refresh the txtDisplay and print the new transcript line according to the .srt file
            player.setOnCompletionListener(this); //To execute this method when the player finishes the song
            player.start(); //Player starts streaming the song
        } catch (Exception e){
            //If any error happens throws the stack and print failed in the logs
            Log.w("Subtitle Status", "Failed!");
            e.printStackTrace();
        }
    }

    //To execute this method when the player finishes the song
    public void onCompletion(MediaPlayer arg0) {
    playGameActivityPresenter.showScoreActivity();
    }

    public void onResume() {
        super.onResume();
    }

    //Fetch the track from the URI
    private int findTrackIndexFor(int mediaTrackType, MediaPlayer.TrackInfo[] trackInfo) {
        int index = -1;
        for (int i = 0; i < trackInfo.length; i++) {
            if (trackInfo[i].getTrackType() == mediaTrackType) {
                return i;
            }
        }
        return index;
    }

    //Display the new transcript line
    @Override
    public void onTimedText(final MediaPlayer mp, final TimedText text) {
        if (text != null) {
            handler.post(() -> {
                playGameActivityPresenter.refreshPlayerDisplay(text); //Refresh the txtDisplay with the new transcript line
            });
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.quitGameButton) { //When the user tapes on the quitGameButton
            playGameActivityPresenter.pauseGame(); //Pauses the player
            playGameActivityPresenter.goBackToChooseGameActivity(); //Show the ChooseGameActivity
        } else if (i == R.id.pauseButton) { //When the user tapes on the pauseButton
            Log.d("Button Hit", pauseButton.getText()+" "); //Send this message to the logs for testing purposes
            if (pauseButton.getText().equals("Pause")){ //Check the pause state of the pauseButton.
                Log.d("Button Hit", "Pause button is clicked"); //Send this message to the logs for testing purposes
                playGameActivityPresenter.pauseGame(); //Pause the player if the button's state is pause
            }
            else if (pauseButton.getText().equals("Start")){ //Check the start state of the pauseButton.
                playGameActivityPresenter.startGame(); //Starts the player if the button's state is start
            }
        } else if (i == R.id.restartButton) { //When the user tapes on the restartButton
            playGameActivityPresenter.pauseGame(); //Pauses the player
            playGameActivityPresenter.restartGame(position); //Restarts the same activity with the same video's id
        }
        else if (i == R.id.firstChoiceButton){ //When the user tapes on the firstChoiceButton
            playGameActivityPresenter.receiveChoice(firstChoiceButton.getText().toString()); //Send the choice to receiveChoice()
        }
        else if (i == R.id.secondChoiceButton){ //When the user tapes on the secondChoiceButton
            playGameActivityPresenter.receiveChoice(secondChoiceButton.getText().toString()); //Send the choice to receiveChoice()
        }
        else if (i == R.id.thirdChoiceButton){ //When the user tapes on the thirdChoiceButton
            playGameActivityPresenter.receiveChoice(thirdChoiceButton.getText().toString()); //Send the choice to receiveChoice()
        }
        else if (i == R.id.fourthChoiceButton){ //When the user tapes on the fourthChoiceButton
            playGameActivityPresenter.receiveChoice(fourthChoiceButton.getText().toString()); //Send the choice to receiveChoice()
        }
    }

    //If the user taps on different physical keys of the handset
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) //When the user tapes on the back button
        {
            playGameActivityPresenter.pauseGame(); //Pauses the player
            playGameActivityPresenter.goBackToChooseGameActivity(); //Show the ChooseGameActivity
        }
        else if ((keyCode == KeyEvent.KEYCODE_POWER)){ //When the user tapes on the power button
            playGameActivityPresenter.pauseGame(); //Pauses the player
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
