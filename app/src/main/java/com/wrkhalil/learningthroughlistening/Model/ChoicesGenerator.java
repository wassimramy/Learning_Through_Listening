package com.wrkhalil.learningthroughlistening.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.*;

public class ChoicesGenerator {

    private List<String> words;
    private List<String> choices = new ArrayList<>();
    private String targetWord;
    private String firstChoice;
    private String secondChoice;
    private String thirdChoice;
    private String fourthChoice;

    ChoicesGenerator (String targetWord, List<String> words){
        this.targetWord = targetWord;
        this.words = words;
        generateFourChoices ();
    }

    public String getTargetWord() {
        return targetWord;
    }

    public String getFirstChoice() {
        //return firstChoice;
        return choices.get(0);
    }

    public String getSecondChoice() {
        //return secondChoice;
        return choices.get(1);
    }

    public String getThirdChoice() {
        //return thirdChoice;
        return choices.get(2);
    }

    public String getFourthChoice() {
        //return fourthChoice;
        return choices.get(3);
    }

    private void generateFourChoices (){

        List<String> shuffledWords = new ArrayList<>();
       for (int i = 0 ; i < words.size() ; i++){
           shuffledWords.add(words.get(i));
       }
        //Collections.EMPTY_LIST = new Collections();
        Collections.shuffle(shuffledWords);

        choices.clear();
        choices = new ArrayList<>();
        //firstChoice = targetWord;
        choices.add(targetWord);
        choices.add("null");
        choices.add("null");
        choices.add("null");

        for (int i = 0 ; i < shuffledWords.size() ; i++){

            if (shuffledWords.get(i).length() == choices.get(0).length() &&
                !shuffledWords.get(i).equals(choices.get(0))){
                choices.set(1, shuffledWords.get(i));
            }
        }

        for (int i = 0 ; i < shuffledWords.size() ; i++){

            if (shuffledWords.get(i).length() == choices.get(0).length() &&
                    !shuffledWords.get(i).equals(choices.get(0)) &&
                    !shuffledWords.get(i).equals(choices.get(1))){
                choices.set(2, shuffledWords.get(i));
            }
        }

        for (int i = 0 ; i < words.size() ; i++){

            if (shuffledWords.get(i).length() == choices.get(0).length() &&
                    !shuffledWords.get(i).equals(choices.get(0)) &&
                    !shuffledWords.get(i).equals(choices.get(1)) &&
                    !shuffledWords.get(i).equals(choices.get(2))){
                choices.set(3, shuffledWords.get(i));
            }
        }

        Collections.shuffle(choices);
        checkForNullChoices(shuffledWords);
    }

    private void checkForNullChoices(List<String> shuffledWords){

        for (int i = 0 ; i < choices.size() ; i ++){
            if (choices.get(i).equals("null")){
                choices.set(i, returnRandomWord(shuffledWords));
            }
        }
    }

    private String returnRandomWord(List<String> shuffledWords){
        Collections.shuffle(shuffledWords);
        return shuffledWords.get(0);
    }

}
