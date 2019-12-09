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

        chooseDifficultyActivityPresenter.updateFields(position);
    }

    public void onResume() {
        super.onResume();
    }


    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.easyGameButton) {
            chooseDifficultyActivityPresenter.startGameActivity(position, "Easy");
        }
        else if (i == R.id.mediumGameButton) {
            chooseDifficultyActivityPresenter.startGameActivity(position, "Medium");
        }
        else if (i == R.id.hardGameButton) {
            chooseDifficultyActivityPresenter.startGameActivity(position, "Hard");
        }
    }
}
