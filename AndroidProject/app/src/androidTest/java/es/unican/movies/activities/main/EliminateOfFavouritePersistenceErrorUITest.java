package es.unican.movies.activities.main;


import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;
import static org.hamcrest.Matchers.not;
import static es.unican.movies.utils.DrawableMatcher.hasDrawable;
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
import es.unican.movies.injection.RepositoriesModule;
import es.unican.movies.injection.SharedPreferencesModule;
import es.unican.movies.service.IMoviesRepository;
import es.unican.movies.utils.SharedPreferencesFakeEliminate;

@UninstallModules({RepositoriesModule.class, SharedPreferencesModule.class})
@HiltAndroidTest
public class EliminateOfFavouritePersistenceErrorUITest {

    @Rule(order = 0)
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule(order = 1)
    public ActivityScenarioRule<MainView> activityRule = new ActivityScenarioRule<>(MainView.class);

    final Context context = InstrumentationRegistry.getInstrumentation().getTargetContext();

    @BindValue
    final IMoviesRepository repository = getTestRepository(context, R.raw.sample_movies);


    @BindValue
    final ISharedPreferences sharedPrefs = new SharedPreferencesFakeEliminate();


    private View decorView;

    @Before
    public void setUp() {

    }


    @Test
    public void eliminateOfFavourite_persistenceError() {
        // 1. Verifica que la lista se muestra
        onView(withId(R.id.lvMovies)).check(matches(isDisplayed()));

        // 2. El usuario pulsa "Favorito" para intentar eliminar
        onData(anything())
                .inAdapterView(withId(R.id.lvMovies))
                .atPosition(0)
                .onChildView(withId(R.id.ibFavourite))
                .perform(click());

        // 3. Espera para la actualización de UI
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 4. El botón mantiene su estado (relleno porque el error no cambió el estado)
        onData(anything())
                .inAdapterView(withId(R.id.lvMovies))
                .atPosition(0)
                .onChildView(withId(R.id.ibFavourite))
                .check(matches(hasDrawable(R.drawable.fullheart)));

        // 5. Entra en la vista detallada
        onData(anything()).inAdapterView(withId(R.id.lvMovies)).atPosition(0).perform(click());

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }

        // 6. La insignia "Favorito" sigue visible debido al error de persistencia
        onView(withId(R.id.tvFavouriteStatus)).check(matches(isDisplayed()));
    }

}

