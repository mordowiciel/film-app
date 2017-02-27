package com.example.mordowiciel.filmapp;

import android.content.res.Configuration;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
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

    private ActionBarDrawerToggle drawerToggle;
    private ListView drawerList;
    private DrawerLayout drawerLayout;

    private class DrawerItemClickListener implements ListView.OnItemClickListener {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            viewShowsThumbnails(position);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_main, new MainFragment())
                    .commit();
        }

        // Create a toolbar for activity.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);


        // Set up the navigation drawer.
        ArrayList<NavDrawerView> navDrawerViews = new ArrayList<>();
        navDrawerTextContent = getResources().getStringArray(R.array.navigation_drawer_text_content);

        for (int i = 0; i < navDrawerTextContent.length; i++)
            navDrawerViews.add(new NavDrawerView(navDrawerTextContent[i], navDrawerImageContent[i]));

        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawerList = (ListView) findViewById(R.id.left_drawer);

        // Set the adapter for list view.
        NavDrawerAdapter adapter = new NavDrawerAdapter(this, R.layout.navigation_drawer_item,
                navDrawerViews);
        drawerList.setAdapter(adapter);
        drawerList.setOnItemClickListener(new DrawerItemClickListener());

        // Set up drawer functions.
        drawerToggle = new ActionBarDrawerToggle(this, drawerLayout,
                R.string.drawer_opened, R.string.drawer_closed) {

            public void onDrawerClosed(View view) {
                super.onDrawerClosed(view);
                invalidateOptionsMenu();
            }

            public void onDrawerOpened(View view) {
                super.onDrawerOpened(view);
                invalidateOptionsMenu();
            }
        };
        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);

        // Create home button.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

    }

    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        // Sync the toggle state after onRestoreInstanceState has occurred.
        drawerToggle.syncState();
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        drawerToggle.onConfigurationChanged(newConfig);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Click on navigation drawer icon.
        int id;
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

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

    /// FRAGMENT OPTIONS ///

    public void showSortingDialog() {

        DialogFragment sortingFragment = new SortbyFragment();
        sortingFragment.show(getSupportFragmentManager(), "sorting");
    }

    @Override
    public void onDialogPopularityClick(DialogFragment dialogFragment) {

        MainFragment mainFragment = (MainFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container_main);

        if (mainFragment.movieIsShown) {
            mainFragment.showPopularMovies();
            mainFragment.tvIsShown = false;
        }

        if (mainFragment.tvIsShown) {
            mainFragment.showPopularTv();
            mainFragment.movieIsShown = false;
        }
    }

    public void onDialogRatingClick(DialogFragment dialogFragment) {

        MainFragment mainFragment = (MainFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container_main);

        if (mainFragment.movieIsShown) {
            mainFragment.showMostRatedMovies();
        }

        if (mainFragment.tvIsShown) {
            mainFragment.showMostRatedTv();
        }
    }

    public void viewShowsThumbnails(int position) {

        MainFragment mainFragment = (MainFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container_main);

        switch (position) {
            case 0:
                mainFragment.movieIsShown = true;
                mainFragment.tvIsShown = false;
                mainFragment.showPopularMovies();
                getSupportActionBar().setTitle("Movies");
                break;
            case 1:
                mainFragment.movieIsShown = false;
                mainFragment.tvIsShown = true;
                mainFragment.showPopularTv();
                getSupportActionBar().setTitle("TV");
                break;
            default:
        }

        drawerList.setItemChecked(position, true);
        drawerLayout.closeDrawer(drawerList);
    }

}
