package com.example.mordowiciel.filmapp.Fragment;


import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.example.mordowiciel.filmapp.R;


public class FilterFragment extends Fragment implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    private View rootView;
    private Calendar calendar;
    private EditText startingDate;
    private EditText endingDate;
    private EditText dateToChange;
    private EditText minVoteAverageEdit;
    private EditText maxVoteAverageEdit;
    private EditText minVoteCountEdit;
    private EditText maxVoteCountEdit;
    private Button filterButton;

    private OnFilterSpecifiedListener mFilterListener;
    private Bundle filterBundle;

    public FilterFragment() {
        // Required empty public constructor
    }

    public interface OnFilterSpecifiedListener {
        public void onFilterSpecified(Bundle filterBundle);
    }

    @Override
    public void onAttach(Context ctx) {
        filterBundle = new Bundle();
        super.onAttach(ctx);
        try {
            mFilterListener = (OnFilterSpecifiedListener) ctx;
        } catch (ClassCastException e) {
            throw new ClassCastException(ctx.toString()
                    + " must implement OnHeadlineSelectedListener");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        rootView = inflater.inflate(R.layout.fragment_filter, container, false);
        return rootView;
    }

    @Override
    public void onStart() {

        super.onStart();

        calendar = Calendar.getInstance();
        startingDate = (EditText) rootView.findViewById(R.id.beginningDate);
        endingDate = (EditText) rootView.findViewById(R.id.endingDate);
        filterButton = (Button) rootView.findViewById(R.id.filter_button);
        minVoteAverageEdit = (EditText) rootView.findViewById(R.id.minVoteAverage);
        maxVoteAverageEdit = (EditText) rootView.findViewById(R.id.maxVoteAverage);

        startingDate.setOnClickListener(this);
        endingDate.setOnClickListener(this);
        filterButton.setOnClickListener(this);
        minVoteAverageEdit.setOnClickListener(this);
        maxVoteAverageEdit.setOnClickListener(this);


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.beginningDate:
                Log.e("BEGINDATE", "Click!");
                dateToChange = startingDate;
                new DatePickerDialog(getContext(), this, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.endingDate:
                dateToChange = endingDate;
                new DatePickerDialog(getContext(), this, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.filter_button:

                Log.e("BUTTONPRESSED", "Click!");
                String startingDateString;
                if (startingDate.getText().toString().equals(""))
                    startingDateString = null;
                else
                    startingDateString = startingDate.getText().toString();

                String endingDateString;
                if (endingDate.getText().toString().equals(""))
                    endingDateString = null;
                else
                    endingDateString = endingDate.getText().toString();

                double minVoteAverage;
                if (minVoteAverageEdit.getText().toString().equals(""))
                    minVoteAverage = 0;
                else
                    minVoteAverage = Double.parseDouble(minVoteAverageEdit.getText().toString());

                double maxVoteAverage;
                if (maxVoteAverageEdit.getText().toString().equals(""))
                    maxVoteAverage = 10;
                else
                    maxVoteAverage = Double.parseDouble(maxVoteAverageEdit.getText().toString());

                filterBundle.putString("startingDate", startingDateString);
                filterBundle.putString("endingDate", endingDateString);
                filterBundle.putDouble("VOTE_AVERAGE_MIN_PARAM", minVoteAverage);
                filterBundle.putDouble("VOTE_AVERAGE_MAX_PARAM", maxVoteAverage);
                filterBundle.putString("SORTING_PARAM", "popularity.desc");
                mFilterListener.onFilterSpecified(filterBundle);
                break;

            default:
                Log.e("DEFAULTCLICK", "Click!");
                break;
        }
    }

    private void updateDateLabel(EditText editText, Calendar calendar) {

        // TODO : Handle user input (for ex. ending date can't be lower than beginning date)
        // TODO : Change the dateFormat to the format used by the API.
        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        String dateString = formatter.format(calendar.getTime());
        editText.setText(dateString);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        calendar.set(Calendar.YEAR, year);
        calendar.set(Calendar.MONTH, month);
        calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        updateDateLabel(dateToChange, calendar);
        calendar = Calendar.getInstance();
    }



}
