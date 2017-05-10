package com.example.mordowiciel.filmapp.Class;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mordowiciel.filmapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by szyma on 10.05.2017.
 */

public class GenreDialogAdapter extends ArrayAdapter<Genre> {

    public GenreDialogAdapter(Context ctx, int textViewResourceId, ArrayList<Genre> genreList) {
        super(ctx, textViewResourceId, genreList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.genre_list_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.genre_name_checked_text);

        Genre item = getItem(position);

        if (item != null) {
            textView.setText(item.getGenreName());
        }

        return convertView;
    }


}
