package adapters;

import android.content.Context;
import android.content.Intent;
import android.content.res.Configuration;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.flixster.MovieDetailsActivity;
import com.example.flixster.MovieTrailerActivity;
import com.example.flixster.R;

import org.parceler.Parcels;

import java.util.List;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import models.Movie;

// Objects for displaying ViewAdapter
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.ViewHolder> {
    Context context;
    List<Movie> movies;

    // Constructor for passing in required objects
    public MovieAdapter(Context context, List<Movie> movies) {
        this.context = context;
        this.movies = movies;
    }

    // Inflates a layout from XML and return to the holder
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Log.d("MovieAdapter", "onCreateViewHolder");
        View movieView = LayoutInflater.from(context).inflate(R.layout.item_movie, parent, false);
        return new ViewHolder(movieView);
    }

    // Populates data into item through holder
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Log.d("MovieAdapter", "onBindViewHolder" + position);
        // Get movie at the position where it is passed in
        Movie movie = movies.get(position);
        // Bind the movie data into the VH
        holder.bind(movie);
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return movies.size();
    }

    // ViewHolder is necessary when implementing an adapter
    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvTitle;
        TextView tvOverview;
        ImageView ivPoster;

        RelativeLayout content;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            // referencing items from the item_movie.xml file
            tvTitle = itemView.findViewById(R.id.tvTitle);
            tvOverview = itemView.findViewById(R.id.tvOverview);
            ivPoster = itemView.findViewById(R.id.ivPoster);
            content = itemView.findViewById(R.id.content);
        }

        // bind method will be used to populate each view
        public void bind(final Movie movie) {
            tvTitle.setText(movie.getTitle());
            tvOverview.setText(movie.getOverview());

            // we use a variable because portrait and landscape have different urls
            String imageUrl;
            // if phone is in landscape
            if (context.getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE) {
                // then imageUrl = backdrop image
                imageUrl = movie.getBackdropPath();
                // else imageUrl = poster image
            } else {
                imageUrl = movie.getPosterPath();
            }

            int radius = 30; // corner radius, higher value = more rounded
            int margin = 0; // crop margin, set to 0 for corners with no crop

            // We use the Glide library to render remote images
            Glide.with(context)
                    .load(movie.getPosterPath())
                    // Rounded edges feature
                    .bitmapTransform(new RoundedCornersTransformation(context, radius, margin))
                    .into(ivPoster);

            // Listens for the user's click
            content.setOnClickListener(new View.OnClickListener() {
                // When a row is clicked, the user is shown the corresponding movie's details (MovieDetailsActivity)
                @Override
                public void onClick(View v) {
                    // Intent created for new activity
                    Intent intent = new Intent(context, MovieDetailsActivity.class);
                    // A parceler is used to serialize the movie
                    intent.putExtra("movie", Parcels.wrap(movie));
                    // Show the activity
                    context.startActivity(intent);
                }
            });

            }
    }
}
