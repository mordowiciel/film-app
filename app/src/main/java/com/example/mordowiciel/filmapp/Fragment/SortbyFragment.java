package com.example.mordowiciel.filmapp.Fragment;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.example.mordowiciel.filmapp.R;


public class SortbyFragment extends DialogFragment {

    // Member listener variable.
    NoticeSortingDialogFragment mListener;

    public interface NoticeSortingDialogFragment {
        void onDialogPopularityClick(DialogFragment dialogFragment);
        void onDialogRatingClick(DialogFragment dialogFragment);
    }

    public SortbyFragment() {
        // Required empty public constructor
    }

    @Override
    public void onAttach(Context ctx) {
        super.onAttach(ctx);

        //Try to attach a listener to given context.
        try {
            mListener = (NoticeSortingDialogFragment) ctx;
        }
        // If catched, then given activity doesn't implement NoticeSortingDialogFragment.
        catch (ClassCastException e) {
            throw new ClassCastException(ctx.toString() + " does not implement NoticeSortingDialogFragment");
        }
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        // !!! inflater.inflate return the rootView, used later in getting TextViews.
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View rootView = inflater.inflate(R.layout.dialog_sortby, null);
        builder.setView(rootView);

        //Set click listeners on both TextViews.
        final TextView popularityTextView = (TextView)rootView.findViewById(R.id.sortby_popularity_textview);
        popularityTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onDialogPopularityClick(SortbyFragment.this);
                dismiss();
            }
        });

        final TextView ratingTextView = (TextView)rootView.findViewById(R.id.sortby_rating_textview);
        ratingTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view){
                mListener.onDialogRatingClick(SortbyFragment.this);
                dismiss();
            }
        });

        return builder.create();
    }


}
