package es.unican.movies.activities.main;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isRoot;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import android.content.Context;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import dagger.hilt.android.testing.HiltAndroidRule;
import dagger.hilt.android.testing.HiltAndroidTest;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.util.List;

import es.unican.movies.R;
import es.unican.movies.model.Movie;

@HiltAndroidTest
@RunWith(AndroidJUnit4.class)
public class MovieDetailIntegrationTest {

    @Rule
    public HiltAndroidRule hiltRule = new HiltAndroidRule(this);

    @Rule
    public ActivityScenarioRule<MainView> activityRule =
            new ActivityScenarioRule<>(MainView.class);

    private List<Movie> movies;

    @Before
    public void setUp() throws Exception {
        hiltRule.inject(); // Inicializa Hilt

        Context context = ApplicationProvider.getApplicationContext();
        InputStream inputStream = context.getResources().openRawResource(R.raw.sample_movies);
        try (Reader reader = new InputStreamReader(inputStream)) {
            Type movieListType = new TypeToken<List<Movie>>() {}.getType();
            movies = new Gson().fromJson(reader, movieListType);
        }
    }

    public static ViewAction waitFor(long millis) {
        return new ViewAction() {
            @Override
            public Matcher<View> getConstraints() {
                return isRoot();
            }

            @Override
            public String getDescription() {
                return "Esperar " + millis + " milisegundos";
            }

            @Override
            public void perform(UiController uiController, View view) {
                uiController.loopMainThreadForAtLeast(millis);
            }
        };
    }

    @Test
    public void givenCompleteData_whenClickMovie_thenShowsCorrectFields() {
        onView(isRoot()).perform(waitFor(1500));
        onData(anything())
                .inAdapterView(withId(R.id.lvMovies))
                .atPosition(0)
                .perform(click());
        onView(withId(R.id.tvTitle)).check(matches(withText("Expediente Warren: El último rito")));
        onView(withId(R.id.tvReleaseYear)).check(matches(withText("2025")));
        onView(withId(R.id.tvGenres)).check(matches(withText("Terror, Suspense, Misterio")));
        onView(withId(R.id.tvSummarizedVote)).check(matches(withText("6.25")));
    }
}
