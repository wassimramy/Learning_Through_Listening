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

    private String id;
    private String title;
    private String thumbnailURL;

    YouTubeDataRetriever(String id){
        this.id = id;
        getYouTubeData();
    }

    public String getTitle() {
        return title;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }

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
                        Log.d("Array Size", jsonArray.toString() + "");
                        JSONObject jo = jsonArray.getJSONObject(0);
                        jo = new JSONObject(jo.getString("snippet"));
                        YouTubeDataRetriever.this.title =  jo.getString("title");
                        Log.d("Video Title :", YouTubeDataRetriever.this.title);
                        jo = new JSONObject(jo.getString("thumbnails"));
                        jo = new JSONObject(jo.getString("default"));
                        YouTubeDataRetriever.this.thumbnailURL =  jo.getString("url");
                        Log.d("Video Title :", YouTubeDataRetriever.this.thumbnailURL);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }, error -> Log.d("Response:", "Unavailable"));

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }
}
