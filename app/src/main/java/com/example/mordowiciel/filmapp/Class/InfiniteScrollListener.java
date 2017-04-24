package com.example.mordowiciel.filmapp.Class;

import android.widget.AbsListView;

public interface InfiniteScrollListener extends AbsListView.OnScrollListener {

    // Method to load more data from DB.
    boolean onLoadMore(int page, int totalItemsCount);

    void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount);

    void onScrollStateChanged(AbsListView view, int scrollState);

}
