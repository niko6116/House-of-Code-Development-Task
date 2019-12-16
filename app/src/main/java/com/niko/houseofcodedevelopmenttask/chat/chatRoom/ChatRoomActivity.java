package com.niko.houseofcodedevelopmenttask.chat.chatRoom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.niko.houseofcodedevelopmenttask.R;

import java.util.Map;

public class ChatRoomActivity extends AppCompatActivity {

    // The room's key in the database
    private String roomID;

    // References to the database
    private DatabaseReference db_send, db_receive;

    // List view for chat elements
    private ScrollView chatView;

    // Layout for chat elements
    private LinearLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        // Set room id.
        this.roomID = "room1"; // TEMPORARY

        // Set references for where messages should be sent to and received from.
        this.db_send = FirebaseDatabase.getInstance().getReference().child("chat-rooms/" + this.roomID + "/messages");
        this.db_receive = FirebaseDatabase.getInstance().getReference().child("chat-rooms/" + this.roomID + "/messages");

        // Set chat view and layout.
        this.chatView = findViewById(R.id.scroll_view_chat);
        this.layout = findViewById(R.id.layout_l);

        // Initialize button for sending messages.
        initializeSendMessageFunctionality();

        // Initialize listener for receiving messages.
        initializeReceiveMessageFunctionality();
    }

    /**
     * Initializes the send message button.
     */
    private void initializeSendMessageFunctionality() {
        findViewById(R.id.fab_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = findViewById(R.id.text_input_edit_text_chat);
                // Read the input field and push a new instance of ChatMessage to the database.
                ChatRoomActivity.this.db_send.push().setValue(new ChatMessage(
                        FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                        input.getText().toString()));

                // Input is cleared.
                input.setText("");
            }
        });
    }

    /**
     * Initializes a listener for receiving messages.
     */
    private void initializeReceiveMessageFunctionality() {
        this.db_receive.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    // Save the message in a map.
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    ChatMessage message = new ChatMessage();

                    message.setMessageUser(map.get("messageUser").toString());
                    message.passMessageTimeString(map.get("messageTime").toString());
                    message.setMessageText(map.get("messageText").toString());

                    // Check if the message is from the logged in user and then display message.
                    if (message.getMessageUser().equals(FirebaseAuth.getInstance().getCurrentUser().getDisplayName())) {
                        displayMessage(message.messageToHtml(), true);
                    } else {
                        displayMessage(message.messageToHtml(), false);
                    }
                } catch (DatabaseException ex) {
                } catch (NullPointerException ex) {
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    /**
     * Displays the passed message.
     * Arranges it to the right if it was sent by the logged in user.
     * Displays it to the left otherwise.
     *
     * @param message is a message that should be displayed.
     * @param type    should be true if the message is from the logged in user.
     */
    private void displayMessage(String message, boolean type) {
        // Put message into text view.
        TextView textView = new TextView(ChatRoomActivity.this);
        textView.setText(Html.fromHtml(message)); // Use 'HTML.fromHtml(...)' for formatting.

        // Create layout for text view.
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.weight = 7.0f;

        // Determine if message should be displayed to the left or the right.
        if (type) {
            lp.gravity = Gravity.RIGHT;
            //textView.setBackgroundResource(R.drawable.bubble_in);
        } else {
            lp.gravity = Gravity.LEFT;
            //textView.setBackgroundResource(R.drawable.bubble_out);
        }

        // Set the text view's layout.
        textView.setLayoutParams(lp);
        // Add the text view to the chat layout.
        this.layout.addView(textView);
        this.chatView.fullScroll(View.FOCUS_DOWN);
    }

}
