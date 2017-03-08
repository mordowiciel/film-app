package com.example.mordowiciel.filmapp;

import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

        String showType = args.getString("SHOW_TYPE");

        if(showType.equals("movie")){
            FetchMovieDetailsById fetchDetails = new FetchMovieDetailsById(getContext());
            fetchDetails.execute(args.getString("SHOW_ID"));
        }

        if (showType.equals("tv")){
            //FetchMovieDetailsById fetchDetails = new FetchMovieDetailsById(getContext());
            //fetchDetails.execute(args.getString("SHOW_ID"));

            //FetchTvDetailsById fetchDetails = new FetchTvDetailsById(getContext());
            //fetchDetails.execute(args.getString("SHOW_ID"));
        }
        
        return rootView;
    }

    public void populateView(MovieClass movieDetails) {

        // Populate the view in the DetailsFragment.

        Activity mActivity = getActivity();

        ImageView imageView = (ImageView) mActivity.findViewById(R.id.movie_details_imageview);
        Picasso.with(mActivity).load(movieDetails.getShowPosterLink())
                .fit()
                .into(imageView);

        TextView titleView = (TextView) mActivity.findViewById(R.id.movie_details_title_textview);
        titleView.setText( Html.fromHtml("<b>" + "Original title: " + "</b> <br>"
                + movieDetails.getShowOriginalTitle()));

        TextView dateView = (TextView) mActivity.findViewById(R.id.movie_details_date_textview);
        dateView.setText(Html.fromHtml("<b>" + "Release date: " + "</b> <br>"
                + movieDetails.getShowReleaseDate()));

        TextView voteView = (TextView) mActivity.findViewById(R.id.movie_details_vote_textview);
        voteView.setText(Html.fromHtml("<b>" + "Vote average: " + "</b> <br>"
                + Double.toString(movieDetails.getShowVoteAverage()) + "/10"));

        TextView overviewView = (TextView) mActivity.findViewById(R.id.movie_details_overview_textview);
        overviewView.setText(movieDetails.getShowOverview());

        // Make the cards visible after loading the content.
        CardView posterCard = (CardView)mActivity.findViewById(R.id.movie_details_poster_card);
        posterCard.setVisibility(View.VISIBLE);

        CardView infoCard = (CardView)mActivity.findViewById(R.id.movie_details_info_card);
        infoCard.setVisibility(View.VISIBLE);

        CardView detailsCard = (CardView)mActivity.findViewById(R.id.movie_details_overview_card);
        detailsCard.setVisibility(View.VISIBLE);

        // Dismiss  the loading panel.
        RelativeLayout loadingPanel = (RelativeLayout)mActivity.findViewById(R.id.movie_details_loading_panel);
        loadingPanel.setVisibility(View.GONE);
    }

}
