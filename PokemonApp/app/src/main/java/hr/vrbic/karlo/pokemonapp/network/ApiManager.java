package hr.vrbic.karlo.pokemonapp.network;

import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Date;

import hr.vrbic.karlo.pokemonapp.BuildConfig;
import hr.vrbic.karlo.pokemonapp.network.deserializers.DateDeserializer;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;

public class ApiManager {

    public static final String API_ENDPOINT = "https://pokeapi.infinum.co";

    private static final Gson GSON = new GsonBuilder()
            .registerTypeAdapter(Date.class, new DateDeserializer())
            .create();

    private static HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
        @Override
        public void log(String message) {

            if (BuildConfig.DEBUG) {
                Log.d("api_tag", message);
            }
        }
    }).setLevel(HttpLoggingInterceptor.Level.BODY);

    private static OkHttpClient client
            = new OkHttpClient.Builder()
            .addNetworkInterceptor(httpLoggingInterceptor)
            .build();

    private static Retrofit REST_ADAPTER = new Retrofit.Builder()
            .baseUrl(API_ENDPOINT)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(GSON))
            .client(client).build();

    private static final PokeService POKE_SERVICE = REST_ADAPTER.create(PokeService.class);

    public static PokeService getService() {
        return POKE_SERVICE;
    }
}
