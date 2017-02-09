package com.example.mordowiciel.filmapp;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity
        implements SortbyFragment.NoticeSortingDialogFragment {


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_main, new MainFragment())
                    .commit();
        }

        //Create a toolbar for activity.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu (Menu menu){
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.action_settings:
                return true;

            case R.id.action_sort:
                showSortingDialog();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showSortingDialog(){

        DialogFragment sortingFragment = new SortbyFragment();
        sortingFragment.show(getSupportFragmentManager(), "sorting");
    }

    // Listeners for options provided in sorting dialog.
    @Override
    public void onDialogPopularityClick(DialogFragment dialogFragment) {

        MainFragment mainFragment = (MainFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container_main);

        mainFragment.showPopularMovies();
    }

    public void onDialogRatingClick(DialogFragment dialogFragment) {

        MainFragment mainFragment = (MainFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container_main);

        mainFragment.showMostRatedMovies();
    }



}
