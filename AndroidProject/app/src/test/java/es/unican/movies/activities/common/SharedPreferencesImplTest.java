package es.unican.movies.activities.common;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Context;

import androidx.test.core.app.ApplicationProvider;

import org.junit.Before;
import org.junit.Test;
import org.robolectric.annotation.Config;

@Config(sdk = 36)
public class SharedPreferencesImplTest {

    private SharedPreferencesImpl sharedPrefs;

    @Before
    public void setUp() {
        // Obtener un contexto de aplicación simulado
        Context context = ApplicationProvider.getApplicationContext();

        // Crear la instancia de SharedPreferencesImpl con ese contexto
        sharedPrefs = new SharedPreferencesImpl(context);

        // Guardar una película pendiente para el caso positivo
        sharedPrefs.savePendingMovie(617126);
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
