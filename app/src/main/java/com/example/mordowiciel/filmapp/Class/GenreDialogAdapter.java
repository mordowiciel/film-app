package com.example.mordowiciel.filmapp.Class;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.mordowiciel.filmapp.R;

import java.util.ArrayList;

/**
 * Created by szyma on 10.05.2017.
 */

public class GenreDialogAdapter extends ArrayAdapter<ShowGenre> {

    public GenreDialogAdapter(Context ctx, int textViewResourceId, ArrayList<ShowGenre> showGenreList) {
        super(ctx, textViewResourceId, showGenreList);
    }

    public View getView(int position, View convertView, ViewGroup parent) {

        if (convertView == null) {
            convertView = LayoutInflater.from(this.getContext()).inflate(R.layout.genre_list_item, parent, false);
        }

        TextView textView = (TextView) convertView.findViewById(R.id.genre_name_checked_text);

        ShowGenre item = getItem(position);

        if (item != null) {
            textView.setText(item.getGenreName());
        }

        return convertView;
    }


}
