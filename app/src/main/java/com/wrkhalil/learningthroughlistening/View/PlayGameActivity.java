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

        //Views
        txtDisplay = findViewById(R.id.txtDisplay);
        txtScore = findViewById(R.id.txtScore);

        //Buttons Listeners
        findViewById(R.id.quitGameButton).setOnClickListener(this);
        firstChoiceButton = findViewById(R.id.firstChoiceButton);
        secondChoiceButton = findViewById(R.id.secondChoiceButton);
        thirdChoiceButton = findViewById(R.id.thirdChoiceButton);
        fourthChoiceButton = findViewById(R.id.fourthChoiceButton);

        firstChoiceButton.setOnClickListener(this);
        secondChoiceButton.setOnClickListener(this);
        thirdChoiceButton.setOnClickListener(this);
        fourthChoiceButton.setOnClickListener(this);

        playGameActivityPresenter.setChoicesButtons(false);

        pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(this);
        findViewById(R.id.restartButton).setOnClickListener(this);

        Uri trackFileUri = Uri.parse(videoTrackPath);


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
            player.setOnTimedTextListener(this);
            player.setOnCompletionListener(this);
            player.start();
        } catch (Exception e) {
            Log.w("Subtitle Status", "Failed!");
            e.printStackTrace();
        }
    }

    public void onCompletion(MediaPlayer arg0) {
    playGameActivityPresenter.showScoreActivity();
    }

    public void onResume() {
        super.onResume();
    }

    private int findTrackIndexFor(int mediaTrackType, MediaPlayer.TrackInfo[] trackInfo) {
        int index = -1;
        for (int i = 0; i < trackInfo.length; i++) {
            if (trackInfo[i].getTrackType() == mediaTrackType) {
                return i;
            }
        }
        return index;
    }

    @Override
    public void onTimedText(final MediaPlayer mp, final TimedText text) {
        if (text != null) {
            handler.post(() -> {
                playGameActivityPresenter.refreshPlayerDisplay(text);
            });
        }
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.quitGameButton) {
            playGameActivityPresenter.pauseGame();
            playGameActivityPresenter.goBackToChooseGameActivity();
        } else if (i == R.id.pauseButton) {
            Log.d("Button Hit", pauseButton.getText()+" ");
            if (pauseButton.getText().equals("Pause")){
                Log.d("Button Hit", "Pause button is clicked");
                playGameActivityPresenter.pauseGame();
            }
            else if (pauseButton.getText().equals("Start")){
                playGameActivityPresenter.startGame();
            }

        } else if (i == R.id.restartButton) {
            playGameActivityPresenter.pauseGame();
            playGameActivityPresenter.restartGame(position);
        }
        else if (i == R.id.firstChoiceButton){
            playGameActivityPresenter.receiveChoice(firstChoiceButton.getText().toString());
        }
        else if (i == R.id.secondChoiceButton){
            playGameActivityPresenter.receiveChoice(secondChoiceButton.getText().toString());
        }
        else if (i == R.id.thirdChoiceButton){
            playGameActivityPresenter.receiveChoice(thirdChoiceButton.getText().toString());
        }
        else if (i == R.id.fourthChoiceButton){
            playGameActivityPresenter.receiveChoice(fourthChoiceButton.getText().toString());
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event)
    {
        if ((keyCode == KeyEvent.KEYCODE_BACK))
        {
            playGameActivityPresenter.pauseGame();
            playGameActivityPresenter.goBackToChooseGameActivity();
        }
        else if ((keyCode == KeyEvent.KEYCODE_POWER)){
            playGameActivityPresenter.pauseGame();
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onPointerCaptureChanged(boolean hasCapture) {

    }
}
