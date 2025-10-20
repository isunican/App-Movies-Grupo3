package es.unican.movies.activities.main;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.squareup.picasso.Picasso;

import java.util.List;

import es.unican.movies.R;
import es.unican.movies.common.ISharedPreferences;
import es.unican.movies.model.Movie;
import es.unican.movies.service.EImageSize;
import es.unican.movies.service.ITmdbApi;


/**
 * Adapter for displaying a list of movies in a ListView.
 * Handles the binding of movie data to the UI elements in each list item,
 * including poster, title, and pending status button.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    /**
     * List of movies to display.
     */
    private final List<Movie> movieList;

    private final Context context;

    private final ISharedPreferences sharedPreferences;


    /**
     * Constructs a new MovieAdapter.
     *
     * @param context the context in which the adapter is running
     * @param movieList the list of movies to display
     * @param sharedPreferences the shared preferences for managing pending movies
     */
    protected MovieAdapter(@NonNull Context context, @NonNull List<Movie> movieList, ISharedPreferences sharedPreferences) {
        super(context, R.layout.activity_main_movie_item, movieList);
        this.context = context;
        this.movieList = movieList;
        this.sharedPreferences =  sharedPreferences;
    }


    /**
     * Returns the view for a specific position in the list.
     * Binds the movie data to the UI elements, handles poster loading,
     * title display, and pending status button logic.
     *
     * @param position the position of the item within the adapter's data set
     * @param convertView the old view to reuse, if possible
     * @param parent the parent that this view will eventually be attached to
     * @return the view corresponding to the data at the specified position
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Movie movie = getItem(position);

        if (convertView == null) {
            convertView = LayoutInflater.from(getContext())
                    .inflate(R.layout.activity_main_movie_item, parent, false);
        }

        // poster
        ImageView ivPoster = convertView.findViewById(R.id.ivPoster);
        String imageUrl = ITmdbApi.getFullImagePath(movie.getPosterPath(), EImageSize.W92);
        Picasso.get().load(imageUrl).fit().centerInside().into(ivPoster);

        // titulo
        TextView tvTitle = convertView.findViewById(R.id.tvTitle);
        tvTitle.setText(movie.getTitle());

        // interaccion con pendientes
        ImageButton ibPending = convertView.findViewById(R.id.ibPending);
        boolean isPending = sharedPreferences.movieIsPending(movie.getId());

        if (isPending) {
            // If its already in pending, we do not show the button
            ibPending.setVisibility(View.GONE);
        } else {
            // If its not in pending, we show the button
            ibPending.setVisibility(View.VISIBLE);

            // Listener linked to the button to add the film clicked to pending
            ibPending.setOnClickListener(v -> {
                boolean persistenceResult = sharedPreferences.savePendingMovie(movie);
                ibPending.setVisibility(View.GONE);
                notifyDataSetChanged();
                // if the new values were successfully written to persistent storage.
                if (persistenceResult) {
                    Toast.makeText(v.getContext(), "Película guardada correctamente en Pendientes", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(v.getContext(), "Ha ocurrido un error. Por favor, vuelve a intentarlo", Toast.LENGTH_LONG).show();
                }
            });
        }

        return convertView;
    }


    /**
     * Returns the number of movies in the adapter.
     * @return the size of the movie list
     */
    @Override
    public int getCount() {
        return movieList.size();
    }


    /**
     * Returns the movie at the specified position.
     * @param position the position of the item
     * @return the movie at the given position
     */
    @Nullable
    @Override
    public Movie getItem(int position) {
        return movieList.get(position);
    }

}
