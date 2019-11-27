package com.wrkhalil.learningthroughlistening;

import android.content.Intent;
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

import java.io.ByteArrayInputStream;
import java.io.Closeable;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URI;
import java.util.Locale;




public class PlayGameActivity extends AppCompatActivity implements MediaPlayer.OnTimedTextListener, View.OnClickListener {
    private static Handler handler = new Handler();
    private TextView txtDisplay;
    private String videoID, videoThumbnailURL, videoClosedCaptions, videoTrackPath;
    private MediaPlayer player;
    private Button pauseButton;
    private int position;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_game);
        Intent intent = getIntent();
        position = intent.getIntExtra ("Position", 0); //get the URI value from the previous activity

        videoID = SignInActivity.videoList.get(position).id;
        videoThumbnailURL = SignInActivity.videoList.get(position).getThumbnailURL();
        SignInActivity.videoList.get(position).generateclosedCaption();
        //videoClosedCaptions = SignInActivity.videoList.get(position).getClosedCaptions();
        videoTrackPath = SignInActivity.videoList.get(position).getTrackPath();

        //Views
        txtDisplay = (TextView) findViewById(R.id.txtDisplay);

        //Buttons Listeners
        findViewById(R.id.quitGameButton).setOnClickListener(this);
        pauseButton = (Button) findViewById(R.id.pauseButton);
        pauseButton.setOnClickListener(this);
        findViewById(R.id.restartButton).setOnClickListener(this);

        //Uri trackFileUri = Uri.parse(videoTrackPath);

        /*


        player = MediaPlayer.create(this, trackFileUri);
        try {
            player.addTimedTextSource(getSubtitleFile(R.raw.sub),
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

         */

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

    private String getSubtitleFile(int resId) {
        String fileName = getResources().getResourceEntryName(resId);
        File subtitleFile = getFileStreamPath(fileName);
        // Copy the file from the res/raw folder to your app folder on the
        // device
        InputStream inputStream = null;
        OutputStream outputStream = null;
        try {
            inputStream = new ByteArrayInputStream(videoClosedCaptions.getBytes());
            outputStream = new FileOutputStream(subtitleFile, false);
            copyFile(inputStream, outputStream);
            return subtitleFile.getAbsolutePath();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            closeStreams(inputStream, outputStream);
        }
        return "";
    }

    private void copyFile(InputStream inputStream, OutputStream outputStream)
            throws IOException {
        final int BUFFER_SIZE = 1024;
        byte[] buffer = new byte[BUFFER_SIZE];
        int length = -1;
        while ((length = inputStream.read(buffer)) != -1) {
            outputStream.write(buffer, 0, length);
        }
    }

    // A handy method I use to close all the streams
    private void closeStreams(Closeable... closeables) {
        if (closeables != null) {
            for (Closeable stream : closeables) {
                if (stream != null) {
                    try {
                        stream.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    @Override
    public void onTimedText(final MediaPlayer mp, final TimedText text) {
        if (text != null) {
            handler.post(new Runnable() {
                @Override
                public void run() {
                    int seconds = mp.getCurrentPosition() / 1000;

                    txtDisplay.setText("[" + secondsToDuration(seconds) + "] "
                            + text.getText());
                }
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
        //player.pause();
    }

    // Start The Game
    public void startGame() {
        pauseButton.setText("Pause");
        //player.start();
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
    }
}
