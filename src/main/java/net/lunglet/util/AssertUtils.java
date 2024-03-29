package net.lunglet.util;

// TODO add an argument range check that includes its endpoint to
// be able to check things like [0, Integer.MAX_VALUE].

public final class AssertUtils {
    public static void assertEquals(final int expected, final int actual) {
        if (expected != actual) {
            throw new AssertionError("expected = " + expected + " != actual = " + actual);
        }
    }

    public static void assertFalse(final boolean condition) {
        if (condition) {
            throw new AssertionError();
        }
    }

    public static void assertNotNull(final Object obj) {
        if (obj == null) {
            throw new AssertionError();
        }
    }

    public static void assertTrue(final boolean condition) {
        if (!condition) {
            throw new AssertionError();
        }
    }

    public static void checkArgument(final boolean condition) {
        if (!condition) {
            throw new IllegalArgumentException();
        }
    }

    public static void checkArgument(final boolean condition, final String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    public static int checkArgumentNonNegative(final int value) {
        if (value < 0) {
            throw new IllegalArgumentException();
        }
        return value;
    }

    /**
     * Checks that argument is in the range [start, stop).
     */
    public static void checkArgumentRange(final int value, final int start, final int stop) {
        if (value < start || value >= stop) {
            throw new IllegalArgumentException(String.format("%d not in range [%d, %d)", value, start, stop));
        }
    }

    private AssertUtils() {
    }
}
