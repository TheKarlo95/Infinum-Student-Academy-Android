package hr.vrbic.karlo.pokemonapp;

import android.app.Application;
import android.content.Context;

import com.raizlabs.android.dbflow.config.FlowConfig;
import com.raizlabs.android.dbflow.config.FlowManager;

public class PokemonApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        context = getApplicationContext();
        FlowManager.init(new FlowConfig.Builder(this).build());
    }

    public static Context getContext() {
        return context;
    }
}
