package com.niko.houseofcodedevelopmenttask.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.google.firebase.auth.FirebaseAuth;
import com.niko.houseofcodedevelopmenttask.MainActivity;
import com.niko.houseofcodedevelopmenttask.R;
import com.niko.houseofcodedevelopmenttask.chat.chatRoom.ChatRoomActivity;

public class ChatActivity extends AppCompatActivity {

    // Authenticator
    private FirebaseAuth auth;

    // Listener
    private FirebaseAuth.AuthStateListener authListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize FirebaseAuth.
        auth = FirebaseAuth.getInstance();
    }

    @Override
    protected void onStart() {
        super.onStart();

        /*
        // Leave chat view if user is not logged in.
        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // If user is not logged in
                if (firebaseAuth.getCurrentUser() == null) {
                    openMainActivity();
                }
            }
        };

        // Add listener to auth
        auth.addAuthStateListener(authListener);
        */

        // Configure logout button
        findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        // TEMPORARY
        openChatRoomActivity();
    }

    /**
     * Signs the user out
     */
    private void signOut() {
        // Firebase sign out
        auth.signOut();

        /*
        // Google sign out
        GoogleApiClient googleApiClient;
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(@NonNull Status status) {
                        openMainActivity();
                    }
                });
        */
    }

    /**
     * Opens MainActivity.
     */
    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

    /**
     * Opens ChatRoomActivity.
     */
    private void openChatRoomActivity() {
        Intent intent = new Intent(this, ChatRoomActivity.class);
        startActivity(intent);
    }

}
