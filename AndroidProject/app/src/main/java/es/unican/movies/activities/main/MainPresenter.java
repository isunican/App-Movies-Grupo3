package es.unican.movies.activities.main;

import java.util.List;
import java.util.stream.Collectors;

import es.unican.movies.common.ISharedPreferences;
import es.unican.movies.model.Movie;
import es.unican.movies.service.ICallback;
import es.unican.movies.service.IMoviesRepository;

public class MainPresenter implements IMainContract.Presenter {

    IMainContract.View view;

    private List<Movie> allMovies;

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
                allMovies = elements;
                view.showMovies(elements);
                view.showLoadCorrect(elements.size());
            }

            @Override
            public void onFailure(Throwable e) {
                view.showLoadError();
            }
        });
    }

    @Override
    public void onMovieSearch(String name) {

        if (allMovies == null || allMovies.isEmpty()) {
            load();
            return;
        }

        if (name.isEmpty()) {
            view.showMovies(allMovies);
            view.showLoadCorrect(allMovies.size());
            return;
        }

        List<Movie> filteredMovies = allMovies.stream()
                .filter(movie ->
                    movie.getTitle() != null && movie.getTitle().toLowerCase().contains(name.toLowerCase())
                ).collect(Collectors.toList());


        view.showMovies(filteredMovies);
        view.showLoadCorrect(filteredMovies.size());
    }

    @Override
    public void onPendingClicked(Movie movie) {
        boolean isPending = sharedPreferences.movieIsPending(movie.getId());
        boolean success = false;
        if (isPending) {
            success = sharedPreferences.removePendingMovie(movie);
        } else {
            success = sharedPreferences.savePendingMovie(movie);
        }
        if (success & isPending) {
            view.updatePendingState();
            view.showRemovePendingSuccess();
        } else if (success & !isPending) {
            view.updatePendingState();
            view.showAddPendingSuccess();
        } else {
            view.showPendingError();
        }
    }

    @Override
    public void onFavouriteClicked(Movie movie) {
        boolean isFavourite = sharedPreferences.movieIsFavourite(movie.getId());
        boolean success = false;
        if (isFavourite) {
            success = sharedPreferences.removeFavouriteMovie(movie);
        } else {
            success = sharedPreferences.saveFavouriteMovie(movie);
        }
        if (success & isFavourite) {
            view.updateFavouriteState();
            view.showRemoveFavouriteSuccess();
        } else if (success & !isFavourite) {
            view.updateFavouriteState();
            view.showAddFavouriteSuccess();
        } else {
            view.showFavouriteError();
        }
    }

    @Override
    public boolean isMoviePending(Movie movie) {
        return sharedPreferences.movieIsPending(movie.getId());
    }

    @Override
    public boolean isMovieFavourite(Movie movie) {
        return sharedPreferences.movieIsFavourite(movie.getId());
    }

}
