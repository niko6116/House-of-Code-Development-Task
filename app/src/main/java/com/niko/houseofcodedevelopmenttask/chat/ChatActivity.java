package com.niko.houseofcodedevelopmenttask.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.firebase.auth.FirebaseAuth;
import com.niko.houseofcodedevelopmenttask.MainActivity;
import com.niko.houseofcodedevelopmenttask.R;

public class ChatActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;
    private GoogleApiClient googleApiClient;

    @Override
    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(authListener);

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // If user is logged in
                if (firebaseAuth.getCurrentUser() == null) {
                    openMainActivity();
                }
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        auth = FirebaseAuth.getInstance();
    }

    /**
     * Signs the user out
     */
    private void signOut() {
        // Firebase sign out
        auth.signOut();

        // Google sign out
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        //put something you want to happen here eg.
                        startActivity(new Intent(ChatActivity.this, MainActivity.class));
                    }
                });
    }

    /**
     * Opens MainActivity.
     */
    private void openMainActivity() {
        Intent intent = new Intent( this, MainActivity.class);
        startActivity(intent);
    }

}
