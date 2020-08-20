package edu.tacoma.uw.kylunr.moviematchup.data;

import android.content.Context;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;


/**
 * This class is used to apply an onClickListener to the entire
 * RecycleViewer
 */
public class RecyclerTouchListener implements RecyclerView.OnItemTouchListener {

    private GestureDetector gestureDetector;
    private ClickListener clickListener;

    public RecyclerTouchListener(Context context, final RecyclerView recyclerView, final ClickListener clickListener) {
        this.clickListener = clickListener;
        gestureDetector = new GestureDetector(context, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent e) {
                return true;
            }

            @Override
            public void onLongPress(MotionEvent e) {
                View child = recyclerView.findChildViewUnder(e.getX(), e.getY());
                if (child != null && clickListener != null) {
                    clickListener.onLongClick(child, recyclerView.getChildPosition(child));
                }
            }
        });
    }

    /**
     * Overrides required method for OnItemTouchListener.
     * Finds the element that was touched and calls the onClick method
     *
     * @param rv
     * @param e
     * @return
     */
    @Override
    public boolean onInterceptTouchEvent(RecyclerView rv, MotionEvent e) {

        View child = rv.findChildViewUnder(e.getX(), e.getY());
        if (child != null && clickListener != null && gestureDetector.onTouchEvent(e)) {
            clickListener.onClick(child, rv.getChildPosition(child));
        }
        return false;
    }

    /**
     * Required overriden method for OnItemTouchListener.
     * Nothing is changed.
     * @param rv
     * @param e
     */
    @Override
    public void onTouchEvent(RecyclerView rv, MotionEvent e) {
    }

    /**
     * Required overriden method for OnItemTouchListener.
     * Nothing is changed.
     * @param disallowIntercept
     */
    @Override
    public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

    }

    /**
     * Interface to allow calling class to override
     * what happens when item is clicked
     */
    public interface ClickListener {
        void onClick(View view, int position);
        void onLongClick(View view, int position);
    }
}