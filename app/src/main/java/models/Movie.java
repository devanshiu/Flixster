package models;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.parceler.Parcel;

import java.util.ArrayList;
import java.util.List;

// Indicates that the class is parcelable
@Parcel
public class Movie {
    // Global variables
    String posterPath;
    String title;
    String overview;
    String backdropPath;
    public Double voteAverage;
    public String releaseDate;
    public int id;
    public Double popularity;

    // No-arg constructor to initialize Movie object; Necessary for the parceler.
    public Movie() {}

    // Constructor to parse JSON objects
    public Movie(JSONObject jsonObject) throws JSONException {
        posterPath = jsonObject.getString("poster_path");
        backdropPath = jsonObject.getString("backdrop_path");
        title = jsonObject.getString("title");
        overview = jsonObject.getString("overview");
        voteAverage = jsonObject.getDouble("vote_average");
        id = jsonObject.getInt("id");
        releaseDate = jsonObject.getString("release_date");
        popularity = jsonObject.getDouble("popularity");
    }

    // Returns a list of movie objects
    public static List<Movie> fromJsonArray(JSONArray movieJsonArray) throws JSONException {
        List<Movie> movies = new ArrayList<>();
        for (int i = 0; i < movieJsonArray.length(); i++) {
            movies.add(new Movie(movieJsonArray.getJSONObject(i)));
        }
        return movies;
    }

    // In this case, for portrait orientation
    public String getPosterPath() {
        // API configuration for movie poster and appending relative paths
        return String.format ("https://image.tmdb.org/t/p/w342/%s",posterPath);
    }

    // In this case, for landscape orientation
    public String getBackdropPath() {
        // API configuration for movie poster and appending relative paths
        return String.format ("https://image.tmdb.org/t/p/w342/%s",backdropPath);
    }

    // Movie title getter method
    public java.lang.String getTitle() {
        return title;
    }

    // Movie synopsis getter method
    public String getOverview() {
        return overview;
    }

    // Movie popularity getter method
    public Double getPopularity() {
        return popularity;
    }

    // Movie release date getter method
    public String getReleaseDate() {
        return releaseDate;
    }

    // Movie vote average getter method
    public Double getVoteAverage() {
        return voteAverage;
    }

    // Movie ID getter method (for Movie Trailer)
    public int getId() {
        return id;
    }
}
