package hr.vrbic.karlo.pokemonapp.utilities;

/**
 * {@code NumberUtils} is a utility class that offers static method that checks the provided argument similar to
 * method {@linkplain java.util.Objects#requireNonNull(Object, String)}.
 *
 * @author Karlo Vrbic
 * @version 1.0
 */
@SuppressWarnings("unused")
public final class NumberUtils {

    /**
     * Unused constructor.
     */
    private NumberUtils() {
    }

    /**
     * Returns {@code number} if it is negative number, or throws {@linkplain IllegalArgumentException} with the given
     * detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't negative
     */
    public static byte requireNegative(byte number, String message) {
        if (number >= 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is negative number, or throws {@linkplain IllegalArgumentException} with the given
     * detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't negative
     */
    public static short requireNegative(short number, String message) {
        if (number >= 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is negative number, or throws {@linkplain IllegalArgumentException} with the given
     * detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't negative
     */
    public static int requireNegative(int number, String message) {
        if (number >= 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is negative number, or throws {@linkplain IllegalArgumentException} with the given
     * detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't negative
     */
    public static long requireNegative(long number, String message) {
        if (number >= 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is negative number, or throws {@linkplain IllegalArgumentException} with the given
     * detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't negative
     */
    public static float requireNegative(float number, String message) {
        if (number >= 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is negative number, or throws {@linkplain IllegalArgumentException} with the given
     * detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't negative
     */
    public static double requireNegative(double number, String message) {
        if (number >= 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is non-negative number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't non-negative
     */
    public static byte requireNonNegative(byte number, String message) {
        if (number < 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is non-negative number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't non-negative
     */
    public static short requireNonNegative(short number, String message) {
        if (number < 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is non-negative number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't non-negative
     */
    public static int requireNonNegative(int number, String message) {
        if (number < 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is non-negative number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't non-negative
     */
    public static long requireNonNegative(long number, String message) {
        if (number < 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is non-negative number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't non-negative
     */
    public static float requireNonNegative(float number, String message) {
        if (number < 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is non-negative number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't non-negative
     */
    public static double requireNonNegative(double number, String message) {
        if (number < 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is positive number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't positive
     */
    public static byte requirePositive(byte number, String message) {
        if (number <= 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is positive number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't positive
     */
    public static short requirePositive(short number, String message) {
        if (number <= 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is positive number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't positive
     */
    public static int requirePositive(int number, String message) {
        if (number <= 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is positive number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't positive
     */
    public static long requirePositive(long number, String message) {
        if (number <= 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is positive number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't positive
     */
    public static float requirePositive(float number, String message) {
        if (number <= 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is positive number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't positive
     */
    public static double requirePositive(double number, String message) {
        if (number <= 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is non-positive number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't non-positive
     */
    public static byte requireNonPositive(byte number, String message) {
        if (number > 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is non-positive number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't non-positive
     */
    public static short requireNonPositive(short number, String message) {
        if (number > 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is non-positive number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't non-positive
     */
    public static int requireNonPositive(int number, String message) {
        if (number > 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is non-positive number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't non-positive
     */
    public static long requireNonPositive(long number, String message) {
        if (number > 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is non-positive number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't non-positive
     */
    public static float requireNonPositive(float number, String message) {
        if (number > 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

    /**
     * Returns {@code number} if it is non-positive number, or throws {@linkplain IllegalArgumentException} with the
     * given detail {@code message}.
     *
     * @param number  number to be checked
     * @param message message of the exception
     * @return {@code number}
     * @throws IllegalArgumentException if parameter {@code number} isn't non-positive
     */
    public static double requireNonPositive(double number, String message) {
        if (number > 0) {
            throw new IllegalArgumentException(message);
        }
        return number;
    }

}
