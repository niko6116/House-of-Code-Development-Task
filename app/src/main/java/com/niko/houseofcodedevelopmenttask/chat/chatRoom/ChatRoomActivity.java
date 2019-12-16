package com.niko.houseofcodedevelopmenttask.chat.chatRoom;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.niko.houseofcodedevelopmenttask.R;

import java.util.ArrayList;
import java.util.Date;

public class ChatRoomActivity extends AppCompatActivity {

    // The room's key in the database
    private String roomID;

    // Reference to the database
    private DatabaseReference db;

    // List of chat elements
    private ArrayList<ChatElement> chatElements;

    // Adaptor used to inject chat elements into the chat.
    private ChatAdapter chatAdaptor;

    // List view for chat elements.
    private ListView listView;

    Dialog d;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        // TEMPORARY
        this.roomID = "room1";

        // Set up variables.
        this.db = FirebaseDatabase.getInstance().getReference().child("chat-rooms/" + this.roomID + "/messages");
        this.chatElements = new ArrayList<>();
        this.listView = findViewById(R.id.list_view_chat);

        // Give adaptor to list view.
        this.chatAdaptor = new ChatAdapter(this, chatElements);
        this.listView.setAdapter(chatAdaptor);

        // Initialize button for sending messages.
        findViewById(R.id.fab_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //displayMessages();

                EditText input = findViewById(R.id.text_input_edit_text_chat);

                try {
                    // Read the input field and push a new instance of ChatMessage to the database.
                    db.push().setValue(new ChatMessage(input.getText().toString(),
                            FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                    );

                    // Input is cleared.
                    input.setText("");
                } catch (DatabaseException ex) {
                }
            }
        });
        if (d != null) {
            d.show();
        }
    }

    /**
     * Display messages.
     */
    private void displayMessages() {
        d = new Dialog(this);
        d.setContentView(R.layout.content_chat_room);

        String mUser = FirebaseAuth.getInstance().getCurrentUser().getDisplayName();
        Long mTime = new Date().getTime();
        String mText = ((EditText) d.findViewById(R.id.text_input_edit_text_chat)).getText().toString();

        // Set data
        ChatMessage m = new ChatMessage();
        m.setMessageUser(mUser);
        m.setMessageTime(mTime);
        m.setMessageText(mText);

        // Get references to the views of message.xml
        TextView messageUser = (TextView)findViewById(R.id.text_view_message_user);
        TextView messageTime = (TextView)findViewById(R.id.text_view_message_time);
        TextView messageText = (TextView)findViewById(R.id.text_view_message_text);

        // Set their text
        messageUser.setText(m.getMessageUser());
        messageTime.setText(m.displayTime());
        messageText.setText(m.getMessageText());

        ((EditText) findViewById(R.id.text_input_edit_text_chat)).setText("");

        chatAdaptor = new ChatAdapter(ChatRoomActivity.this, chatElements);
        listView.setAdapter(chatAdaptor);
    }

    /**
     * Retrieves messages.
     *
     * @return
     */
    public ArrayList<ChatElement> retrieveChatMessages() {
        this.db.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                fetchData(dataSnapshot);
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
        return this.chatElements;
    }

    /**
     * Fetches data from the database.
     *
     * @param dataSnapshot
     */
    private void fetchData(DataSnapshot dataSnapshot) {
        this.chatElements.clear();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            ChatMessage e = ds.getValue(ChatMessage.class);
            this.chatElements.add(e);
        }
    }

    /**
     * Not in use
     */
    private void displayChatMessages() {
        /*
        // get list
        ListView listView = (ListView)findViewById(R.id.list_view_chat);

        adapter = new FirebaseListAdapter<ChatMessage>(this, ChatMessage.class,
                R.layout.content_chat_room, FirebaseDatabase.getInstance().getReference()) {
            @Override
            protected void populateView(View v, ChatMessage model, int position) {
                // Get references to the views of message.xml
                TextView messageUser = (TextView)v.findViewById(R.id.text_view_message_user);
                TextView messageTime = (TextView)v.findViewById(R.id.text_view_message_time);
                TextView messageText = (TextView)v.findViewById(R.id.text_view_message_text);

                // Set their text
                messageText.setText(model.getMessageText());
                messageUser.setText(model.getMessageUser());

                // Format the date before showing it
                messageTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                        model.getMessageTime()));
            }
        };

        listView.setAdapter(adapter);
        */
    }

}
