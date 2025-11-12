package es.unican.movies.activities.main;

import java.util.List;

import es.unican.movies.common.ISharedPreferences;
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


        /**
         * The presenter is informed that the user wants to search for a movie by name.
         * This method should be called only by the View when a search query is submitted
         * (for example, when the user presses Enter or taps the search icon).
         *
         * The presenter will handle the logic of querying the data source (repository)
         * to find movies that match the provided name and then update the View with the results.
         */
        public void onMovieSearch(String name);

        /**
         * Listener for when the 'Add to Pending' button is clicked
         * on a movie.
         * @param movie movie whose button was clicked
         */
        public void onPendingClicked(Movie movie);

        /**
         * Listener for when the 'Add to Favourite' button is clicked
         * on a movie.
         * @param movie movie whose button was clicked
         */
        public void onFavouriteClicked(Movie movie);

        /**
         * Checks whether a movie is Pending or not.
         * @param movie the movie to check
         * @return true if is Pending, false otherwise
         */
        public boolean isMoviePending(Movie movie);

        /**
         * Checks whether a movie is Favourite or not.
         * @param movie the movie to check
         * @return true if is Favourite, false otherwise
         */
        public boolean isMovieFavourite(Movie movie);

        public void onListHomeClicked();
        public void onListFavouritesClicked();
        public void onListPendingClicked();
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

        /**
         * Add or remove a movie to the Pending list based
         * on its current state.
         */
        public void updatePendingState();

        /**
         * Show a success Toast when a movie is added to Pending.
         */
        public void showAddPendingSuccess();

        /**
         * Show a success Toast when a movie is removed from Pending.
         */
        public void showRemovePendingSuccess();

        /**
         * Show an error Toast when a persistence error happens when
         * trying to add a movie to Pending.
         */
        public void showPendingError();

        public void updateFavouriteState();

        public void showAddFavouriteSuccess();

        public void showRemoveFavouriteSuccess();

        public void showFavouriteError();

        /**
         * Get the current ISharedPreferences instance
         * @return ISharedPreferences a specific instance of ISharedPreferences
         */
        public ISharedPreferences getSharedPreferences();

    }
}
