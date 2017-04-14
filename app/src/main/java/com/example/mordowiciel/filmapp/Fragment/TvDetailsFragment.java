package com.example.mordowiciel.filmapp.Fragment;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.mordowiciel.filmapp.Class.SeasonsListAdapter;
import com.example.mordowiciel.filmapp.Class.ShowThumbnail;
import com.example.mordowiciel.filmapp.Class.TvClass;
import com.example.mordowiciel.filmapp.Fetch.FetchTvDetailsById;
import com.example.mordowiciel.filmapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class TvDetailsFragment extends Fragment {

    private SeasonsListAdapter recyclerAdapter;
    private ArrayList<ShowThumbnail> showThumbnails;

    public TvDetailsFragment() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Get the arguments from  the ShowDetailsActivity.
        Bundle args = getArguments();
        showThumbnails = new ArrayList<>();

        View rootView = inflater.inflate(R.layout.fragment_tv_details, container, false);

        RecyclerView recyclerView = (RecyclerView)rootView.findViewById(R.id.season_info_recycler_view);

        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(getActivity(),
                LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerAdapter = new SeasonsListAdapter(getActivity(), showThumbnails);
        recyclerView.setAdapter(recyclerAdapter);


        /// Anonymous class containing custom ItemDecoration to separate the cards in the view.
        RecyclerView.ItemDecoration separator = new RecyclerView.ItemDecoration() {

            int space = 15;

            @Override
            public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
                if (parent.getChildAdapterPosition(view) == 0){
                    outRect.right = space;
                }
                else if (parent.getChildAdapterPosition(view) == parent.getAdapter().getItemCount() - 1){
                    outRect.left = space;
                }
                else {
                    outRect.left = space;
                    outRect.right = space;
                }
            }
        };
        recyclerView.addItemDecoration(separator);

        FetchTvDetailsById fetchDetails = new FetchTvDetailsById(getContext());
        fetchDetails.execute(args.getString("SHOW_ID"));

        return rootView;
    }

    public void populateView(TvClass tvDetails) {

        Activity mActivity = getActivity();

        ImageView imageView = (ImageView) mActivity.findViewById(R.id.tv_details_imageview);
        Picasso.with(mActivity).load(tvDetails.getShowPosterLink())
                .fit()
                .into(imageView);

        TextView titleView = (TextView) mActivity.findViewById(R.id.tv_details_title_textview);
        titleView.setText(Html.fromHtml("<b>" + "Original title: " + "</b> <br>"
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
        CardView posterCard = (CardView) mActivity.findViewById(R.id.tv_details_poster_card);
        posterCard.setVisibility(View.VISIBLE);

        CardView infoCard = (CardView) mActivity.findViewById(R.id.tv_details_info_card);
        infoCard.setVisibility(View.VISIBLE);

        CardView detailsCard = (CardView) mActivity.findViewById(R.id.tv_details_overview_card);
        detailsCard.setVisibility(View.VISIBLE);

        ArrayList<ShowThumbnail> seasonsInfo = tvDetails.getSeasonsThumbnails();
        for (ShowThumbnail season: seasonsInfo){
            showThumbnails.add(season);
        }

        // Dismiss  the loading panel.
        RelativeLayout loadingPanel = (RelativeLayout) mActivity.findViewById(R.id.tv_details_loading_panel);
        loadingPanel.setVisibility(View.GONE);

    }
}
