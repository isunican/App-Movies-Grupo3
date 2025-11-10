package es.unican.movies.activities.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.parceler.Parcels;

import java.util.List;

import javax.inject.Inject;

import dagger.hilt.android.AndroidEntryPoint;
import es.unican.movies.R;
import es.unican.movies.activities.details.DetailsView;
import es.unican.movies.activities.info.InfoActivity;
import es.unican.movies.common.ISharedPreferences;
import es.unican.movies.model.Movie;
import es.unican.movies.service.IMoviesRepository;


/**
 * Activity to show the list of movies.
 * Implements the View in the MVP architecture for the main screen.
 * Handles UI initialization, menu actions, and navigation to details and info screens.
 */
@AndroidEntryPoint
public class MainView extends AppCompatActivity implements IMainContract.View {

    /**
     * Presenter that will take control of this view.
     */
    private IMainContract.Presenter presenter;


    /**
     * Repository that can be used to retrieve movies or series.
     */

    @Inject
    IMoviesRepository repository;


    /**
     * Shared preferences for managing persistent movie data.
     */
    @Inject
    public ISharedPreferences sharedPreferences;


    /**
     * Reference to the ListView that shows the list of movies.
     */
    private ListView lvMovies;

    private MovieAdapter adapter;

    private FloatingActionButton fabUp;

    /**
     * Called when the activity is starting. Sets up the toolbar, presenter, and shared preferences.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // The default theme does not include a toolbar.
        // In this app the toolbar is explicitly declared in the layout
        // This sets this toolbar as the activity ActionBar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        // instantiate presenter, let it take control
        presenter = new MainPresenter();
        presenter.init(this);
    }


    /**
     * Initializes the contents of the Activity's options menu.
     * @param menu The options menu in which items are placed.
     * @return true for the menu to be displayed; false otherwise
     */
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.menu, menu);
        return true;
    }


    /**
     * Called when an item in the action bar menu is selected.
     * Handles navigation to the info screen.
     * @param item The menu item that was selected.
     * @return true if the selection was handled, false otherwise
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int itemId = item.getItemId();
        if (itemId == R.id.menuItemInfo) {
            presenter.onMenuInfoClicked();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }


    /**
     * Initializes the view components and sets up the ListView item click listener.
     */
    @Override
    public void init() {
        lvMovies = findViewById(R.id.lvMovies);
        TextView tvNotFound = findViewById(R.id.tvNotFound);
        lvMovies.setEmptyView(tvNotFound);
        lvMovies.setOnItemClickListener((parent, view, position, id) -> {
            Movie movie = (Movie) parent.getItemAtPosition(position);
            presenter.onItemClicked(movie);
        });

        fabUp = findViewById(R.id.fabUp);
        fabUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lvMovies.setSelection(0);
            }
        });

        SearchView svMovies = findViewById(R.id.svPeliculas);
        svMovies.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                //if (query != null && !query.trim().isEmpty()) {
                //    presenter.onMovieSearch(query.trim());
                //} else {
                //    presenter.onMovieSearch("");
                //}
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText != null && !newText.trim().isEmpty()) {
                    presenter.onMovieSearch(newText.trim());
                } else {
                    presenter.onMovieSearch("");
                }
                return true;
            }

        });
    }


    /**
     * Returns the movies repository for retrieving movie data.
     * @return the movies repository
     */
    @Override
    public IMoviesRepository getMoviesRepository() {
        return repository;
    }


    /**
     * Displays the list of movies in the ListView using the MovieAdapter.
     * @param movies the list of movies to display
     */
    @Override
    public void showMovies(List<Movie> movies) {
        this.adapter = new MovieAdapter(this, movies, sharedPreferences, presenter);
        lvMovies.setAdapter(adapter);
    }


    /**
     * Shows a toast indicating the number of movies loaded successfully.
     * @param movies the number of movies loaded
     */
    @Override
    public void showLoadCorrect(int movies) {
        Toast.makeText(this, "Loaded " + movies + " movies", Toast.LENGTH_SHORT).show();
    }


    /**
     * Shows a toast indicating an error occurred while loading movies.
     */
    @Override
    public void showLoadError() {
        Toast.makeText(this, "Error loading movies", Toast.LENGTH_SHORT).show();
    }


    /**
     * Navigates to the details screen for the selected movie.
     * @param movie the movie to show details for
     */
    @Override
    public void showMovieDetails(Movie movie) {
        Intent intent = new Intent(this, DetailsView.class);
        intent.putExtra(DetailsView.INTENT_MOVIE, Parcels.wrap(movie));
        startActivity(intent);
    }


    /**
     * Navigates to the info activity screen.
     */
    @Override
    public void showInfoActivity() {
        startActivity(new Intent(this, InfoActivity.class));
    }

    @Override
    public void updatePendingState() {
        adapter.notifyDataSetChanged();
    }

    @Override
    public void showAddPendingSuccess() {
        Toast.makeText(this, "Película guardada correctamente en Pendientes", Toast.LENGTH_LONG).show();
    }

    public void showRemovePendingSuccess() {
        Toast.makeText(this, "Película eliminada correctamente de Pendientes", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showPendingError() {
        Toast.makeText(this, "Ha ocurrido un error. Por favor, vuelve a intentarlo", Toast.LENGTH_LONG).show();
    }

    @Override
    public void updateFavouriteState() { adapter.notifyDataSetChanged(); }

    @Override
    public void showAddFavouriteSuccess() {
        Toast.makeText(this, "Película guardada correctamente en Favoritos", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showRemoveFavouriteSuccess() {
        Toast.makeText(this, "Película eliminada correctamente de Favoritos", Toast.LENGTH_LONG).show();
    }

    @Override
    public void showFavouriteError() {
        Toast.makeText(this, "Ha ocurrido un error. Por favor, vuelve a intentarlo", Toast.LENGTH_LONG).show();
    }

    @Override
    public ISharedPreferences getSharedPreferences() {
        return sharedPreferences;
    }

}