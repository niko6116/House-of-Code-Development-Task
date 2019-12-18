package com.niko.houseofcodedevelopmenttask;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.firebase.auth.FirebaseUser;
import com.niko.houseofcodedevelopmenttask.chat.ChatActivity;
import com.niko.houseofcodedevelopmenttask.login.AuthenticationUtility;
import com.niko.houseofcodedevelopmenttask.login.LoginActivity;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Create thread for splash screen.
        Thread splash = new Thread() {
            public void run() {
                try {
                    // Thread sleeps.
                    sleep(2000);

                    //Remove activity
                    finish();
                } catch (Exception e) {
                }
            }
        };

        // Start thread.
        splash.start();

        // Check for existing signed in Google account.
        // If a user is already signed in, the GoogleSignInAccount will be non-null.
        // This will only be done the first time this page is accessed during a session.
        if (!AuthenticationUtility.getInstance().isLoggedOut()) {
            GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
            AuthenticationUtility.getInstance().firebaseAuthWithGoogle(this, account);
        } else openLoginActivity();
    }

    /**
     * If signed in, go to chat room.
     *
     * @param user
     */
    public void updateUI(FirebaseUser user) {
        if (user != null) {
            openChatActivity();
        } else {
            openLoginActivity();
        }
    }

    /**
     * Opens LoginActivity.
     */
    private void openLoginActivity() {
        AuthenticationUtility.getInstance().loggedOut();
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }

    /**
     * Opens ChatActivity.
     */
    private void openChatActivity() {
        AuthenticationUtility.getInstance().loggedIn();
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

}
