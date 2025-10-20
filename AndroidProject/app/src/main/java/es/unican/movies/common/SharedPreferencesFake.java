package es.unican.movies.common;

import es.unican.movies.model.Movie;

public class SharedPreferencesFake implements ISharedPreferences {

    @Override
    public boolean movieIsPending(int movieId) {
        return false;
    }

    @Override
    public boolean savePendingMovie(Movie movie) {
        return false;
    }
}
