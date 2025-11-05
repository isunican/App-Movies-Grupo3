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
public class MainPresenterPendingTest {

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
     * UT3.a - Película pendiente y eliminación exitosa
     */
    @Test
    public void onPendingClicked_removeSuccess() {
        when(prefsMock.movieIsPending(617126)).thenReturn(true);
        when(prefsMock.removePendingMovie(movie)).thenReturn(true);

        presenter.onPendingClicked(movie);

        // Verificar que se llamo a movieIsPending con el ID correcto
        verify(prefsMock).movieIsPending(617126);

        // Verificar que se llamo a removePendingMovie con la película correcta
        verify(prefsMock).removePendingMovie(movie);

        // Verificar que se actualizo el estado pendiente
        verify(viewMock).updatePendingState();

        // Verificar que se mostro el mensaje de éxito
        verify(viewMock).showRemovePendingSuccess();

        // Verificar que NO se llamaron metodos no deseados
        verify(viewMock, Mockito.never()).showAddPendingSuccess();
        verify(viewMock, Mockito.never()).showPendingError();
        verify(prefsMock, Mockito.never()).savePendingMovie(Mockito.any());
    }

}
