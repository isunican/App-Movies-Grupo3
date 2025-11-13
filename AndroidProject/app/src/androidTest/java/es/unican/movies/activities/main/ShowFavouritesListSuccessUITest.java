package es.unican.movies.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.anything;
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
public class ShowFavouritesListSuccessUITest {

    @Rule(order = 0)
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @BindValue
    final IMoviesRepository repository = getTestRepository(context, R.raw.sample_movies);


    @Test
    public void showFavouritesListSuccess() {

        // verifica que están las películas
        onView(withId(R.id.lvMovies)).check(matches(isDisplayed()));


        // a. El pulsa "Favorito" (botón relleno) sobre la película en posición 0 "Los Cuatro Fantásticos: Primeros pasos"
        // que es para añadirla
        onData(anything())
                .inAdapterView(withId(R.id.lvMovies))
                .atPosition(0)
                .onChildView(withId(R.id.ibFavourite))
                .perform(click());

        try {
            Thread.sleep(1000); // ajustar si es necesario
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        onView(withId(R.id.btnFavourites)).perform(click());

        // 4. Verifica que la lista de favoritos se muestra
        onView(withId(R.id.lvMovies)).check(matches(isDisplayed()));

        // 5. Comprueba que el nombre "Los Cuatro Fantásticos: Primeros pasos" aparece en la lista
        onData(anything())
                .inAdapterView(withId(R.id.lvMovies))
                .atPosition(0)
                .onChildView(withId(R.id.tvTitle))
                .check(matches(withText("Los Cuatro Fantásticos: Primeros pasos")));

    }



}
