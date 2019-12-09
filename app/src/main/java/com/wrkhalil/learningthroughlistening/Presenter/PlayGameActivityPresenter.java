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

    private PlayGameActivity view;
    private Model model;
    private boolean choiceButtonStatus = false;
    private String firstChoice, secondChoice, thirdChoice, fourthChoice, wrongChoice;
    public int position = 0, score = 0, choicesIndex = 0;


    public void refreshPlayerDisplay(TimedText text){
        view.txtDisplay.setTextColor(Color.parseColor(	"#878383"));
        if (text.getText().contains("_")){
            wrongChoice = "null";
            setChoicesButtons(true);
            if (choicesIndex < Model.videoList.get(position).getChoices().size()-1){
                choicesIndex ++;
            }
        }
        else {
            setChoicesButtons(false);
        }
        view.txtDisplay.setText(text.getText());
    }
    public void showScoreActivity(){
        Intent intent = new Intent(view, ScoreActivity.class);
        intent.putExtra("Position", position); //Sends the URI value to the ShowPictureActivity to fetch the picture
        intent.putExtra("Score", score); //Sends the URI value to the ShowPictureActivity to fetch the picture
        view.startActivity(intent); //Start the activity
        view.finish();
    }

    //Instantiate the presenter
    public PlayGameActivityPresenter(PlayGameActivity view) {
        this.view = view;
        this.model = new Model();
    }

    // Restart The Game
    public void restartGame(int position) {
        Intent intent = new Intent(view, PlayGameActivity.class);
        intent.putExtra("Position", position); //Sends the URI value to the ShowPictureActivity to fetch the picture
        view.startActivity(intent); //Start the activity
        view.finish();
    }

    // Back to Choose Game Activity
    public void goBackToChooseGameActivity() {
        Intent intent = new Intent(view, ChooseGameActivity.class);
        view.startActivity(intent); //Start the activity
        view.finish();
    }

    // Pause The Game
    public void pauseGame() {
        view.pauseButton.setText("Start");
        choiceButtonStatus = view.firstChoiceButton.isEnabled();
        fetchDataFromButtons(false);
        setChoiceButtonStatus(false);
        view.player.pause();
    }

    // Start The Game
    public void startGame() {
        view.pauseButton.setText("Pause");
        setChoiceButtonStatus(choiceButtonStatus);
        fetchDataFromButtons(choiceButtonStatus);
        view.player.start();
    }

    private void fetchDataFromButtons(boolean choicesEnable){
        if (!choicesEnable){
            firstChoice = view.firstChoiceButton.getText().toString();
            secondChoice = view.secondChoiceButton.getText().toString();
            thirdChoice = view.thirdChoiceButton.getText().toString();
            fourthChoice = view.fourthChoiceButton.getText().toString();
        }
        else{
            view.firstChoiceButton.setText(firstChoice);
            view.secondChoiceButton.setText(secondChoice);
            view.thirdChoiceButton.setText(thirdChoice);
            view.fourthChoiceButton.setText(fourthChoice);
        }
    }

    // Check if the choice is Correct
    public void receiveChoice(String choice) {
        String txtDisplayString = view.txtDisplay.getText() + "";
        String targetWord = Model.videoList.get(position).getChoices().get(choicesIndex-1).getTargetWord();

        if(!wrongChoice.equals("null") && !choice.equals(targetWord)){
            view.txtDisplay.setTextColor(Color.parseColor(	"#FF0000"));
            txtDisplayString = txtDisplayString.replace("X_" + wrongChoice + "_X", "X_" + choice + "_X");
            wrongChoice = choice;

        }
        else if (!wrongChoice.equals("null") && choice.equals(targetWord)){
            view.txtDisplay.setTextColor(Color.parseColor(	"#3CB371"));
            txtDisplayString = txtDisplayString.replace("X_" + wrongChoice + "_X", choice);
            setChoicesButtons(false);
            score += 100;
        }
        else if (!choice.equals(targetWord)){
            view.txtDisplay.setTextColor(Color.parseColor(	"#FF0000"));
            txtDisplayString = txtDisplayString.replace(generateUnderscores(targetWord), "X_" + choice + "_X");
            wrongChoice = choice;
        }
        else{
            view.txtDisplay.setTextColor(Color.parseColor(	"#3CB371"));
            txtDisplayString = txtDisplayString.replace(generateUnderscores(targetWord), choice);
            setChoicesButtons(false);
            score += 100;
        }

        view.txtScore.setText("Score: " + score);
        view.txtDisplay.setText(txtDisplayString);
        Log.d("txtDisplayString", txtDisplayString);
    }

    public String generateUnderscores(String targetWord){
        return model.generateUnderscores(targetWord);
    }

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

    private void setChoiceButtonStatus(boolean choicesEnable){

        view.firstChoiceButton.setEnabled(choicesEnable);
        view.secondChoiceButton.setEnabled(choicesEnable);
        view.thirdChoiceButton.setEnabled(choicesEnable);
        view.fourthChoiceButton.setEnabled(choicesEnable);

        if (!choicesEnable){
            view.firstChoiceButton.setText(model.generateUnderscores(view.firstChoiceButton.getText().toString()));
            view.secondChoiceButton.setText(model.generateUnderscores(view.secondChoiceButton.getText().toString()));
            view.thirdChoiceButton.setText(model.generateUnderscores(view.thirdChoiceButton.getText().toString()));
            view.fourthChoiceButton.setText(model.generateUnderscores(view.fourthChoiceButton.getText().toString()));
        }
    }

}
