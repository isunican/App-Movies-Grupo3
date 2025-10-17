package es.unican.movies.activities.details;

import es.unican.movies.model.Movie;
import es.unican.movies.common.ISharedPreferences;

public interface IDetailsContract {

    public interface Presenter {

        /**
         * Links the presenter with its view.
         * Only the View should call this method
         * @param view the view that will be controlled by this presenter
         */
        public void init(IDetailsContract.View view);
    }

    public interface View {

        /**
         * Initialize the view. Typically this should initialize all the listeners in the view.
         * Only the Presenter should call this method
         */
        public void init();

        public ISharedPreferences getSharedPreferences();

        public Movie getMovie();

        public void hidePendingBadge();
    }
}
