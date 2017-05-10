package com.example.mordowiciel.filmapp.Fragment;

import android.app.AlertDialog;
import android.app.Dialog;
import android.graphics.drawable.Drawable;
import android.support.v4.app.DialogFragment;
import android.content.DialogInterface;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.content.res.ResourcesCompat;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.CheckedTextView;
import android.widget.ListView;

import com.example.mordowiciel.filmapp.Class.Genre;
import com.example.mordowiciel.filmapp.Class.GenreDialogAdapter;
import com.example.mordowiciel.filmapp.Database.DatabaseContract;
import com.example.mordowiciel.filmapp.Database.DatabaseHelper;
import com.example.mordowiciel.filmapp.R;

import java.util.ArrayList;

/**
 * Created by szyma on 10.05.2017.
 */

public class GenreFragment extends DialogFragment {

    ArrayList<Genre> dbGenreList;
    ArrayList<Genre> selectedGenreList;


    public GenreFragment() {

    }

    private ArrayList<Genre> getGenresFromDatabase() {

        DatabaseHelper dbHelper = new DatabaseHelper(getActivity());
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        Cursor cursor = db.query(
                DatabaseContract.GenreTable.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
        );

        ArrayList<Genre> genreList = new ArrayList<>();
        while (cursor.moveToNext()) {
            int genreId = cursor.getInt(cursor
                    .getColumnIndexOrThrow(DatabaseContract.GenreTable._ID));
            String genreName = cursor.getString(cursor
                    .getColumnIndexOrThrow(DatabaseContract.GenreTable.COLUMN_NAME));

            Genre genre = new Genre(genreId, genreName);
            genreList.add(genre);
        }

        return genreList;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        dbGenreList = getGenresFromDatabase();
        selectedGenreList = new ArrayList<>();

        final GenreDialogAdapter adapter = new GenreDialogAdapter(getActivity(),
                R.layout.genre_list_item, dbGenreList);


        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Choose the genres")
                .setAdapter(adapter, null)
                .setPositiveButton("OK", null)
                .setNegativeButton("Cancel", null);

        AlertDialog dialog = builder.create();
        dialog.getListView().setItemsCanFocus(false);
        dialog.getListView().setChoiceMode(ListView.CHOICE_MODE_MULTIPLE);
        dialog.getListView().setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                GenreDialogAdapter genreAdapter = (GenreDialogAdapter) parent.getAdapter();
                Genre item = genreAdapter.getItem(position);
                Log.e("onItemClick", "Clicked " + item.getGenreName());
                CheckedTextView checkedTextView = (CheckedTextView)
                        view.findViewById(R.id.genre_name_checked_text);

                //Drawable checked = getActivity().getResources().getDrawable(R.drawable.ic_done);
                Drawable checked = ResourcesCompat
                        .getDrawable(getActivity().getResources(), R.drawable.ic_done, null);

                if (checkedTextView.isChecked()) {
                    checkedTextView.setCheckMarkDrawable(null);
                    //selectedGenreList.remove(item);
                } else {
                    checkedTextView.setCheckMarkDrawable(checked);
                    //selectedGenreList.add(item);
                }
            }
        });

        return dialog;
    }
}
