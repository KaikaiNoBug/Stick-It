package com.example.su2021cs5520hw7.services;

import com.example.su2021cs5520hw7.model.User;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class UserService {
    /**
     * Creates & pushes a new user into Firebase.
     *
     * @return Returns the new Firebase entry ID.
     */
    public static User createNewUser(FirebaseDatabase db, String username) {
        DatabaseReference dbRef = db.getReference();
        DatabaseReference newUserRef = dbRef.child("users").push();
        User user = new User(username);
        newUserRef.setValue(user);

        System.out.println("[UserService] Created new user with username: " + username);
        return user;
    }
}