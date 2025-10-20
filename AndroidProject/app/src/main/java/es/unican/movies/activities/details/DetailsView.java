
/**
 * Activity to show the details of a movie.
 * Implements the View in the MVP architecture for the Details screen.
 */
package es.unican.movies.activities.details;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

import org.parceler.Parcels;

import es.unican.movies.R;
import es.unican.movies.common.ISharedPreferences;
import es.unican.movies.common.SharedPreferencesImpl;
import es.unican.movies.model.Movie;
import es.unican.movies.service.EImageSize;
import es.unican.movies.service.ITmdbApi;
import lombok.Getter;

/**
 * Activity to show the details of a movie.
 */

public class DetailsView extends AppCompatActivity implements IDetailsContract.View {

    public static final String INTENT_MOVIE = "INTENT_MOVIE";
    private IDetailsContract.Presenter presenter;
    private ISharedPreferences sharedPreferences;
    @Getter
    private Movie movie;

    /**
     * Called when the activity is starting. Sets up the presenter, retrieves the movie,
     * loads the movie fields, and initializes the presenter.
     * @param savedInstanceState If the activity is being re-initialized after previously being shut down then this Bundle contains the data it most recently supplied in onSaveInstanceState(Bundle). Otherwise it is null.
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        // Create presenter
        this.presenter = new DetailsPresenter();
        sharedPreferences = new SharedPreferencesImpl(this);

        // Get movie
        this.movie = Parcels.unwrap(getIntent().getExtras().getParcelable(INTENT_MOVIE));
        assert movie != null;

        this.loadFields(movie);

        this.presenter.init(this);
    }

    /**
     * Initializes the view. Can be used to set up listeners or UI components.
     */
    @Override
    public void init() {
        //
    }

    /**
     * Returns the shared preferences instance for persistent storage.
     * @return the shared preferences implementation
     */
    @Override
    public ISharedPreferences getSharedPreferences() {
        return this.sharedPreferences;
    }

    /**
     * Hides the pending badge in the UI if the movie is not pending.
     */
    @Override
    public void hidePendingBadge() {
        TextView tvPendingStatus = findViewById(R.id.tvPendingStatus);
        tvPendingStatus.setVisibility(TextView.GONE);
    }

    /**
     * Retrieve movie details fields and populate them with the correct data.
     * @param movie the movie whose details are to be displayed
     */
    protected void loadFields(Movie movie) {
        TextView tvTitle = findViewById(R.id.tvTitle);
        TextView tvId = findViewById(R.id.tvId);
        TextView tvReleaseYear = findViewById(R.id.tvReleaseYear);
        TextView tvDuration = findViewById(R.id.tvDuration);
        TextView tvGenres = findViewById(R.id.tvGenres);
        TextView tvVoteAverage = findViewById(R.id.tvVoteAverage);
        TextView tvSummarizedVote = findViewById(R.id.tvSummarizedVote);

        tvTitle.setText(movie.getTitle());
        tvId.setText(String.valueOf(movie.getId()));
        tvReleaseYear.setText(formatValue(movie.getReleaseYear()));
        tvDuration.setText(formatValue(movie.getDurationHoursMinutes()));
        tvGenres.setText(formatValue(movie.getGenresList()));
        tvVoteAverage.setText(formatValue(movie.getVoteAverage()));
        tvSummarizedVote.setText(formatValue(movie.getSummarizedAverage()));

        // Movie poster
        ImageView ivPoster = findViewById(R.id.ivPoster);
        String imageUrl = ITmdbApi.getFullImagePath(movie.getPosterPath(), EImageSize.W92);
        Picasso.get().load(imageUrl).fit().centerInside().into(ivPoster);
    }

    /**
     * Returns "-" if the value is empty or null, else returns the string representation.
     * @param value the string value to format
     * @return formatted string
     */
    private String formatValue(String value) {
        return (value == null || value.isEmpty()) ? "-" : value;
    }

    /**
     * Returns "-" if the value is negative, else returns the formatted string representation.
     * @param value the integer value to format
     * @return formatted string
     */
    private String formatValue(int value) {
        return value < 0 ? "-" : String.format("%.2f", value);
    }

    /**
     * Returns "-" if the value is negative, else returns the formatted string representation.
     * @param value the double value to format
     * @return formatted string
     */
    private String formatValue(double value) {
        return value < 0 ? "-" : String.format("%.2f", value);
    }
}