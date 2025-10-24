package es.unican.movies.activities.main;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.not;

import android.content.Context;
import android.view.View;

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


    // decorView de la activity para localizar Toasts
    private View decorView;

    @Before
    public void setUp() {

        hiltRule.inject(); // Asegúrate de llamar a esto antes de lanzar la actividad

        // Capturar la decorView de la activity para poder localizar los Toasts
        activityRule.getScenario().onActivity(activity -> decorView = activity.getWindow().getDecorView());
    }

    /*
    private void waitForToast(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) { }
    }*/

    /**
     * Success case:
     * The user successfully adds a movie to "Pending".
     */
    @Test
    public void addToPending_success() {
        // a. El usuario ve la lista de películas y pulsa "Añadir a pendientes"
        onData(anything())
                .inAdapterView(withId(R.id.lvMovies))
                .atPosition(1)
                .onChildView(withId(R.id.ibPending))
                .perform(click());

        // b. Aparece un Toast indicando que ha habido un error al guardar
        // Esperar un momento para que el Toast aparezca
        /*
        waitForToast(1500);
        onView(withText("Película guardada correctamente en Pendientes"))
                .inRoot(withDecorView(not(is(decorView))))
                .check(matches(isDisplayed()));
        */

        // c. El botón "Añadir a pendientes" del ítem pulsado desaparece
        onData(anything())
                .inAdapterView(withId(R.id.lvMovies))
                .atPosition(1)
                .onChildView(withId(R.id.ibPending))
                .check(matches(not(isDisplayed())));

        // d. El usuario entra a la vista detallada de la película
        onData(anything()).inAdapterView(withId(R.id.lvMovies)).atPosition(1).perform(click());

        // e. En la vista detallada aparece la insignia "Pendiente"
        onView(withId(R.id.tvPendingStatus)).check(matches(isDisplayed()));
    }
}