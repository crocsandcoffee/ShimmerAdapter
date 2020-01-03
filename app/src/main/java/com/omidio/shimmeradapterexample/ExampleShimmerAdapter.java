package com.omidio.shimmeradapterexample;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.omidio.shimmeradapter.ShimmerAdapter;

public class ExampleShimmerAdapter extends ShimmerAdapter {

    private static final int VIEW_TYPE_ITEM = 1;

    public ExampleShimmerAdapter() {
    }

    @Override
    public void onBindViewHolderCallback(@NonNull RecyclerView.ViewHolder holder, int position) {
        ((ItemViewHolder)holder).setup(position);
    }

    @Override
    public void onBindViewShimmerHolderCallback(@NonNull ShimmerViewHolder holder, int position) {
    }

    @Override
    public int getViewType(int position) {
        return VIEW_TYPE_ITEM;
    }

    @Override
    public RecyclerView.ViewHolder getViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ItemViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_layout, null, false));
    }

    @Override
    public int getShimmerLayoutId() {
        return R.layout.shimmer_layout;
    }

    @Override
    public int getDataCount() {
        return 10;
    }

    @Override
    public int getShimmerItemsCount() {
        return 10;
    }

    public static class ItemViewHolder extends RecyclerView.ViewHolder {

        public ItemViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void setup(int pos) {
            ((TextView)itemView.findViewById(R.id.label)).setText("Item adapter position: " + pos);
            ((TextView)itemView.findViewById(R.id.label2)).setText("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.");
        }
    }
}
