package com.wrkhalil.learningthroughlistening.Model;

import android.util.Log;
import android.view.Display;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.wrkhalil.learningthroughlistening.Presenter.ChooseDifficultyActivityPresenter;
import com.wrkhalil.learningthroughlistening.View.ChooseDifficultyActivity;

import java.io.File;
import java.io.IOException;

public class AudioFileRetriever {

    //Attributes
    private String id;
    private String trackPath;

    //AudioFileRetriever Constructor
    AudioFileRetriever(String id){
        this.id = id; //Fetch id
        downloadTrack(); //Executed to download the audio file temporarily
    }

    //Executed to download the audio file temporarily
    private void downloadTrack(){

        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a reference to a file from a Google Cloud Storage URI
        StorageReference gsReference = storage.getReferenceFromUrl("gs://learning-through-" +
                "listening.appspot.com/server/saving-data/fireblog/videos/" + id + ".mp3");

        File localFile = null;
        try {
            localFile = File.createTempFile("audio", "mp3"); //Create a temporary .mp3 file
        } catch (IOException e) {
            e.printStackTrace(); //Print the error stack in case of a problem
        }

        File finalLocalFile = localFile;
        gsReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
            // Local temp file has been created
            AudioFileRetriever.this.trackPath = finalLocalFile.getAbsolutePath(); //Return the temporary file path
            Model.fetchingAudio = true; //Set the fetchingAudio to true to notify the model that the data retrieval state is accomplished successfully
            ChooseDifficultyActivityPresenter.settingButtonsStatus(); //Try to change the enable state of the buttons
            Log.d("Fetching mp3 File", "Location on mobile device: "
                    + finalLocalFile.getAbsolutePath()); //Print the path in log for testing purposes
        }).addOnFailureListener(exception -> {
            // Handle any errors
            Model.fetchingAudio = false;  //Set the fetchingAudio to false to notify the model that the data retrieval state has failed
            downloadTrack(); //Resend the request
        });
    }

    //Getters
    public String getTrackPath() {
        return trackPath;
    }
}
