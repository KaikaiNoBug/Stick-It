package com.example.su2021cs5520hw7.services;

import com.example.su2021cs5520hw7.model.User;
import com.example.su2021cs5520hw7.model.Sticker;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


public class StickerService {
    /**
     * Creates & pushes a new sticker into Firebase.
     */
    public static Sticker createNewSticker(FirebaseDatabase db, String senderName, String receiverName, int emojiID) {
        DatabaseReference dbRef = db.getReference();
        DatabaseReference newStkRef = dbRef.child("Stickers").push();

        Sticker newStk = new Sticker(senderName, receiverName, emojiID);
        newStkRef.setValue(newStk);

        System.out.println("[StickerService] Created new sticker from "
                + newStk.senderName + " to "
                + newStk.receiverName + " with emojiID:"
                + newStk.emojiID);

        return newStk;
    }
}