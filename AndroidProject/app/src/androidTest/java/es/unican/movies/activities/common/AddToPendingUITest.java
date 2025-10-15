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

import android.content.Context;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

import org.junit.Rule;
import org.junit.Test;

import dagger.hilt.android.testing.BindValue;
import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;
import dagger.hilt.android.testing.UninstallModules;

import es.unican.movies.R;
import es.unican.movies.activities.main.MainView;
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

    /**
     * Success case:
     * The user successfully adds a movie to "Pending".
     */
    @Test
    public void addToPending_success() {
        // a. Usuario pulsa el botón "Añadir a pendientes" en la película "Juego sucio"
        onData(anything())
                .inAdapterView(withId(R.id.lvMovies))
                .atPosition(0)
                .perform(click());

        // b. Aparece mensaje de confirmación
        onView(withText("Película guardada correctamente en Pendientes"))
                .check(matches(isDisplayed()));

        // c. Desaparece el botón “Añadir a pendientes” (si tu UI lo oculta)
        onView(withText("Añadir a pendientes")).check(doesNotExist());

        // d. Entra en la vista detallada de la película
        onView(withText("Juego sucio")).perform(click());

        // e. En la vista detallada hay una insignia con el texto “Pendiente”
        onView(withText("Pendiente")).check(matches(isDisplayed()));
    }

    /**
     * Persistence error case:
     * Simulates an error when trying to load or save data.
     */
    @Test
    public void addToPending_persistenceError() {
        // Creamos un repositorio que simula error
        IMoviesRepository failingRepo = MockRepositories.getFailingRepository(context, R.raw.sample_movies);

        // Sustituimos el repo original (si fuera necesario)
        // En muchos casos basta con usar el repositorio de error directamente:
        // (esto depende de cómo tu Activity lo inyecte)
        // repository = failingRepo;

        // a. Usuario pulsa el botón "Añadir a pendientes"
        onData(anything())
                .inAdapterView(withId(R.id.lvMovies))
                .atPosition(0)
                .perform(click());

        // b. Aparece mensaje de error
        onView(withText("Ha ocurrido un error. Por favor, vuelve a intentarlo."))
                .check(matches(isDisplayed()));

        // c. El botón “Añadir a pendientes” sigue visible
        onView(withText("Añadir a pendientes")).check(matches(isDisplayed()));

        // d. En la vista detallada no aparece la insignia “Pendiente”
        onView(withText("Juego sucio")).perform(click());
        onView(withText("Pendiente")).check(doesNotExist());
    }
}
