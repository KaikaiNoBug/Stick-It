package com.example.su2021cs5520hw7;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.su2021cs5520hw7.model.Sticker;

import java.util.ArrayList;
import java.util.List;

public class gridviewAdapter extends BaseAdapter {
    Context context;
    //int [] stickerId;
    // hard code, shoud receive from database
    //String senderName = "max";
    List<Sticker> stickers;

    LayoutInflater inflater;
    private int [] stickerList = {R.drawable.empty_icon, R.drawable.smile_icon,
            R.drawable.yummy_icon,
            R.drawable.sleepy_icon};

    public gridviewAdapter(Context context, List<Sticker> stickers){
        this.context = context;
        this.stickers = stickers;
    }

    @Override
    public int getCount() {
        return stickers.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null){
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }
        if (convertView == null){
            convertView = inflater.inflate(R.layout.gridview_item, null);
        }
        ImageView imageView = convertView.findViewById(R.id.grid_stickers);
        imageView.setImageResource(stickerList[stickers.get(position).getEmojiID()]);
        TextView tv = convertView.findViewById(R.id.senderName);
        tv.setText(stickers.get(position).getSenderName());

        return convertView;
    }
}
