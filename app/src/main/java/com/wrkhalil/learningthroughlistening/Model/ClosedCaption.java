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

    //Attributes
    private String id;
    private NitrxgenSRTFileParser nitrxgenSRTFileParser;
    private NitrxgenTXTFileParser nitrxgenTXTFileParser;
    private SRTFile srtFile;
    private String path;
    private RequestQueue queue = Volley.newRequestQueue(BaseApplication.getAppContext());


    //ClosedCaption Constructor
    ClosedCaption (String id){
        this.id = id; //Fetch id
        getSRTFile(); //Executed to fetch the SRT file
    }

    //Fetch the .srt file
    private void getSRTFile (){

        String url ="https://www.nitrxgen.net/youtube_cc/" + id + "/0.srt";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Display the first 500 characters of the response string.
                    parseSRTFile(response); //Send the response to parseSRTFile() to get parsed
                    Log.d("Response from nitrxgen:", "SRT File is fetched successfully " + id); //Notify the developer about the retrieval state for testing purposes
                }, error -> {
            Log.d("Response from nitrxgen:", "SRT File is unavailable " + id); //Notify the developer about the retrieval state for testing purposes
            getSRTFile(); //Restart the request
        });
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    //Fetch .txt file
    private void getTXTFile(){
        // Instantiate the RequestQueue.
        String url ="https://www.nitrxgen.net/youtube_cc/" + id + "/0.txt";

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                response -> {
                    // Display the first 500 characters of the response string.
                    Log.d("Response from nitrxgen:", "TXT File is fetched successfully " + id); //Notify the developer about the retrieval state for testing purposes
                    parseTXTFile(response); //Send the response to parseTXTFile() to get parsed
                    Model.fetchingTranscript = true; //Notify the model that the data retrieval stage is accomplished successfully
                    ChooseDifficultyActivityPresenter.settingButtonsStatus(); //Try to change the enable state of the buttons

                }, error -> {
            Model.fetchingTranscript = false; //Notify the model that the data retrieval stage has failed
            ChooseDifficultyActivityPresenter.settingButtonsStatus();
            Log.d("Response from nitrxgen:", "TXT File is unavailable " + id); //Notify the developer about the retrieval state for testing purposes
            getTXTFile(); //Restart the request
        });

        // Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    //Executed to parse the .txt file and generate the new .srt file
    private void parseTXTFile(String transcript){
        nitrxgenTXTFileParser = new NitrxgenTXTFileParser(id, transcript); //Parse the .txt file
        generateSRTFile(); //Write the new .srt file
    }

    //Executed to parse the .srt file and fetch the .txt file
    private void parseSRTFile(String transcript){
        nitrxgenSRTFileParser = new NitrxgenSRTFileParser(id, transcript); //Parse the .srt file
        getTXTFile(); //Fetch the .txt file
    }

    //Write the new .srt file
    private void generateSRTFile(){
        if (nitrxgenTXTFileParser.getGeneratedTranscript() != null &&
                nitrxgenSRTFileParser.getParsedTranscript() != null) //Check if both files are available
            srtFile = new SRTFile( id, nitrxgenSRTFileParser.getParsedTranscript(),
                    nitrxgenTXTFileParser.getGeneratedTranscript()); //Merge both array lists to write the new .srt file
            this.path = srtFile.getPath(); //Receive the path of the new .srt file
            Log.d("ClosedCaption", "generateSRTFile: Both SRT & TXT files are generated"); //Notify the developer about the retrieval state for testing purposes
    }

    //Getters
    public List<ChoicesGenerator> getChoices() {
        return nitrxgenTXTFileParser.getChoices();
    }
    public String getPath() {
        return path;
    }

}
