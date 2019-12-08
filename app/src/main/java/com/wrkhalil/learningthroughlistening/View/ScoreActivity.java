package com.wrkhalil.learningthroughlistening.View;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wrkhalil.learningthroughlistening.Model.Model;
import com.wrkhalil.learningthroughlistening.R;

public class ScoreActivity extends Activity implements View.OnClickListener {

    private ImageView songThumbnail;
    private TextView txtSongTitle, txtScore;
    private Button backButton, submitScoreButton;
    private int position, score;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_score);
        Intent intent = getIntent();
        position = intent.getIntExtra ("Position", 0); //get the URI value from the previous activity
        score = intent.getIntExtra ("Score", 0); //get the URI value from the previous activity

        //videoClosedCaptions = SignInActivity.videoList.get(position).getClosedCaptionPath();

        //Views
        songThumbnail = findViewById(R.id.songThumbnail);
        txtSongTitle = findViewById(R.id.txtSongTitle);
        txtScore = findViewById(R.id.txtScore);

        RequestOptions requestOptions = new RequestOptions(); //Set the options of for the displayed picture
        requestOptions.placeholder(R.drawable.ic_launcher_background); //Picture displayed when the app is fetching the picture
        requestOptions.error(R.drawable.ic_launcher_background); //Picture displayed when the picture is not fetched
        requestOptions.circleCrop(); //Display the picture in a circle view
        requestOptions.override(600, 600); //Set the resolution of the picture
        Glide.with(this)
                .load(Uri.parse(Model.videoList.get(position).getThumbnailURL())) // Uri of the picture
                .apply(requestOptions) // Set the options
                .into(songThumbnail); // The container where the picture is displayed

        txtSongTitle.setText(Model.videoList.get(position).getTitle());
        txtScore.setText("Score: " + score);

        //Buttons Listeners
        backButton = findViewById(R.id.backButton);
        submitScoreButton = findViewById(R.id.submitScoreButton);

        backButton.setOnClickListener(this);
        submitScoreButton.setOnClickListener(this);
    }

    // Back to Choose Game Activity
    public void goBackToChooseGameActivity() {
        Intent intent = new Intent(this, SignInActivity.class);
        startActivity(intent); //Start the activity
        this.finish();
    }

    private void submitScore(){
        Model.operatingUser.score += score;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/saving-data/fireblog/users");
        final DatabaseReference usersRef = ref.child( Model.operatingUser.firebaseID);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                usersRef.setValue(Model.operatingUser.toMap());
                updateNumberOfPlays();
                Log.d("Account Info Updated:", Model.operatingUser.fullName + " " + Model.operatingUser.score);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    private void updateNumberOfPlays(){

        Model.videoList.get(position).plays ++;
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/saving-data/fireblog/videos");
        final DatabaseReference usersRef = ref.child(Model.videoList.get(position).id);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                usersRef.setValue(Model.videoList.get(position).toMap());
                Log.d("Video Info Updated:", Model.videoList.get(position).plays + " Plays");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    public void onClick(View v) {
        int i = v.getId();
        if (i == R.id.backButton) {
            goBackToChooseGameActivity();
        }
        else if (i == R.id.submitScoreButton){
            submitScore();
            goBackToChooseGameActivity();
        }
    }
}
