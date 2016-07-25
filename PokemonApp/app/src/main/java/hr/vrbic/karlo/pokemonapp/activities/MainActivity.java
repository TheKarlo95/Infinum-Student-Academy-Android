package hr.vrbic.karlo.pokemonapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import butterknife.ButterKnife;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.beans.Pokemon;
import hr.vrbic.karlo.pokemonapp.fragments.AbstractFragment;
import hr.vrbic.karlo.pokemonapp.fragments.AddPokemonFragment;
import hr.vrbic.karlo.pokemonapp.fragments.DetailsFragment;
import hr.vrbic.karlo.pokemonapp.fragments.PokemonListFragment;

public class MainActivity extends AppCompatActivity implements PokemonListFragment.OnFragmentInteractionListener {

    public static final String POKEMON_LIST_TAG = "pokemon_list";
    public static final String ADD_POKEMON_TAG = "add_pokemon";
    private static final String DETAILS_TAG = "details_pokemon";

    private static final String FRAGMENT = "fragment";
    private static final String FRAGMENT_TAG = "fragment_tag";
    private static final String FRAGMENT_2 = "fragment_2";
    private static final String FRAGMENT_TAG_2 = "fragment_tag_2";

    public AbstractFragment fragment;
    private String fragmentTag;

    private AbstractFragment fragment2;
    private String fragmentTag2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.pokemon_title);

        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            if (getResources().getBoolean(R.bool.is_tablet_landscape)) {
                replaceFragmentTabletLandscape(PokemonListFragment.newInstance(), POKEMON_LIST_TAG, true);
                replaceFragmentTabletLandscape(AddPokemonFragment.newInstance(), ADD_POKEMON_TAG, false);
            } else {
                replaceFragment(PokemonListFragment.newInstance(), POKEMON_LIST_TAG);
            }
        } else {
            fragment2 = (AbstractFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT_2);
            fragmentTag2 = savedInstanceState.getString(FRAGMENT_TAG_2);
            fragment = (AbstractFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT);
            fragmentTag = savedInstanceState.getString(FRAGMENT_TAG);

            if (fragment2 != null && fragmentTag2 != null) {
                if (getResources().getBoolean(R.bool.is_tablet_landscape)) {
                    replaceFragmentTabletLandscape(PokemonListFragment.newInstance(), POKEMON_LIST_TAG, true);
                    replaceFragmentTabletLandscape(fragment2, fragmentTag2, false);
                } else {
                    replaceFragment(fragment2, fragmentTag2);
                }
            } else {
                // second fragment haven't been used so far
                if (getResources().getBoolean(R.bool.is_tablet_landscape)) {
                    replaceFragmentTabletLandscape(PokemonListFragment.newInstance(), POKEMON_LIST_TAG, true);
                    if (fragment instanceof PokemonListFragment) {
                        // portrait -> landscape; fragment was PokemonListFragment
                        replaceFragmentTabletLandscape(AddPokemonFragment.newInstance(), ADD_POKEMON_TAG, false);
                    } else {
                        // portrait -> landscape; fragment was something else so its alright
                        replaceFragmentTabletLandscape(fragment, fragmentTag, false);
                    }
                } else {
                    // isn't tablet on landscape
                    replaceFragment(fragment, fragmentTag);
                }
            }
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!getResources().getBoolean(R.bool.is_tablet_landscape) && !(fragment instanceof PokemonListFragment)) {
            getMenuInflater().inflate(R.menu.menu_empty, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_add_button, menu);
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment != null && fragmentTag != null) {
            getSupportFragmentManager().putFragment(outState, FRAGMENT, fragment);
            outState.putString(FRAGMENT_TAG, fragmentTag);
        }
        if (fragment2 != null && fragmentTag2 != null) {
            getSupportFragmentManager().putFragment(outState, FRAGMENT_2, fragment2);
            outState.putString(FRAGMENT_TAG_2, fragmentTag2);
        }
    }

    @Override
    public void onBackPressed() {
        if (getResources().getBoolean(R.bool.is_tablet_landscape)) {
            if (fragment2 instanceof AddPokemonFragment) {
                ((AddPokemonFragment) fragment2).goBack();
            }
            finish();
        } else {
            if (fragment instanceof PokemonListFragment) {
                finish();
            } else if (fragment instanceof AddPokemonFragment) {
                ((AddPokemonFragment) fragment).goBack();
            } else if (fragment instanceof DetailsFragment) {
                replaceFragment(PokemonListFragment.newInstance(), POKEMON_LIST_TAG);
            }
        }
    }

    @Override
    public void openPokemonDetails(Pokemon pokemon) {
        if (getResources().getBoolean(R.bool.is_tablet_landscape)) {
            replaceFragmentTabletLandscape(DetailsFragment.newInstance(pokemon), DETAILS_TAG, false);
        } else {
            replaceFragment(DetailsFragment.newInstance(pokemon), DETAILS_TAG);
        }
    }

    @Override
    public void openAddPokemon() {
        if (getResources().getBoolean(R.bool.is_tablet_landscape)) {
            replaceFragmentTabletLandscape(AddPokemonFragment.newInstance(), ADD_POKEMON_TAG, false);
        } else {
            replaceFragment(AddPokemonFragment.newInstance(), ADD_POKEMON_TAG);
        }
    }

    public void replaceFragment(AbstractFragment fragment, String tag) {
        this.fragment = fragment.copy();
        this.fragmentTag = tag;
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.container, this.fragment);
        transaction.addToBackStack(tag);
        transaction.commit();
    }

    public void replaceFragmentTabletLandscape(AbstractFragment fragment, String tag, boolean first) {
        if (first) {
            this.fragment = fragment.copy();
            this.fragmentTag = tag;
        } else {
            this.fragment2 = fragment.copy();
            this.fragmentTag2 = tag;
        }
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        if (first) {
            transaction.replace(R.id.container1, this.fragment);
        } else {
            transaction.replace(R.id.container2, this.fragment2);
        }
        transaction.addToBackStack(tag);
        transaction.commit();
    }

}
