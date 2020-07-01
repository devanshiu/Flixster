package com.example.flixster;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.codepath.asynchttpclient.AsyncHttpClient;
import com.codepath.asynchttpclient.callback.JsonHttpResponseHandler;
import com.google.android.youtube.player.YouTubeBaseActivity;


import org.json.JSONArray;
import org.json.JSONObject;
import  org.json.JSONException;
import org.parceler.Parcels;
import models.Movie;

import jp.wasabeef.glide.transformations.RoundedCornersTransformation;
import okhttp3.Headers;


    public class MovieDetailsActivity extends YouTubeBaseActivity {
    // Pertain to the movie displayed
    public static Movie movie;
    public String Video_URL = "";

    // View objects
    public TextView tvTitle;
    public ImageView ivBackdrop;
    public TextView tvOverview;
    public TextView tvRelease;
    public RatingBar rbVoteAverage;
    public TextView tvPopularity;

    // Used to retrieve video from MovieDB API
    public String videoKey;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        // We use intent to pass the the movie in and unwrap it
        movie = (Movie) Parcels.unwrap(getIntent().getParcelableExtra("movie"));

        // Finds view identified by the ID attribute from activity_movie_details.xml
        ivBackdrop = findViewById(R.id.ivPoster);
        tvTitle = findViewById(R.id.tvTitle);
        tvOverview = findViewById(R.id.tvOverview);
        rbVoteAverage = findViewById(R.id.ratingBar);
        tvRelease = findViewById(R.id.tvReleaseDate);
        tvPopularity = findViewById(R.id.tvPop);

        // Used to retrieve trailer for each movie
        Video_URL = "https://api.themoviedb.org/3/movie/" + movie.id + "/videos?api_key=a07e22bc18f5cb106bfe4cc1f83ad8ed";

        // Set title, overview, release date, and popularity
        tvTitle.setText(movie.getTitle());
        tvOverview.setText(movie.getOverview());
        tvRelease.setText("Release Date: " + movie.getReleaseDate());
        tvPopularity.setText("Popularity: " + movie.getPopularity());

        // Vote average is calculated on 0-10 scale so divide by 2 to convert to a 0-5 scale
        float voteAverage = movie.getVoteAverage().floatValue();
        rbVoteAverage.setRating(voteAverage = voteAverage > 0 ? voteAverage / 2.0f : voteAverage);

        // Higher value indicates more rounded (corner radius)
        int radius = 30;
        // Crop margin; 0 indicates no crop
        int margin = 0;
        // Renders image that the user clicks on to view trailer
        Glide.with(MovieDetailsActivity.this)
                .load(movie.getBackdropPath())
                .bitmapTransform(new RoundedCornersTransformation(getApplicationContext(), radius, margin))
                .into(ivBackdrop);

        // Pass initialization on View.OnClickListener to setOnClickListener
        ivBackdrop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Ensures that key is not null
                if (videoKey == null)
                    return;
                // Intent: abstract description of an operation to be performed (in this case: go from details activity -> trailer activity)
                Intent i = new Intent(MovieDetailsActivity.this, MovieTrailerActivity.class);

                // Pass values and retrieve them in other activity using key
                i.putExtra("key", videoKey);
                startActivity(i);
            }
        });

        AsyncHttpClient client = new AsyncHttpClient();
        client.get(Video_URL, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Headers headers, JSON json) {
                // Get JSON object for data
                JSONObject jsonObject = json.jsonObject;
                try {
                    JSONArray results = jsonObject.getJSONArray("results");
                    // Gets movie info at position zero
                    JSONObject movies = (JSONObject) results.get(0);
                    // For each movie, get value (passed to MovieTrailerActivity)
                    videoKey = movies.getString("key");


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Headers headers, String response, Throwable throwable) {
                Log.e("MovieTrailerActivity", "HTTP request failed");
            }
        });
    }
}