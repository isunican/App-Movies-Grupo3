package es.unican.movies.activities.main;

import static org.junit.Assert.assertEquals;
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
}
