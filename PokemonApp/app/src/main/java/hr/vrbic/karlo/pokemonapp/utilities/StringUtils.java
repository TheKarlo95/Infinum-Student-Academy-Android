package hr.vrbic.karlo.pokemonapp.utilities;

import android.widget.EditText;

import java.util.Objects;

import hr.vrbic.karlo.pokemonapp.PokemonApp;
import hr.vrbic.karlo.pokemonapp.R;

/**
 * {@code StringUtils} is a utility class that offers static methods that do some tasks with {@linkplain String strings}.
 *
 * @author Karlo Vrbic
 * @version 1.0
 */
@SuppressWarnings("unused")
public class StringUtils {

    /**
     * Unused constructor.
     */
    private StringUtils() {
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

    public static String getString(EditText editText) {
        String str = null;
        if (editText.getText().toString().trim().equalsIgnoreCase("")) {
            editText.setError(PokemonApp.getContext().getString(R.string.cannot_be_empty));
        } else {
            str = editText.getText().toString();
        }
        return str;
    }
}
