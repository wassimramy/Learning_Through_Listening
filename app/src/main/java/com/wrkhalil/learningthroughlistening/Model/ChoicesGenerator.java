package com.wrkhalil.learningthroughlistening.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class ChoicesGenerator {

    //Attributes
    private List<String> words;
    private List<String> choices = new ArrayList<>();
    private String targetWord;

    //ChoicesGenerator Constructor
    ChoicesGenerator (String targetWord, List<String> words){
        this.targetWord = targetWord; //Fetch the targetWord
        this.words = words; //Fetch the parsed words array list
        generateFourChoices (); //Executed to generate the other choices and shuffle them after
    }

    //Executed to generate the other choices and shuffle them after
    private void generateFourChoices (){

        List<String> shuffledWords = new ArrayList<>(); //Temporary list for the shuffled parsed words

       for (int i = 0 ; i < words.size() ; i++){
           shuffledWords.add(words.get(i)); //Add all teh words to the list
       }
        Collections.shuffle(shuffledWords); //Shuffle all the words in shuffledWords list

        choices.clear(); //Clear the choices list
        choices = new ArrayList<>();
        choices.add(targetWord); //Add the target word to the first place
        choices.add("null"); //Add Null to the second place
        choices.add("null"); //Add Null to the third place
        choices.add("null"); //Add Null to the fourth place

        //Place a word of the same size (as the target word) to the second choice
        for (int i = 0 ; i < shuffledWords.size() ; i++){
            if (shuffledWords.get(i).length() == choices.get(0).length() &&
                !shuffledWords.get(i).equals(choices.get(0))){
                choices.set(1, shuffledWords.get(i));
            }
        }

        //Place a word of the same size (as the target word) to the third choice
        for (int i = 0 ; i < shuffledWords.size() ; i++){

            if (shuffledWords.get(i).length() == choices.get(0).length() &&
                    !shuffledWords.get(i).equals(choices.get(0)) &&
                    !shuffledWords.get(i).equals(choices.get(1))){
                choices.set(2, shuffledWords.get(i));
            }
        }

        //Place a word of the same size (as the target word) to the fourth choice
        for (int i = 0 ; i < words.size() ; i++){

            if (shuffledWords.get(i).length() == choices.get(0).length() &&
                    !shuffledWords.get(i).equals(choices.get(0)) &&
                    !shuffledWords.get(i).equals(choices.get(1)) &&
                    !shuffledWords.get(i).equals(choices.get(2))){
                choices.set(3, shuffledWords.get(i));
            }
        }

        //Shuffle all choices
        Collections.shuffle(choices);
        //Check if there is any choice set to null
        checkForNullChoices(shuffledWords);
    }

    private void checkForNullChoices(List<String> shuffledWords){

        for (int i = 0 ; i < choices.size() ; i ++){
            if (choices.get(i).equals("null")){ //Enters if the choice is equal to null
                choices.set(i, returnRandomWord(shuffledWords)); //Place it with a random word from the shuffledWords list
            }
        }
    }

    //Executed to return a random word from the shuffledWords list
    private String returnRandomWord(List<String> shuffledWords){
        Collections.shuffle(shuffledWords); //Shuffle the shuffledWords list
        return shuffledWords.get(0); //Return the first place
    }

    //Getters
    public String getTargetWord() {
        return targetWord;
    }

    public String getFirstChoice() {
        return choices.get(0);
    }

    public String getSecondChoice() {
        return choices.get(1);
    }

    public String getThirdChoice() {
        return choices.get(2);
    }

    public String getFourthChoice() {
        return choices.get(3);
    }

}
