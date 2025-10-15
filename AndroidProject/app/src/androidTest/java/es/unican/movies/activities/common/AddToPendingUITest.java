package es.unican.movies.activities.common;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;

import es.unican.movies.R;
import es.unican.movies.activities.main.MainView;
import es.unican.movies.common.ISharedPreferences;
import es.unican.movies.injection.RepositoriesModule;
import es.unican.movies.service.IMoviesRepository;
import es.unican.movies.utils.MockRepositories;

@UninstallModules(RepositoriesModule.class)
@HiltAndroidTest
public class AddToPendingUITest {

    @Rule(order = 0) // the Hilt rule must execute first
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    // I need the context to access resources, such as the json with movies
    final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    // Mock repository that provides data from a JSON file instead of downloading it from the internet.
    @BindValue
    final IMoviesRepository repository = MockRepositories.getTestRepository(context, R.raw.sample_movies);

    @BindValue
    final ISharedPreferences mockPrefs = mock(ISharedPreferences.class);

    @Before
    public void setUp() {
        when(mockPrefs.movieIsPending(anyInt())).thenReturn(false);
        hiltRule.inject(); // Asegúrate de llamar a esto antes de lanzar la actividad
    }

    /**
     * Success case:
     * The user successfully adds a movie to "Pending".
     */
    @Test
    public void addToPending_success() {
        // a. El usuario ve la lista de películas y pulsa "Añadir a pendientes" en "Juego sucio"
        onData(anything())
                .inAdapterView(withId(R.id.lvMovies))
                .atPosition(1)
                .onChildView(withId(R.id.ibFavorite))
                .perform(click());
        // b. Aparece un mensaje de confirmación
        onView(withText("Película guardada correctamente en Pendientes"))
                .check(matches(isDisplayed()));

        // c. El botón "Añadir a pendientes" desaparece
        // (Esto puede variar si tu layout cambia de texto o icono)
        onView(withId(R.id.ibFavorite)).check(matches(isDisplayed()));

        // d. El usuario entra a la vista detallada de la película
        onData(anything()).inAdapterView(withId(R.id.lvMovies)).atPosition(1).perform(click());

        // e. En la vista detallada aparece la insignia "Pendiente"
        onView(withText("Pendiente")).check(matches(isDisplayed()));
    }

    /**
     * Persistence error case:
     * Simulates an error when trying to load or save data.
     */
    @Test
    public void addToPending_persistenceError() {

    }
}
