package es.unican.movies.injection;


import android.content.Context;

import dagger.Module;
import dagger.Provides;
import dagger.hilt.InstallIn;
import dagger.hilt.android.components.ActivityComponent;
import dagger.hilt.android.qualifiers.ApplicationContext;
import es.unican.movies.common.ISharedPreferences;
import es.unican.movies.common.SharedPreferencesImpl;

@Module
@InstallIn(ActivityComponent.class)
public abstract class SharedPreferencesModule {
    @Provides
    public static ISharedPreferences provideSharedPreferences(@ApplicationContext Context context) {
        return new SharedPreferencesImpl(context);
    }
}
