package com.wrkhalil.learningthroughlistening.Model;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wrkhalil.learningthroughlistening.Presenter.ChooseDifficultyActivityPresenter;
import com.wrkhalil.learningthroughlistening.View.ChooseDifficultyActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Video {

    //Attributes
    public String id;
    private ClosedCaption closedCaption;
    private YouTubeDataRetriever youTubeDataRetriever;
    private FirebaseDataRetriever firebaseDataRetriever;
    private AudioFileRetriever audioFileRetriever;
    public int plays;

    //Video Constructor
    Video (){
    }

    //Video Constructor
    Video (String id, int plays){
        this.id = id; //Fetch id
        this.plays = plays; //Fetch plays
        youTubeDataRetriever = new YouTubeDataRetriever (id); //Retrieve video thumbnail and title
        firebaseDataRetriever = new FirebaseDataRetriever(id); //Retrieve the # of plays
    }

    //Executed to generate closed caption
    public void generateClosedCaption(){
        Model.fetchingTranscript = false; //Set fetchingTranscript to false to block the user from proceeding to the game until closedCaption is generated
        ChooseDifficultyActivityPresenter.settingButtonsStatus(); //Called to disable the buttons since the data is not ready yet
        closedCaption = new ClosedCaption(id); //Generates the closed captions
    }

    //Executed to fetch the audio file
    public void downloadAudioFile(){
        Model.fetchingAudio = false; //Set fetchingAudio to false to block the user from proceeding to the game until the file is loaded
        ChooseDifficultyActivityPresenter.settingButtonsStatus(); //Called to disable the buttons since the data is not ready yet
        audioFileRetriever = new AudioFileRetriever(id); //Grabs the audio file
    }

    //Format the object sent to Firebase
    private Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id); //Grab the instance's id
        result.put("plays", plays); //Grab the number of plays
        return result;
    }

    //Executed when the user finishes a game and submits the score
    public void incrementNumberOfPlays(){

        plays ++; //Increment the number of plays
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/saving-data/fireblog/videos");
        final DatabaseReference videosRef = ref.child(id); //Point at the right video entry

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                videosRef.setValue(toMap()); //Format the object for Firebase to store it
                Log.d("Video Info Update:", plays + " Plays"); //Send a log for the developer for testing purposes
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.d("Video Info Update:", "Update Failed"); //Send a log for the developer for testing purposes
            }
        });
    }

    //Getters
    public String getClosedCaptionPath() {
        return closedCaption.getPath();
    }
    public String getTrackPath(){
        return audioFileRetriever.getTrackPath();
    }
    public String getTitle (){
        return youTubeDataRetriever.getTitle();
    }
    public String getThumbnailURL (){
        return youTubeDataRetriever.getThumbnailURL();
    }
    public int getPlays() {
        return firebaseDataRetriever.getPlays();
    }
    public List<ChoicesGenerator> getChoices() {
        return closedCaption.getChoices();
    }
}
