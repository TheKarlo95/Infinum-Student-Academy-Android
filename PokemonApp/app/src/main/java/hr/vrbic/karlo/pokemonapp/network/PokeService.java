package hr.vrbic.karlo.pokemonapp.network;

import hr.vrbic.karlo.pokemonapp.model.MoveListResponse;
import hr.vrbic.karlo.pokemonapp.model.MoveResponse;
import hr.vrbic.karlo.pokemonapp.model.PokemonListResponse;
import hr.vrbic.karlo.pokemonapp.model.TypeListResponse;
import hr.vrbic.karlo.pokemonapp.model.UserCreateRequest;
import hr.vrbic.karlo.pokemonapp.model.UserCreateResponse;
import hr.vrbic.karlo.pokemonapp.model.UserLoginRequest;
import hr.vrbic.karlo.pokemonapp.model.UserLoginResponse;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;


public interface PokeService {

    @POST("/api/v1/users")
    Call<UserCreateResponse> userCreate(@Body UserCreateRequest requestBody);

    @POST("/api/v1/users/login")
    Call<UserLoginResponse> userLogin(@Body UserLoginRequest requestBody);

    @GET("/api/v1/pokemons")
    Call<PokemonListResponse> getAllPokemons(@Header("Authorization") String authToken);

    @GET("/api/v1/types")
    Call<TypeListResponse> getAllTypes(@Header("Authorization") String authToken);

    @GET("/api/v1/moves")
    Call<MoveListResponse> getAllMoves(@Header("Authorization") String authToken);

    @GET("/api/v1/moves/{id}")
    Call<MoveResponse> getMove(@Header("Authorization") String authToken,
                               @Path("id") int moveId);

    @Multipart
    @POST("/api/v1/pokemons")
    Call<Void> createPokemon(@Header("Authorization")
                             String authToken,
                             @Part(value = "data[attributes][name]", encoding = "text/plain")
                             String name,
                             @Part("data[attributes][height]")
                             double height,
                             @Part("data[attributes][weight]")
                             double weight,
                             @Part("data[attributes][gender_id]")
                             int genderId,
                             @Part(value = "data[attributes][description]", encoding = "text/plain")
                             String description,
                             @Part("data[attributes][type_ids][]")
                             int[] category,
                             @Part("data[attributes][move_ids][]")
                             int[] moves,
                             @Part("data[attributes][image]\"; filename=\"pokemon.jpg")
                             RequestBody image);
}
