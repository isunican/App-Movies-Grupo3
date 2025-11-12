package es.unican.movies.common;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import es.unican.movies.model.Movie;


public class SharedPreferencesImpl implements ISharedPreferences {

    private static final String PREFS_PENDING_NAME = "PENDING_FILMS";
    private static final String PREFS_FAVOURITE_NAME = "FAVOURITE_FILMS";
    private final SharedPreferences prefsPending;
    private final SharedPreferences prefsFavourite;
    private final Gson gson = new Gson();

    public SharedPreferencesImpl(Context context) {
        prefsPending = context.getSharedPreferences(PREFS_PENDING_NAME, Context.MODE_PRIVATE);
        prefsFavourite = context.getSharedPreferences(PREFS_FAVOURITE_NAME, Context.MODE_PRIVATE);
    }

    /**
     * Check if a film is marked as pending
     */
    @Override
    public boolean movieIsPending(int movieId) {
        return prefsPending.contains(String.valueOf(movieId));
    }

    /**
     * Guarda una película completa como pendiente.
     * Usa el id como clave y guarda el objeto serializado a JSON.
     * retorna true si la persistencia fue satisfactoria
     */
    @Override
    public boolean savePendingMovie(Movie movie) {
        String json = gson.toJson(movie);
        return prefsPending.edit().putString(String.valueOf(movie.getId()), json).commit();
    }

    /**
     * Check if a film is marked as favorite
     */
    @Override
    public boolean movieIsFavourite(int movieId) {
        return prefsFavourite.contains(String.valueOf(movieId));
    }

    /**
     * Guarda una película completa como favorita.
     * Usa el id como clave y guarda el objeto serializado a JSON.
     * retorna true si la persistencia fue satisfactoria
     */
    @Override
    public boolean saveFavouriteMovie(Movie movie) {
        String json = gson.toJson(movie);
        return prefsFavourite.edit().putString(String.valueOf(movie.getId()), json).commit();
    }

    /**
     * Elimina una película de la lista de favoritos.
     *
     * @return true si se eliminó correctamente, false en caso contrario
     */
    @Override
    public boolean removeFavouriteMovie(Movie movie) {
        return prefsFavourite.edit().remove(String.valueOf(movie.getId())).commit();
    }

    /**
     * Elimina una película marcada como pendiente de las preferencias.
     */
    @Override
    public boolean removePendingMovie(Movie movie) {
        return prefsPending.edit().remove(String.valueOf(movie.getId())).commit();
    }

    @Override
    public List<Movie> getAllPendingMovies() {
        List<Movie> listaPending = new ArrayList<>();
        prefsPending.getAll().forEach((key, value) -> {
            Movie movie = gson.fromJson(value.toString(), Movie.class);
            listaPending.add(movie);
        });

        return listaPending;
    }

    @Override
    public List<Movie> getAllFavouriteMovies() {
        List<Movie> listaFavourites = new ArrayList<>();
        prefsFavourite.getAll().forEach((key, value) -> {
            Movie movie = gson.fromJson(value.toString(), Movie.class);
            listaFavourites.add(movie);
        });

        return listaFavourites;

    }
}
