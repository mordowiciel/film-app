package com.example.mordowiciel.filmapp.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.mordowiciel.filmapp.Activity.ShowDetailsActivity;
import com.example.mordowiciel.filmapp.Fetch.FetchDiscoverMovies;
import com.example.mordowiciel.filmapp.Fetch.FetchDiscoverTv;
import com.example.mordowiciel.filmapp.Class.ImageAdapter;
import com.example.mordowiciel.filmapp.Class.InfiniteScrollListener;
import com.example.mordowiciel.filmapp.Fetch.FetchGenres;
import com.example.mordowiciel.filmapp.R;
import com.example.mordowiciel.filmapp.Class.ShowThumbnail;

import java.util.ArrayList;

public class MainFragment extends Fragment implements AdapterView.OnItemClickListener,
                                            InfiniteScrollListener{

    private FetchDiscoverMovies fetchDiscoverMovies;
    private FetchDiscoverTv fetchDiscoverTv;
    private ImageAdapter imageAdapter;
    private GridView gridView;
    private boolean tvIsShown;
    private boolean movieIsShown;
    Bundle filterBundle;

    public MainFragment() {
        // Required empty public constructor
    }

    @Override
    public void onSaveInstanceState (Bundle state) {
        super.onSaveInstanceState(state);
        state.putBoolean("movieIsShown", movieIsShown);
        state.putBoolean("tvIsShown", tvIsShown);
        state.putBundle("filterBundle", filterBundle);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if (savedInstanceState != null) {
            movieIsShown = savedInstanceState.getBoolean("movieIsShown");
            tvIsShown = savedInstanceState.getBoolean("tvIsShown");
            filterBundle = savedInstanceState.getBundle("filterBundle");
        }
        else {
            movieIsShown = true;
            tvIsShown = false;
            filterBundle = new Bundle();
            filterBundle.putString("SORTING_PARAM", "popularity.desc");
            filterBundle.putInt("PAGE_PARAM", 1);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<ShowThumbnail> showThumbnails = new ArrayList<>();

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        gridView = (GridView) rootView.findViewById(R.id.fragment_main_gridview);
        imageAdapter = new ImageAdapter(getActivity(), R.layout.image_item, showThumbnails);

        gridView.setAdapter(imageAdapter);
        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();

        if(movieIsShown) {
            fetchDiscoverMovies = new FetchDiscoverMovies(imageAdapter);
            fetchDiscoverMovies.execute(filterBundle);
        }

        if(tvIsShown) {
            fetchDiscoverTv = new FetchDiscoverTv(imageAdapter);
            fetchDiscoverTv.execute(filterBundle);
        }

        //Create a click listener for a particular movie.
        gridView.setOnItemClickListener(this);
        gridView.setOnScrollListener(this);
    }

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

        String showId = imageAdapter.getItem(position).getShowId();
        String showTitle = imageAdapter.getItem(position).getShowTitle();

        Bundle intentExtras = new Bundle();
        intentExtras.putString("SHOW_ID", showId);
        intentExtras.putString("SHOW_TITLE", showTitle);

        if (movieIsShown)
            intentExtras.putString("SHOW_TYPE", "movie");

        if (tvIsShown)
            intentExtras.putString("SHOW_TYPE", "tv");

        Intent intent = new Intent(getActivity(), ShowDetailsActivity.class);
        intent.putExtras(intentExtras);

        startActivity(intent);
    }

    // Method to load more data from DB.
    @Override
    public boolean onLoadMore(int page, int totalItemsCount) {

        //FetchMoviesPassedParam params = new FetchMoviesPassedParam("popularity.desc", page, 2014);
        filterBundle.putInt("PAGE_PARAM", page);
        if (movieIsShown) {
            fetchDiscoverMovies = new FetchDiscoverMovies(imageAdapter);
            fetchDiscoverMovies.execute(filterBundle);
        }
        if (tvIsShown) {
            fetchDiscoverTv = new FetchDiscoverTv(imageAdapter);
            fetchDiscoverTv.execute(filterBundle);
        }
        return true;
    }

    @Override
    public void onScroll (AbsListView view, int firstVisibleItem, int visibleItemCount,
                          int totalItemCount) {

        // Minimum number of items to have below current scroll position to load more items.
        int visibleThreshold = 4;

        // The total number of items in the dataset after last load.
        int previousTotalItemCount = 0;

        // Set the starting page index.
        int startingPageIndex = 1;

        // The offset number of current page of data received from DB.
        int currentPage = 0;

        // True - waiting for the set of data to load.
        boolean loading = true;


        // If total item count is 0 - list of items is invalidated, reset to initial state
        if (totalItemCount < previousTotalItemCount){
            currentPage = startingPageIndex;
            previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0)
                loading = true;
        }

        // If data is still loaded, we have to check if the dataset count has changed.
        // If yes, we accept it and change the page numbers.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
            currentPage++;
        }

        // If data is currently not being downloaded, we have to check if we have reached visible
        // threshold and reload more data.
        // If we do need to download more data, execute onMoreLoad.

        if (!loading && (firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount) {
            loading = true;
            currentPage++;
            onLoadMore(currentPage,totalItemCount);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState){
        // No action.
    }

    public void showPopularMovies() {
        movieIsShown = true;
        tvIsShown = false;
        imageAdapter.clear();
        fetchDiscoverMovies = new FetchDiscoverMovies(imageAdapter);
        filterBundle.putString("SORTING_PARAM", "popularity.desc");
        fetchDiscoverMovies.execute(filterBundle);
    }

    public void showMostRatedMovies() {
        movieIsShown = true;
        tvIsShown = false;
        imageAdapter.clear();
        fetchDiscoverMovies = new FetchDiscoverMovies(imageAdapter);
        filterBundle.putString("SORTING_PARAM", "vote_average.desc");
        fetchDiscoverMovies.execute(filterBundle);
    }

    public void showPopularTv() {
        movieIsShown = false;
        tvIsShown = true;
        imageAdapter.clear();
        fetchDiscoverTv = new FetchDiscoverTv(imageAdapter);
        filterBundle.putString("SORTING_PARAM", "popularity.desc");
        fetchDiscoverTv.execute(filterBundle);
    }

    public void showMostRatedTv(){
        movieIsShown = false;
        tvIsShown = true;
        imageAdapter.clear();
        fetchDiscoverTv = new FetchDiscoverTv(imageAdapter);
        filterBundle.putString("SORTING_PARAM", "vote_average.desc");
        fetchDiscoverTv.execute(filterBundle);
    }

    public boolean tvIsShown() {
        return tvIsShown;
    }

    public boolean movieIsShown() {
        return movieIsShown;
    }


}

