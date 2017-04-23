package com.example.mordowiciel.filmapp.Fragment;


import android.app.DatePickerDialog;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

    public FilterFragment() {
        // Required empty public constructor
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

        startingDate.setOnClickListener(this);
        endingDate.setOnClickListener(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onClick(View view) {

        switch (view.getId()) {

            case R.id.beginningDate:
                dateToChange = startingDate;
                new DatePickerDialog(getContext(), this, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            case R.id.endingDate:
                dateToChange = endingDate;
                new DatePickerDialog(getContext(), this, calendar.get(Calendar.YEAR),
                        calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show();
                break;

            default:
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
