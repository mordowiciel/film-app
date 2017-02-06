package com.example.mordowiciel.filmapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ImageAdapter extends ArrayAdapter<Movie> {


    public ImageAdapter (Context ctx, int textViewResourceId, ArrayList <Movie> movies){
        super(ctx, textViewResourceId, movies);
    }

    public View getView (int position, View convertView, ViewGroup parent) {

        if (convertView == null){
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.image_item, parent, false);
        }

        ImageView imageView = (ImageView) convertView.findViewById(R.id.image_item_imageview);
        TextView textView = (TextView) convertView.findViewById(R.id.image_item_textview);

        Movie item = getItem(position);

        if (item != null) {
            textView.setText(item.getMovieTitle());
            Picasso.with(getContext())
                    .load(item.getMoviePosterLink())
                    .fit()
                    .into(imageView);
        }

        return convertView;
    }
}