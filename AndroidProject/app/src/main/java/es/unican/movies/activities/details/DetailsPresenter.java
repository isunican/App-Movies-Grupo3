
/**
 * Presenter implementation for the Details screen.
 * Handles the logic for displaying movie details and managing UI state.
 */
package es.unican.movies.activities.details;

import es.unican.movies.common.ISharedPreferences;
import es.unican.movies.model.Movie;


public class DetailsPresenter implements IDetailsContract.Presenter {

    IDetailsContract.View view;

    /**
     * Initializes the presenter with the given view, sets up the UI, and manages
     * the pending and favourite badge visibility based on the movie's pending
     * and favourite status.
     * @param view the view to be controlled by this presenter
     */
    @Override
    public void init(IDetailsContract.View view) {
        this.view = view;
        this.view.init();

        ISharedPreferences sharedPreferences = view.getSharedPreferences();

        Movie movie = this.view.getMovie();
        boolean isMoviePending = sharedPreferences.movieIsPending(movie.getId());
        boolean isMovieFavourite = sharedPreferences.movieIsFavourite(movie.getId());

        if (!isMoviePending) {
            this.view.hidePendingBadge();
        }

        if (!isMovieFavourite) {
            this.view.hideFavouriteBadge();
        }
    }
}
