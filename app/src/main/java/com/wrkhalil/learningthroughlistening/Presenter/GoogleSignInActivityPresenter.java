package com.wrkhalil.learningthroughlistening.Presenter;

import com.wrkhalil.learningthroughlistening.Model.Model;
import com.wrkhalil.learningthroughlistening.View.GoogleSignInActivity;

public class GoogleSignInActivityPresenter {
    private GoogleSignInActivity view;
    private Model model;

    //Instantiate the presenter
    public GoogleSignInActivityPresenter(GoogleSignInActivity view) {
        this.view = view;
        this.model = new Model();
    }
}
