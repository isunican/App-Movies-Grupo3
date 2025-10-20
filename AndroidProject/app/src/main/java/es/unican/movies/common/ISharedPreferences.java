package es.unican.movies.common;

import es.unican.movies.model.Movie;

public interface ISharedPreferences {

    public boolean movieIsPending(int movieId);

    public boolean savePendingMovie(Movie movie);

}
