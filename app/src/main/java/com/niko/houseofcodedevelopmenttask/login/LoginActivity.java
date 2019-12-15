package com.niko.houseofcodedevelopmenttask.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.niko.houseofcodedevelopmenttask.R;
import com.niko.houseofcodedevelopmenttask.chat.ChatActivity;

public class LoginActivity extends AppCompatActivity {

    // Authenticator
    private FirebaseAuth auth;

    // Google sign in client
    private GoogleSignInClient googleSignInClient;

    // Access code for authentication
    private final static int RC_SIGN_IN = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        // Configure Google sign in
        // Configure sign in to request the user's ID, basic profile and email address.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        // GoogleSignInClient is given the specifications from the GoogleSignInOptions object.
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Configure sign in button
        findViewById(R.id.button_login_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();

        /*
        // Check for existing signed in Google account
        // If a user is already signed in, the GoogleSignInAccount will be non-null.
        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        firebaseAuthWithGoogle(account);
        */
    }

    /**
     * Sign in.
     */
    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    /**
     * Called by startActivityForResult(signInIntent, RC_SIGN_IN).
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


        // Result returned from GoogleSignInClient.getSignInIntent(data);
        if (requestCode == RC_SIGN_IN) {
            openChatActivity();
            // The Task returned from this call is always completed.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            try {
                GoogleSignInAccount account = task.getResult(ApiException.class);
                firebaseAuthWithGoogle(account);
            } catch (ApiException ex) {
                Snackbar.make(findViewById(R.id.button_login_google),
                        "signInResult:failed code=" + ex.getStatusCode(), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    /**
     * Authenticates with Firebase
     *
     * @param account
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        if (account != null) {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            auth.signInWithCredential(credential).
                    addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in was a success
                                FirebaseUser user = auth.getCurrentUser();
                                // Update UI
                                updateUI(user);
                            } else {
                                // Sign in failed.
                                Snackbar.make(findViewById(R.id.button_login_google), "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                                updateUI(null);
                            }
                        }
                    });
        }
    }

    /**
     * If signed in, go to chat room.
     *
     * @param user
     */
    private void updateUI(FirebaseUser user) {
        if (user != null) {
            openChatActivity();
        }
    }

    /**
     * Opens ChatActivity.
     */
    private void openChatActivity() {
        Intent intent = new Intent(this, ChatActivity.class);
        startActivity(intent);
    }

}
