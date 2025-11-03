package es.unican.movies.common;

import es.unican.movies.model.Movie;

/**
 * Interfaz para operaciones de preferencia compartida relacionadas con
 * películas (marcadas como pendientes o como favoritas).
 */
public interface ISharedPreferences {

    /**
     * Comprueba si una película está marcada como pendiente.
     *
     * @param movieId identificador único de la película a comprobar
     * @return {@code true} si la película está marcada como pendiente,
     *         {@code false} en caso contrario o si no existe
     */
    public boolean movieIsPending(int movieId);

    /**
     * Guarda una película como pendiente en las preferencias.
     * Si la película ya estaba marcada, puede sobrescribir su estado.
     *
     * @param movie objeto Movie a marcar como pendiente (no debe ser {@code null})
     * @return {@code true} si el guardado se realizó con éxito,
     *         {@code false} si hubo un error o el parámetro fue inválido
     */
    public boolean savePendingMovie(Movie movie);

    /**
     * Comprueba si una película está marcada como favorita.
     *
     * @param movieId identificador único de la película a comprobar
     * @return {@code true} si la película está marcada como favorita,
     *         {@code false} en caso contrario o si no existe
     */
    public boolean movieIsFavourite(int movieId);

    /**
     * Guarda una película como favorita en las preferencias.
     * Si la película ya estaba marcada, puede sobrescribir su estado.
     *
     * @param movie objeto Movie a marcar como favorita (no debe ser {@code null})
     * @return {@code true} si el guardado se realizó con éxito,
     *         {@code false} si hubo un error o el parámetro fue inválido
     */
    public boolean saveFavouriteMovie(Movie movie);

    public boolean removePendingMovie(Movie movie);

}
