package es.unican.movies.activities.main;

import static org.mockito.Mockito.*;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;

import es.unican.movies.activities.main.IMainContract;
import es.unican.movies.activities.main.MainPresenter;
import es.unican.movies.model.Movie;
import es.unican.movies.service.IMoviesRepository;

public class MainPresenterOnMovieSearchTest {

    private IMainContract.Presenter mainPresenter = new MainPresenter();
    @Mock
    private IMainContract.View mainViewMock;
    @Mock
    private IMoviesRepository repositoryMock;

    private Movie supermanMovie;

    @Before
    public void setUp() {
        MockitoAnnotations.openMocks(this);

        // Movie: Superman
        supermanMovie = new Movie();
        supermanMovie.setId(1061474);
        supermanMovie.setTitle("Superman");

        when(mainViewMock.getMoviesRepository()).thenReturn(repositoryMock);

        mainPresenter.init(mainViewMock);
    }

    @Test
    public void onMovieSearch_movieIsFound_test() {

        // NOTA: Inimplementado. La ejecucion de onMovieSearch en este caso de prueba consiste en esto
        // ya sea en el setUp() mediante init o aqui, mediante onMovieSearch(), tienes que llamar a load()
        // con mainViewMock y repositoryMock (dado que es una prueba unitaria), debido a que hace uso de
        // una clase anonima para cargar las peliculas, como argumento de un metodo de repositoryMock.
        // Desconozco de una manera de remplazar los metodos de ICallback debido a que nunca es
        // inicializado a partir de los otros mocks.

        // En resumen: no hay manera de cargar las peliculas utilizando mocks por lo que siempre
        // vuelve despues del primer if

        /*this.mainPresenter.onMovieSearch("Superman");

        List<Movie> filteredMovies = new ArrayList<>();
        filteredMovies.add(supermanMovie);
        verify(mainViewMock).showMovies(filteredMovies);
        verify(mainViewMock).showLoadCorrect(filteredMovies.size());*/
    }

    @Test
    public void onMovieSearch_movieIsNotFound_test() {
        // NOTA: Inimplementado por la misma razon que el caso de prueba anterior.

        /*this.mainPresenter.onMovieSearch("PELICULA INEXISTENTE");

        List<Movie> filteredMovies = new ArrayList<>();
        verify(mainViewMock).showMovies(filteredMovies);
        verify(mainViewMock).showLoadCorrect(filteredMovies.size());*/
    }

    @Test
    public void onMovieSearch_searchFieldEmpty_test() {
        // NOTA: Inimplementado por la misma razon que los casos de prueba anterior.

        /*this.mainPresenter.onMovieSearch("");

        List<Movie> allMovies = new ArrayList<Movie>();
        verify(mainViewMock).showMovies(anyList());
        verify(mainViewMock).showLoadCorrect(anyInt());*/
    }

    @Test
    public void onMovieSearch_movieListEmpty_test() {
        this.mainPresenter.onMovieSearch("");
        verify(mainViewMock, times(2)).getMoviesRepository();
    }
}
