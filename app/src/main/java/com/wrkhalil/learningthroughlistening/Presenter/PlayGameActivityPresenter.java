package com.wrkhalil.learningthroughlistening.Presenter;

import android.content.Intent;
import android.graphics.Color;
import android.media.TimedText;
import android.util.Log;

import com.wrkhalil.learningthroughlistening.Model.Model;
import com.wrkhalil.learningthroughlistening.View.ChooseGameActivity;
import com.wrkhalil.learningthroughlistening.View.PlayGameActivity;
import com.wrkhalil.learningthroughlistening.View.ScoreActivity;

public class PlayGameActivityPresenter {

    //Attributes
    private PlayGameActivity view;
    private Model model;
    private boolean choiceButtonStatus = false;
    private String firstChoice, secondChoice, thirdChoice, fourthChoice, wrongChoice;
    public int position = 0, score = 0, choicesIndex = 0;


    //PlayGameActivityPresenter Constructor
    public PlayGameActivityPresenter(PlayGameActivity view) {
        this.view = view; //Fetch View
        this.model = new Model(); //Instantiate model
    }

    //Update the transcript text view
    public void refreshPlayerDisplay(TimedText text){
        view.txtDisplay.setTextColor(Color.parseColor(	"#878383")); //Set the transcript color to Grey
        if (text.getText().contains("_")){ //Enter if there is a missing word
            wrongChoice = "null"; //Set the wrongChoice to null since no choices have been made
            setChoicesButtons(true); //Enable the choices button for the user to select the answer
            if (choicesIndex < Model.videoList.get(position).getChoices().size()-1){
                choicesIndex ++; //Index of the target word
            }
        }
        else {
            setChoicesButtons(false); //Disable the choices if no words are missing
        }
        view.txtDisplay.setText(text.getText()); //Display the transcript
     }

     //Executed to start the ScoreActivity
    public void showScoreActivity(){
        Intent intent = new Intent(view, ScoreActivity.class);
        intent.putExtra("Position", position); //Sends the video's id value to the ScoreActivity
        intent.putExtra("Score", score); //Sends the earned score to the ScoreActivity
        view.startActivity(intent); //Start the activity
        view.finish(); //Destroy the current activity
    }



    // Restart The Game
    public void restartGame(int position) {
        Intent intent = new Intent(view, PlayGameActivity.class);
        intent.putExtra("Position", position); ////Sends the video's id value to the PlayGameActivity
        view.startActivity(intent); //Start the activity
        view.finish(); //Destroy the current view
    }

    // Back to Choose Game Activity
    public void goBackToChooseGameActivity() {
        Intent intent = new Intent(view, ChooseGameActivity.class);
        view.startActivity(intent); //Start the activity
        view.finish(); //Destroy the current view
    }

    // Pause The Game
    public void pauseGame() {
        view.pauseButton.setText("Start");  //Change the button text to start
        choiceButtonStatus = view.firstChoiceButton.isEnabled(); //Check the state of the choice buttons
        fetchDataFromButtons(false); //Executed to store the text of the choice button and replace it with the obfuscated version
        setChoiceButtonStatus(false); //Disable the choice buttons
        view.player.pause(); //Pause player
    }

    // Start The Game
    public void startGame() {
        view.pauseButton.setText("Pause"); //Change the button text to pause
        setChoiceButtonStatus(choiceButtonStatus); //Depending on the state of the choice buttons before pressing pause, the state of the buttons will be stated
        fetchDataFromButtons(choiceButtonStatus); //Depending on the state of the choice buttons before pressing pause, the text displayed on the buttons will be stated
        view.player.start(); //Start player
    }

    //Executed to either save the data displayed on the  choice buttons or display the choices on the buttons
    private void fetchDataFromButtons(boolean choicesEnable){
        if (!choicesEnable){ //Enter if the ChoiceEnable is false to retrieve the data from the buttons and save it
            firstChoice = view.firstChoiceButton.getText().toString();
            secondChoice = view.secondChoiceButton.getText().toString();
            thirdChoice = view.thirdChoiceButton.getText().toString();
            fourthChoice = view.fourthChoiceButton.getText().toString();
        }
        else{ //Enter if the ChoiceEnable is true to displayed values on the buttons
            view.firstChoiceButton.setText(firstChoice);
            view.secondChoiceButton.setText(secondChoice);
            view.thirdChoiceButton.setText(thirdChoice);
            view.fourthChoiceButton.setText(fourthChoice);
        }
    }

    // Check if the choice is Correct
    public void receiveChoice(String choice) {
        String txtDisplayString = view.txtDisplay.getText() + ""; //Retrieve the transcript line
        String targetWord = Model.videoList.get(position).getChoices().get(choicesIndex-1).getTargetWord(); //Retrieve the target word

        if(!wrongChoice.equals("null") && !choice.equals(targetWord)){ //Enter if wrong choice has been made and not a first time choice
            view.txtDisplay.setTextColor(Color.parseColor(	"#FF0000")); // Change the transcript color to red
            txtDisplayString = txtDisplayString.replace("X_" + wrongChoice + "_X", "X_" + choice + "_X"); // Place the choice in the missing place
            wrongChoice = choice; //Save the wrong choice

        }
        else if (!wrongChoice.equals("null") && choice.equals(targetWord)){ //Enter if the correct has been made
            view.txtDisplay.setTextColor(Color.parseColor(	"#3CB371")); // Change the transcript color to Green
            txtDisplayString = txtDisplayString.replace("X_" + wrongChoice + "_X", choice); // Place the choice in the missing place
            setChoicesButtons(false); //Disable choices button
            score += 100; //Increment the score
        }
        else if (!choice.equals(targetWord)){ //Enter if wrong choice has been made at the first time choice
            view.txtDisplay.setTextColor(Color.parseColor(	"#FF0000")); // Change the transcript color to red
            txtDisplayString = txtDisplayString.replace(generateUnderscores(targetWord), "X_" + choice + "_X"); // Place the choice in the missing place
            wrongChoice = choice; //Save the wrong choice
        }
        else{ //Enter if the correct has been made from the first time
            view.txtDisplay.setTextColor(Color.parseColor(	"#3CB371")); // Change the transcript color to Green
            txtDisplayString = txtDisplayString.replace(generateUnderscores(targetWord), choice); // Place the choice in the missing place
            setChoicesButtons(false); //Disable choices button
            score += 100; //Increment the score
        }

        view.txtScore.setText("Score: " + score); //Update the score
        view.txtDisplay.setText(txtDisplayString); //Display the new transcript
        Log.d("txtDisplayString", txtDisplayString); //For testing purpose
    }

    //Obfuscate a word with underscores
    public String generateUnderscores(String targetWord){
        return model.generateUnderscores(targetWord);
    }

    //Populate the choices button
    public void setChoicesButtons(boolean choicesEnable){
        view.firstChoiceButton.setText(
                Model.videoList.get(position).getChoices().get(choicesIndex).getFirstChoice());

        view.secondChoiceButton.setText(
                Model.videoList.get(position).getChoices().get(choicesIndex).getSecondChoice());

        view.thirdChoiceButton.setText(
                Model.videoList.get(position).getChoices().get(choicesIndex).getThirdChoice());

        view.fourthChoiceButton.setText(
                Model.videoList.get(position).getChoices().get(choicesIndex).getFourthChoice());

        setChoiceButtonStatus(choicesEnable);
    }

    //Disable or enable the choices button
    private void setChoiceButtonStatus(boolean choicesEnable){

        view.firstChoiceButton.setEnabled(choicesEnable);
        view.secondChoiceButton.setEnabled(choicesEnable);
        view.thirdChoiceButton.setEnabled(choicesEnable);
        view.fourthChoiceButton.setEnabled(choicesEnable);

        if (!choicesEnable){ //Obfuscate the buttons text if choicesEnable is false
            view.firstChoiceButton.setText(model.generateUnderscores(view.firstChoiceButton.getText().toString()));
            view.secondChoiceButton.setText(model.generateUnderscores(view.secondChoiceButton.getText().toString()));
            view.thirdChoiceButton.setText(model.generateUnderscores(view.thirdChoiceButton.getText().toString()));
            view.fourthChoiceButton.setText(model.generateUnderscores(view.fourthChoiceButton.getText().toString()));
        }
    }

}
