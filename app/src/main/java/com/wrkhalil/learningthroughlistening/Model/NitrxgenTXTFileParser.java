package com.wrkhalil.learningthroughlistening.Model;

import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class NitrxgenTXTFileParser {

    //Attributes
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

    //NitrxgenTXTFileParser Constructor
    NitrxgenTXTFileParser(String id, String closedCaptionsPlaintext){
        this.id = id; //Fetch id
        this.closedCaptionsPlaintext = closedCaptionsPlaintext; //Fetch the .txt file
        parseSingleWords(); //Executed to parse each word and count how many words in each line
        setMissingWordsOffset(); //Decide which word will be omitted
        generateOmittedWordsList(); //Replace the omitted word in the array list with an obfuscated version
    }

    //Replace the omitted word in the array list with an obfuscated version
    private void generateOmittedWordsList(){
        int numberOfWordsInALine = 1;
        int line = 0;

        String text;
        if (words.size() != 0){
            omittedWords.add(words.get(0));
            generatedTranscript.add(words.get(0));
            for (int i = 1 ; i < lineNumber.size() ; i++){
                if (line == lineNumber.get(i)){
                    numberOfWordsInALine ++; //Keep track of the line number
                    if (numberOfWordsInALine == omittedWordsOffset.get(line)){
                        choices.add(new ChoicesGenerator(words.get(i), words)); //Generate 3 other choices
                        generatedTranscript.set (line, generatedTranscript.get(line) + " " +
                                replaceOmittedWordWithUnderscore(words.get(i))); //Add the obfuscated version of the target word to the list
                        targetWords.add(words.get(i)); //Add the replaced word to the targetWords list
                    }
                    else{
                        generatedTranscript.set (line, generatedTranscript.get(line) + " " + words.get(i)); //If the word is not a target, add the word to the list
                    }
                }
                else{
                    generatedTranscript.add(words.get(i)); //Add a new line to the list
                    line ++; //Add a new line to the index
                    numberOfWordsInALine = 1; //Words counter
                }
            }
        }
    }

    //Executed to set the offset of the target word
    private void setMissingWordsOffset(){
        for (int i = 0 ; i < numberOfWords.size() ; i += 2){
            if (numberOfWords.get(i) > 2){ //Remove from a line with amount of words more than 2
                omittedWordsOffset.add((numberOfWords.get(i) + numberOfWords.get(i)%3 )/2); //Remove a word from the middle
                omittedWordsOffset.add(0); //Add a dummy variable
            }
            else{
                omittedWordsOffset.add(0); //Add a dummy variable
                omittedWordsOffset.add(0); //Add a dummy variable
            }
        }
    }

    //Executed to parse each word from the lyrics solely
    private void parseSingleWords(){
        int numberOfWordsInALine = 0; //Counter for the number of words in a single line
        int start = 0 , end = 0, line = 0; //Counters to signify the start of a word, the end of a word, and line number
        for (int i = 1 ; i < closedCaptionsPlaintext.length()-1; i++){
            boolean ready = false; //A boolean variable to check whether the boundaries of a word are set or not to store this word in the array list

            if (closedCaptionsPlaintext.charAt(i) == '\n'){
                line ++; //Increment the line counter when a carriage return is detected
                start = i; //Set the start counter to the index in the case of a new line
                numberOfWords.add(numberOfWordsInALine); //Store the number of words in teh previous line
                numberOfWordsInALine = 0; //Reset the counter
            }
            else if (closedCaptionsPlaintext.charAt(i+1) == ' '
                    || closedCaptionsPlaintext.charAt(i+1) == '.'
                    || closedCaptionsPlaintext.charAt(i+1) == '<'
                    || closedCaptionsPlaintext.charAt(i+1) == ','
                    || closedCaptionsPlaintext.charAt(i+1) == '\n'){
                end = i+1; //Set the end boundary of a word
                ready = true; //Set Ready to true when the end boundary is set
            }

            if (ready){ //Enters when the word's boundaries are set
                if (end != start + 1){ //Check if the word is not just one character
                    words.add(closedCaptionsPlaintext.substring(start+1, end)); //Save the word to the array list
                    lineNumber.add(line); //Add the line number to an array list
                    numberOfWordsInALine ++; //Increment the number of parsed words by 1
                }
                start = end; //Set the Start to the End boundary
            }
        }
    }

    //Executed to replace each character of the target word with "_" character
    private String replaceOmittedWordWithUnderscore(String target){
        String result; //Temporary variable to store the obfuscated version of a word
        result = "_"; //Place the first "_"
        for (int i = 1; i < target.length() ; i++){
            result += "_"; //Add an "_" for each character
        }
        return result; //Return the obfuscated version of a word
    }

    //Getters
    public List<String> getGeneratedTranscript() {
        return generatedTranscript;
    }
    public List<ChoicesGenerator> getChoices() {
        return choices;
    }
}
