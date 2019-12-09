package com.wrkhalil.learningthroughlistening.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NitrxgenSRTFileParser {

    private String id;
    private String transcript;
    private List<String> parsedTranscript = new ArrayList<>();

    NitrxgenSRTFileParser (String id, String transcript){
        //Log.d( "NitrxgenSRTFileParser", "Generating the new SRT file!");
        this.transcript = transcript;
        this.id = id;
        generateNewSRTFile();
    }

    public List<String> getParsedTranscript() {
        return parsedTranscript;
    }

    private void generateNewSRTFile(){

        String subString;
        int start = 0, end;
        for (int i = 0 ; i < transcript.length()-1; i++){
            if (transcript.charAt(i) == '\n'){
                end = i;
                subString = transcript.substring(start, end);
                parsedTranscript.add(subString);
                start = end+1;
            }
        }
    }
}
