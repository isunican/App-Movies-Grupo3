package es.unican.movies.common;

import es.unican.movies.model.Movie;

public interface ISharedPreferences {

    public boolean movieIsPending(int movieId);

    public void savePendingMovie(Movie movie);

}
