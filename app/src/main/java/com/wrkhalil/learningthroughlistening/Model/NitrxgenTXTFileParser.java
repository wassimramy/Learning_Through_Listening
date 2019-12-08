package com.wrkhalil.learningthroughlistening.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NitrxgenTXTFileParser {

    private String id;
    private String closedCaptionsPlaintext;
    private List<String> words = new ArrayList<>();
    private List<String> omittedWords = new ArrayList<>();
    private List<String> targetWords = new ArrayList<>();
    private List<String> generatedTranscript = new ArrayList<>();
    private List<Integer> targetWordsLineNumber = new ArrayList<>();
    private List<Integer> omittedWordsOffset = new ArrayList<>();
    private List<Integer> lineNumber = new ArrayList<>();
    private List<Integer> numberOfWords = new ArrayList<>();
    private List<ChoicesGenerator> choices = new ArrayList<>();

    NitrxgenTXTFileParser(String id, String closedCaptionsPlaintext){
        this.id = id;
        this.closedCaptionsPlaintext = closedCaptionsPlaintext;
        parseSingleWords();
        setMissingWordsOffset();
        generateOmittedWordsList();
    }

    public List<String> getGeneratedTranscript() {
        return generatedTranscript;
    }

    private void generateOmittedWordsList(){
        int numberOfWordsInALine = 1;
        int line = 0, targetNumber = 0;

        String text;
        if (words.size() != 0){
            text = words.get(0);
            omittedWords.add(words.get(0));
            generatedTranscript.add(words.get(0));

            for (int i = 1 ; i < lineNumber.size() ; i++){

                if (line == lineNumber.get(i)){

                    numberOfWordsInALine ++;

                    if (numberOfWordsInALine == omittedWordsOffset.get(line)){

                        omittedWords.add(replaceOmittedWordWithUnderscore(words.get(i)));
                        choices.add(new ChoicesGenerator(words.get(i), words));
                        /*
                        generatedTranscript.set (line, generatedTranscript.get(line) + " " +
                                replaceOmittedWordWithUnderscore(words.get(i)) + " (" +
                                choices.get(targetNumber).getFirstChoice() + ", " +
                                choices.get(targetNumber).getSecondChoice() + ", " +
                                choices.get(targetNumber).getThirdChoice() + ", " +
                                choices.get(targetNumber).getFourthChoice() + ") ");
                         */

                        generatedTranscript.set (line, generatedTranscript.get(line) + " " +
                                replaceOmittedWordWithUnderscore(words.get(i)));

                        targetWords.add(words.get(i));
                        targetWordsLineNumber.add(line);

                        //text = text + " " + omittedWords.get(i) + " (" + targetWords.get(targetNumber) +" " + targetWordsLineNumber.get(targetNumber) + ")";
                        targetNumber ++;
                    }
                    else{
                        //text = text + " " + words.get(i);
                        omittedWords.add(words.get(i));
                        generatedTranscript.set (line, generatedTranscript.get(line) + " " + words.get(i));
                        //text = text + " " + omittedWords.get(i);
                    }
                }
                else{
                    //text = text + " " + omittedWordsOffset.get(line) + "th word \n" +  words.get(i);
                    omittedWords.add(words.get(i));
                    generatedTranscript.add(words.get(i));
                    line ++;
                    numberOfWordsInALine = 1;
                    //text = text + "\n" + omittedWords.get(i);
                }
            }
            //checkGeneratedTranscript();
            //Log.d("Parsed Words of", text + " Line: " + line + " " + id);
        }


    }

    private void checkGeneratedTranscript (){
        for (int i = 0 ; i < generatedTranscript.size() ; i++){
            Log.d("Generated Transcript", generatedTranscript.get(i));
        }
    }
    private void setMissingWordsOffset(){
        for (int i = 0 ; i < numberOfWords.size() ; i += 2){
            if (numberOfWords.get(i) > 2){
                omittedWordsOffset.add((numberOfWords.get(i) + numberOfWords.get(i)%3 )/2);
                omittedWordsOffset.add(0);
            }
            else{
                omittedWordsOffset.add(0);
                omittedWordsOffset.add(0);
            }
        }
        omittedWordsOffset.add(0);
        omittedWordsOffset.add(0);
        omittedWordsOffset.add(0);
        omittedWordsOffset.add(0);
        omittedWordsOffset.add(0);
        omittedWordsOffset.add(0);
        omittedWordsOffset.add(0);
        omittedWordsOffset.add(0);
    }

    private void parseSingleWords(){
        int numberOfWordsInALine = 0;
        int start = 0, end = 0, line = 0;
        for (int i = 1 ; i < closedCaptionsPlaintext.length()-1; i++){
            boolean ready = false;

            if (closedCaptionsPlaintext.charAt(i) == '\n'){
                line ++;
                start = i;

                numberOfWords.add(numberOfWordsInALine);
                numberOfWordsInALine = 0;
            }
            else if (closedCaptionsPlaintext.charAt(i+1) == ' '
                    || closedCaptionsPlaintext.charAt(i+1) == '.'
                    || closedCaptionsPlaintext.charAt(i+1) == '<'
                    || closedCaptionsPlaintext.charAt(i+1) == ','
                    || closedCaptionsPlaintext.charAt(i+1) == '\n'){
                end = i+1;
                ready = true;
            }

            if (ready){
                if (end != start + 1){
                    words.add(closedCaptionsPlaintext.substring(start+1, end));
                    lineNumber.add(line);
                    numberOfWordsInALine ++;
                    //Log.d("Parsed Word of", closedCaptionsPlaintext.substring(start+1, end) + " Line: " + line + " " + id + " " + start + " " + end);
                }
                start = end;
            }
        }
    }

    private String replaceOmittedWordWithUnderscore(String target){
        String result;
        result = "_";
        for (int i = 1; i < target.length() ; i++){
            result += "_";
        }
        return result;
    }

    public List<ChoicesGenerator> getChoices() {
        return choices;
    }
}
