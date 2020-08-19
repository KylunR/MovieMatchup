package edu.tacoma.uw.kylunr.moviematchup.data;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import edu.tacoma.uw.kylunr.moviematchup.MatchupActivity;
import edu.tacoma.uw.kylunr.moviematchup.R;

import static android.content.ContentValues.TAG;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder> {

    private List<RecommendationItem> list;
    private Context context;

    public RecyclerViewAdapter(List<RecommendationItem> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.recommendations_item, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        RecommendationItem currentItem = list.get(position);

        holder.number.setText(currentItem.getNumber());
        holder.title.setText(currentItem.getTitle());
        Log.e("E", "Setting Image");
        PosterDownloader posterDownloader = new PosterDownloader(holder.poster, context);
        posterDownloader.execute(currentItem.getPoster());
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

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
