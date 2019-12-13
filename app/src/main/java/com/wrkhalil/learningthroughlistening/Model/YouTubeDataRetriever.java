package com.wrkhalil.learningthroughlistening.Model;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class YouTubeDataRetriever {

    //Attributes
    private String id;
    private String title;
    private String thumbnailURL;

    //YouTubeDataRetriever Constructor
    YouTubeDataRetriever(String id){
        this.id = id; //Fetch id
        getYouTubeData(); //Send an HTTP request to YouTube Data Api to fetch the song's title and thumbnail
    }

    //Fetch the song's title and thumbnail image URL
    private void getYouTubeData (){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(BaseApplication.getAppContext());
        String url ="https://www.googleapis.com/youtube/v3/videos?part=snippet%2CcontentDetails%2Cstatistics&id=" + id + "&key=AIzaSyCZ6OFVEKfntQbp62CNoopteGvqbatJ-Pw";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> {
                    try {
                        JSONObject jsonObject = new JSONObject(response);
                        JSONArray jsonArray = jsonObject.getJSONArray("items");
                        Log.d("Array Size", jsonArray.toString() + ""); //Send a log for the developer for testing purposes
                        JSONObject jo = jsonArray.getJSONObject(0);
                        jo = new JSONObject(jo.getString("snippet"));
                        YouTubeDataRetriever.this.title =  jo.getString("title"); //Store the song's title
                        Log.d("Video Title :", YouTubeDataRetriever.this.title);
                        jo = new JSONObject(jo.getString("thumbnails"));
                        jo = new JSONObject(jo.getString("default"));
                        YouTubeDataRetriever.this.thumbnailURL =  jo.getString("url"); //Store the song's thumbnailURL
                        Log.d("Video Title :", YouTubeDataRetriever.this.thumbnailURL); //Send a log for the developer for testing purposes
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                },
                //Send a log for the developer for testing purposes
                error -> Log.d("Response:", "Unavailable") );

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }


    //Getters
    public String getTitle() {
        return title;
    }
    public String getThumbnailURL() {
        return thumbnailURL;
    }
}
