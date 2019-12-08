package com.wrkhalil.learningthroughlistening.Presenter;

import com.wrkhalil.learningthroughlistening.Model.Model;
import com.wrkhalil.learningthroughlistening.View.PlayGameActivity;

public class PlayGameActivityPresenter {

    private PlayGameActivity view;
    private Model model;

    //Instantiate the presenter
    public PlayGameActivityPresenter(PlayGameActivity view) {
        this.view = view;
        this.model = new Model();
    }
}
