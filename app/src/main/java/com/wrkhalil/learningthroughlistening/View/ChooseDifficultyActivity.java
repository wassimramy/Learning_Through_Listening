package com.wrkhalil.learningthroughlistening.View;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.wrkhalil.learningthroughlistening.Presenter.ChooseDifficultyActivityPresenter;
import com.wrkhalil.learningthroughlistening.R;



public class ChooseDifficultyActivity extends AppCompatActivity implements
        View.OnClickListener {

    public static Button easyGameButton, mediumGameButton, hardGameButton;
    private ChooseDifficultyActivityPresenter chooseDifficultyActivityPresenter;
    private int position;
    public ImageView songThumbnail;
    public TextView txtSongTitle;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_difficulty);
        chooseDifficultyActivityPresenter = new ChooseDifficultyActivityPresenter(this);

        Intent intent = getIntent();
        position = intent.getIntExtra ("Position", 0); //get the song's id value from the ChooseGameActivity

        //Buttons declaration
        easyGameButton = findViewById(R.id.easyGameButton);
        mediumGameButton = findViewById(R.id.mediumGameButton);
        hardGameButton = findViewById(R.id.hardGameButton);

        //Buttons Listeners
        easyGameButton.setOnClickListener(this);
        mediumGameButton.setOnClickListener(this);
        hardGameButton.setOnClickListener(this);

        //Views
        songThumbnail = findViewById(R.id.songThumbnail);
        txtSongTitle = findViewById(R.id.txtSongTitle);

        //Display the song's information
        chooseDifficultyActivityPresenter.updateFields(position);
    }

    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.easyGameButton) { //When the user taps on the easyGameButton
            chooseDifficultyActivityPresenter.startGameActivity(position, "Easy"); //Pass the song's id and difficulty to the StartGameActivity
        }
        else if (i == R.id.mediumGameButton) { //When the user taps on the mediumGameButton
            chooseDifficultyActivityPresenter.startGameActivity(position, "Medium"); //Pass the song's id and difficulty to the StartGameActivity
        }
        else if (i == R.id.hardGameButton) { //When the user taps on the hardGameButton
            chooseDifficultyActivityPresenter.startGameActivity(position, "Hard"); //Pass the song's id and difficulty to the StartGameActivity
        }
    }
}
