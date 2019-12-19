package com.omidio.shimmeradapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;

import androidx.annotation.LayoutRes;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;

/**
 * A custom adapter that shows a shimmer placeholder list items
 * until data is loaded. Once data is loaded, showShimmer should be called
 * which will notify the adapter the data set has changed.
 * <p>
 * Subclasses can provide their own custom shimmer animation, use the default,
 * or pass in a Shimmer configuration to use the Facebook ShimmerFrameLayout
 * <p>
 * NOTE: Subclasses are responsible for not overwriting the view type SHIMMER_VIEW_TYPE.
 */
public abstract class ShimmerAdapter extends RecyclerView.Adapter {

    public static final int SHIMMER_VIEW_TYPE = 0;

    private final float FROM_ALPHA = 1.0f;
    private final float TO_ALPHA = 0.3f;
    private final long SHIMMER_DURATION_MILLIS = 1000L;

    private boolean useShimmerConfig = false;
    private boolean showShimmer = true;
    private Animation shimmerAnimation;
    private Shimmer shimmerConfig = null;

    public ShimmerAdapter() {
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        LayoutInflater inflater = LayoutInflater.from(parent.getContext());

        if (viewType == SHIMMER_VIEW_TYPE) {

            if (useShimmerConfig) {

                ShimmerFrameLayout shimmerFrameLayout = (ShimmerFrameLayout) inflater
                        .inflate(R.layout.shimmer_frame_layout, parent, false);

                View view = inflater.inflate(getShimmerLayoutId(), shimmerFrameLayout, false);

                if (view.getLayoutParams() != null) {
                    shimmerFrameLayout.setLayoutParams(view.getLayoutParams());
                }

                shimmerFrameLayout.addView(view);

                if (this.shimmerConfig != null) {
                    shimmerFrameLayout.setShimmer(shimmerConfig);
                }

                return new ShimmerViewHolder(shimmerFrameLayout);

            } else {
                return new ShimmerViewHolder(inflater.inflate(getShimmerLayoutId(), parent, false));
            }
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
        if (holder.getItemViewType() == SHIMMER_VIEW_TYPE && !useShimmerConfig) {
            ((ShimmerViewHolder) holder).startShimmer(getShimmerAnimation());
        }
    }

    @Override
    public void onViewDetachedFromWindow(@NonNull RecyclerView.ViewHolder holder) {
        if (holder.getItemViewType() == SHIMMER_VIEW_TYPE && !useShimmerConfig) {
            ((ShimmerViewHolder) holder).stopShimmer();
        }
    }

    private Animation getShimmerAnimation() {
        if (shimmerAnimation == null) {
            shimmerAnimation = new AlphaAnimation(FROM_ALPHA, TO_ALPHA);
            shimmerAnimation.setDuration(SHIMMER_DURATION_MILLIS);
            shimmerAnimation.setInterpolator(new AccelerateDecelerateInterpolator());
            shimmerAnimation.setRepeatCount(Animation.INFINITE);
            shimmerAnimation.setRepeatMode(Animation.REVERSE);
        }
        return shimmerAnimation;
    }

    /**
     * Changes the shimmer mode and notifies the adapter
     */
    public void showShimmer(boolean showShimmer) {
        this.showShimmer = showShimmer;
        notifyDataSetChanged();
    }

    /**
     * A custom animation can be passed in to be used for the shimmer animation.
     */
    public void useCustomShimmerAnimation(Animation animation) {
        this.shimmerAnimation = animation;
        this.useShimmerConfig = false;
    }

    /**
     * If this setting is called, ShimmerFrameLayout will be used instead of the default
     * Animation. A Shimmer config can be passed in to be set on the
     * ShimmerFrameLayout. Passing null will use the default.
     */
    public void useShimmerConfig(@Nullable Shimmer shimmer) {
        this.useShimmerConfig = true;
        this.shimmerConfig = shimmer;
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
