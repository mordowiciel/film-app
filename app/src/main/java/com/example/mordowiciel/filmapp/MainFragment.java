package com.example.mordowiciel.filmapp;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private FetchDiscoverMovies fetchPopular;
    private ImageAdapter imageAdapter;

    public MainFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        ArrayList<ShowThumbnail> showThumbnails = new ArrayList<>();

        View rootView = inflater.inflate(R.layout.fragment_main, container, false);

        GridView gridView = (GridView) rootView.findViewById(R.id.fragment_main_gridview);
        imageAdapter = new ImageAdapter(getActivity(), R.layout.image_item, showThumbnails);

        gridView.setAdapter(imageAdapter);

        //Create ASyncTask and execute it.
        fetchPopular = new FetchDiscoverMovies(imageAdapter);
        FetchMoviesPassedParam params = new FetchMoviesPassedParam("popularity.desc", 1);
        fetchPopular.execute(params);

        //Create a click listener for a particular movie.
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String showId = imageAdapter.getItem(position).getShowId();
                String showTitle = imageAdapter.getItem(position).getShowTitle();

                Bundle intentExtras = new Bundle();
                intentExtras.putString("SHOW_ID", showId);
                intentExtras.putString("SHOW_TITLE", showTitle);

                Intent intent = new Intent(getActivity(), MovieDetailsActivity.class);
                intent.putExtras(intentExtras);

                startActivity(intent);
            }
        });

        gridView.setOnScrollListener(new InfiniteScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {

                FetchMoviesPassedParam params = new FetchMoviesPassedParam("popularity.desc", page);
                fetchPopular = new FetchDiscoverMovies(imageAdapter);
                fetchPopular.execute(params);
                return true;
            }
        });
        return rootView;
    }


    public void showPopularMovies() {
        imageAdapter.clear();
        fetchPopular = new FetchDiscoverMovies(imageAdapter);
        FetchMoviesPassedParam params = new FetchMoviesPassedParam("popularity.desc", 1);
        fetchPopular.execute(params);
    }

    public void showMostRatedMovies() {
        imageAdapter.clear();
        fetchPopular = new FetchDiscoverMovies(imageAdapter);
        FetchMoviesPassedParam params = new FetchMoviesPassedParam("vote_average.desc", 1);
        fetchPopular.execute(params);
    }


}

