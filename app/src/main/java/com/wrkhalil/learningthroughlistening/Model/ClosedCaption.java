package com.wrkhalil.learningthroughlistening.Model;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.wrkhalil.learningthroughlistening.Presenter.ChooseDifficultyActivityPresenter;
import com.wrkhalil.learningthroughlistening.View.ChooseDifficultyActivity;

import java.util.List;

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

    public List<ChoicesGenerator> getChoices() {
        return nitrxgenTXTFileParser.getChoices();
    }

    public String getPath() {
        return path;
    }

    private void getSRTFile (){

        String url ="https://www.nitrxgen.net/youtube_cc/" + id + "/0.srt";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Display the first 500 characters of the response string.
                    parseSRTFile(response);
                    Log.d("Response from nitrxgen:", "SRT File is fetched successfully " + id);
                }, error -> {
            Log.d("Response from nitrxgen:", "SRT File is unavailable " + id);
            getSRTFile();
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
                    Model.fetchingTranscript = true;
                    ChooseDifficultyActivityPresenter.settingButtonsStatus();

                }, error -> {
            Model.fetchingTranscript = false;
            ChooseDifficultyActivityPresenter.settingButtonsStatus();
            Log.d("Response from nitrxgen:", "TXT File is unavailable " + id);
            getTXTFile();
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
