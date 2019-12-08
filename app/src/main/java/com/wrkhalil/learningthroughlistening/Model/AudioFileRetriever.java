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

    private String id;
    private String trackPath;

    AudioFileRetriever(String id){
        this.id = id;
        downloadTrack();
    }

    public String getTrackPath() {
        return trackPath;
    }

    private void downloadTrack(){

        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a reference to a file from a Google Cloud Storage URI
        StorageReference gsReference = storage.getReferenceFromUrl("gs://learning-through-" +
                "listening.appspot.com/server/saving-data/fireblog/videos/" + id + ".mp3");

        File localFile = null;
        try {
            localFile = File.createTempFile("audio", "mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }

        File finalLocalFile = localFile;
        gsReference.getFile(localFile).addOnSuccessListener(taskSnapshot -> {
            // Local temp file has been created
            AudioFileRetriever.this.trackPath = finalLocalFile.getAbsolutePath();
            Model.fetchingAudio = true;
            ChooseDifficultyActivityPresenter.settingButtonsStatus();
            Log.d("Fetching mp3 File", "Location on mobile device: "
                    + finalLocalFile.getAbsolutePath());
        }).addOnFailureListener(exception -> {
            // Handle any errors
            Model.fetchingAudio = false;
            ChooseDifficultyActivityPresenter.settingButtonsStatus();
            downloadTrack();
        });
    }

}
