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

    @Override
    public void init() {
        //
    }

    @Override
    public ISharedPreferences getSharedPreferences() {
        return this.sharedPreferences;
    }

    @Override
    public void hidePendingBadge() {
        TextView tvPendingStatus = findViewById(R.id.tvPendingStatus);
        tvPendingStatus.setVisibility(TextView.GONE);
    }

    /**
     * Retrieve movie details fields and populate them with the correct data.
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
     * Returns "-" if the value is empty or negative, else returns the string representation.
     */
    private String formatValue(String value) {
        return (value == null || value.isEmpty()) ? "-" : value;
    }

    /**
     * Returns "-" if the value is empty or negative, else returns the string representation.
     */
    private String formatValue(int value) {
        return value < 0 ? "-" : String.format("%.2f", value);
    }

    /**
     * Returns "-" if the value is empty or negative, else returns the string representation.
     */
    private String formatValue(double value) {
        return value < 0 ? "-" : String.format("%.2f", value);
    }
}