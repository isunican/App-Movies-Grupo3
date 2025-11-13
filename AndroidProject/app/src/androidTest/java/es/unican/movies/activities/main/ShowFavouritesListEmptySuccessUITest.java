package es.unican.movies.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
import static es.unican.movies.utils.Matchers.listSize;
import static es.unican.movies.utils.MockRepositories.getTestRepository;

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
import es.unican.movies.injection.RepositoriesModule;
import es.unican.movies.service.IMoviesRepository;

@UninstallModules(RepositoriesModule.class)
@HiltAndroidTest
public class ShowFavouritesListEmptySuccessUITest {

    @Rule(order = 0)
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @BindValue
    final IMoviesRepository repository = getTestRepository(context, R.raw.sample_movies);


    @Test
    public void showFavouritesListEmptySuccess() {

        // Verifica que la lista principal está visible
        onView(withId(R.id.lvMovies)).check(matches(isDisplayed()));

        // Abre la lista de favoritos
        onView(withId(R.id.btnFavourites)).perform(click());

        // Comprueba que la lista está vacía
        onView(withId(R.id.lvMovies)).check(matches(listSize(0)));

        // Comprueba que aparece el mensaje de "No hay películas añadidas a la lista de favoritas."
        onView(withId(R.id.tvNotFound))
                .check(matches(isDisplayed()))
                .check(matches(withText("No hay películas añadidas a la lista de favoritas.")));

    }


}
