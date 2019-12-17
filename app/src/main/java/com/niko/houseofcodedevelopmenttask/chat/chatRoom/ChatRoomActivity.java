package com.niko.houseofcodedevelopmenttask.chat.chatRoom;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Html;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseException;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.niko.houseofcodedevelopmenttask.R;
import com.niko.houseofcodedevelopmenttask.chat.ChatActivity;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;

public class ChatRoomActivity extends AppCompatActivity {

    // The room's key in the database
    private String roomID;

    // References to the database
    private DatabaseReference db_send, db_receive;

    // The Firebase storage
    private FirebaseStorage storage;
    private StorageReference storageRef;

    // The main layout for chat rooms
    private View mainView;

    // List view for chat elements
    private ScrollView chatView;

    // Layout for chat elements
    private LinearLayout chat_layout;

    // Key for get image requests
    private final int PICK_IMAGE_REQUEST = 7;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_room);

        // Set room id.
        this.roomID = getIntent().getExtras().getString("roomID");

        // Leave if there is no room id.
        if (roomID == null) {
            openChatActivity();
        }

        // Set references for where messages should be sent to and received from.
        this.db_send = FirebaseDatabase.getInstance().getReference().child("chat-rooms/" + this.roomID + "/messages");
        this.db_receive = FirebaseDatabase.getInstance().getReference().child("chat-rooms/" + this.roomID + "/messages");

        // Set the Firebase Storage.
        this.storage = FirebaseStorage.getInstance();
        this.storageRef = storage.getReference();

        // Set mainView, chatView and chatLayout.
        this.mainView = findViewById(R.id.layout_chat_room_main);
        this.chatView = findViewById(R.id.scroll_view_chat);
        this.chat_layout = findViewById(R.id.layout_l);

        // Initialize the buttons.
        initializeChatButtonFunctionality();

        // Initialize listener for receiving messages and images.
        initializeDisplayFunctionality();
    }

    /**
     * Initialize the send message button and the choose upload button.
     */
    private void initializeChatButtonFunctionality() {
        // Initialize button for sending messages.
        findViewById(R.id.fab_chat).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendMessage();
            }
        });

        // Initialize button for choosing and displaying images.
        findViewById(R.id.fab_upload_image).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Choose image for upload.
                chooseImage();
            }
        });
    }

    /**
     * Initialize a listener for receiving and displaying messages and images.
     */
    private void initializeDisplayFunctionality() {
        // Initialize listener for receiving messages.
        this.db_receive.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try {
                    // Save the message in a map.
                    Map<String, Object> map = (Map<String, Object>) dataSnapshot.getValue();

                    ChatMessage message = new ChatMessage();

                    message.setMessageUserID(map.get("messageUserID").toString());
                    message.setMessageUser(map.get("messageUser").toString());
                    message.passMessageTimeString(map.get("messageTime").toString());
                    message.setMessageText(map.get("messageText").toString());

                    // Check if the message is from the logged in user and then display message.
                    if (message.getMessageUserID().equals(
                            FirebaseAuth.getInstance().getCurrentUser().getUid())) {
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

        //  Initialize listener for receiving images.

        // Set reference to image.
        //StorageReference pathRef = this.storageRef.child("chat-images");

        // Download image.
        // Not implemented.
    }

    /**
     * Send the message from the input to the database.
     */
    private void sendMessage() {
        EditText input = findViewById(R.id.text_input_edit_text_chat);
        // Read the input field and push a new instance of ChatMessage to the database.
        ChatRoomActivity.this.db_send.push().setValue(new ChatMessage(
                FirebaseAuth.getInstance().getCurrentUser().getUid(),
                FirebaseAuth.getInstance().getCurrentUser().getDisplayName(),
                input.getText().toString()));

        // Input is cleared.
        input.setText("");
    }

    /**
     * Start an intent to choose an image.
     */
    private void chooseImage() {
        // Create intent and start activity for result.
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
    }

    /**
     * If intent was to pick and image and it succeeded, upload the image.
     *
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        try {
            // Check if intent was to pick an image and it succeeded.
            if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                    && data != null && data.getData() != null) {
                // Set file path.
                Uri filePath = data.getData();
                try {
                    // Upload the chosen image.
                    uploadImage(filePath);
                } catch (Exception e) {
                }
            }
        } catch (NullPointerException ex) {
        }
    }

    /**
     * Upload the chosen image to Firebase.
     *
     * @param filePath
     */
    private void uploadImage(Uri filePath) {
        if (filePath != null) {
            final ProgressDialog progressDialog = new ProgressDialog(this);
            // Create progress dialog.
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            // Set reference for upload.
            StorageReference ref = this.storageRef.child(
                    "chat-images/" + this.roomID + "/" + UUID.randomUUID().toString());
            // Upload image.
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            progressDialog.dismiss();
                            Snackbar.make(ChatRoomActivity.this.mainView,
                                    "Uploaded", Snackbar.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Snackbar.make(ChatRoomActivity.this.mainView,
                                    "Failed " + e.getMessage(), Snackbar.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0 * taskSnapshot.getBytesTransferred()
                                    / taskSnapshot.getTotalByteCount());
                            progressDialog.setMessage("Uploaded " + (int) progress + "%");
                        }
                    });
        }
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

        // Set layout parameters for text view.
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.weight = 7.0f;

        // Determine if message should be displayed to the left or the right.
        if (type) {
            lp.gravity = Gravity.END;
            //textView.setBackgroundResource(R.drawable.bubble_in);
        } else {
            lp.gravity = Gravity.START;
            //textView.setBackgroundResource(R.drawable.bubble_out);
        }

        // Set the text view's layout parameters.
        textView.setLayoutParams(lp);

        // Add the text view to the chat layout.
        this.chat_layout.addView(textView);

        this.chatView.fullScroll(View.FOCUS_DOWN);
    }

    private void displayImage(Uri filePath) {
        // Save the image in an image view.
        ImageView imageView = new ImageView(this);
        Bitmap bitmap = null;
        try {
            bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
            imageView.setImageBitmap(bitmap);

            // Add the image view to the chat layout.
            this.chat_layout.addView(imageView);

            this.chatView.fullScroll(View.FOCUS_DOWN);
        } catch (IOException e) {
            e.printStackTrace();
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
