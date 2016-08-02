package hr.vrbic.karlo.pokemonapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import hr.vrbic.karlo.pokemonapp.Consumer;
import hr.vrbic.karlo.pokemonapp.PokemonInteractor;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.fragments.AbstractFragment;
import hr.vrbic.karlo.pokemonapp.fragments.AddPokemonFragment;
import hr.vrbic.karlo.pokemonapp.fragments.DetailsFragment;
import hr.vrbic.karlo.pokemonapp.fragments.PokemonListFragment;
import hr.vrbic.karlo.pokemonapp.model.Category;
import hr.vrbic.karlo.pokemonapp.model.Pokemon;
import hr.vrbic.karlo.pokemonapp.model.User;
import hr.vrbic.karlo.pokemonapp.utilities.ApplicationUtils;
import hr.vrbic.karlo.pokemonapp.utilities.NetworkUtils;
import hr.vrbic.karlo.pokemonapp.utilities.ToastUtils;

public class MainActivity extends AppCompatActivity implements PokemonListFragment.OnFragmentInteractionListener {

    static final String USER = "user";
    private static final String FRAGMENT = "fragment";

    private int oldOrientation;
    private AbstractFragment fragment;
    private User user;

    private HashMap<Integer, String> categories;

    private PokemonInteractor interactor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.pokemon_title);

        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras != null) {
                user = getIntent().getExtras().getParcelable(USER);
            }
            initActivityFragments();
        } else {
            user = savedInstanceState.getParcelable(USER);
            recreateActivityFragments(savedInstanceState);
        }

        interactor = new PokemonInteractor(this, user);
        getPokemonCategories();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (fragment instanceof PokemonListFragment && !ApplicationUtils.isTabletAndLandscape()) {
            getMenuInflater().inflate(R.menu.menu_add_button, menu);
        } else {
            getMenuInflater().inflate(R.menu.menu_empty, menu);
        }
        return true;
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        if (fragment != null) {
            getSupportFragmentManager().putFragment(outState, FRAGMENT, fragment);
        }
        outState.putParcelable(USER, user);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fragment = (AbstractFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT);
        user = savedInstanceState.getParcelable(USER);
        interactor = new PokemonInteractor(this, user);
    }

    @Override
    public void onBackPressed() {
        if (ApplicationUtils.isTabletAndLandscape()) {
            finish();
        } else {
            if (fragment instanceof PokemonListFragment) {
                finish();
            } else if (fragment instanceof AddPokemonFragment) {
                ((AddPokemonFragment) fragment).goBack();
            } else {
                replaceFragment(PokemonListFragment.newInstance(user), R.id.container);
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (interactor != null) {
            interactor.cancelAllCalls();
        }
    }

    @Override
    public void openPokemonDetails(Pokemon pokemon) {
        DetailsFragment fragment = DetailsFragment.newInstance(user, pokemon);
        if (ApplicationUtils.isTabletAndLandscape()) {
            replaceFragment(fragment, R.id.container2);
        } else {
            replaceFragment(fragment, R.id.container);
        }
    }

    @Override
    public void openAddPokemon() {
        if (NetworkUtils.isNetworkAvailable()) {
            AddPokemonFragment fragment = AddPokemonFragment.newInstance(user);
            if (ApplicationUtils.isTabletAndLandscape()) {
                replaceFragment(fragment, R.id.container2);
            } else {
                replaceFragment(fragment, R.id.container);
            }
        } else {
            ToastUtils.showToast(this, R.string.add_pokemon_fail_no_connection);
        }
    }

    /**
     * Replaces fragment in the container view with ID
     * {@linkplain R.id#container1 R.id.container1} with {@linkplain PokemonListFragment} and
     * doesn't save the fragment.
     */

    public void replaceFirstFragmentWithList() {
        replaceFragment(PokemonListFragment.newInstance(user), R.id.container1, false);
    }

    /**
     * Replaces fragment in the container view with ID specified by {@code containerViewId} and saves the fragment.
     * If fragment of the same class is already in the container nothing happens.
     *
     * @param fragment        the fragment that is going to be added to container view
     * @param containerViewId the ID of the container view
     */
    public void replaceFragment(AbstractFragment fragment, int containerViewId) {
        if (oldOrientation == getResources().getConfiguration().orientation) {
            replaceFragment(fragment, containerViewId, true);
        }
    }

    /**
     * Replaces fragment in the container view with ID specified by {@code containerViewId}.
     *
     * @param fragment        the fragment that is going to be added to container view
     * @param containerViewId the ID of the container view
     * @param save            indicates whether this activity should remember this fragment
     */
    public void replaceFragment(AbstractFragment fragment, int containerViewId, boolean save) {
        AbstractFragment copy = fragment.copy();
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewId, copy);
        transaction.addToBackStack(copy.getFragmentTag());
        transaction.commit();
        if (save) {
            this.fragment = copy;
        }
    }

    public Map<Integer, String> getCategories() {
        return categories != null ? Collections.unmodifiableMap(categories) : null;
    }

    public List<Category> getCategoryList() {
        List<Category> categories = new ArrayList<>();
        for (Map.Entry<Integer, String> entry : this.categories.entrySet()) {
            categories.add(new Category(entry.getKey(), entry.getValue()));
        }
        return categories != null ? Collections.unmodifiableList(categories) : null;
    }

    private void initActivityFragments() {
        oldOrientation = getResources().getConfiguration().orientation;

        if (ApplicationUtils.isTabletAndLandscape()) {
            replaceFirstFragmentWithList();
            replaceFragment(AddPokemonFragment.newInstance(user), R.id.container2);
        } else {
            replaceFragment(PokemonListFragment.newInstance(user), R.id.container);
        }
    }

    private void recreateActivityFragments(Bundle savedInstanceState) {
        fragment = (AbstractFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT);

        if (ApplicationUtils.isTablet()) {
            if (ApplicationUtils.isLandscape()) {
                replaceFirstFragmentWithList();
                if (fragment instanceof PokemonListFragment) {
                    replaceFragment(AddPokemonFragment.newInstance(user), R.id.container2);
                } else {
                    replaceFragment(fragment, R.id.container2);
                }
            } else {
                replaceFragment(PokemonListFragment.newInstance(user), R.id.container);
            }
        } else {
            replaceFragment(fragment, R.id.container);
        }
        oldOrientation = getResources().getConfiguration().orientation;
    }

    private void getPokemonCategories() {
        categories = new HashMap<>();
        interactor.getAllCategories(new Consumer<List<Category>>() {
            @Override
            public void accept(List<Category> categories) {
                if (categories != null) {
                    for (Category category : categories) {
                        MainActivity.this.categories.put(category.getId(), category.getName());
                    }
                } else {
                    ToastUtils.showToast(MainActivity.this, R.string.get_categories_fail);
                }
            }
        });
    }

}
