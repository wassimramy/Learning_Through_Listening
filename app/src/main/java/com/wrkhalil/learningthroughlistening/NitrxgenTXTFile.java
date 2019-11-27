package com.wrkhalil.learningthroughlistening;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class NitrxgenTXTFile {


    private String id;
    private String transcript;
    private NitrxgenTXTFileParser nitrxgenTXTFileParser;

    NitrxgenTXTFile(String id){
        this.id = id;
        getTXTFile(id);
    }

    private void getTXTFile(String id){
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(BaseApplication.getAppContext());
        String url ="https://www.nitrxgen.net/youtube_cc/" + id + "/0.txt";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        NitrxgenTXTFile.this.transcript = response;
                        Log.d("Response from nitrxgen:", "TXT File is fetched successfully " + id);
                        parseTXTFile();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                NitrxgenTXTFile.this.transcript = "Unavailable";
                Log.d("Response from nitrxgen:", "TXT File is unavailable " + id);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void parseTXTFile(){
        nitrxgenTXTFileParser = new NitrxgenTXTFileParser(id, transcript);
    }

}
