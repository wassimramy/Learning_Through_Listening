package com.wrkhalil.learningthroughlistening;

import android.util.Log;

public class NitrxgenSRTFileParser {

    private String id;
    private String transcript;

    NitrxgenSRTFileParser (String id, String transcript){
        Log.d( "NitrxgenSRTFileParser", "Generating the new SRT file!");
        this.transcript = transcript;
        this.id = id;
        generateNewSRTFile();
    }

    public String getTranscript() {
        return transcript;
    }

    private void generateNewSRTFile(){

        String subString;
        int line = 0, start = 0, end = 0;
        for (int i = 0 ; i < transcript.length()-1; i++){
            if (transcript.charAt(i) == '\n'){
                end = i;
                line ++;
                subString = transcript.substring(start, end);
                Log.d( "NitrxgenSRTFileParser", "Group --> " + line + " " + subString +" " + id);
                start = end+1;
            }
        }
    }

}
