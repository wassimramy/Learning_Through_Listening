package com.wrkhalil.learningthroughlistening;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import static com.wrkhalil.learningthroughlistening.SignInActivity.videoList;


public class ChooseDifficultyActivity extends AppCompatActivity implements
        View.OnClickListener {

    public static Button easyGameButton, mediumGameButton, hardGameButton;
    private int position;

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

        easyGameButton.setEnabled(false);
        mediumGameButton.setEnabled(false);
        hardGameButton.setEnabled(false);

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
