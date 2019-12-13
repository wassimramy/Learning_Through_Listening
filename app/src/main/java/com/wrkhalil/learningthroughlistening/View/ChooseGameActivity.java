package com.wrkhalil.learningthroughlistening.View;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.wrkhalil.learningthroughlistening.Presenter.ChooseGameActivityPresenter;
import com.wrkhalil.learningthroughlistening.R;

public class ChooseGameActivity extends AppCompatActivity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        RecyclerView recyclerView;
        RecyclerView.LayoutManager layoutManager;

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_game);
        recyclerView = findViewById(R.id.my_recycler_view);
        ChooseGameActivityPresenter chooseGameActivityPresenter = new ChooseGameActivityPresenter(this);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Retrieve the position of the item clicked in the recycleView and send it to ChooseDifficultyActivity to play the game
        chooseGameActivityPresenter.updateRecyclerView(recyclerView);
    }

    public void onResume() {
        super.onResume();
    }
}
