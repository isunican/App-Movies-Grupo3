package es.unican.movies.activities.main;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withEffectiveVisibility;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.CoreMatchers.anything;

import android.content.Context;
import android.view.View;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.platform.app.InstrumentationRegistry;

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
import es.unican.movies.utils.MockRepositories;

@UninstallModules({RepositoriesModule.class, SharedPreferencesModule.class})
@HiltAndroidTest
public class AddToPendingFailUITest {

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
    final ISharedPreferences sharedPrefs = new SharedPreferencesFakeAdd();

    // decorView de la activity para localizar Toasts
    private View decorView;

    /*private void waitForToast(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) { }
    }*/

    /**
     * Persistence error case:
     * Simulates an error when trying to load or save data.
    */
     @Test
     // a. El usuario ve la lista de películas y pulsa "Añadir a pendientes"
     public void addToPending_persistenceError() {
         onData(anything())
         .inAdapterView(withId(R.id.lvMovies))
         .atPosition(1)
         .onChildView(withId(R.id.ibPending))
         .perform(click());

         // b. Aparece un Toast indicando que ha habido un error al guardar
         //Esperar un momento para que el Toast aparezca
         /*
         waitForToast(1500);
         onView(withText("Ha ocurrido un error. Por favor, vuelve a intentarlo"))
                 .inRoot(withDecorView(not(is(decorView))))
                 .check(matches(isDisplayed()));
        */


         // c. El botón "Añadir a pendientes" sigue visible en ese ítem
         onData(anything())
         .inAdapterView(withId(R.id.lvMovies))
         .atPosition(1)
         .onChildView(withId(R.id.ibPending))
         .check(matches(isDisplayed()));

         // d. El usuario entra a la vista detallada de la película
         onData(anything())
         .inAdapterView(withId(R.id.lvMovies))
         .atPosition(1)
         .perform(click());

         // e. En la vista detallada NO debe haber una insignia "Pendiente"
         onView(withId(R.id.tvPendingStatus))
                 .check(matches(withEffectiveVisibility(ViewMatchers.Visibility.GONE)));
     }

}
