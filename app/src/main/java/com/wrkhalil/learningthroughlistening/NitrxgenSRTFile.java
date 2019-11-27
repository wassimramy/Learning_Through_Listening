package com.wrkhalil.learningthroughlistening;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class NitrxgenSRTFile {


    private String id;
    private String transcript;
    private NitrxgenSRTFileParser nitrxgenSRTFileParser;

    NitrxgenSRTFile(String id){
        this.id = id;
        getSRTFile(id);
    }

    public NitrxgenSRTFileParser getNitrxgenSRTFileParser(){
        return nitrxgenSRTFileParser;
    }

    private void getSRTFile (String id){

        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(BaseApplication.getAppContext());
        String url ="https://www.nitrxgen.net/youtube_cc/" + id + "/0.srt";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        NitrxgenSRTFile.this.transcript =  response;
                        generateNewSRTFile();
                        Log.d("Response from nitrxgen:", "SRT File is fetched successfully " + id);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NitrxgenSRTFile.this.transcript =  "SRT File is Unavailable";
                Log.d("Response from nitrxgen:", "SRT File is unavailable " + id);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void generateNewSRTFile(){
        nitrxgenSRTFileParser = new NitrxgenSRTFileParser(id, transcript);
    }
}
