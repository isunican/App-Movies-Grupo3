package es.unican.movies.activities.details;

import es.unican.movies.activities.main.IMainContract;

public class DetailsPresenter implements IDetailsContract.Presenter {

    IDetailsContract.View view;

    @Override
    public void init(IDetailsContract.View view) {
        this.view = view;
        this.view.init();
    }
}
