package com.wrkhalil.learningthroughlistening;

import android.util.Log;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.ArrayList;
import java.util.List;

public class SRTFile {

    private List<String> parsedTranscript = new ArrayList<>();
    private List<String> generatedTranscript = new ArrayList<>();
    private StringBuilder closedCaptions = new StringBuilder();

    SRTFile(List<String> parsedTranscript, List<String> generatedTranscript){
        this.parsedTranscript = parsedTranscript;
        this.generatedTranscript = generatedTranscript;
        generateClosedCaptions();
        CheckClosedCaptions();
    }

    private void generateClosedCaptions(){
        int generatedTranscriptIndex = 0;
        for (int i = 0 ; i < parsedTranscript.size() ; i++){
            if (parsedTranscript.get(i).charAt(0) == generatedTranscript.get(generatedTranscriptIndex).charAt(0)){
                parsedTranscript.set(i, generatedTranscript.get(generatedTranscriptIndex));
                generatedTranscriptIndex ++;
            }
        }
    }

    private void CheckClosedCaptions(){

        closedCaptions.append(parsedTranscript.get(0));
        for (int i = 1 ; i < parsedTranscript.size() ; i++){
                closedCaptions.append  ('\n' + parsedTranscript.get(i));
        }

        Log.d("SRTFile", "CheckClosedCaptions: " + closedCaptions);
    }

}
