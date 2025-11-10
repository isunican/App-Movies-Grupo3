package es.unican.movies.activities.main;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;

import es.unican.movies.common.ISharedPreferences;
import es.unican.movies.model.Movie;
import es.unican.movies.service.IMoviesRepository;

/**
 * Pruebas unitarias del metodo onPendingClicked() de MainPresenter
 */
public class MainPresenterFavouriteTest {

    private MainPresenter presenter;

    @Mock
    private IMainContract.View viewMock;

    @Mock
    private ISharedPreferences prefsMock;

    @Mock
    private IMoviesRepository repositoryMock;

    private Movie movie;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        presenter = new MainPresenter();

        // Configurar el presenter
        when(viewMock.getSharedPreferences()).thenReturn(prefsMock);
        when(viewMock.getMoviesRepository()).thenReturn(repositoryMock);
        presenter.init(viewMock);

        // Película de ejemplo
        movie = new Movie();
        movie.setId(617126);
        movie.setTitle("Los Cuatro Fantásticos");
    }

    /**
     * UT3.a - Película favorita y eliminación exitosa
     */
    @Test
    public void onFavouriteClicked_removeSuccess() {
        when(prefsMock.movieIsFavourite(617126)).thenReturn(true);
        when(prefsMock.removeFavouriteMovie(movie)).thenReturn(true);

        presenter.onFavouriteClicked(movie);

        // Verificar que se llamo a movieIsFavourite con el ID correcto
        verify(prefsMock).movieIsFavourite(617126);

        // Verificar que se llamo a removeFavouriteMovie con la película correcta
        verify(prefsMock).removeFavouriteMovie(movie);

        // Verificar que se actualizo el estado pendiente
        verify(viewMock).updateFavouriteState();

        // Verificar que NO se llamaron metodos no deseados
        verify(viewMock, Mockito.never()).showFavouriteError();
        verify(prefsMock, Mockito.never()).saveFavouriteMovie(Mockito.any());
    }

}
