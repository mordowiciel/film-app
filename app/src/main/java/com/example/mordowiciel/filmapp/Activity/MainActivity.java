package com.example.mordowiciel.filmapp.Activity;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.support.design.widget.NavigationView;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.example.mordowiciel.filmapp.Fetch.FetchGenres;
import com.example.mordowiciel.filmapp.Fragment.FilterFragment;
import com.example.mordowiciel.filmapp.Fragment.MainFragment;
import com.example.mordowiciel.filmapp.R;
import com.example.mordowiciel.filmapp.Fragment.SortbyFragment;

public class MainActivity extends AppCompatActivity
        implements SortbyFragment.NoticeSortingDialogFragment,
        NavigationView.OnNavigationItemSelectedListener {


    private ActionBarDrawerToggle drawerToggle;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_drawer);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container_main, new MainFragment())
                    .commit();
        }

        // Perform first-time run caching.
        SharedPreferences preferences = this.getSharedPreferences(getString(R.string.shared_prefs), Context.MODE_PRIVATE);
        boolean isFirstRun = preferences.getBoolean("firstRun", true);
        Log.e("isFirstRun:", String.valueOf(isFirstRun));

        if (isFirstRun) {
            FetchGenres fetchGenres = new FetchGenres(this);
            fetchGenres.execute();

            SharedPreferences.Editor editor = preferences.edit();
            editor.putBoolean("firstRun", false);
            editor.commit();
        }


        // Create a toolbar for activity.
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // Set up drawer functions.
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.left_drawer);
        navigationView.setNavigationItemSelectedListener(this);

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

        // Create home button.
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawerToggle.setDrawerIndicatorEnabled(true);
        drawerLayout.addDrawerListener(drawerToggle);

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
    public boolean onNavigationItemSelected(MenuItem item) {

        MainFragment mainFragment = (MainFragment) getSupportFragmentManager()
                .findFragmentById(R.id.container_main);

        switch (item.getItemId()) {

            case R.id.nav_movie:
                mainFragment.movieIsShown = true;
                mainFragment.tvIsShown = false;
                mainFragment.showPopularMovies();
                getSupportActionBar().setTitle("Movies");
                break;

            case R.id.nav_tv:
                mainFragment.movieIsShown = false;
                mainFragment.tvIsShown = true;
                mainFragment.showPopularTv();
                getSupportActionBar().setTitle("TV");
                break;

            default:
                return super.onOptionsItemSelected(item);
        }

        drawerLayout.closeDrawer(navigationView);
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        // Click on navigation drawer icon.
        if (drawerToggle.onOptionsItemSelected(item)) {
            return true;
        }

        switch (item.getItemId()) {

            case R.id.action_settings:
                return true;

            case R.id.action_sort:
                showSortingDialog();
                return true;

            case R.id.action_filter:
                showFilteringFragment();
                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /*

        /////// *** DIALOG OPTIONS *** ///////

    */

    public void showSortingDialog() {

        DialogFragment sortingFragment = new SortbyFragment();
        sortingFragment.show(getSupportFragmentManager(), "sorting");
    }

    public void showFilteringFragment() {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fragmentTransaction.setCustomAnimations(R.anim.enter, R.anim.exit,
                R.anim.pop_enter, R.anim.pop_exit);
        FilterFragment filterFragment = new FilterFragment();
        fragmentTransaction.replace(R.id.container_main, filterFragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();

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

}
