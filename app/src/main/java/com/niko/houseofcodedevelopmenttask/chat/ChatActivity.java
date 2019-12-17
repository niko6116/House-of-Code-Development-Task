package com.niko.houseofcodedevelopmenttask.chat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.niko.houseofcodedevelopmenttask.MainActivity;
import com.niko.houseofcodedevelopmenttask.R;
import com.niko.houseofcodedevelopmenttask.chat.chatRoom.ChatRoomActivity;

import java.util.LinkedList;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    // Authenticator
    private FirebaseAuth auth;

    // Listener that checks if the user is logged in
    private FirebaseAuth.AuthStateListener authListener;

    // Reference to the database
    private DatabaseReference db_chat_rooms;

    // List view for chat rooms
    private ScrollView chatRoomView;

    // Layout for chat rooms
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // Initialize auth and the listener.
        this.auth = FirebaseAuth.getInstance();
        //initializeAuthListener();

        // Configure logout button.
        findViewById(R.id.button_logout).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signOut();
            }
        });

        // Set reference for where chat rooms are located.
        this.db_chat_rooms = FirebaseDatabase.getInstance().getReference().child("chat-rooms");

        // Set chat view and layout.
        this.chatRoomView = findViewById(R.id.scroll_view_chat_rooms);
        this.layout = findViewById(R.id.layout_crl);
    }

    @Override
    protected void onStart() {
        super.onStart();

        getChatRooms();
    }

    /**
     * Show the chat rooms.
     */
    private void getChatRooms() {
        this.db_chat_rooms.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                List<ChatRoom> roomList = new LinkedList<>();
                for (DataSnapshot child : snapshot.getChildren()) {
                    try {
                        String crKey = child.getKey();
                        String crName = (String) child.child("name").getValue();
                        String crDescription = (String) child.child("description").getValue();
                        DataSnapshot crMessages = child.child("messages");

                        roomList.add(new ChatRoom(crKey, crName, crDescription, crMessages));
                    } catch (DatabaseException ex) {
                    } catch (NullPointerException ex) {
                    }
                }

                displayChatRooms(roomList);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Display a list of chat rooms.
     * @param chatRooms
     */
    private void displayChatRooms(List<ChatRoom> chatRooms) {
        // Clear chat room layout.
        this.layout.removeAllViews();

        for (ChatRoom room : chatRooms) {
            // Create room view
            LinearLayout roomView = new LinearLayout(ChatActivity.this);

            // Put room name and description into a view.
            TextView textView = new TextView(ChatActivity.this);
            textView.setText(Html.fromHtml(room.roomToHtml())); // Use 'HTML.fromHtml(...)' for formatting.

            // Create layout for room view.
            LinearLayout.LayoutParams lpRw = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpRw.bottomMargin = 64;
            lpRw.topMargin = 24;
            lpRw.leftMargin = 24;
            lpRw.rightMargin = 24;

            // Create layout for the text view.
            LinearLayout.LayoutParams lpTw = new LinearLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            lpTw.rightMargin = 64;

            // Create button and set OnClickListener.
            FloatingActionButton fab = new FloatingActionButton(ChatActivity.this);
            fab.setOnClickListener(room.getOnClickListener(ChatActivity.this));

            // Set button layout parameters.
            fab.setLayoutParams(new LinearLayout.LayoutParams(200, 200));
            // Set button icon.
            fab.setImageResource(R.drawable.chevron_right);
            //Bitmap bm = BitmapFactory.decodeResource(getResources(), R.drawable.chevron_right);
            //fab.setImageBitmap(Bitmap.createScaledBitmap(bm, 8, 8, true));

            // Add text view and button to room view layout.
            roomView.addView(textView);
            roomView.addView(fab);

            // Set layout parameters for the room view and the text view.
            roomView.setLayoutParams(lpRw);
            textView.setLayoutParams(lpTw);

            // Add the room view to the chat layout.
            this.layout.addView(roomView);

            this.chatRoomView.fullScroll(View.FOCUS_DOWN);
        }
    }

    /**
     * Initializes the authentication listener.
     */
    private void initializeAuthListener() {
        // Leave chat view if user is not logged in.
        this.authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                // If user is not logged in
                if (firebaseAuth.getCurrentUser() == null) {
                    openMainActivity();
                }
            }
        };

        // Add listener to auth.
        this.auth.addAuthStateListener(this.authListener);
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

        // Leave chat view.
        openMainActivity();
    }

    /**
     * Opens ChatRoomActivity.
     */
    public void openChatRoomActivity(String roomID) {
        Intent intent = new Intent(this, ChatRoomActivity.class);
        intent.putExtra("roomID", roomID);
        startActivity(intent);
    }

    /**
     * Opens MainActivity.
     */
    private void openMainActivity() {
        Intent intent = new Intent(this, MainActivity.class);
        startActivity(intent);
    }

}
