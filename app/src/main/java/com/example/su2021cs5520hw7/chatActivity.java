package com.example.su2021cs5520hw7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;


import com.example.su2021cs5520hw7.model.Sticker;
import com.example.su2021cs5520hw7.model.User;
import com.example.su2021cs5520hw7.services.StickerService;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class chatActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private FloatingActionButton addButton;
    private AlertDialog.Builder dialogBuilder;
    private AlertDialog dialog;
    private ImageView displaySticker;
    private TextView displayUser;
    private User currentUser;
    private FirebaseDatabase db;
    private DatabaseReference mRef;
    private int [] stickerList = {R.drawable.empty_icon, R.drawable.smile_icon,
            R.drawable.yummy_icon,
            R.drawable.sleepy_icon};
    private List<String> receiverList = new ArrayList<>();
    private Spinner receiverSpinner;
    private ArrayAdapter<String> adapter;
    private String receiverName;
    int stickerID;
    private static final String CURRENT_IMAGE_ID = "CURRENT_IMAGE_ID";
    private static final String CHANNEL_ID = "CHANNEL_ID";
    private static final String CHANNEL_NAME = "CHANNEL_NAME";
    private static final String CHANNEL_DESCRIPTION = "CHANNEL_DESCRIPTION";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        stickerID = 0;
        addButton = findViewById(R.id.addButton);
        displayUser = findViewById(R.id.displayUser);
        displayUser.setText("Current User: " +currentUser.getUsername());
        displaySticker = findViewById(R.id.currentSticker);
        displaySticker.setImageResource(stickerList[stickerID]); // default empty image
        db = FirebaseDatabase.getInstance();
        mRef=FirebaseDatabase.getInstance().getReference();
        mRef.child("users").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                Iterator<DataSnapshot> dataSnapshots = dataSnapshot.getChildren().iterator();
                while (dataSnapshots.hasNext()) {
                    DataSnapshot dataSnapshotChild = dataSnapshots.next();
                    String Uname = dataSnapshotChild.child("username").getValue().toString();
                    receiverList.add(Uname);
                    adapter.notifyDataSetChanged(); //notifyDataSetChanged() after updated List.
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        messageReceivedNotification(mRef);

        // Add the spinner view to select receiver.
        receiverSpinner = findViewById(R.id.selectReceiver);
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, receiverList);
        receiverSpinner.setAdapter(adapter);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        receiverSpinner.setOnItemSelectedListener(this);
    }


    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        receiverName = parent.getItemAtPosition(position).toString();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    // handles screen rotation
    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        outState.putInt(CURRENT_IMAGE_ID, stickerID);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState){
        super.onRestoreInstanceState(savedInstanceState);
        stickerID = savedInstanceState.getInt(CURRENT_IMAGE_ID);
        displaySticker.setImageResource(stickerList[stickerID]);
    }

    public void messageReceivedNotification(DatabaseReference mRef) {
        mRef.child("Stickers").orderByKey().limitToLast(1).addChildEventListener(new ChildEventListener() {

            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String previousChildName) {
                Log.d("newChild ", dataSnapshot.toString());
                Sticker sticker = dataSnapshot.getValue(Sticker.class);
                if (sticker.getReceiverName().equals(currentUser.getUsername())) {
                    String content = "You received a new sticker from " + sticker.getSenderName();
                    int imageID = sticker.getEmojiID();
                    showNotification(content, imageID);
                }
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {
                // If any child is updated/changed to the database reference
            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {
                // If any child is removed to the database reference
            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String previousChildName) {
                // If any child is moved to the database reference
            }

            @Override
            public void onCancelled(DatabaseError error) {

            }
        });
    }

    private void showNotification(String content, int imageID) {
        Intent intent = new Intent(this, chatActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 , intent,
                PendingIntent.FLAG_ONE_SHOT);
        Notification notification;
        NotificationCompat.Builder builder;
        NotificationManager notificationManager = getSystemService(NotificationManager.class);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            NotificationChannel notificationChannel = new NotificationChannel(CHANNEL_ID, CHANNEL_NAME, NotificationManager.IMPORTANCE_HIGH);
            // Configure the notification channel
            notificationChannel.setDescription(CHANNEL_DESCRIPTION);
            notificationManager.createNotificationChannel(notificationChannel);
            builder = new NotificationCompat.Builder(this, CHANNEL_ID);

        } else {
            builder = new NotificationCompat.Builder(this);
        }
        Bitmap icon = BitmapFactory.decodeResource(this.getResources(),
                stickerList[imageID]);
        notification = builder.setContentTitle("Sticker Received")
                .setContentText(content)
                .setLargeIcon(icon)
                .setStyle(new NotificationCompat.BigPictureStyle()
                        .bigPicture(icon)
                        .bigLargeIcon(null))
                .setSmallIcon(R.mipmap.ic_launcher)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .build();
        notificationManager.notify(0, notification);
    }

    // show up the dialog when click on floating + button
    public void onclickAddButton(View view){
        dialogBuilder = new AlertDialog.Builder (this);
        final View stickSelectionView = getLayoutInflater().inflate(R.layout.sticker_dialog, null);
        dialogBuilder.setView(stickSelectionView);
        dialog = dialogBuilder.create();
        dialog.show();
    }

    // select sticker
    // select from three stickers in the popup dialog
    public void onclickStickers(View view){
        switch (view.getId()){
            case R.id.smileIcon:
                stickerID = 1;
                displaySticker.setImageResource(stickerList[stickerID]);
                dialog.dismiss();
                break;
            case R.id.yummyIcon:
                stickerID = 2;
                displaySticker.setImageResource(stickerList[stickerID]);
                dialog.dismiss();
                break;
            case R.id.sleepyIcon:
                stickerID = 3;
                displaySticker.setImageResource(stickerList[stickerID]);
                dialog.dismiss();
                break;
        }
    }

    // send sticker message
    public void onclickSendButton(View view){
        System.out.println("ReceiverList :" + receiverList);
        String senderName = currentUser.getUsername();
        if (stickerID != 0 && !receiverName.isEmpty()) {
            displaySticker.setImageResource(stickerList[0]);
            StickerService.createNewSticker(db, senderName, receiverName, stickerID);
            Snackbar.make(view, "Sticker has been sent!", Snackbar.LENGTH_LONG)
                    .show();
            stickerID = 0;
        } else if (stickerID == 0) {
            Snackbar.make(view, "Please select a sticker", Snackbar.LENGTH_LONG)
                    .show();
        }
    }

    public void onclickHistoryButton(View view){
        Intent intent = new Intent(this, historyActivity.class);
        intent.putExtra("currentUser", currentUser);
        startActivity(intent);
    }
}