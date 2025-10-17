package es.unican.movies.activities.details;

import es.unican.movies.common.ISharedPreferences;
import es.unican.movies.model.Movie;

public class DetailsPresenter implements IDetailsContract.Presenter {

    IDetailsContract.View view;

    @Override
    public void init(IDetailsContract.View view) {
        this.view = view;
        this.view.init();

        ISharedPreferences sharedPreferences = view.getSharedPreferences();

        Movie movie = this.view.getMovie();
        boolean isMoviePending = sharedPreferences.movieIsPending(movie.getId());

        if (!isMoviePending) {
            this.view.hidePendingBadge();
        }
    }
}
