package com.example.mordowiciel.filmapp.Fragment;


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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import com.example.mordowiciel.filmapp.R;


public class FilterFragment extends Fragment implements View.OnClickListener,
        DatePickerDialog.OnDateSetListener {

    private View rootView;
    private Calendar calendar;
    private EditText startingDateEditText;
    private EditText endingDateEditText;
    private EditText dateToChange;
    private EditText minVoteAverageEditText;
    private EditText maxVoteAverageEditText;
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
        startingDateEditText = (EditText) rootView.findViewById(R.id.beginningDate);
        endingDateEditText = (EditText) rootView.findViewById(R.id.endingDate);
        filterButton = (Button) rootView.findViewById(R.id.filter_button);
        minVoteAverageEditText = (EditText) rootView.findViewById(R.id.minVoteAverage);
        maxVoteAverageEditText = (EditText) rootView.findViewById(R.id.maxVoteAverage);

        startingDateEditText.setOnClickListener(this);
        endingDateEditText.setOnClickListener(this);
        filterButton.setOnClickListener(this);
        minVoteAverageEditText.setOnClickListener(this);
        maxVoteAverageEditText.setOnClickListener(this);


    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.beginningDate:
                dateToChange = startingDateEditText;
                DatePickerDialog startingDateDialog = new DatePickerDialog(getContext(), this, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                if (!endingDateEditText.getText().toString().equals("")) {
                    String dateFormat = "dd/MM/yyyy";
                    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());

                    Date endingDate = null;
                    try {
                        endingDate = formatter.parse(endingDateEditText.getText().toString());
                    } catch (ParseException e) {
                        Log.e("ParseException", e.getMessage());
                    }


                    startingDateDialog.getDatePicker().setMaxDate(endingDate.getTime());
                }

                startingDateDialog.show();

                break;

            case R.id.endingDate:
                dateToChange = endingDateEditText;
                DatePickerDialog endingDateDialog = new DatePickerDialog(getContext(), this, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));

                if (!startingDateEditText.getText().toString().equals("")) {
                    String dateFormat = "dd/MM/yyyy";
                    SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());

                    Date startingDate = null;
                    try {
                        startingDate = formatter.parse(startingDateEditText.getText().toString());
                    } catch (ParseException e) {
                        Log.e("ParseException", e.getMessage());
                    }


                    endingDateDialog.getDatePicker().setMinDate(startingDate.getTime());
                }

                endingDateDialog.show();
                break;

            case R.id.filter_button:

                Log.e("BUTTONPRESSED", "Click!");
                String startingDateString;
                if (startingDateEditText.getText().toString().equals(""))
                    startingDateString = null;
                else
                    startingDateString = startingDateEditText.getText().toString();

                String endingDateString;
                if (endingDateEditText.getText().toString().equals(""))
                    endingDateString = null;
                else
                    endingDateString = endingDateEditText.getText().toString();

                double minVoteAverage;
                if (minVoteAverageEditText.getText().toString().equals(""))
                    minVoteAverage = 0;
                else
                    minVoteAverage = Double.parseDouble(minVoteAverageEditText.getText().toString());

                double maxVoteAverage;
                if (maxVoteAverageEditText.getText().toString().equals(""))
                    maxVoteAverage = 10;
                else
                    maxVoteAverage = Double.parseDouble(maxVoteAverageEditText.getText().toString());


                String sourceFormat = "dd/MM/yyyy";
                SimpleDateFormat sourceFormatter = new SimpleDateFormat(sourceFormat,
                        Locale.getDefault());

                String targetFormat = "yyyy-MM-dd";
                SimpleDateFormat targetFormatter = new SimpleDateFormat(targetFormat,
                        Locale.getDefault());

                try {
                    Date startingDate = sourceFormatter.parse(startingDateEditText.getText().toString());
                    startingDateString = targetFormatter.format(startingDate);

                    Date endingDate = sourceFormatter.parse(endingDateEditText.getText().toString());
                    endingDateString = targetFormatter.format(endingDate);
                } catch (ParseException e) {
                    Log.e("ParseException", e.getMessage());
                }

                Log.e("StartingDateString", "" + startingDateString);
                Log.e("EndingDateString", "" + endingDateString);

                filterBundle.putString("PRIMARY_RELEASE_DATE_MIN_PARAM", startingDateString);
                filterBundle.putString("PRIMARY_RELEASE_DATE_MAX_PARAM", endingDateString);
                filterBundle.putDouble("VOTE_AVERAGE_MIN_PARAM", minVoteAverage);
                filterBundle.putDouble("VOTE_AVERAGE_MAX_PARAM", maxVoteAverage);
                filterBundle.putString("SORTING_PARAM", "popularity.desc");
                mFilterListener.onFilterSpecified(filterBundle);
                break;

            default:
                break;
        }
    }

    private void updateDateLabel(EditText editText, Calendar calendar) {

        String dateFormat = "dd/MM/yyyy";
        SimpleDateFormat formatter = new SimpleDateFormat(dateFormat, Locale.getDefault());
        String dateString = formatter.format(calendar.getTime());
        editText.setText(dateString);

        Date date = null;

        try {
            date = formatter.parse(dateString);
        } catch (ParseException e) {
            Log.e("ParseException", e.getMessage());
        }

        Log.e("DateString", date.toString());

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
