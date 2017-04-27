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
        InfiniteScrollListener {

    private FetchDiscoverMovies fetchDiscoverMovies;
    private FetchDiscoverTv fetchDiscoverTv;
    private ImageAdapter imageAdapter;
    private GridView gridView;
    ArrayList<ShowThumbnail> showThumbnails;
    private boolean tvIsShown;
    private boolean movieIsShown;
    Bundle filterBundle;

    //////////// INFINITE SCROLLER PARAMETERS ////////////

    // Minimum number of items to have below current scroll position to load more items.
    int visibleThreshold;

    // The total number of items in the dataset after last load.
    int previousTotalItemCount;

    // Set the starting page index.
    int startingPageIndex;

    // The offset number of current page of data received from DB.
    int currentPage;

    // True - waiting for the set of data to load.
    boolean loading;

    public MainFragment() {

    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {

        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putBundle("filterBundle", filterBundle);
        savedInstanceState.putInt("visibleThreshold", visibleThreshold);
        savedInstanceState.putInt("previousTotalItemCount", previousTotalItemCount);
        savedInstanceState.putInt("startingPageIndex", startingPageIndex);
        savedInstanceState.putInt("currentPage", currentPage);
        savedInstanceState.putBoolean("loading", loading);
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            showThumbnails = new ArrayList<>();
            filterBundle = new Bundle();
        } else {
            filterBundle = savedInstanceState.getBundle("filterBundle");
            visibleThreshold = savedInstanceState.getInt("visibleThreshold");
            previousTotalItemCount = savedInstanceState.getInt("previousTotalItemCount");
            startingPageIndex = savedInstanceState.getInt("startingPageIndex");
            currentPage = savedInstanceState.getInt("currentPage");
            loading = savedInstanceState.getBoolean("loading");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        gridView = (GridView) rootView.findViewById(R.id.fragment_main_gridview);
        imageAdapter = new ImageAdapter(getActivity(), R.layout.image_item, showThumbnails);
        gridView.setAdapter(imageAdapter);

        gridView.setOnItemClickListener(this);
        gridView.setOnScrollListener(this);

        showPopularMovies();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
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

        filterBundle.putInt("PAGE_PARAM", page);
        Log.e("Loading page", String.valueOf(page));

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
    public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount,
                         int totalItemCount) {

        // If there is no items in the adapter, perform first load.
        if (totalItemCount == 0) {
            currentPage++;
            onLoadMore(currentPage, totalItemCount);
        }

        // If data is still loaded, we have to check if the dataset count has changed.
        // If yes, we accept it and change the page numbers.
        if (loading && (totalItemCount > previousTotalItemCount)) {
            loading = false;
            previousTotalItemCount = totalItemCount;
        }

        // If data is currently not being downloaded, we have to check if we have reached visible
        // threshold and reload more data.
        // If we do need to download more data, execute onMoreLoad.
        if (!loading && (firstVisibleItem + visibleItemCount + visibleThreshold) >= totalItemCount) {
            loading = true;
            currentPage++;
            onLoadMore(currentPage, totalItemCount);
        }
    }

    private void setInitialScrollerState() {
        visibleThreshold = 4;
        previousTotalItemCount = 0;
        startingPageIndex = 1;
        currentPage = 0;
        loading = true;
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState) {
        // No action.
    }

    public void showPopularMovies() {
        movieIsShown = true;
        tvIsShown = false;
        filterBundle.putString("SORTING_PARAM", "popularity.desc");
        setInitialScrollerState();
        imageAdapter.clear();
    }

    public void showMostRatedMovies() {
        movieIsShown = true;
        tvIsShown = false;
        filterBundle.putString("SORTING_PARAM", "vote_average.desc");
        setInitialScrollerState();
        imageAdapter.clear();
    }

    public void showPopularTv() {
        movieIsShown = false;
        tvIsShown = true;
        filterBundle.putString("SORTING_PARAM", "popularity.desc");
        setInitialScrollerState();
        imageAdapter.clear();
    }

    public void showMostRatedTv() {
        movieIsShown = false;
        tvIsShown = true;
        filterBundle.putString("SORTING_PARAM", "vote_average.desc");
        setInitialScrollerState();
        imageAdapter.clear();
    }

    public boolean tvIsShown() {
        return tvIsShown;
    }

    public boolean movieIsShown() {
        return movieIsShown;
    }


}

