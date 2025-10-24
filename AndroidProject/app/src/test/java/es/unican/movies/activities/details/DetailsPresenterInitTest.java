package es.unican.movies.activities.details;

import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import es.unican.movies.common.ISharedPreferences;
import es.unican.movies.model.Movie;

public class DetailsPresenterInitTest {

    private IDetailsContract.Presenter detailsPresenter = new DetailsPresenter();
    @Mock
    private IDetailsContract.View detailsViewMock;
    @Mock
    private ISharedPreferences prefsMock;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Movie: Los Cuatro Fantásticos: Primeros pasos
        Movie movie = new Movie();
        movie.setId(617126);
        movie.setTitle("Los Cuatro Fantásticos: Primeros pasos");

        when(detailsViewMock.getSharedPreferences()).thenReturn(prefsMock);
        when(detailsViewMock.getMovie()).thenReturn(movie);
    }

    @Test
    public void initTest_movieIsPendingAndFavourite() {
        // simulate the movie being pending and favourite
        when(prefsMock.movieIsPending(617126)).thenReturn(true);
        when(prefsMock.movieIsFavourite(617126)).thenReturn(true);

        this.detailsPresenter.init(detailsViewMock);

        verify(detailsViewMock).init();
        verify(detailsViewMock, never()).hidePendingBadge();
        verify(detailsViewMock, never()).hideFavouriteBadge();
    }

    @Test
    public void initTest_movieIsNotPendingOrFavourite() {
        // simulate the movie not being pending or favourite
        when(prefsMock.movieIsPending(617126)).thenReturn(false);
        when(prefsMock.movieIsFavourite(617126)).thenReturn(false);

        this.detailsPresenter.init(detailsViewMock);

        verify(detailsViewMock).init();
        verify(detailsViewMock).hidePendingBadge();
        verify(detailsViewMock).hideFavouriteBadge();
    }
}
