package es.unican.movies.utils;

import es.unican.movies.common.ISharedPreferences;
import es.unican.movies.model.Movie;

public class SharedPreferencesFakeAdd implements ISharedPreferences {

    @Override
    public boolean movieIsPending(int movieId) {
        return false;
    }

    @Override
    public boolean savePendingMovie(Movie movie) {
        return false;
    }

    @Override
    public boolean movieIsFavourite(int movieId) {
        return false;
    }

    @Override
    public boolean saveFavouriteMovie(Movie movie) {
        return false;
    }

    @Override
    public boolean removePendingMovie(Movie movie) {
        return false;
    }

    @Override
    public boolean removeFavouriteMovie(Movie movie) {
        return false;
    }
}