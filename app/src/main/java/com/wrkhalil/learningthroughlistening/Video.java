package com.wrkhalil.learningthroughlistening;

import android.util.Log;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;

public class Video {

    public String id;
    private String title;
    private String thumbnailURL;
    private String closedCaptions;
    private String missingClosedCaptions;
    private String trackPath;
    public int plays;

    Video (){

    }

    Video (String id, int plays){
        this.id = id;
        this.plays = plays;

        getYouTubeData(id);
        getClosedCaptionsData(id);
        getFirebaseData(id);
        //getTrackData(id);
    }

    public String getClosedCaptions(){
        return closedCaptions;
    }

    public String getTrackPath(){
        return trackPath;
    }

    public void getTrackData (String id){

        FirebaseStorage storage = FirebaseStorage.getInstance();

        // Create a storage reference from our app
        StorageReference storageRef = storage.getReference();

        // Create a reference to a file from a Google Cloud Storage URI
        StorageReference gsReference = storage.getReferenceFromUrl("gs://learning-through-listening.appspot.com/server/saving-data/fireblog/videos/" + id + ".mp3");

        File localFile = null;
        try {
            localFile = File.createTempFile("audio", "mp3");
        } catch (IOException e) {
            e.printStackTrace();
        }

        File finalLocalFile = localFile;
        gsReference.getFile(localFile).addOnSuccessListener(new OnSuccessListener<FileDownloadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(FileDownloadTask.TaskSnapshot taskSnapshot) {
                // Local temp file has been created
                Video.this.trackPath = finalLocalFile.getAbsolutePath();
                Log.d("Fetching mp3 File", "Location on mobile device: " + finalLocalFile.getAbsolutePath());
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });


    }

    public void getClosedCaptionsData (String id){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(BaseApplication.getAppContext());
        String url ="https://www.nitrxgen.net/youtube_cc/" + id + "/0.srt";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        Video.this.closedCaptions = response;
                        //Log.d("Response from nitrxgen:", "nitrxgen: " + response);
                        Video.this.getMissingClosedCaptions();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Video.this.closedCaptions = "Unavailable";
                Log.d("Response from nitrxgen:", "Unavailable");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);

    }

    private void getMissingClosedCaptions (){

        for (int i = 1 ; i < closedCaptions.length()-1; i++){
        int start = 0;
        int end = i;
        boolean ready = false;

        if (closedCaptions.charAt(i-1) == ' '){
            start = i-1;
        }
        else if (closedCaptions.charAt(i+1) == ' ' || closedCaptions.charAt(i+1) == ','){
            end = i+1;
            ready = true;
        }

        if (ready){
            ready = false;
            Log.d("Parsed Word", "Parsed Word: " + closedCaptions.substring(start, end));
        }

        }
    }

    public void getYouTubeData (String id){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(BaseApplication.getAppContext());
        String url ="https://www.googleapis.com/youtube/v3/videos?part=snippet%2CcontentDetails%2Cstatistics&id=" + id + "&key=AIzaSyCZ6OFVEKfntQbp62CNoopteGvqbatJ-Pw";

// Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        //Log.d("Response:", response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray jsonArray = jsonObject.getJSONArray("items");
                            Log.d("Array Size", jsonArray.toString() + "");
                            JSONObject jo = jsonArray.getJSONObject(0);
                            jo = new JSONObject(jo.getString("snippet"));
                            Video.this.title =  jo.getString("title");
                            Log.d("Video Title :", Video.this.title);
                            jo = new JSONObject(jo.getString("thumbnails"));
                            jo = new JSONObject(jo.getString("default"));
                            Video.this.thumbnailURL =  jo.getString("url");
                            Log.d("Video Title :", Video.this.thumbnailURL);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response:", "Unavailable");
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    public void getFirebaseData(String id) {

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/saving-data/fireblog/videos");
        DatabaseReference videoRef = ref.child(id+"/plays");

        ValueEventListener postListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // Get Post object and use the values to update the UI
                Video.this.plays  = dataSnapshot.getValue(Integer.class);
                Log.d("This video has been played", plays + " plays");
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                // Getting Post failed, log a message
                Log.w("loadPost:onCancelled", databaseError.toException());
            }
        };
        videoRef.addValueEventListener(postListener);
    }

    public String getTitle (){
        return title;
    }

    public String getThumbnailURL (){
        return thumbnailURL;
    }
}
