package com.wrkhalil.learningthroughlistening.View;

import android.app.ProgressDialog;
import android.content.Context;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;

import com.wrkhalil.learningthroughlistening.R;

@SuppressWarnings("deprecation")
public class MainActivity extends AppCompatActivity {

    @VisibleForTesting
    public ProgressDialog mProgressDialog;

    //Executed when the user is logging in
    public void showProgressDialog() {

        //Sets the settings for the progress dialog
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            //noinspection deprecation
            mProgressDialog.setMessage("Loading");
            mProgressDialog.setIndeterminate(true);
        }
        mProgressDialog.show(); //Display the progress dialog
    }
    //Executed after the logging process is done
    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss(); //Destroy the progress dialog
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        hideProgressDialog();
    }
}