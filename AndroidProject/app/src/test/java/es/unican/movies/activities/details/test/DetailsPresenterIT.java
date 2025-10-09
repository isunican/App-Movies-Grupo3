package es.unican.movies.activities.details.test;

import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import es.unican.movies.model.Movie;
import es.unican.movies.presenter.DetailsPresenter;
import es.unican.movies.view.IDetailsView;
import es.unican.movies.service.IMoviesRepository;
import java.util.HashMap;
import java.util.Map;

public class DetailsPresenterIT {

    private IDetailsViewSpy view;
    private IMoviesRepository repository;
    private DetailsPresenter presenter;

    @Before
    public void setUp() {
        view = new IDetailsViewSpy();
        repository = new InMemoryMoviesRepository();
        presenter = new DetailsPresenter(view, repository);
    }

    @Test
    public void testMovieDetailsIntegration() {
        // Simula la conversión del JSON a Movie
        Movie movie = new Movie(
                "Los Cuatro Fantásticos: Primeros pasos",
                2025,
                115,
                "Ciencia ficción, Aventura",
                "/ckfiXWGEMWrUP53cc6QyHijLlhl.jpg",
                7.176,
                null // Puedes calcular la puntuación sumaria si tu lógica lo requiere
        );
        repository.addMovie(617126, movie);

        presenter.onMovieSelected(617126);

        assertEquals("Los Cuatro Fantásticos: Primeros pasos", view.title);
        assertEquals("2025", view.year);
        assertEquals("115", view.duration);
        assertEquals("Ciencia ficción, Aventura", view.genres);
        assertEquals("/ckfiXWGEMWrUP53cc6QyHijLlhl.jpg", view.poster);
        assertEquals("7.2", view.averageScore); // Redondea según tu lógica
        assertEquals("-", view.aggregateScore); // Si no hay sumaria, espera guion
    }

    // Repositorio en memoria
    static class InMemoryMoviesRepository implements IMoviesRepository {
        private Map<Integer, Movie> movies = new HashMap<>();
        public void addMovie(int id, Movie movie) { movies.put(id, movie); }
        @Override
        public Movie getMovieById(int id) { return movies.get(id); }
    }

    // Vista espía
    static class IDetailsViewSpy implements IDetailsView {
        String title, year, duration, genres, poster, averageScore, aggregateScore;
        public void showTitle(String t) { title = t; }
        public void showYear(String y) { year = y; }
        public void showDuration(String d) { duration = d; }
        public void showGenres(String g) { genres = g; }
        public void showPoster(String p) { poster = p; }
        public void showAverageScore(String s) { averageScore = s; }
        public void showAggregateScore(String s) { aggregateScore = s; }
    }
}
