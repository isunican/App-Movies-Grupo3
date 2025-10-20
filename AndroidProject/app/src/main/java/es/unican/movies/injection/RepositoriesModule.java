package es.unican.movies.injection;

import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import es.unican.movies.common.ISharedPreferences;
import es.unican.movies.common.SharedPreferencesImpl;
import es.unican.movies.service.IMoviesRepository;
import es.unican.movies.service.MoviesRepository;

/**
 * This class is the provider of @link{IMoviesRepository} implementations
 * Any time somebody demands an @link{IMoviesRepository} implementation, Hilt will inject the implementation
 * provided by this module
 *
 * InstalllIn: this tells Hilt that this module is available to every Activity that is annotated
 * with AndroidEntryPoint. Instead of ActivityComponent.class, I could use SingletonComponent.class,
 * and it seems to work too
 */
@Module
@InstallIn(ActivityComponent.class)
public abstract class RepositoriesModule {

    @Provides
    public static IMoviesRepository provideRepository() {
        return MoviesRepository.INSTANCE;
    }

    @Provides
    public static ISharedPreferences provideSharedPreferences(@ApplicationContext Context context) {
        return new SharedPreferencesImpl(context);
    }

}
