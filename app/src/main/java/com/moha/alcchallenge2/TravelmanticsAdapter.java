package com.moha.alcchallenge2;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.List;

public class TravelmanticsAdapter extends ArrayAdapter<Travelmantics> {
    public TravelmanticsAdapter(Context context, List<Travelmantics> objects) {
        super(context, 0, objects);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = ((Activity) getContext()).getLayoutInflater().inflate(R.layout.common_travelmantic, parent, false);
        }

        ImageView photoImageView = convertView.findViewById(R.id.imageView);
        TextView priceTextView = convertView.findViewById(R.id.priceTV);
        TextView titleTextView = convertView.findViewById(R.id.titleTV);
        TextView messageTextView = convertView.findViewById(R.id.messageTV);

        Travelmantics travelmantics = getItem(position);

        Glide.with(photoImageView.getContext())
                .load(travelmantics.getmImageUrl())
                .into(photoImageView);

        priceTextView.setText(String.valueOf(travelmantics.getPrice()));

        titleTextView.setText(travelmantics.getmTitle());

        messageTextView.setText(travelmantics.getmMessage());

        return convertView;
    }
}
