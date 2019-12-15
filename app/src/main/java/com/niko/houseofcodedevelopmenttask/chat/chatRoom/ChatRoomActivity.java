package com.niko.houseofcodedevelopmenttask.chat.chatRoom;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.niko.houseofcodedevelopmenttask.R;

public class ChatRoomActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        // Initialize button for sending messages.
        findViewById(R.id.fab_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText input = (EditText) findViewById(R.id.text_input_edit_text_chat);

                // Read the input field and push a new instance of ChatMessage to the database.
                DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("chat-rooms/room1/messages");
                ref.push().setValue(new ChatMessage(input.getText().toString(),
                                FirebaseAuth.getInstance().getCurrentUser().getDisplayName())
                        );

                // Input is cleared.
                input.setText("");
            }
        });
    }

}
