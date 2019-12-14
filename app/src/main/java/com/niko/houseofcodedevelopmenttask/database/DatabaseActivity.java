package com.niko.houseofcodedevelopmenttask.database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.firebase.client.Firebase;
import com.niko.houseofcodedevelopmenttask.R;

public class DatabaseActivity extends AppCompatActivity {

    private Firebase root;

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
                String value1 = ((EditText) findViewById(R.id.edit_text_value1)).getText().toString();
                String value2 = ((EditText) findViewById(R.id.edit_text_value2)).getText().toString();
                testSendToDatabase(value1, value2);
            }
        });
    }

    private void testSendToDatabase(String val1, String val2) {
        //Firebase.setAndroidContext(this);
        //root.push().setValue(value);
        //Firebase firebaseKey = root.child(key);
        //firebaseKey.setValue(value);

        Firebase element = root.push();
        Firebase key1 = element.child("Key1");
        key1.setValue(val1);
        Firebase key2 = element.child("Key2");
        key2.setValue(val2);
    }

}
