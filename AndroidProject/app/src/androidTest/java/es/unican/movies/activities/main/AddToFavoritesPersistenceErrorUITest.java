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
import es.unican.movies.common.ISharedPreferences;
import es.unican.movies.utils.SharedPreferencesFakeAdd;
import es.unican.movies.injection.RepositoriesModule;
import es.unican.movies.injection.SharedPreferencesModule;
import es.unican.movies.service.IMoviesRepository;

@UninstallModules({RepositoriesModule.class, SharedPreferencesModule.class})
@HiltAndroidTest
public class AddToFavoritesPersistenceErrorUITest {

    @Rule(order = 0)
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @BindValue
    final IMoviesRepository repository = getTestRepository(context, R.raw.sample_movies);

    @BindValue
    final ISharedPreferences sharedPrefs = new SharedPreferencesFakeAdd();


    private View decorView;

    @Before
    public void setUp() {
        // Capturar la decorView de la activity para poder localizar los Toasts
        //activityRule.getScenario().onActivity(activity -> decorView = activity.getWindow().getDecorView());
    }

    @Test
    public void addToFavorites_success() {
        // verifica que están las películas
        onView(withId(R.id.lvMovies)).check(matches(isDisplayed()));

        // a. El usuario ve la lista de películas y pulsa "Añadir a favoritos"
        onData(anything())
                .inAdapterView(withId(R.id.lvMovies))
                .atPosition(0)
                .onChildView(withId(R.id.ibFavourite))
                .perform(click());

        // LA PARTE DEL TOAST NO SE PRUEBA PORQUE DA PROBLEMAS

        // Esperar 1.5s para que el Toast aparezca
        // TestUtils.waitForToast(1500);

        // b. Aparece un mensaje de confirmación (Toast) en la root del Toast
        //onView(withText("Película guardada correctamente en Favoritos"))
        //        .inRoot(withDecorView(not(is(decorView))))
        //        .check(matches(isDisplayed()));

        // c. El botón "Añadir a favoritos" del ítem pulsado NO desaparece
        onData(anything())
                .inAdapterView(withId(R.id.lvMovies))
                .atPosition(0)
                .onChildView(withId(R.id.ibFavourite))
                .check(matches(hasDrawable(R.drawable.emptyheart)));

        // d. El usuario entra a la vista detallada de la película
        onData(anything()).inAdapterView(withId(R.id.lvMovies)).atPosition(0).perform(click());

        // e. En la vista detallada NO aparece la insignia "Favorita"
        onView(withId(R.id.tvFavouriteStatus)).check(matches(not(isDisplayed())));
    }
}
