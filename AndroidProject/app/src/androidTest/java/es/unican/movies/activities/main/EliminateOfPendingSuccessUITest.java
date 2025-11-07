package es.unican.movies.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.not;

import static es.unican.movies.utils.Matchers.hasDrawable;
import static es.unican.movies.utils.MockRepositories.getTestRepository;

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

@UninstallModules(RepositoriesModule.class)
@HiltAndroidTest
public class EliminateOfPendingSuccessUITest {

    @Rule(order = 0)
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @BindValue
    final IMoviesRepository repository = getTestRepository(context, R.raw.sample_movies);

    private View decorView;

    @Before
    public void setUp() {
        // Capturar la decorView de la activity para poder localizar los Toasts
        //activityRule.getScenario().onActivity(activity -> decorView = activity.getWindow().getDecorView());
    }

    @Test
    public void eliminateOfPending_success() {
        // verifica que están las películas
        onView(withId(R.id.lvMovies)).check(matches(isDisplayed()));

        // a. El pulsa "Pendiente" (botón relleno) sobre la película en posición 0 para añadirla
        onData(anything())
                .inAdapterView(withId(R.id.lvMovies))
                .atPosition(0)
                .onChildView(withId(R.id.ibPending))
                .perform(click());

        try {
            Thread.sleep(1000); // ajustar si es necesario
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // a. El pulsa "Pendiente" (botón relleno) sobre la película en posición 0 para eliminarla
        onData(anything())
                .inAdapterView(withId(R.id.lvMovies))
                .atPosition(0)
                .onChildView(withId(R.id.ibPending))
                .perform(click());

        // LA PARTE DEL TOAST NO SE PRUEBA PORQUE DA PROBLEMAS
        // (comentada como en el original)

        try {
            Thread.sleep(1000); // ajustar si es necesario
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        // c. El botón "Pendiente" del ítem pulsado cambia a estado no seleccionado
        onData(anything())
                .inAdapterView(withId(R.id.lvMovies))
                .atPosition(0)
                .onChildView(withId(R.id.ibPending))
                .check(matches(hasDrawable(R.drawable.pendingsymbol)));
        // d. El usuario entra a la vista detallada de la película
        onData(anything()).inAdapterView(withId(R.id.lvMovies)).atPosition(0).perform(click());

        // Espera breve para permitir que la UI se actualice (parche rápido).
        try {
            Thread.sleep(1000); // ajustar si es necesario
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // e. En la vista detallada ya no aparece la insignia "Pendiente"
        onView(withId(R.id.tvPendingStatus)).check(matches(not(isDisplayed())));
    }
}
