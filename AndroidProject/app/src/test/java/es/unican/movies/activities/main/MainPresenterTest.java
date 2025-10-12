package es.unican.movies.activities.main;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;

import es.unican.movies.model.Movie;
import es.unican.movies.service.IMoviesRepository;

public class MainPresenterTest {

    private IMainContract.View viewMock;
    private IMoviesRepository repositoryMock;
    private MainPresenter presenter;

    @Before
    public void setUp() {
        viewMock = mock(IMainContract.View.class);
        repositoryMock = mock(IMoviesRepository.class);
        when(viewMock.getMoviesRepository()).thenReturn(repositoryMock);
        presenter = new MainPresenter(repositoryMock);
        presenter.init(viewMock);
    }

    @Test
    public void givenValidMovie_whenOnItemClicked_thenShowMovieDetails() {
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setRuntime(148);
        movie.setVoteAverage(8.8);
        movie.setVoteCount(1000);

        presenter.onItemClicked(movie);

        verify(viewMock).showMovieDetails(movie);
    }

    @Test
    public void givenNullMovie_whenOnItemClicked_thenDoNothing() {
        presenter.onItemClicked(null);

        verify(viewMock, never()).showMovieDetails(any());
    }

    @Test
    public void givenPresenter_whenOnMenuInfoClicked_thenShowInfoActivity() {
        presenter.onMenuInfoClicked();

        verify(viewMock).showInfoActivity();
    }

    @Test
    public void givenValidMovie_whenShowMovieDetail_thenShowAllFieldsAndSummarizedAverage() {
        // Arrange
        Movie movie = new Movie();
        movie.setTitle("Inception");
        movie.setRuntime(148);
        movie.setVoteAverage(8.8);
        movie.setVoteCount(1000);

        // Act
        presenter.onItemClicked(movie);

        // Assert
        verify(viewMock).showMovieDetails(movie);
        assertEquals( (8.8 + 2 * Math.log10(1 + 1000)) / 2, movie.getSummarizedAverage(), 0.01 );
    }

    @Test
    public void givenMovieWithMissingData_whenShowMovieDetail_thenShowDashesAndNaN() {
        Movie movie = new Movie();
        movie.setTitle(null);
        movie.setRuntime(120);
        movie.setVoteAverage(Double.NaN);
        movie.setVoteCount(8);
        MainPresenter presenter = new MainPresenter(repositoryMock);
        presenter.init(viewMock);

        presenter.onItemClicked(movie);

        verify(viewMock).showMovieDetails(movie);
        assertEquals(null, movie.getTitle());
        assertEquals("-", movie.getGenresList());
        assertTrue(Double.isNaN(movie.getSummarizedAverage()));
    }

    @Test
    public void givenMovieWithAnomalousData_whenShowMovieDetail_thenShowDashesAndNaN() {
        Movie movie = new Movie();
        movie.setTitle("Fake Movie");
        movie.setRuntime(120);
        movie.setVoteAverage(-5.0);
        movie.setVoteCount(7);
        MainPresenter presenter = new MainPresenter(repositoryMock);
        presenter.init(viewMock);

        presenter.onItemClicked(movie);

        verify(viewMock).showMovieDetails(movie);
        assertTrue(Double.isNaN(movie.getSummarizedAverage()));
    }

    @Test
    public void givenValidValues_whenGetSummarizedAverage_thenReturnsExpectedResult() {
        Movie movie = new Movie();
        movie.setVoteAverage(7.176);
        movie.setVoteCount(1687);
        double resultado = movie.getSummarizedAverage();
        // normalized_count = 6.454 → promedio = 6.815 → 6.82
        assertEquals(6.82, resultado, 0.01);
    }

    @Test
    public void givenNegativeValue_whenGetSummarizedAverage_thenReturnsNaN() {
        Movie movie = new Movie();
        movie.setVoteAverage(-3.0);
        movie.setVoteCount(120);
        double resultado = movie.getSummarizedAverage();
        // dato inválido → se considera inexistente
        assertTrue(Double.isNaN(resultado));
    }

    @Test
    public void givenMissingValue_whenGetSummarizedAverage_thenReturnsNull() {
        Movie movie = new Movie();
        movie.setVoteAverage(6.2);
        movie.setVoteCount(null);
        Double resultado = movie.getSummarizedAverage();
        // valor faltante → no existe puntuación sumaria
        assertNull(resultado);
    }

    @Test
    public void givenOnlyOneValidComponent_whenGetSummarizedAverage_thenReturnsNaN() {
        Movie movie = new Movie();
        movie.setVoteAverage(6.5);
        movie.setVoteCount(-10); // valor inválido
        double resultado = movie.getSummarizedAverage();
        // Se ignora el inválido → retorna NaN
        assertTrue(Double.isNaN(resultado));
    }
}
