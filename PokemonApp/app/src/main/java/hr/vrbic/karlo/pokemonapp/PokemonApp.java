package hr.vrbic.karlo.pokemonapp;

import android.app.Application;
import android.content.Context;

public class PokemonApp extends Application {

    private static Context CONTEXT;

    @Override
    public void onCreate() {
        super.onCreate();
        CONTEXT = getApplicationContext();
    }

    public static Context getContext() {
        return CONTEXT;
    }
}
