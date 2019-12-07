package com.wrkhalil.learningthroughlistening;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import static com.wrkhalil.learningthroughlistening.SignInActivity.videoList;


public class ChooseDifficultyActivity extends AppCompatActivity implements
        View.OnClickListener {

    public static Button easyGameButton, mediumGameButton, hardGameButton;
    public static boolean fetchingTranscript, fetchingAudio = false;
    private int position;
    private ImageView songThumbnail;
    private TextView txtSongTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_difficulty);

        Intent intent = getIntent();
        position = intent.getIntExtra ("Position", 0); //get the URI value from the previous activity

        easyGameButton = findViewById(R.id.easyGameButton);
        mediumGameButton = findViewById(R.id.mediumGameButton);
        hardGameButton = findViewById(R.id.hardGameButton);

        easyGameButton.setOnClickListener(this);
        mediumGameButton.setOnClickListener(this);
        hardGameButton.setOnClickListener(this);

        //Views
        songThumbnail = findViewById(R.id.songThumbnail);
        txtSongTitle = findViewById(R.id.txtSongTitle);

        RequestOptions requestOptions = new RequestOptions(); //Set the options of for the displayed picture
        requestOptions.placeholder(R.drawable.ic_launcher_background); //Picture displayed when the app is fetching the picture
        requestOptions.error(R.drawable.ic_launcher_background); //Picture displayed when the picture is not fetched
        requestOptions.circleCrop(); //Display the picture in a circle view
        requestOptions.override(600, 600); //Set the resolution of the picture
        Glide.with(this)
                .load(Uri.parse(SignInActivity.videoList.get(position).getThumbnailURL())) // Uri of the picture
                .apply(requestOptions) // Set the options
                .into(songThumbnail); // The container where the picture is displayed

        txtSongTitle.setText(SignInActivity.videoList.get(position).getTitle());

        settingButtonsStatus();

        videoList.get(position).generateClosedCaption();
        videoList.get(position).downloadAudioFile();

    }

    public void startGameActivity(int position, String difficulty){
        Intent intent = new Intent(this, PlayGameActivity.class);
        intent.putExtra("Position", position); //Sends the URI value to the ShowPictureActivity to fetch the picture
        intent.putExtra("Difficulty", difficulty); //Sends the URI value to the ShowPictureActivity to fetch the picture
        startActivity(intent); //Start the activity
        this.finish();
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

    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.easyGameButton) {
            startGameActivity(position, "Easy");
        }
        else if (i == R.id.mediumGameButton) {
            startGameActivity(position, "Medium");
        }
        else if (i == R.id.hardGameButton) {
            startGameActivity(position, "Hard");
        }
    }
}
