package hr.vrbic.karlo.pokemonapp.utilities;

import java.util.Objects;

/**
 * {@code Strings} is a utility class that offers static methods that do some tasks with {@linkplain String strings}.
 *
 * @author Karlo Vrbic
 * @version 1.0
 */
@SuppressWarnings("unused")
public class Strings {

    /**
     * Unused constructor.
     */
    private Strings() {
    }

    /**
     * Returns the trimmed {@code string}.
     *
     * @param string       the string to be tested
     * @param nullMessage  detail message for {@linkplain NullPointerException}
     * @param emptyMessage detail message for {@linkplain IllegalArgumentException}
     * @return the trimmed {@code string}
     * @throws NullPointerException     if parameter {@code string} is {@code null}
     * @throws IllegalArgumentException if parameter trimmed {@code string} is empty
     *                                  ({@linkplain String#isEmpty()} returns {@code true})
     */
    public static String requireNonNullAndNonEmpty(String string, String nullMessage, String emptyMessage) {
        Objects.requireNonNull(string, nullMessage);
        if (string.trim().isEmpty()) {
            throw new IllegalArgumentException(emptyMessage);
        }
        return string.trim();
    }
}