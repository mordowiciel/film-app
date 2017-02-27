package com.example.mordowiciel.filmapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by szyma on 26.02.2017.
 */

public class NavDrawerAdapter extends ArrayAdapter<NavDrawerView> {

    private int[] imageIdsArray;

    public NavDrawerAdapter (Context ctx, int textViewResourceId, ArrayList<NavDrawerView> views){
        super(ctx, textViewResourceId, views);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent){

        if (convertView == null){
            convertView = LayoutInflater.from(this.getContext()).
                    inflate(R.layout.navigation_drawer_item, parent, false);
        }

        NavDrawerView item = getItem(position);

        if(item != null){
            TextView textView = (TextView) convertView.findViewById
                    (R.id.navigation_drawer_item_textview);
            textView.setText(item.getMenuText());

            ImageView imageView = (ImageView) convertView.findViewById
                    (R.id.navigation_drawer_item_image);
            imageView.setImageResource(item.getImageId());

        }

        return convertView;
    }
}
