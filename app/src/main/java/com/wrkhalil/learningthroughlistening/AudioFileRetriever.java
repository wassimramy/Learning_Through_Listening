package com.wrkhalil.learningthroughlistening;

import android.app.ProgressDialog;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

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
            ChooseDifficultyActivity.easyGameButton.setEnabled(true);
            ChooseDifficultyActivity.mediumGameButton.setEnabled(true);
            ChooseDifficultyActivity.hardGameButton.setEnabled(true);
            Log.d("Fetching mp3 File", "Location on mobile device: "
                    + finalLocalFile.getAbsolutePath());
        }).addOnFailureListener(exception -> {
            // Handle any errors
            ChooseDifficultyActivity.easyGameButton.setEnabled(false);
            ChooseDifficultyActivity.mediumGameButton.setEnabled(false);
            ChooseDifficultyActivity.hardGameButton.setEnabled(false);
        });
    }

}
