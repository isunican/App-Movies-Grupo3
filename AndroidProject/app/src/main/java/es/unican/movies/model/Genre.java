package es.unican.movies.model;

import com.google.gson.annotations.SerializedName;

import org.parceler.Parcel;

import lombok.Getter;
import lombok.Setter;

/**
 * A movie using the TMDB data model.
 */
@Getter
@Setter
@Parcel
public class Genre {

    @SerializedName("id")
    protected int id;

    @SerializedName("name")
    protected String name;

}
