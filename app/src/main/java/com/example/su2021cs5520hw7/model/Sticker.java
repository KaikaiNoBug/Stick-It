package com.example.su2021cs5520hw7.model;

import com.google.firebase.database.IgnoreExtraProperties;

import java.io.Serializable;

@IgnoreExtraProperties
public class Sticker implements Serializable {
    public String senderName;
    public String receiverName;
    public int emojiID;

    public Sticker() {
        senderName = null;
        receiverName = null;
        emojiID = -1;
    }

    public Sticker(String senderName, String receiverName, int emojiID) {
        this.senderName = senderName;
        this.receiverName = receiverName;
        this.emojiID = emojiID;
    }

    public String getSenderName() {
        return senderName;
    }

    public String getReceiverName() {
        return receiverName;
    }

    public int getEmojiID() {
        return emojiID;
    }

    @Override
    public String toString() {
        return String.valueOf(emojiID);
    }
}