package com.wrkhalil.learningthroughlistening.Presenter;

import android.app.ProgressDialog;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.wrkhalil.learningthroughlistening.Model.Model;
import com.wrkhalil.learningthroughlistening.Model.User;
import com.wrkhalil.learningthroughlistening.R;
import com.wrkhalil.learningthroughlistening.View.GoogleSignInActivity;
import com.wrkhalil.learningthroughlistening.View.SignInActivity;

public class GoogleSignInActivityPresenter {

    //Attributes
    private GoogleSignInActivity view;
    private Model model;
    // [START declare_auth]
    public FirebaseAuth mAuth;
    // [END declare_auth]
    private static final String TAG = "GoogleActivity";
    private static final int RC_SIGN_IN = 9001;
    private GoogleSignInClient mGoogleSignInClient;

    //GoogleSignInActivityPresenter Constructor
    public GoogleSignInActivityPresenter(GoogleSignInActivity view) {
        this.view = view; //Fetch View
        this.model = new Model(); //Instantiate model
    }

    //Executed when the user taps on the back button
    public void goBackToSignInActivity() {
        Intent intent = new Intent(view, SignInActivity.class);
        view.startActivity(intent);
        view.finish(); //Destroy the current view
    }

    //Executed to updateUI in either cases login or logout
    public void updateUI(FirebaseUser user) {
        view.hideProgressDialog();
        if (user != null) { //If the user is logged in
            view.mStatusTextView.setText("Google User: " + user.getDisplayName());
            view.mDetailTextView.setText("Firebase User: " + user.getEmail());
            view.findViewById(R.id.signInAndBack).setVisibility(View.GONE); //Set the visibility of the signInAndBack button
            view.findViewById(R.id.signOutAndBack).setVisibility(View.VISIBLE); //Set the visibility of the signOutAndBack button

            checkIfUserExists(user);
        } else { //If the user is logged out
            view.mStatusTextView.setText(R.string.signed_out);
            view.mDetailTextView.setText(null);
            view.findViewById(R.id.signInAndBack).setVisibility(View.VISIBLE); //Set the visibility of the signInAndBack button
            view.findViewById(R.id.signOutAndBack).setVisibility(View.GONE); //Set the visibility of the signOutAndBack button
        }
    }

    //Executed after user's log in to check whether it is the first time or not
    private void checkIfUserExists(final FirebaseUser user){

        Model.operatingUser = new User(user.getUid(), user.getDisplayName(), user.getEmail()); //Save the user's information in operatingUser
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("server/saving-data/fireblog/users");
        final DatabaseReference usersRef = ref.child(user.getUid());

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                if (snapshot.hasChild(user.getUid())) { //Enter if it is not the first time for the user to login
                    Model.operatingUser  = snapshot.child(user.getUid()).getValue(User.class); //Retrieve the user information
                    Log.d("Account Info Fetched:", Model.operatingUser.fullName + " " + Model.operatingUser.score); //Display the score in the log for testing purposes
                }
                else{ //Enter if it is the first time for the user to login
                    Model.operatingUser = new User(user.getUid(), user.getDisplayName(), user.getEmail()); //For new users, create a new User instance
                    usersRef.setValue(Model.operatingUser.toMap()); //Store the new user information on Firebase Database
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    // [START auth_with_google]
    public void firebaseAuthWithGoogle(GoogleSignInAccount acct) {
        Log.d(TAG, "firebaseAuthWithGoogle:" + acct.getId());
        // [START_EXCLUDE silent]
        view.showProgressDialog();
        // [END_EXCLUDE]

        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(view, task -> {
                    if (task.isSuccessful()) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithCredential:success");
                        FirebaseUser user = mAuth.getCurrentUser();
                        updateUI(user);
                    } else {
                        // If sign in fails, display a message to the developer.
                        Log.w(TAG, "signInWithCredential:failure", task.getException());
                        updateUI(null);
                    }

                    // [START_EXCLUDE]
                    view.hideProgressDialog();
                    // [END_EXCLUDE]
                });
    }
    // [END auth_with_google]

    // [START signin]
    public void signIn() {
        Intent signInIntent = mGoogleSignInClient.getSignInIntent();
        view.startActivityForResult(signInIntent, RC_SIGN_IN);
    }
    // [END signin]

    // Firebase sign out
    public void signOut() {
        mAuth.signOut();

        // Google sign out
        mGoogleSignInClient.signOut().addOnCompleteListener(view,
                task -> updateUI(null));
    }

    public void configSignIn(){
        // [START config_signin]
        // Configure Google Sign In
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(view.getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        // [END config_signin]

        mGoogleSignInClient = GoogleSignIn.getClient(view, gso);
    }

    public void initializeAuth(){
        // [START initialize_auth]
        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();
        // [END initialize_auth]
    }


}
