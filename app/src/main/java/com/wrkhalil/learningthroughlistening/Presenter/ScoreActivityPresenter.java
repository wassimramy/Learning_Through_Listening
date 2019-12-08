package com.wrkhalil.learningthroughlistening.Presenter;

import com.wrkhalil.learningthroughlistening.Model.Model;
import com.wrkhalil.learningthroughlistening.View.ScoreActivity;

public class ScoreActivityPresenter {

    private ScoreActivity view;
    private Model model;

    //Instantiate the presenter
    public ScoreActivityPresenter(ScoreActivity view) {
        this.view = view;
        this.model = new Model();
    }
}
