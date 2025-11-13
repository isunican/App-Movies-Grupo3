package es.unican.movies.utils;

import java.util.Collections;
import java.util.List;

import es.unican.movies.common.ISharedPreferences;
import es.unican.movies.model.Movie;

public class SharedPreferencesFakeEliminate implements ISharedPreferences {

    private boolean movieIsPending = true;
    private boolean movieIsFavourite = true;


    @Override
    public boolean movieIsPending(int movieId) {
        return movieIsPending;
    }

    @Override
    public boolean savePendingMovie(Movie movie) {
        movieIsPending = true;
        return true;
    }

    @Override
    public boolean movieIsFavourite(int movieId) {
        return movieIsFavourite;
    }

    @Override
    public boolean saveFavouriteMovie(Movie movie) {
        movieIsFavourite = true;
        return true;
    }

    @Override
    public boolean removePendingMovie(Movie movie) {
        movieIsPending = true;
        return false;
    }

    @Override
    public boolean removeFavouriteMovie(Movie movie) {
        movieIsFavourite = true;
        return false;
    }

    @Override
    public List<Movie> getAllPendingMovies() {
        return Collections.emptyList();
    }

    @Override
    public List<Movie> getAllFavouriteMovies() {
        return Collections.emptyList();
    }
}