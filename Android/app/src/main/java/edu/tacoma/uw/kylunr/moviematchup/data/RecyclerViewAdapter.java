package edu.tacoma.uw.kylunr.moviematchup.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import edu.tacoma.uw.kylunr.moviematchup.R;

/**
 * This class provides an adapter for our RecyclerView.
 * Sets items using the RecyclerViewItem class.
 */
public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<RecyclerViewItem> list;    // List of items in recyclerview
    private Context context;

    public RecyclerViewAdapter(List<RecyclerViewItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    /**
     * Creates the View Holder for the RecyclerView
     * @param parent
     * @param viewType
     * @return
     */
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recommendations_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    /**
     * Sets the values of the RecyclerView
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecyclerViewItem currentItem = list.get(position);

        holder.number.setText(currentItem.getNumber());
        holder.title.setText(currentItem.getTitle());
        PosterDownloader posterDownloader = new PosterDownloader(holder.poster, context);
        posterDownloader.execute(currentItem.getPoster());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    /**
     * Provides a ViewHolder for our RecyclerView that includes
     * the views including in each item
     */
    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView number;
        public TextView title;
        public ImageView poster;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            this.number = itemView.findViewById(R.id.number);
            this.title = itemView.findViewById(R.id.title);
            this.poster = itemView.findViewById(R.id.poster);
        }
    }
}
