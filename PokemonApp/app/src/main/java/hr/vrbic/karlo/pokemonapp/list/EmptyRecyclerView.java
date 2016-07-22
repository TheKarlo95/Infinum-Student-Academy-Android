package hr.vrbic.karlo.pokemonapp.list;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.view.View;

/**
 * {@code EmptyRecyclerView} is a {@linkplain RecyclerView} that dispalys given {@linkplain View} when adapter's
 * method {@linkplain Adapter#getItemCount()} returns {@code 0}.
 *
 * @author Karlo Vrbić
 * @version 1.0
 * @see RecyclerView
 */
public class EmptyRecyclerView extends RecyclerView {

    /**
     * View that is display when adapter is empty.
     */
    private View emptyView;

    /**
     * Observer that checks if adapter is empty.
     */
    final private AdapterDataObserver observer = new AdapterDataObserver() {
        @Override
        public void onChanged() {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeInserted(int positionStart, int itemCount) {
            checkIfEmpty();
        }

        @Override
        public void onItemRangeRemoved(int positionStart, int itemCount) {
            checkIfEmpty();
        }
    };

    @SuppressWarnings("javadoc")
    public EmptyRecyclerView(Context context) {
        super(context);
    }

    @SuppressWarnings("javadoc")
    public EmptyRecyclerView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @SuppressWarnings("javadoc")
    public EmptyRecyclerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public void setAdapter(Adapter adapter) {
        final Adapter oldAdapter = getAdapter();
        if (oldAdapter != null) {
            oldAdapter.unregisterAdapterDataObserver(observer);
        }
        super.setAdapter(adapter);
        if (adapter != null) {
            adapter.registerAdapterDataObserver(observer);
        }

        checkIfEmpty();
    }

    /**
     * Sets the empty view that is displayed when adapter is empty.
     *
     * @param emptyView the empty view
     */
    public void setEmptyView(View emptyView) {
        this.emptyView = emptyView;
        checkIfEmpty();
    }

    /**
     * Checks if adapter is empty and sets the right visibility to this {@code EmptyRecyclerView} and {@code emptyView}.
     */
    private void checkIfEmpty() {
        if (emptyView != null && getAdapter() != null) {
            final boolean emptyViewVisible = getAdapter().getItemCount() == 0;
            emptyView.setVisibility(emptyViewVisible ? VISIBLE : GONE);
            setVisibility(emptyViewVisible ? GONE : VISIBLE);
        }
    }

    /**
     * {@code OnClickListener} is a listener class that is used whenever user clicked on {@code EmptyRecyclerView}.
     *
     * @param <T> type of objects contained in adapter
     */
    public interface OnClickListener<T> {

        /**
         * Invoked when item in {@code EmptyRecyclerView} is clicked.
         *
         * @param object the object
         */
        void onClick(T object);

    }

    /**
     * {@code OnChangeListener} is a listener class that is used whenever adapter's data is changed.
     */
    public interface OnChangeListener {

        /**
         * Invoked when some objects are added to {@code source} adapter.
         *
         * @param source the source adapter
         * @param index0 index of the first removed object
         * @param index1 index of the last removed object
         */
        void objectsAdded(RecyclerView.Adapter source, int index0, int index1);

        /**
         * Invoked when some objects are removed from {@code source} adapter.
         *
         * @param source the source adapter
         * @param index0 index of the first removed object
         * @param index1 index of the last removed object
         */
        void objectsRemoved(RecyclerView.Adapter source, int index0, int index1);

    }
}