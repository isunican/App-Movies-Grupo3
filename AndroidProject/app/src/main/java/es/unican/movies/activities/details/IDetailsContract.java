
/**
 * Contract interface for the Details screen, defining the responsibilities
 * of the Presenter and the View in the MVP architecture.
 */
package es.unican.movies.activities.details;

import es.unican.movies.model.Movie;
import es.unican.movies.common.ISharedPreferences;

public interface IDetailsContract {

    /**
     * Presenter interface for the Details screen.
     * Handles the business logic and interacts with the View.
     */
    public interface Presenter {
        /**
         * Links the presenter with its view.
         * Only the View should call this method.
         * @param view the view that will be controlled by this presenter
         */
        public void init(IDetailsContract.View view);
    }

    /**
     * View interface for the Details screen.
     * Handles UI updates and user interactions.
     */
    public interface View {
        /**
         * Initialize the view. Typically this should initialize all the listeners in the view.
         * Only the Presenter should call this method.
         */
        public void init();

        /**
         * Returns the shared preferences instance for persistent storage.
         * @return the shared preferences implementation
         */
        public ISharedPreferences getSharedPreferences();

        /**
         * Returns the movie being displayed in the details view.
         * @return the current movie
         */
        public Movie getMovie();

        /**
         * Hides the pending badge in the UI if the movie is not pending.
         */
        public void hidePendingBadge();

        /**
         * Hides the favourite badge in the UI if the movie is not favourite.
         */
        public void hideFavouriteBadge();
    }
}
