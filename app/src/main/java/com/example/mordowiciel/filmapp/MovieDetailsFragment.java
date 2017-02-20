package com.example.mordowiciel.filmapp;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class MovieDetailsFragment extends Fragment {

    public MovieDetailsFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get the arguments from  the MovieDetailsActivity.
        Bundle args = getArguments();
        View rootView = inflater.inflate(R.layout.fragment_movie_details, container, false);

        String movieOriginalTitle = args.getString("MOVIE_ORIG_TITLE");
        String movieOverview = args.getString("MOVIE_OVERVIEW");
        String movieDate = args.getString("MOVIE_DATE");
        double movieVote = args.getDouble("MOVIE_VOTE");
        String moviePosterLink = args.getString("MOVIE_POSTER");

        ImageView imageView = (ImageView) rootView.findViewById(R.id.movie_details_imageview);
        Picasso.with(getActivity()).load(moviePosterLink)
                .fit()
                .into(imageView);

        TextView titleView = (TextView) rootView.findViewById(R.id.movie_details_title_textview);
        titleView.setText( Html.fromHtml("<b>" + titleView.getText() + "</b> <br>"
                + movieOriginalTitle));

        TextView dateView = (TextView) rootView.findViewById(R.id.movie_details_date_textview);
        dateView.setText(Html.fromHtml("<b>" + dateView.getText() + "</b> <br>"
                + movieDate));

        TextView voteView = (TextView) rootView.findViewById(R.id.movie_details_vote_textview);
        voteView.setText(Html.fromHtml("<b>" + voteView.getText() + "</b> <br>"
                + Double.toString(movieVote) + "/10"));

        TextView overviewView = (TextView) rootView.findViewById(R.id.movie_details_overview_textview);
        overviewView.setText(movieOverview);

        return rootView;
    }

}
