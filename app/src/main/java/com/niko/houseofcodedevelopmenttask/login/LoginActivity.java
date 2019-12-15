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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.niko.houseofcodedevelopmenttask.MainActivity;
import com.niko.houseofcodedevelopmenttask.R;
import com.niko.houseofcodedevelopmenttask.chat.ChatActivity;

public class LoginActivity extends AppCompatActivity {

    // Authenticator
    private FirebaseAuth auth;

    // Listener checking authentication state
    private FirebaseAuth.AuthStateListener authListener;

    // Google sign in client
    private GoogleSignInClient googleSignInClient;

    // Google api client
    private GoogleApiClient googleApiClient;

    // Access code for authentication
    private final static int RC_SIGN_IN = 10;

    @Override
    protected void onStart() {
        super.onStart();

        auth.addAuthStateListener(authListener);

        // Check for existing signed in Google account.
        // If the user is already signed in, the GoogleSignInAccount will be non-null.
        //GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
        //updateUI(account);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize FirebaseAuth
        auth = FirebaseAuth.getInstance();

        findViewById(R.id.button_login_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signIn();
            }
        });

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // If user is logged in
                if (firebaseAuth.getCurrentUser() != null) {
                    openChatActivity();
                }
            }
        };

        // Configure sign in to request the user's ID, basic profile and email address.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id)).requestEmail().build();

        // Build Google api client.
        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, new GoogleApiClient.OnConnectionFailedListener() {
                    /* Manages what happens during failure. */
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(LoginActivity.this,
                                "Something went wrong", Toast.LENGTH_SHORT).show();
                    }
                })
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso).build();

        /*
        // GoogleSignInClient with the specifications from the GoogleSignInOptions object.
        googleSignInClient = GoogleSignIn.getClient(this, gso);

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(googleSignInClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

        findViewById(R.id.button_login_google).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                switch (v.getId()) {
                    case R.id.button_login_google:
                        signIn();
                        break;
                    // ...
            }
        });
        }
        */
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent intent) {
        super.onActivityResult(requestCode, resultCode, intent);

        // Result returned from GoogleSignInClient.getSignInIntent(intent);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(intent);
            //Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(intent); // Completed Task returned
            //handleSignInResult(task);
            if (result.isSuccess()) {
                // Google sign in was successful. Authenticate with Firebase.
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);
            } else {
                // Google sign in failed
                Toast.makeText(LoginActivity.this,
                        "Authentication went wrong", Toast.LENGTH_SHORT).show();
            }
        }
    }

    /**
     * Authenticate with Firebase
     *
     * @param account
     */
    private void firebaseAuthWithGoogle(GoogleSignInAccount account) {
        AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = auth.getCurrentUser();
                            updateUI(user);
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w("TAG", "signInWithCredential:failure", task.getException());
                            //Toast.makeText(LoginActivity.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                            Snackbar.make(new View(LoginActivity.this),
                                    "Authentication Failed.", Snackbar.LENGTH_SHORT).show();
                            updateUI(null);
                        }

                        // ...
                    }
                });
    }

    /**
     * Sign in.
     */
    private void signIn() {
        Intent signInIntent = googleSignInClient.getSignInIntent();
        startActivityForResult(signInIntent, RC_SIGN_IN);
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
     * If signed in, go to chat room.
     *
     * @param account
     */
    private void updateUI2(GoogleSignInAccount account) {
        if (account != null) {
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
