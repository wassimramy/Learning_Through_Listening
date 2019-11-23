package com.wrkhalil.learningthroughlistening;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;


public class PlayGameActivity extends AppCompatActivity {

    public List<Video> videoList;
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

        //populateVideoListFromFirebase();
    }


    public void onResume() {
        super.onResume();
    }



}
