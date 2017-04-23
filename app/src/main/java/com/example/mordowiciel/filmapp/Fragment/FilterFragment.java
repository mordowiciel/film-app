package com.example.mordowiciel.filmapp.Fragment;


import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.mordowiciel.filmapp.R;


public class FilterFragment extends DialogFragment {


    public FilterFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.dialog_filter, container, false);
        return rootView;
    }

//    @Override
//    public Dialog onCreateDialog(Bundle savedInstanceState) {
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
//        builder.setTitle("Set data filters");
//        // !!! inflater.inflate return the rootView, used later in getting TextViews.
//        LayoutInflater inflater = getActivity().getLayoutInflater();
//        View rootView = inflater.inflate(R.layout.dialog_filter, null);
//        builder.setView(rootView);
//
//        return builder.create();
//    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

}
