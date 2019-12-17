package com.omidio.shimmeradapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * A custom adapter that shows a shimmer placeholder list items
 * until data is loaded. Once data is loaded, setShowShimmer should be called
 * which will notify the adapter the data set has changed.
 *
 * Subclasses can override to provide their own shimmer animation.
 *
 */
public abstract class ShimmerAdapter extends RecyclerView.Adapter {

    public static final int SHIMMER_VIEW_TYPE = 0;

    private boolean showShimmer;
    private Animation shimmerAnimation;
    private final float FROM_ALPA = 1.0f;
    private final float TO_ALPHA = 0.3f;
    private final long SHIMMER_DURATION_MILLIS = 1000L;

    public ShimmerAdapter(boolean showShimmerInitially) {
        showShimmer = showShimmerInitially;
        shimmerAnimation = new AlphaAnimation(FROM_ALPA, TO_ALPHA);
        shimmerAnimation.setDuration(SHIMMER_DURATION_MILLIS);
        shimmerAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
        shimmerAnimation.setRepeatCount(Animation.INFINITE);
        shimmerAnimation.setRepeatMode(Animation.REVERSE);
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        if (viewType == SHIMMER_VIEW_TYPE) {
            return new ShimmerViewHolder(inflater.inflate(getShimmerLayoutId(), null, false));
        } else {
            return getViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        if (holder.getItemViewType() != SHIMMER_VIEW_TYPE) {
            onBindViewHolderCallback(holder, position);
        }
    }

    @Override
    public int getItemCount() {
        return showShimmer ? getShimmerItemsCount() : getDataCount();
    }

    @Override
    public int getItemViewType(int position) {
        return showShimmer ? SHIMMER_VIEW_TYPE : getViewType(position);
    }

    @Override
    public void onViewAttachedToWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder.getItemViewType() == SHIMMER_VIEW_TYPE) {
            ((ShimmerViewHolder)holder).startShimmer(getShimmerAnimation());
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder.getItemViewType() == SHIMMER_VIEW_TYPE) {
            ((ShimmerViewHolder)holder).stopShimmer();
        }
    }

    public Animation getShimmerAnimation() {
        return shimmerAnimation;
    }

    public boolean getShowShimmer() {
        return this.showShimmer;
    }

    /**
     * Changes the shimmer mode and notifies the adapter
     */
    public void setShowShimmer(boolean showShimmer) {
        this.showShimmer = showShimmer;
        notifyDataSetChanged();
    }

    /**
     * Called when the holder is not an instance of ShimmerViewHolder.
     * Subclasses that override are responsible for typical business logic in OnBindViewHolder().
     */
    public abstract void onBindViewHolderCallback(@NonNull RecyclerView.ViewHolder holder, int position);

    /**
     * This method is called to retrieve the viewType for an item in the
     * specified position when the viewType is not VIEW_TYPE_SHIMMER
     */
    public abstract int getViewType(int position);

    /**
     * This method is called to retrieve the ViewHolder instance for a viewType that is not VIEW_TYPE_SHIMMER
     */
    public abstract RecyclerView.ViewHolder getViewHolder(@NonNull ViewGroup parent, int viewType);

    /**
     * Return the layout id of the shimmer list item
     */
    public abstract @LayoutRes int getShimmerLayoutId();

    /**
     * Return the number of data items
     */
    public abstract int getDataCount();

    /**
     * Return the number of shimmer items to display
     */
    public abstract int getShimmerItemsCount();

    /**
     * Custom ViewHolder that handles the shimmer animation on each shimmer list item
     */
    public static class ShimmerViewHolder extends RecyclerView.ViewHolder {

        public ShimmerViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        public void startShimmer(Animation shimmerAnimation) {
            itemView.startAnimation(shimmerAnimation);
        }

        public void stopShimmer() {
            itemView.clearAnimation();
        }
    }
}
