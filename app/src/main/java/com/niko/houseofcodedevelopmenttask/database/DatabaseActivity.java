package com.niko.houseofcodedevelopmenttask.database;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;

import com.firebase.client.Firebase;
import com.niko.houseofcodedevelopmenttask.MainActivity;
import com.niko.houseofcodedevelopmenttask.R;

public class DatabaseActivity extends AppCompatActivity {

    private Firebase firebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_database);
    }

    private void testDB() {
        // DB Test
        //Firebase.setAndroidContext(this);
        //firebase = new Firebase("https://hoc-task.firebaseio.com/");
        findViewById(R.id.button_database_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Firebase firebaseChild = firebase.child("Test");
                //firebaseChild.setValue("test value");
                //MainActivity.passToDatabase("https://hoc-task.firebaseio.com/", "Test", "test value");
            }
        });
    }

}
