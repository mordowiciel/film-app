package com.example.mordowiciel.filmapp.Fragment;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import com.example.mordowiciel.filmapp.Activity.ShowDetailsActivity;
import com.example.mordowiciel.filmapp.Fetch.FetchDiscoverMovies;
import com.example.mordowiciel.filmapp.Fetch.FetchDiscoverTv;
import com.example.mordowiciel.filmapp.Class.ImageAdapter;
import com.example.mordowiciel.filmapp.Class.InfiniteScrollListener;
import com.example.mordowiciel.filmapp.R;
import com.example.mordowiciel.filmapp.Class.ShowThumbnail;

import java.util.ArrayList;

public class MainFragment extends Fragment {

    private FetchDiscoverMovies fetchDiscoverMovies;
    private FetchDiscoverTv fetchDiscoverTv;
    private ImageAdapter imageAdapter;
    public boolean tvIsShown;
    public boolean movieIsShown;
    Bundle filterBundle;

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
        movieIsShown = true;
        fetchDiscoverMovies = new FetchDiscoverMovies(imageAdapter);

        filterBundle = new Bundle();
        filterBundle.putString("SORTING_PARAM", "popularity.desc");
        filterBundle.putInt("PAGE_PARAM", 1);

        fetchDiscoverMovies.execute(filterBundle);

        //Create a click listener for a particular movie.
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {

                String showId = imageAdapter.getItem(position).getShowId();
                String showTitle = imageAdapter.getItem(position).getShowTitle();

                Bundle intentExtras = new Bundle();
                intentExtras.putString("SHOW_ID", showId);
                intentExtras.putString("SHOW_TITLE", showTitle);

                if(movieIsShown)
                    intentExtras.putString("SHOW_TYPE", "movie");

                if(tvIsShown)
                    intentExtras.putString("SHOW_TYPE", "tv");

                Intent intent = new Intent(getActivity(), ShowDetailsActivity.class);
                intent.putExtras(intentExtras);

                startActivity(intent);
            }
        });

        gridView.setOnScrollListener(new InfiniteScrollListener() {
            @Override
            public boolean onLoadMore(int page, int totalItemsCount) {

                //FetchMoviesPassedParam params = new FetchMoviesPassedParam("popularity.desc", page, 2014);
                filterBundle.putInt("PAGE_PARAM", page);
                if(movieIsShown){
                    fetchDiscoverMovies = new FetchDiscoverMovies(imageAdapter);
                    fetchDiscoverMovies.execute(filterBundle);
                }
                if(tvIsShown){
                    fetchDiscoverTv = new FetchDiscoverTv(imageAdapter);
                    fetchDiscoverTv.execute(filterBundle);
                }
                return true;
            }
        });
        return rootView;
    }


    public void showPopularMovies() {
        imageAdapter.clear();
        fetchDiscoverMovies = new FetchDiscoverMovies(imageAdapter);
        filterBundle.putString("SORTING_PARAM", "popularity.desc");
        fetchDiscoverMovies.execute(filterBundle);
    }

    public void showMostRatedMovies() {
        imageAdapter.clear();
        fetchDiscoverMovies = new FetchDiscoverMovies(imageAdapter);
        filterBundle.putString("SORTING_PARAM", "vote_average.desc");
        fetchDiscoverMovies.execute(filterBundle);
    }

    public void showPopularTv() {
        imageAdapter.clear();
        fetchDiscoverTv = new FetchDiscoverTv(imageAdapter);
        filterBundle.putString("SORTING_PARAM", "popularity.desc");
        fetchDiscoverTv.execute(filterBundle);
    }

    public void showMostRatedTv(){
        imageAdapter.clear();
        fetchDiscoverTv = new FetchDiscoverTv(imageAdapter);
        filterBundle.putString("SORTING_PARAM", "vote_average.desc");
        fetchDiscoverTv.execute(filterBundle);
    }


}

