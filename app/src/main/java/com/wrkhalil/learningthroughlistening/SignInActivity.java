package com.wrkhalil.learningthroughlistening;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.annotation.VisibleForTesting;
import androidx.appcompat.app.AppCompatActivity;


public class SignInActivity extends AppCompatActivity {


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Button listeners
        //findViewById(R.id.signInWithGoogleButton).setOnClickListener(signInWithGoogle);
        //findViewById(R.id.signInWithLocalButton).setOnClickListener(this);
        //findViewById(R.id.disconnectButton).setOnClickListener(this);
    }

    public void signInWithGoogle(View view) {

        Intent intent = new Intent(this, GoogleSignInActivity.class);
        startActivity(intent);
        this.finish();
    }

    public void signInWithLocal(View view) {
        Intent intent = new Intent(this, FacebookLoginActivity.class);
        startActivity(intent);
    }

}
