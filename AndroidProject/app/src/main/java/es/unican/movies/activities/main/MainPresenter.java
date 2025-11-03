package es.unican.movies.activities.main;

import java.util.List;

import es.unican.movies.common.ISharedPreferences;
import es.unican.movies.model.Movie;
import es.unican.movies.service.ICallback;
import es.unican.movies.service.IMoviesRepository;

public class MainPresenter implements IMainContract.Presenter {

    IMainContract.View view;

    private ISharedPreferences sharedPreferences;

    @Override
    public void init(IMainContract.View view) {
        this.view = view;
        this.sharedPreferences = view.getSharedPreferences();
        this.view.init();
        load();
    }

    @Override
    public void onItemClicked(Movie movie) {
        if (movie == null) {
            return;
        }
        view.showMovieDetails(movie);
    }

    @Override
    public void onMenuInfoClicked() {
        view.showInfoActivity();
    }

    /**
     * Loads the movies from the repository, and sends them to the view
     */
    private void load() {
        IMoviesRepository repository = view.getMoviesRepository();
        repository.requestAggregateMovies(new ICallback<List<Movie>>() {
            @Override
            public void onSuccess(List<Movie> elements) {
                view.showMovies(elements);
                view.showLoadCorrect(elements.size());
            }

            @Override
            public void onFailure(Throwable e) {
                view.showLoadError();
            }
        });
    }

    public void onPendingClicked(Movie movie) {
        boolean isPending = sharedPreferences.movieIsPending(movie.getId());
        boolean success = false;
        if (isPending) {
            success = sharedPreferences.removePendingMovie(movie);
        } else {
            success = sharedPreferences.savePendingMovie(movie);
        }
        view.updatePendingState();
        if (success & isPending) {
            view.showRemovePendingSuccess();
        } else if (success & !isPending) {
            view.showAddPendingSuccess();
        } else {
            view.showPendingError();
        }
    }

    public boolean isMoviePending(Movie movie) {
        return sharedPreferences.movieIsPending(movie.getId());
    }

}
