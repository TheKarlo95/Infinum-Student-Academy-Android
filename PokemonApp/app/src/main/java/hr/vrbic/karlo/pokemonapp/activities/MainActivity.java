package hr.vrbic.karlo.pokemonapp.activities;

import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.ButterKnife;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.fragments.AbstractFragment;
import hr.vrbic.karlo.pokemonapp.fragments.AddPokemonFragment;
import hr.vrbic.karlo.pokemonapp.fragments.DetailsFragment;
import hr.vrbic.karlo.pokemonapp.fragments.PokemonListFragment;
import hr.vrbic.karlo.pokemonapp.model.Category;
import hr.vrbic.karlo.pokemonapp.model.Pokemon;
import hr.vrbic.karlo.pokemonapp.model.TypeListResponse;
import hr.vrbic.karlo.pokemonapp.model.User;
import hr.vrbic.karlo.pokemonapp.network.ApiManager;
import hr.vrbic.karlo.pokemonapp.utilities.ApplicationUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements PokemonListFragment.OnFragmentInteractionListener {

    static final String USER = "user";
    private static final String CATEGORIES = "categories";
    private static final String FRAGMENT = "fragment";

    private int oldOrientation;
    private AbstractFragment fragment;
    private User user;

    private HashMap<Integer, String> categories;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setTitle(R.string.pokemon_title);

        ButterKnife.bind(this);

        if (savedInstanceState == null) {
            user = getIntent().getExtras().getParcelable(USER);
            getPokemonCategories();
            initActivityFragments();
        } else {
            user = savedInstanceState.getParcelable(USER);
            categories = (HashMap<Integer, String>) savedInstanceState.getSerializable(CATEGORIES);
            recreateActivityFragments(savedInstanceState);
        }

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
        outState.putSerializable(CATEGORIES, categories);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        fragment = (AbstractFragment) getSupportFragmentManager().getFragment(savedInstanceState, FRAGMENT);
        user = savedInstanceState.getParcelable(USER);
        categories = (HashMap<Integer, String>) savedInstanceState.getSerializable(CATEGORIES);
    }

    @Override
    public void onBackPressed() {
        if (ApplicationUtils.isTabletAndLandscape()) {
            finish();
        } else {
            if (fragment instanceof PokemonListFragment) {
                finish();
            } else if (fragment instanceof AddPokemonFragment){
                ((AddPokemonFragment) fragment).goBack();
            } else {
                replaceFragment(PokemonListFragment.newInstance(user), R.id.container);
            }
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
        AddPokemonFragment fragment = AddPokemonFragment.newInstance(user);
        if (ApplicationUtils.isTabletAndLandscape()) {
            replaceFragment(fragment, R.id.container2);
        } else {
            replaceFragment(fragment, R.id.container);
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
        for(Map.Entry<Integer, String> entry : this.categories.entrySet()) {
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
        Call<TypeListResponse> categoriesCall = ApiManager.getService().getAllTypes(user.getAuthorization());
        categoriesCall.enqueue(new Callback<TypeListResponse>() {
            @Override
            public void onResponse(Call<TypeListResponse> call, Response<TypeListResponse> response) {
                if (response.isSuccessful()) {
                    for (Category category : response.body().getCategories()) {
                        categories.put(category.getId(), category.getName());
                    }
                } else {
                    Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<TypeListResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    Toast.makeText(MainActivity.this, "failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

}
