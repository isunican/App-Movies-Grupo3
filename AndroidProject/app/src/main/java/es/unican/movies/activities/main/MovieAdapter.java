package es.unican.movies.activities.main;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

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
 * Adapter for the list of movies.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    /**
     * List of movies to display.
     */
    private final List<Movie> movieList;

    private final Context context;

    private final ISharedPreferences sharedPreferences;

    // Constructor without OnItemClickListener, which is now handled by the ListView itself
    protected MovieAdapter(@NonNull Context context, @NonNull List<Movie> movieList, ISharedPreferences sharedPreferences) {
        super(context, R.layout.activity_main_movie_item, movieList);
        this.context = context;
        this.movieList = movieList;
        this.sharedPreferences =  sharedPreferences;
    }

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

        // interaccion con favoritos
        ImageButton ibFavorite = convertView.findViewById(R.id.ibPending);
        boolean isFavorite = sharedPreferences.movieIsPending(movie.getId());

        if (isFavorite) {
            // If its already in favorites, we do not show the button
            ibFavorite.setVisibility(View.GONE);
        } else {
            // If its not in favorites, we show the button
            ibFavorite.setVisibility(View.VISIBLE);

            // Listener linked to the button to add the film clicked to favorites
            ibFavorite.setOnClickListener( v -> {
                sharedPreferences.savePendingMovie(movie);
                ibFavorite.setVisibility(View.GONE);
                notifyDataSetChanged();
            });
        }

        return convertView;
    }

    @Override
    public int getCount() {
        return movieList.size();
    }

    @Nullable
    @Override
    public Movie getItem(int position) {
        return movieList.get(position);
    }

}
