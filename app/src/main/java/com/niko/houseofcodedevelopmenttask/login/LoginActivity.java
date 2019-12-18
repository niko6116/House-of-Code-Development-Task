package com.niko.houseofcodedevelopmenttask.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.facebook.FacebookSdk;
import com.facebook.appevents.AppEventsLogger;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseUser;
import com.niko.houseofcodedevelopmenttask.MainActivity;
import com.niko.houseofcodedevelopmenttask.R;

public class LoginActivity extends AppCompatActivity {

    // Google sign in client
    private GoogleSignInClient googleSignInClient;

    // Access code for authentication
    private final static int RC_SIGN_IN = 10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Configure Google sign in.
        // Configure sign in to request the user's ID, basic profile and email address.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();
        // GoogleSignInClient is given the specifications from the GoogleSignInOptions object.
        this.googleSignInClient = GoogleSignIn.getClient(this, gso);

        // Configure login button
        findViewById(R.id.button_login_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();

        // Go to ChatActivity if logged in.
        if (!AuthenticationUtility.getInstance().isLoggedOut()) {
            openChatActivity();
        }
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
     *
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
                // Authenticate.
                GoogleSignInAccount account = task.getResult(ApiException.class);
                if (AuthenticationUtility.getInstance().firebaseAuthWithGoogle(this, account)) {

                }
            } catch (ApiException ex) {
                Snackbar.make(findViewById(R.id.button_login_google),
                        "signInResult:failed code=" + ex.getStatusCode(), Snackbar.LENGTH_LONG).show();
            }
        }
    }

    /**
     * If signed in, go to chat room.
     *
     * @param user
     */
    public void updateUI(FirebaseUser user) {
        if (user != null) {
            openChatActivity();
        } else
            Snackbar.make(findViewById(R.id.button_login_google),
                    "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
    }

    /**
     * Opens ChatActivity.
     */
    private void openChatActivity() {
        AuthenticationUtility.getInstance().loggedIn();
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
