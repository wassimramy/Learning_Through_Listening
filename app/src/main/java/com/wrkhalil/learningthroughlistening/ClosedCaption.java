package com.wrkhalil.learningthroughlistening;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class ClosedCaption {

    private String id;
    private NitrxgenSRTFileParser nitrxgenSRTFileParser;
    private NitrxgenTXTFileParser nitrxgenTXTFileParser;
    private SRTFile srtFile;
    private String path;
    private RequestQueue queue = Volley.newRequestQueue(BaseApplication.getAppContext());


    ClosedCaption (String id){
        this.id = id;
        getSRTFile();
    }

    public String getPath() {
        return path;
    }

    private void getSRTFile (){

        String url ="https://www.nitrxgen.net/youtube_cc/" + id + "/0.srt";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        // Display the first 500 characters of the response string.
                        parseSRTFile(response);
                        Log.d("Response from nitrxgen:", "SRT File is fetched successfully " + id);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response from nitrxgen:", "SRT File is unavailable " + id);
            }
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void getTXTFile(){
        // Instantiate the RequestQueue.
        //RequestQueue queue = Volley.newRequestQueue(BaseApplication.getAppContext());
        String url ="https://www.nitrxgen.net/youtube_cc/" + id + "/0.txt";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Display the first 500 characters of the response string.
                    Log.d("Response from nitrxgen:", "TXT File is fetched successfully " + id);
                    parseTXTFile(response);

                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("Response from nitrxgen:", "TXT File is unavailable " + id);
            }
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    private void parseTXTFile(String transcript){
        nitrxgenTXTFileParser = new NitrxgenTXTFileParser(id, transcript);
        generateSRTFile();
    }

    private void parseSRTFile(String transcript){
        nitrxgenSRTFileParser = new NitrxgenSRTFileParser(id, transcript);
        getTXTFile();
    }

    private void generateSRTFile(){
        if (nitrxgenTXTFileParser.getGeneratedTranscript() != null &&
                nitrxgenSRTFileParser.getParsedTranscript() != null)

            srtFile = new SRTFile( id, nitrxgenSRTFileParser.getParsedTranscript(),
                    nitrxgenTXTFileParser.getGeneratedTranscript());

            this.path = srtFile.getPath();

            Log.d("ClosedCaption", "generateSRTFile: Both SRT & TXT files are generated");
    }

}
