package com.example.su2021cs5520hw7;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.su2021cs5520hw7.services.UserService;

import com.example.su2021cs5520hw7.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Iterator;

public class MainActivity extends AppCompatActivity {
    private EditText usernameInput;
    private User currentUser;
    private FirebaseDatabase db;
    private DatabaseReference mRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        usernameInput = findViewById(R.id.usernameInput);
        db = FirebaseDatabase.getInstance();
    }

    // click on log in button
    public void onclickLoginButton(View view){
        String enteredUsername = usernameInput.getText().toString().trim().toLowerCase();
        currentUser = new User(enteredUsername);
        checkUserExisting(enteredUsername);
        if (validateUserName(enteredUsername)){
            openChatActivity(view);
        }
    }

    // verify if input username is not empty
    // verify username is found or not?
    private boolean validateUserName(String username){
        if (username.isEmpty()){
            usernameInput.setError("Field can't be empty");
            return false;
        } else{
            usernameInput.setError(null);
            return true;
        }
    }

    public void checkUserExisting(String username) {
        mRef = db.getReference().child("users");
        mRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot snapshot) {
                Iterator<DataSnapshot> dataSnapshots = snapshot.getChildren().iterator();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    String Uname = dataSnapshotChild.child("username").getValue().toString();
                    if (Uname.equals(username)) {
                        //usernameInput.setError("Username already exists");
                        return;
                    }
                }
                UserService.createNewUser(db, username);
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
    }

    // open chat page
    public void openChatActivity(View view){
        Intent intent = new Intent(this, chatActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }
}