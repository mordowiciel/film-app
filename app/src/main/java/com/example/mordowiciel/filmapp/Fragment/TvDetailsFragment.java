package com.example.mordowiciel.filmapp.Fragment;

import android.app.Activity;
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

import com.example.mordowiciel.filmapp.Class.TvClass;
import com.example.mordowiciel.filmapp.Fetch.FetchTvDetailsById;
import com.example.mordowiciel.filmapp.R;
import com.squareup.picasso.Picasso;

/**
 * Created by szyma on 06.04.2017.
 */

public class TvDetailsFragment extends Fragment {

    public TvDetailsFragment(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get the arguments from  the ShowDetailsActivity.
        Bundle args = getArguments();
        View rootView = inflater.inflate(R.layout.fragment_tv_details, container, false);

        String showType = args.getString("SHOW_TYPE");

        FetchTvDetailsById fetchDetails = new FetchTvDetailsById(getContext());
        fetchDetails.execute(args.getString("SHOW_ID"));

        return rootView;
    }

    public void populateView(TvClass tvDetails) {

        Activity mActivity = getActivity();

        ImageView imageView = (ImageView)mActivity.findViewById(R.id.tv_details_imageview);
        Picasso.with(mActivity).load(tvDetails.getShowPosterLink())
                .fit()
                .into(imageView);

        TextView titleView = (TextView) mActivity.findViewById(R.id.tv_details_title_textview);
        titleView.setText( Html.fromHtml("<b>" + "Original title: " + "</b> <br>"
                + tvDetails.getShowOriginalTitle()));

        TextView dateView = (TextView) mActivity.findViewById(R.id.tv_details_date_textview);
        dateView.setText(Html.fromHtml("<b>" + "Release date: " + "</b> <br>"
                + tvDetails.getShowReleaseDate()));

        TextView voteView = (TextView) mActivity.findViewById(R.id.tv_details_vote_textview);
        voteView.setText(Html.fromHtml("<b>" + "Vote average: " + "</b> <br>"
                + Double.toString(tvDetails.getShowVoteAverage()) + "/10"));

        TextView overviewView = (TextView) mActivity.findViewById(R.id.tv_details_overview_textview);
        overviewView.setText(tvDetails.getShowOverview());

        // Make the cards visible after loading the content.
        CardView posterCard = (CardView)mActivity.findViewById(R.id.tv_details_poster_card);
        posterCard.setVisibility(View.VISIBLE);

        CardView infoCard = (CardView)mActivity.findViewById(R.id.tv_details_info_card);
        infoCard.setVisibility(View.VISIBLE);

        CardView detailsCard = (CardView)mActivity.findViewById(R.id.tv_details_overview_card);
        detailsCard.setVisibility(View.VISIBLE);

        // Dismiss  the loading panel.
        RelativeLayout loadingPanel = (RelativeLayout)mActivity.findViewById(R.id.tv_details_loading_panel);
        loadingPanel.setVisibility(View.GONE);

    }
}