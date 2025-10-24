package es.unican.movies.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

/**
 * A movie using the TMDB data model.
 */
@Getter
@Setter
@Parcel
public class Movie {

    @SerializedName("id")
    protected int id;

    @SerializedName("title")
    protected String title;

    @SerializedName("poster_path")
    protected String posterPath;

    @SerializedName("release_date")
    protected String releaseDate;

    @SerializedName("runtime")
    protected int runtime;

    @SerializedName("genres")
    protected List<Genre> genres;

    @SerializedName("vote_average")
    protected Double voteAverage;

    @SerializedName("vote_count")
    protected Integer voteCount;

    /**
     * Get the release year for the movie.
     * The serialized data contains the whole release data in YYYY-mm-dd format.
     *
     * @return String release year for the movie
     */
    public String getReleaseYear() {
        String[] dateList = this.releaseDate.split("-");

        return dateList[0];
    }

    /**
     * Get the duration of the movie in hours (h) and minutes (min).
     * The serialized data contains the duration in minutes.
     *
     * @return String duration in hours (h) and minutes (min)
     */
    public String getDurationHoursMinutes() {
        int hours = this.runtime / 60;
        int minutes = this.runtime % 60;

        return hours + "h " + minutes + "min";
    }

    /**
     * Get a list of the movie's genres in a single string.
     *
     * @return String a list of the movie's genres
     */
    public String getGenresList() {
        if (genres == null || genres.isEmpty()) return "-";

        StringBuilder sb = new StringBuilder();
        for (Genre g : genres) {
            sb.append(g.getName()).append(", ");
        }

        // Remove trailing ", "
        sb.setLength(sb.length() - 2);

        return sb.toString();
    }

    /**
     * Get the summarized score for the movie.
     *
     * @return double summarized score for the movie
     */
    public Double getSummarizedAverage() {
        if (voteAverage == null || voteCount == null) {
            return null;
        }
        if (Double.isNaN(voteAverage) || voteAverage < 0 || voteCount < 0) {
            return Double.NaN;
        }
        Double normalizedCount = 2 * Math.log10(1 + voteCount);
        return (voteAverage + normalizedCount) / 2;
    }

}
