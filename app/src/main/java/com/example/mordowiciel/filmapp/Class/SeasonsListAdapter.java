package com.example.mordowiciel.filmapp.Class;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mordowiciel.filmapp.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;


public class SeasonsListAdapter extends RecyclerView.Adapter<SeasonsListAdapter.SeasonViewHolder> {

    private ArrayList<ShowThumbnail> showThumbnailsList;
    private Context ctx;

    class SeasonViewHolder extends RecyclerView.ViewHolder {

        protected CardView cardView;
        protected ImageView imageView;
        protected TextView textView;

        public SeasonViewHolder(View view) {
            super(view);
            this.cardView = (CardView) view.findViewById(R.id.image_item_cardview);
            this.imageView = (ImageView) view.findViewById(R.id.image_item_imageview);
            this.textView = (TextView) view.findViewById(R.id.image_item_textview);
        }
    }

    public SeasonsListAdapter(Context ctx, ArrayList<ShowThumbnail> showThumbnails) {
        this.showThumbnailsList = showThumbnails;
        this.ctx = ctx;
    }

    @Override
    public SeasonViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.image_item, null);
        SeasonViewHolder seasonViewHolder = new SeasonViewHolder(view);
        return seasonViewHolder;
    }

    @Override
    public void onBindViewHolder (SeasonViewHolder seasonViewHolder, int i) {

        ShowThumbnail showThumbnailItem = showThumbnailsList.get(i);

        if (showThumbnailItem.getShowPosterLink() != null) {
            Picasso.with(ctx).load(showThumbnailItem.getShowPosterLink())
                    .fit()
                    .into(seasonViewHolder.imageView);
        } else {
            seasonViewHolder.imageView.setImageDrawable(ContextCompat.getDrawable(ctx,
                    R.drawable.no_picture_poster));
        }

        seasonViewHolder.textView.setText(showThumbnailItem.getShowTitle());
    }

    @Override
    public int getItemCount() {
        return (null != showThumbnailsList ? showThumbnailsList.size() : 0);
    }

}
