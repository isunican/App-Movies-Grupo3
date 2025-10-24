package es.unican.movies.utils;

public class TestUtils {
    /**
     * Wait for a given amount of time.
     * @param ms
     */
    public static void waitForToast(long ms) {
        try {
            Thread.sleep(ms);
        } catch (InterruptedException ignored) { }
    }
}
