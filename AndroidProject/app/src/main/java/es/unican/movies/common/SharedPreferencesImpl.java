package es.unican.movies.common;

import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import com.google.gson.Gson;

import es.unican.movies.model.Movie;


public class SharedPreferencesImpl implements ISharedPreferences {

    private static final String PREFS_NAME = "PENDING_FILMS";
    private final SharedPreferences prefsPending;
    private final Gson gson = new Gson();

    public SharedPreferencesImpl(Context context) {
        prefsPending = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
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
}
