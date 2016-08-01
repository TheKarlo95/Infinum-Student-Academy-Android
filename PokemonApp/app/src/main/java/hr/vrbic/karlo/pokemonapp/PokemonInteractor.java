package hr.vrbic.karlo.pokemonapp;


import android.content.Context;
import android.support.annotation.Nullable;
import android.widget.Toast;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.BaseModel;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.ArrayList;
import java.util.List;

import hr.vrbic.karlo.pokemonapp.model.Ability;
import hr.vrbic.karlo.pokemonapp.model.Category;
import hr.vrbic.karlo.pokemonapp.model.MoveListResponse;
import hr.vrbic.karlo.pokemonapp.model.Pokemon;
import hr.vrbic.karlo.pokemonapp.model.PokemonListResponse;
import hr.vrbic.karlo.pokemonapp.model.TypeListResponse;
import hr.vrbic.karlo.pokemonapp.model.User;
import hr.vrbic.karlo.pokemonapp.network.ApiManager;
import hr.vrbic.karlo.pokemonapp.network.PokeService;
import hr.vrbic.karlo.pokemonapp.utilities.NetworkUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonInteractor {

    private Call<PokemonListResponse> pokemonListCall;
    private Call<MoveListResponse> abilitiesCall;
    private Call<TypeListResponse> categoriesCall;

    private Context context;

    private List<Ability> abilities;
    private List<Category> categories;

    public PokemonInteractor(Context context, User user) {
        this.context = context;

        PokeService service = ApiManager.getService();

        if(user != null) {
            this.pokemonListCall = service.getAllPokemons(user.getAuthorization());
            this.abilitiesCall = service.getAllMoves(user.getAuthorization());
            this.categoriesCall = service.getAllTypes(user.getAuthorization());
        }
    }

    public void getAllPokemons(final Consumer<List<Pokemon>> onSuccessful) {
        getAllAbilitiesInternet(new Consumer<List<Ability>>() {
            @Override
            public void accept(List<Ability> abilities) {
                PokemonInteractor.this.abilities = abilities;

                if (NetworkUtils.isNetworkAvailable()) {
                    proceedWithGettingPokemonInternet(onSuccessful);
                } else {
                    proceedWithGettingPokemonDatabase(onSuccessful);
                }
            }
        });
        getAllCategoriesInternet(new Consumer<List<Category>>() {
            @Override
            public void accept(List<Category> categories) {
                PokemonInteractor.this.categories = categories;

                if (NetworkUtils.isNetworkAvailable()) {
                    proceedWithGettingPokemonInternet(onSuccessful);
                } else {
                    proceedWithGettingPokemonDatabase(onSuccessful);
                }
            }
        });
    }

    public void getAllCategories(final Consumer<List<Category>> onSuccessful) {
        if (NetworkUtils.isNetworkAvailable()) {
            getAllCategoriesInternet(onSuccessful);
        } else {
            getAllCategoriesDatabase(onSuccessful);
        }
    }

    public void getAllAbilities(final Consumer<List<Ability>> onSuccessful) {
        if (NetworkUtils.isNetworkAvailable()) {
            getAllAbilitiesInternet(onSuccessful);
        } else {
            getAllAbilitiesDatabase(onSuccessful);
        }
    }

    private void proceedWithGettingPokemonInternet(final Consumer<List<Pokemon>> onSuccessful) {
        if (abilities != null && categories != null) {
            pokemonListCall.clone().enqueue(new Callback<PokemonListResponse>() {
                @Override
                public void onResponse(Call<PokemonListResponse> call, Response<PokemonListResponse> response) {
                    if (response.isSuccessful()) {
                        List<Pokemon> pokemons = convertToPokemonList(response.body().getPokemonDataList());
                        clearTable(Pokemon.class);
                        saveAll(pokemons);

                        onSuccessful.accept(pokemons);
                    } else {
                        Toast.makeText(context, R.string.get_pokemon_fail, Toast.LENGTH_LONG).show();
                    }
                    PokemonInteractor.this.abilities = null;
                    PokemonInteractor.this.categories = null;
                }

                @Override
                public void onFailure(Call<PokemonListResponse> call, Throwable t) {
                    if (!call.isCanceled()) {
                        Toast.makeText(context, R.string.get_pokemon_fail, Toast.LENGTH_LONG).show();
                    }
                    PokemonInteractor.this.abilities = null;
                    PokemonInteractor.this.categories = null;
                }

                private List<Pokemon> convertToPokemonList(List<PokemonListResponse.PokemonData> pokemonDataList) {
                    if (pokemonDataList != null && !pokemonDataList.isEmpty()) {
                        List<Pokemon> pokemons = new ArrayList<>();
                        for (PokemonListResponse.PokemonData pokemonData : pokemonDataList) {
                            pokemons.add(convertToPokemon(pokemonData));
                        }

                        return pokemons;
                    } else {
                        return null;
                    }
                }

                private Pokemon convertToPokemon(PokemonListResponse.PokemonData pokemonData) {
                    List<Ability> abilities = null;
                    List<Category> categories = null;

                    List<Integer> abilityIds = pokemonData.getAbilityIds();
                    if (abilityIds != null) {
                        abilities = new ArrayList<>();
                        for (Integer abilityId : abilityIds) {
                            for (Ability ability : PokemonInteractor.this.abilities) {
                                if (ability.getId() == abilityId) {
                                    abilities.add(ability);
                                    break;
                                }
                            }
                        }
                    }
                    List<Integer> categoryIds = pokemonData.getCategoryIds();
                    if (categoryIds != null) {
                        categories = new ArrayList<>();
                        for (Integer categoryId : pokemonData.getCategoryIds()) {
                            for (Category category : PokemonInteractor.this.categories) {
                                if (category.getId() == categoryId) {
                                    categories.add(category);
                                    break;
                                }
                            }
                        }
                    }

                    return new Pokemon(pokemonData.getName(),
                            pokemonData.getHeight(),
                            pokemonData.getWeight(),
                            categories,
                            abilities,
                            pokemonData.getDescription(),
                            pokemonData.getImageUrl());
                }
            });
        }
    }

    private void proceedWithGettingPokemonDatabase(final Consumer<List<Pokemon>> onSuccessful) {
        SQLite.select()
                .from(Pokemon.class)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<Pokemon>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @Nullable List<Pokemon> tResult) {
                        if (tResult != null) {
                            onSuccessful.accept(tResult);
                        } else {
                            Toast.makeText(context, R.string.get_pokemons_fail_db, Toast.LENGTH_LONG).show();
                        }
                    }
                }).execute();
    }

    public void getAllCategoriesInternet(final Consumer<List<Category>> onSuccessful) {
        categoriesCall.clone().enqueue(new Callback<TypeListResponse>() {
            @Override
            public void onResponse(Call<TypeListResponse> call, Response<TypeListResponse> response) {
                if (response.isSuccessful()) {
                    List<Category> categories = response.body().getCategories();

                    clearTable(Category.class);
                    saveAll(categories);

                    onSuccessful.accept(response.body().getCategories());
                } else {
                    Toast.makeText(context, R.string.get_categories_fail, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TypeListResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    Toast.makeText(context, R.string.get_categories_fail, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getAllCategoriesDatabase(final Consumer<List<Category>> onSuccessful) {
        SQLite.select()
                .from(Category.class)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<Category>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @Nullable List<Category> tResult) {
                        if (tResult != null) {
                            onSuccessful.accept(tResult);
                        } else {
                            Toast.makeText(context, R.string.get_categories_fail_db, Toast.LENGTH_LONG).show();
                        }
                    }
                }).execute();
    }

    private void getAllAbilitiesInternet(final Consumer<List<Ability>> onSuccessful) {
        abilitiesCall.clone().enqueue(new Callback<MoveListResponse>() {
            @Override
            public void onResponse(Call<MoveListResponse> call, Response<MoveListResponse> response) {
                if (response.isSuccessful()) {
                    List<Ability> abilities = response.body().getAbilities();

                    clearTable(Ability.class);
                    saveAll(abilities);

                    onSuccessful.accept(abilities);
                } else {
                    Toast.makeText(context, R.string.get_abilities_fail, Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MoveListResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    Toast.makeText(context, R.string.get_abilities_fail, Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getAllAbilitiesDatabase(final Consumer<List<Ability>> onSuccessful) {
        SQLite.select()
                .from(Ability.class)
                .async()
                .queryListResultCallback(new QueryTransaction.QueryResultListCallback<Ability>() {
                    @Override
                    public void onListQueryResult(QueryTransaction transaction, @Nullable List<Ability> tResult) {
                        if (tResult != null) {
                            onSuccessful.accept(tResult);
                        } else {
                            Toast.makeText(context, R.string.get_abilities_fail_db, Toast.LENGTH_LONG).show();
                        }
                    }
                }).execute();
    }

    public void cancelAllCalls() {
        if (pokemonListCall != null) {
            pokemonListCall.cancel();
        }
        if (abilitiesCall != null) {
            abilitiesCall.cancel();
        }
        if (categoriesCall != null) {
            categoriesCall.cancel();
        }
    }

    private static void clearTable(Class tableClazz) {
        SQLite.delete()
                .from(tableClazz)
                .execute();
    }

    private static void saveAll(List<? extends BaseModel> list) {
        for (BaseModel model : list) {
            model.save();
        }
    }

}
