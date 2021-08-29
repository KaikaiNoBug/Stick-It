package com.example.su2021cs5520hw7;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.util.Log;
import android.widget.GridView;
import android.widget.TextView;

import com.example.su2021cs5520hw7.databinding.ActivityMainBinding;
import com.example.su2021cs5520hw7.model.Sticker;
import com.example.su2021cs5520hw7.model.User;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class historyActivity extends AppCompatActivity {
    GridView gridView;
    int numStickerSent;
    private TextView displayNums;
    int[] receivedStickersId; // a list of received stickers' id
    private User currentUser;
    TextView displayUser;
    List<Sticker> stickerList;


    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);

        currentUser = (User) getIntent().getSerializableExtra("currentUser");
        stickerList = new ArrayList<>();
        displayUser = findViewById(R.id.displayUser2);
        displayUser.setText("Current User: " +currentUser.getUsername());
        // display # sent
        //numStickerSent = stickerList.size(); // change
        displayNums = findViewById(R.id.numberSent);


        // display history of received stickers
        //receivedStickersId = new int[]{1, 2, 3, 2, 1, 2, 3, 1, 2, 2, 1}; // change


        gridView = findViewById(R.id.gridView);
        //gridviewAdapter gridviewAdapter = new gridviewAdapter(historyActivity.this, receivedStickersId);
        gridviewAdapter gridviewAdapter = new gridviewAdapter(historyActivity.this, stickerList);
        gridView.setAdapter(gridviewAdapter);

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Stickers");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                stickerList.clear();
                numStickerSent = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Log.d("hello", snapshot.toString());
                    Sticker sticker = snapshot.getValue(Sticker.class);
                    //check if name is equal here
                    //Sticker sticker = snapshot.get();
                    //Long emojiId = (Long) snapshot.child("emojiID").getValue();
                    //String userFrom = snapshot.child("receiverName").getValue().toString();
                    //String userTo = snapshot.child("senderName").getValue().toString();
                    //Log.d("emojiID is: ", String.valueOf(sticker.getEmojiID()));
                    //Log.d("userFrom is: ", sticker.senderName);
                    //Log.d("userTo is: ", sticker.receiverName);
                    if (sticker.getSenderName().equals(currentUser.getUsername())) {
                        numStickerSent++;
                    }
                    if (sticker.getReceiverName().equals(currentUser.getUsername())) {
                        stickerList.add(sticker);
                    }
                }
                gridviewAdapter.notifyDataSetChanged();
                Log.d("numStickerSent: ", String.valueOf(numStickerSent));
                displayNums.setText("You have sent " + Integer.toString(numStickerSent) + " stickers");
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });



    }
}