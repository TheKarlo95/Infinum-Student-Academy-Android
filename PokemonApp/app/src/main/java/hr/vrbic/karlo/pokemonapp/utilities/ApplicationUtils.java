package hr.vrbic.karlo.pokemonapp.utilities;

import android.content.res.Configuration;

import hr.vrbic.karlo.pokemonapp.PokemonApp;
import hr.vrbic.karlo.pokemonapp.R;

/**
 * Created by TheKarlo95 on 26.7.2016..
 */
public class ApplicationUtils {

    /**
     * Unused constructor.
     */
    private ApplicationUtils() {
    }

    public static boolean isTablet() {
        return PokemonApp.getContext().getResources().getBoolean(R.bool.is_tablet);
    }

    public static boolean isLandscape() {
        return PokemonApp.getContext().getResources().getConfiguration().orientation == Configuration
                .ORIENTATION_LANDSCAPE;
    }

    public static boolean isTabletAndLandscape() {
        return isTablet() && isLandscape();
    }

}
