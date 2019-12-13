package com.wrkhalil.learningthroughlistening.Model;

import java.util.ArrayList;
import java.util.List;

public class NitrxgenSRTFileParser {

    //Attributes
    private String id;
    private String transcript;
    private List<String> parsedTranscript = new ArrayList<>();

    NitrxgenSRTFileParser (String id, String transcript){
        this.transcript = transcript; //Fetch transcript
        this.id = id; //Fetch id
        generateNewSRTFile(); //Executed to parse the .srt file
    }


    private void generateNewSRTFile(){
        String subString; //Temporary variable to store the parsed line
        int start = 0, end; //Counters to set the beginning and the end boundaries of a line
        for (int i = 0 ; i < transcript.length()-1; i++){
            if (transcript.charAt(i) == '\n'){ //Enter when '\n' is detected
                end = i; //Set the end boundary of a line to the '\n' index
                subString = transcript.substring(start, end); //Store the line temporarily
                parsedTranscript.add(subString); //Add the line to the array list
                start = end+1; //Set the start boundary of the next line to the current line end+1
            }
        }
    }

    //Getters
    public List<String> getParsedTranscript() {
        return parsedTranscript;
    }

}
