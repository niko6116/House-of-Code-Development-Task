package com.niko.houseofcodedevelopmenttask.database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.niko.houseofcodedevelopmenttask.R;

public class DatabaseActivity extends AppCompatActivity {

    private Firebase root;
    private EditText key;
    private EditText value;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);

        Firebase.setAndroidContext(this);

        // Test DB
        root = new Firebase("https://hoc-task.firebaseio.com/Test");
        findViewById(R.id.button_database_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                key = findViewById(R.id.edit_text_key);
                value = findViewById(R.id.edit_text_value);
                sendToDatabase(key.getText().toString(), value.getText().toString());
            }
        });
    }

    private void sendToDatabase(String child, String value) {
        Firebase firebaseChild = root.child(child);
        firebaseChild.setValue(value);
    }

}
