package com.example.mordowiciel.filmapp.Class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mordowiciel.filmapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<ShowThumbnail> {


    public ImageAdapter (Context ctx, int textViewResourceId, ArrayList <ShowThumbnail> showThumbnails){
        super(ctx, textViewResourceId, showThumbnails);
    }

    public View getView (int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.image_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_item_imageview);
        TextView textView = (TextView) convertView.findViewById(R.id.image_item_textview);

        ShowThumbnail item = getItem(position);

        if (item != null) {
            textView.setText(item.getShowTitle());
            Picasso.with(getContext())
                    .load(item.getShowPosterLink())
                    .fit()
                    .into(imageView);
        }

        return convertView;
    }
}