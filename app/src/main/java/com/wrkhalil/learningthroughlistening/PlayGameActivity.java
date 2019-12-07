package com.wrkhalil.learningthroughlistening;
import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.media.TimedText;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Locale;


public class PlayGameActivity extends AppCompatActivity implements MediaPlayer.OnTimedTextListener, View.OnClickListener {
    private static Handler handler = new Handler();
    private TextView txtDisplay, txtScore;
    private String videoID, videoThumbnailURL, videoClosedCaptions, videoTrackPath, wrongChoice;
    private MediaPlayer player;
    private Button pauseButton;
    private Button firstChoiceButton, secondChoiceButton, thirdChoiceButton, fourthChoiceButton;
    private int position, score = 0, hit = 1, miss = 1;
    private String targetWord = "null";
    private int choicesIndex = 0;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        Intent intent = getIntent();
        position = intent.getIntExtra ("Position", 0); //get the URI value from the previous activity

        videoID = SignInActivity.videoList.get(position).id;
        videoThumbnailURL = SignInActivity.videoList.get(position).getThumbnailURL();
        videoTrackPath = SignInActivity.videoList.get(position).getTrackPath();

        //videoClosedCaptions = SignInActivity.videoList.get(position).getClosedCaptionPath();
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

        setChoicesButtons(false);

        pauseButton = findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(this);
        findViewById(R.id.restartButton).setOnClickListener(this);

        Uri trackFileUri = Uri.parse(videoTrackPath);


        player = MediaPlayer.create(this, trackFileUri);
        try {
            player.addTimedTextSource(SignInActivity.videoList.get(position).getClosedCaptionPath() ,
                    MediaPlayer.MEDIA_MIMETYPE_TEXT_SUBRIP);
            int textTrackIndex = findTrackIndexFor(
                    MediaPlayer.TrackInfo.MEDIA_TRACK_TYPE_TIMEDTEXT, player.getTrackInfo());
            if (textTrackIndex >= 0) {
                player.selectTrack(textTrackIndex);
            } else {
                Log.w("Subtitle Status", "Cannot find text track!");
            }
            player.setOnTimedTextListener(this);
            player.start();
        } catch (Exception e) {
            Log.w("Subtitle Status", "Failed!");
            e.printStackTrace();
        }
    }

    private void setChoicesButtons(boolean choicesEnable){

        PlayGameActivity.this.firstChoiceButton.setText(
                SignInActivity.videoList.get(position).getChoices().get(choicesIndex).getFirstChoice());

        PlayGameActivity.this.secondChoiceButton.setText(
                SignInActivity.videoList.get(position).getChoices().get(choicesIndex).getSecondChoice());

        PlayGameActivity.this.thirdChoiceButton.setText(
                SignInActivity.videoList.get(position).getChoices().get(choicesIndex).getThirdChoice());

        PlayGameActivity.this.fourthChoiceButton.setText(
                SignInActivity.videoList.get(position).getChoices().get(choicesIndex).getFourthChoice());

        PlayGameActivity.this.firstChoiceButton.setEnabled(choicesEnable);
        PlayGameActivity.this.secondChoiceButton.setEnabled(choicesEnable);
        PlayGameActivity.this.thirdChoiceButton.setEnabled(choicesEnable);
        PlayGameActivity.this.fourthChoiceButton.setEnabled(choicesEnable);

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
                int seconds = mp.getCurrentPosition() / 10000;

                txtDisplay.setTextColor(Color.parseColor(	"#878383"));

                if (text.getText().contains("_")){
                    wrongChoice = "null";
                    PlayGameActivity.this.setChoicesButtons(true);
                    PlayGameActivity.this.choicesIndex ++;
                }
                else {
                    PlayGameActivity.this.setChoicesButtons(false);
                }

                txtDisplay.setText("[" + secondsToDuration(seconds) + "] "
                        + text.getText());
            });
        }
    }

    // To display the seconds in the duration format 00:00:00
    public String secondsToDuration(int seconds) {
        return String.format("%02d:%02d:%02d", seconds / 3600,
                (seconds % 3600) / 60, (seconds % 60), Locale.US);
    }

    // Back to Choose Game Activity
    public void goBackToChooseGameActivity() {
        Intent intent = new Intent(this, ChooseGameActivity.class);
        startActivity(intent); //Start the activity
        this.finish();
    }

    // Pause The Game
    public void pauseGame() {
        pauseButton.setText("Start");
        player.pause();
    }

    // Start The Game
    public void startGame() {
        pauseButton.setText("Pause");
        player.start();
    }

    private String generateUnderscores(String target){
        String result;
        result = "_";
        for (int i = 1; i < target.length() ; i++){
            result += "_";
        }
        return result;
    }

    // Check if the choice is Correct
    public void receiveChoice(String choice) {
        String txtDisplayString = txtDisplay.getText() + "";
        targetWord = SignInActivity.videoList.get(position).getChoices().get(choicesIndex-1).getTargetWord();

        if(wrongChoice != "null" && !choice.equals(targetWord)){
            txtDisplay.setTextColor(Color.parseColor(	"#FF0000"));
            txtDisplayString = txtDisplayString.replace("X_" + wrongChoice + "_X", "X_" + choice + "_X");
            wrongChoice = choice;

        }
        else if (wrongChoice != "null" && choice.equals(targetWord)){
            txtDisplay.setTextColor(Color.parseColor(	"#3CB371"));
            txtDisplayString = txtDisplayString.replace("X_" + wrongChoice + "_X", choice);
            PlayGameActivity.this.setChoicesButtons(false);
            score += 100;
        }
        else if (!choice.equals(targetWord)){
            txtDisplay.setTextColor(Color.parseColor(	"#FF0000"));
            txtDisplayString = txtDisplayString.replace(generateUnderscores(targetWord), "X_" + choice + "_X");
            wrongChoice = choice;
        }
        else{
            txtDisplay.setTextColor(Color.parseColor(	"#3CB371"));
            txtDisplayString = txtDisplayString.replace(generateUnderscores(targetWord), choice);
            PlayGameActivity.this.setChoicesButtons(false);
            score += 100;
        }

        txtScore.setText("Score: " + score);
        txtDisplay.setText(txtDisplayString);
        Log.d("txtDisplayString", txtDisplayString);
    }


    // Restart The Game
    public void restartGame() {
        Intent intent = new Intent(this, PlayGameActivity.class);
        intent.putExtra("Position", position); //Sends the URI value to the ShowPictureActivity to fetch the picture
        startActivity(intent); //Start the activity
        this.finish();
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.quitGameButton) {
            pauseGame();
            goBackToChooseGameActivity();
        } else if (i == R.id.pauseButton) {
            Log.d("Button Hit", pauseButton.getText()+" ");
            if (pauseButton.getText().equals("Pause")){
                Log.d("Button Hit", "Pause button is clicked");
                pauseGame();
            }
            else if (pauseButton.getText().equals("Start")){
                startGame();
            }

        } else if (i == R.id.restartButton) {
            pauseGame();
            restartGame();
        }
        else if (i == R.id.firstChoiceButton){
            receiveChoice(firstChoiceButton.getText().toString());
        }
        else if (i == R.id.secondChoiceButton){
            receiveChoice(secondChoiceButton.getText().toString());
        }
        else if (i == R.id.thirdChoiceButton){
            receiveChoice(thirdChoiceButton.getText().toString());
        }
        else if (i == R.id.fourthChoiceButton){
            receiveChoice(fourthChoiceButton.getText().toString());
        }
    }
}
