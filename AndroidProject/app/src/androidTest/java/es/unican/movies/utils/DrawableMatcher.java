package es.unican.movies.utils;

import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;
import androidx.core.content.ContextCompat;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;

import java.util.Objects;

public class DrawableMatcher {

    public static Matcher<View> hasDrawable(int drawableResId) {
        return new TypeSafeMatcher<View>() {
            @Override
            public boolean matchesSafely(View view) {
                if (!(view instanceof ImageButton)) {
                    return false;
                }
                ImageButton imageButton = (ImageButton) view;
                Drawable expectedDrawable = ContextCompat.getDrawable(view.getContext(), drawableResId);
                Drawable actualDrawable = imageButton.getDrawable();

                if (expectedDrawable == null || actualDrawable == null) {
                    return false;
                }

                return Objects.equals(expectedDrawable.getConstantState(), actualDrawable.getConstantState());
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has drawable with resource id: " + drawableResId);
            }
        };
    }
}
