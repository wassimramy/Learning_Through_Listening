package com.wrkhalil.learningthroughlistening.Model;

import com.wrkhalil.learningthroughlistening.Presenter.ChooseDifficultyActivityPresenter;
import com.wrkhalil.learningthroughlistening.View.ChooseDifficultyActivity;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Video {

    public String id;
    private ClosedCaption closedCaption;
    private YouTubeDataRetriever youTubeDataRetriever;
    private FirebaseDataRetriever firebaseDataRetriever;
    private AudioFileRetriever audioFileRetriever;
    public int plays;

    Video (){
    }

    Video (String id, int plays){
        this.id = id;
        this.plays = plays;
        youTubeDataRetriever = new YouTubeDataRetriever (id);
        firebaseDataRetriever = new FirebaseDataRetriever(id);
    }


    public void generateClosedCaption(){
        Model.fetchingTranscript = false;
        ChooseDifficultyActivityPresenter.settingButtonsStatus();
        closedCaption = new ClosedCaption(id);
    }

    public void downloadAudioFile(){
        Model.fetchingAudio = false;
        ChooseDifficultyActivityPresenter.settingButtonsStatus();
        audioFileRetriever = new AudioFileRetriever(id);
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("id", id);
        result.put("plays", plays);
        return result;
    }

    public String getClosedCaptionPath() {
        return closedCaption.getPath();
    }

    public String getTrackPath(){
        return audioFileRetriever.getTrackPath();
    }

    public String getTitle (){
        return youTubeDataRetriever.getTitle();
    }

    public String getThumbnailURL (){
        return youTubeDataRetriever.getThumbnailURL();
    }

    public int getPlays() {
        return firebaseDataRetriever.getPlays();
    }

    public List<ChoicesGenerator> getChoices() {
        return closedCaption.getChoices();
    }
}
