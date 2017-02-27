package com.example.mordowiciel.filmapp;

import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.Arrays;

public class MainActivity extends AppCompatActivity
        implements SortbyFragment.NoticeSortingDialogFragment {

    private String[] navDrawerTextContent;
    private int[] navDrawerImageContent = {
        R.drawable.ic_popular,
        R.drawable.ic_rating
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_main, new MainFragment())
                    .commit();
        }

        // Create a toolbar for activity.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up the navigation drawer.
        ArrayList<NavDrawerView>navDrawerViews = new ArrayList<>();
        navDrawerTextContent = getResources().getStringArray(R.array.navigation_drawer_text_content);

        for (int i = 0; i < navDrawerTextContent.length; i++)
            navDrawerViews.add(new NavDrawerView(navDrawerTextContent[i], navDrawerImageContent[i]));

        DrawerLayout drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        ListView drawerList = (ListView) findViewById(R.id.left_drawer);

        //Set the adapter for list view.
//        drawerList.setAdapter(new ArrayAdapter<String>(this, R.layout.navigation_drawer_item,
//                R.id.navigation_drawer_textview, navDrawerContent));

        NavDrawerAdapter adapter = new NavDrawerAdapter(this, R.layout.navigation_drawer_item,
                navDrawerViews);
        drawerList.setAdapter(adapter);


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
