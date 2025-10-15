package es.unican.movies.activities.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.RuntimeEnvironment;
import org.robolectric.annotation.Config;

import es.unican.movies.common.SharedPreferencesImpl;
import es.unican.movies.model.Movie;

@RunWith(RobolectricTestRunner.class)
@Config(sdk = 36)
public class SharedPreferencesImplTest {

    private SharedPreferencesImpl sharedPrefs;

    @Before
    public void setUp() {
        Context context = RuntimeEnvironment.getApplication();
        sharedPrefs = new SharedPreferencesImpl(context);
        Movie movie = new Movie();
        movie.setId(617126);
        sharedPrefs.savePendingMovie(movie);
    }

    /**
     * UT1.a - Entrada: 617126
     * Resultado esperado: true
     */
    @Test
    public void movieIsPending_returnsTrue_whenMovieIsSaved() {
        boolean result = sharedPrefs.movieIsPending(617126);
        assertTrue(result);
    }

    /**
     * UT1.b - Entrada: 1267319
     * Resultado esperado: false
     */
    @Test
    public void movieIsPending_returnsFalse_whenMovieNotSaved() {
        boolean result = sharedPrefs.movieIsPending(1267319);
        assertFalse(result);
    }
}
