package hr.vrbic.karlo.pokemonapp.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.beans.Pokemon;
import hr.vrbic.karlo.pokemonapp.list.EmptyRecyclerView;
import hr.vrbic.karlo.pokemonapp.list.PokemonListAdapter;


/**
 * {@code PokemonListActivity} is a launcher activity that display list of Pokemons.
 *
 * @author Karlo Vrbić
 * @version 1.0
 * @see AppCompatActivity
 */
public class PokemonListActivity extends AppCompatActivity {

    /**
     * Request code for starting add Pokemon activity.
     */
    private static final int ADD_POKEMON_REQUEST = 1;
    /**
     * Request code for starting details activity.
     */
    private static final int POKEMON_DETAILS_REQUEST = 2;

    /**
     * Key for Pokemon used in intent extras.
     */
    static final String POKEMON = "pokemon";

    /**
     * Key for Pokemons used in savedInstanceState.
     */
    static final String POKEMONS = "pokemons";

    /**
     * List of Pokemons.
     */
    @BindView(R.id.erv_pokemons)
    EmptyRecyclerView ervPokemons;
    /**
     * Linear layout displayed when list is empty.
     */
    @BindView(R.id.ll_empty_list)
    LinearLayout llEmptyList;

    /**
     * Adapter for {@linkplain EmptyRecyclerView ervPokemons} with Pokemon data.
     */
    private PokemonListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pokemon_list);
        setTitle(R.string.pokemon_title);

        ButterKnife.bind(this);

        this.adapter = new PokemonListAdapter(this, null, new EmptyRecyclerView.OnClickListener<Pokemon>() {
            @Override
            public void onClick(Pokemon pokemon) {
                Intent intent = new Intent(PokemonListActivity.this, DetailsActivity.class);
                Bundle extras = new Bundle();
                extras.putParcelable(POKEMON, pokemon);

                intent.putExtras(extras);
                startActivityForResult(intent, POKEMON_DETAILS_REQUEST);
            }
        });

        ervPokemons.setAdapter(adapter);
        ervPokemons.setEmptyView(llEmptyList);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);

        List<Pokemon> pokemonList = adapter.getAllPokemons();

        if (pokemonList != null && !pokemonList.isEmpty()) {
            Pokemon[] pokemons = adapter.getAllPokemons().toArray(new Pokemon[1]);
            outState.putParcelableArray(POKEMONS, pokemons);
        }
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        Pokemon[] pokemons = (Pokemon[]) savedInstanceState.getParcelableArray(POKEMONS);
        adapter.addAll(pokemons);
    }

    @Override
    public Object onRetainCustomNonConfigurationInstance() {
        return adapter.getAllPokemons();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_pokemon_list, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.add_pokemon:
                Intent intent = new Intent(PokemonListActivity.this, AddPokemonActivity.class);
                startActivityForResult(intent, ADD_POKEMON_REQUEST);
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == ADD_POKEMON_REQUEST) {
            if (resultCode == Activity.RESULT_OK && data != null) {
                Bundle extras = data.getExtras();
                Pokemon pokemon = extras.getParcelable(POKEMON);

                if (pokemon != null && !adapter.add(pokemon)) {
                    Toast.makeText(this, getString(R.string.pokemon_already_exists, pokemon.getName()),
                            Toast.LENGTH_LONG).show();
                }
            }
        }
    }

}
