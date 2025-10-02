package es.unican.movies.activities.main;

import java.util.List;

import es.unican.movies.model.Movie;
import es.unican.movies.service.IMoviesRepository;

/**
 * Interfaces that define the contract between the Main Presenter and View.
 */
public interface IMainContract {

    /**
     * Methods that must be implemented in the Main Presenter.
     * Only the View should call these methods.
     */
    public interface Presenter {

        /**
         * Links the presenter with its view.
         * Only the View should call this method
         * @param view the view that will be controlled by this presenter
         */
        public void init(View view);

        /**
         * The presenter is informed that a movie has been clicked
         * Only the View should call this method
         * @param movie the movie that has been clicked
         */
        public void onItemClicked(Movie movie);

        /**
         * The presenter is informed that the Info item in the menu has been clicked
         * Only the View should call this method
         */
        public void onMenuInfoClicked();

    }

    /**
     * Methods that must be implemented in the Main View.
     * Only the Presenter should call these methods.
     */
    public interface View {

        /**
         * Initialize the view. Typically this should initialize all the listeners in the view.
         * Only the Presenter should call this method
         */
        public void init();

        /**
         * Returns a repository that can be called by the Presenter to retrieve movies or series.
         * This method must be located in the view because Android resources must be accessed
         * in order to instantiate a repository (for example Internet Access). This requires
         * dependencies to Android. We want to keep the Presenter free of Android dependencies,
         * therefore the Presenter should be unable to instantiate repositories and must rely on
         * the view to create the repository.
         * Only the Presenter should call this method
         * @return a repository that can be called by the Presenter to retrieve movies or series
         */
        public IMoviesRepository getMoviesRepository();

        /**
         * The view is requested to display the given list of movies.
         * Only the Presenter should call this method
         * @param movies the list of movies
         */
        public void showMovies(List<Movie> movies);

        /**
         * The view is requested to display a notification indicating that the movies
         * were loaded correctly.
         * Only the Presenter should call this method
         * @param movies number of movies
         */
        public void showLoadCorrect(int movies);

        /**
         * The view is requested to display a notificacion indicating that the movies
         * were not loaded correctly.
         * Only the Presenter should call this method
         */
        public void showLoadError();

        /**
         * The view is requested to display the detailed view of the given movie.
         * Only the Presenter should call this method
         * @param movie the movie
         */
        public void showMovieDetails(Movie movie);

        /**
         * The view is requested to open the info activity.
         * Only the Presenter should call this method
         */
        public void showInfoActivity();

    }
}
