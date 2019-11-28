package com.wrkhalil.learningthroughlistening;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import static com.wrkhalil.learningthroughlistening.SignInActivity.videoList;


public class ChooseGameActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);
        recyclerView = findViewById(R.id.my_recycler_view);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Retrieve the position of the item clicked in the recycleView and send it to startItemEditActivity to show the respective item information
        VideoAdapter videoAdapter = new VideoAdapter(this, videoList, position -> startGameActivity(position));
        recyclerView.setAdapter(videoAdapter); //Update the recyclerView
    }

    public void startGameActivity(int position){
        Intent intent = new Intent(this, ChooseDifficultyActivity.class);
        intent.putExtra("Position", position); //Sends the URI value to the ShowPictureActivity to fetch the picture
        startActivity(intent); //Start the activity
        this.finish();
    }

    public void onResume() {
        super.onResume();
    }



}
