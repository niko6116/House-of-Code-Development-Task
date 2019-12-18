package com.niko.houseofcodedevelopmenttask.login;

import android.app.Activity;
import android.content.Context;

import androidx.annotation.NonNull;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.niko.houseofcodedevelopmenttask.MainActivity;

/**
 * Singleton class intended to help with authentication.
 */
public class AuthenticationUtility {

    // The only instance of this class.
    private static AuthenticationUtility instance;

    // Authenticator
    private FirebaseAuth auth;

    // Automatic sign in should only happen once.
    private boolean loggedOut;

    private AuthenticationUtility() {
        this.loggedOut = false;

        // Initialize FirebaseAuth
        this.auth = FirebaseAuth.getInstance();
    }

    public static AuthenticationUtility getInstance() {
        if (instance == null) {
            instance = new AuthenticationUtility();
        }

        return instance;
    }

    public boolean isLoggedOut() {
        return loggedOut;
    }

    public void loggedOut() {
        loggedOut = true;
    }

    public void loggedIn() {
        loggedOut = false;
    }

    /**
     * Authenticate with Firebase.
     *
     * @param account
     */
    public boolean firebaseAuthWithGoogle(final Context context, GoogleSignInAccount account) {
        if (account != null) {
            AuthCredential credential = GoogleAuthProvider.getCredential(account.getIdToken(), null);
            auth.signInWithCredential(credential).
                    addOnCompleteListener((Activity) context, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                // Sign in was a success.
                                FirebaseUser user = AuthenticationUtility.getInstance().getAuth().getCurrentUser();
                                if (context instanceof MainActivity) {
                                    ((MainActivity) context).updateUI(user);
                                }
                                if (context instanceof LoginActivity) {
                                    ((LoginActivity) context).updateUI(user);
                                }
                            } else {
                                // Sign in failed.
                                if (context instanceof MainActivity) {
                                    ((MainActivity) context).updateUI(null);
                                }
                                if (context instanceof LoginActivity) {
                                    ((LoginActivity) context).updateUI(null);
                                }
                            }
                        }
                    });
        }
        return false;
    }


    public FirebaseAuth getAuth() {
        return this.auth;
    }
}
