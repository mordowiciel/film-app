package com.example.mordowiciel.filmapp;

import android.widget.AbsListView;

public abstract class InfiniteScrollListener implements AbsListView.OnScrollListener {

    // Minimum number of items to have below current scroll position to load more items.
    private int visibleThreshold = 4;

    // The total number of items in the dataset after last load.
    private int previousTotalItemCount = 0;

    // Set the starting page index.
    private int startingPageIndex = 1;

    // The offset number of current page of data received from DB.
    private int currentPage = 0;

    // True - waiting for the set of data to load.
    private boolean loading = true;

    public InfiniteScrollListener(){

    }

    public InfiniteScrollListener(int visibleThreshold) {
        this.visibleThreshold = visibleThreshold;
    }

    public InfiniteScrollListener(int visibleThreshold, int startingPageIndex) {
        this.visibleThreshold = visibleThreshold;
        this.startingPageIndex = startingPageIndex;
        this.currentPage = startingPageIndex;
    }

    // Method to load more data from DB.
    public abstract boolean onLoadMore(int page, int totalItemsCount);

    @Override
    public void onScroll (AbsListView view, int firstVisibleItem, int visibleItemCount,
                          int totalItemCount) {

        // If total item count is 0 - list of items is invalidated, reset to initial state
        if (totalItemCount < previousTotalItemCount){
            this.currentPage = this.startingPageIndex;
            this.previousTotalItemCount = totalItemCount;
            if (totalItemCount == 0)
                this.loading = true;
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
            onLoadMore(currentPage++,totalItemCount);
        }
    }

    @Override
    public void onScrollStateChanged(AbsListView view, int scrollState){
        // No action.
    }

}
