package es.unican.movies.activities.details.test;

import static org.mockito.Mockito.*;
import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

import es.unican.movies.activities.details.DetailsPresenter;
import es.unican.movies.view.IDetailsView;
import es.unican.movies.model.Movie;
import es.unican.movies.service.IMoviesRepository;

public class DetailsViewTest {

    private IDetailsView view;
    private IMoviesRepository repository;
    private DetailsPresenter presenter;

    @Before
    public void setUp() {
        view = mock(IDetailsView.class);
        repository = mock(IMoviesRepository.class);
        presenter = new DetailsPresenter(view, repository);
    }

    @Test
    public void testSuccessCase() {
        Movie movie = buildAnomalousMovie();
        when(repository.getMovieById(anyInt())).thenReturn(movie);

        presenter.onMovieSelected(1);

        verify(view).showTitle("Prisionero de guerra");
        verify(view).showYear("2025");
        verify(view).showDuration("113");
        verify(view).showGenres("Ciencia ficción, Suspense");
        verify(view).showPoster("/1XET89sjRm9mUuHXhGIlKTNd5uD.jpg");
        verify(view).showAverageScore("7.3");
        verify(view).showAggregateScore("8.0");
    }

    @Test
    public void testMissingData() {
        Movie movie = new Movie(
                null,    // título ausente
                0,       // año ausente
                0,       // duración ausente
                null,    // géneros ausentes
                null,    // poster ausente
                null,    // puntuación media ausente
                null     // puntuación sumaria ausente
        );
        when(repository.getMovieById(anyInt())).thenReturn(movie);

        presenter.onMovieSelected(2);

        verify(view).showTitle("-");
        verify(view).showYear("-");
        verify(view).showDuration("-");
        verify(view).showGenres("-");
        verify(view).showPoster("-");
        verify(view).showAverageScore("-");
        verify(view).showAggregateScore("-");
    }

    @Test
    public void testAnomalousData() {
        Movie movie = buildMovie();
        when(repository.getMovieById(anyInt())).thenReturn(movie);

        presenter.onMovieSelected(3);

        verify(view).showTitle("Prisionero de guerra");
        verify(view).showYear("-");
        verify(view).showDuration("-");
        verify(view).showGenres("Ciencia ficción, Suspense");
        verify(view).showPoster("/1XET89sjRm9mUuHXhGIlKTNd5uD.jpg");
        verify(view).showAverageScore("-");
        verify(view).showAggregateScore("8.0");
    }

    private Movie buildMovie() {
        return new Movie(
                "Prisionero de guerra",
                2025,
                113,
                "Ciencia ficción, Suspense",
                "/1XET89sjRm9mUuHXhGIlKTNd5uD.jpg",
                7.319,
                8.0
        );
    }

    private Movie buildAnomalousMovie() {
        return new Movie(
                "Prisionero de guerra",
                -1,
                -10,
                "Ciencia ficción, Suspense",
                "/1XET89sjRm9mUuHXhGIlKTNd5uD.jpg",
                -5.0,
                8.0
        );
    }

    @Test
    public void testCalculateAggregateScore_Success() {
        double voteAverage = 7.176;
        int voteCount = 1687;
        double normalizedCount = Math.log10(voteCount);
        double expected = (voteAverage + normalizedCount) / 2;
        String result = DetailsPresenter.calcularPuntuacionSumaria(voteAverage, voteCount);
        assertEquals(String.format("%.2f", expected), result);
    }

    @Test
    public void testCalculateAggregateScore_InvalidValue() {
        double voteAverage = -3;
        int voteCount = 120;
        String result = DetailsPresenter.calcularPuntuacionSumaria(voteAverage, voteCount);
        assertEquals("-", result);
    }

    @Test
    public void testCalculateAggregateScore_MissingValue() {
        Double voteAverage = null;
        int voteCount = 1687;
        String result = DetailsPresenter.calcularPuntuacionSumaria(voteAverage, voteCount);
        assertEquals("-", result);
    }
}
