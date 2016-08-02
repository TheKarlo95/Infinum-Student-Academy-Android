package hr.vrbic.karlo.pokemonapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.raizlabs.android.dbflow.structure.database.transaction.QueryTransaction;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hr.vrbic.karlo.pokemonapp.Consumer;
import hr.vrbic.karlo.pokemonapp.PokemonInteractor;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.list.EmptyRecyclerView;
import hr.vrbic.karlo.pokemonapp.list.PokemonListAdapter;
import hr.vrbic.karlo.pokemonapp.model.Pokemon;
import hr.vrbic.karlo.pokemonapp.model.PokemonListResponse;
import hr.vrbic.karlo.pokemonapp.model.User;
import hr.vrbic.karlo.pokemonapp.network.ApiManager;
import hr.vrbic.karlo.pokemonapp.utilities.NetworkUtils;
import hr.vrbic.karlo.pokemonapp.utilities.ToastUtils;
import retrofit2.Call;

public class PokemonListFragment extends AbstractFragment {

    public static final String FRAGMENT_TAG = "pokemon_list";
    private static final String USER = "user";

    @BindView(R.id.erv_pokemons)
    EmptyRecyclerView ervPokemons;
    @BindView(R.id.ll_empty_list)
    LinearLayout llEmptyList;
    @BindView(R.id.srl_list)
    SwipeRefreshLayout swipeRefreshLayout;

    Call<PokemonListResponse> pokedexCall = null;

    private PokemonListAdapter adapter;
    private User user;
    private Unbinder unbinder;
    private OnFragmentInteractionListener listener;
    private PokemonInteractor interactor;

    public PokemonListFragment() {
        // Required empty public constructor
    }

    public static PokemonListFragment newInstance(@Nullable User user) {
        PokemonListFragment fragment = new PokemonListFragment();
        if (user != null) {
            fragment.user = user;
            fragment.pokedexCall = ApiManager.getService().getAllPokemons(user.getAuthorization());
        }
        return fragment;
    }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    @Override
    public AbstractFragment copy() {
        PokemonListFragment fragment = new PokemonListFragment();
        fragment.user = this.user;
        fragment.pokedexCall = this.pokedexCall;
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            user = savedInstanceState.getParcelable(USER);
            if (user != null) {
                pokedexCall = ApiManager.getService().getAllPokemons(user.getAuthorization());
            }
        }
        interactor = new PokemonInteractor(getContext(), user);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemon_list, container, false);

        unbinder = ButterKnife.bind(this, view);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (NetworkUtils.isNetworkAvailable() && user != null) {
                    interactor.getAllPokemons(new Consumer<List<Pokemon>>() {
                        @Override
                        public void accept(List<Pokemon> pokemons) {
                            setPokemonList(pokemons);
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    });
                } else {
                    ToastUtils.showToast(getContext(), R.string.refresh_fail_no_connection);
                    swipeRefreshLayout.setRefreshing(false);
                }
            }
        });

        this.adapter = new PokemonListAdapter(getContext(), null, new EmptyRecyclerView.OnClickListener<Pokemon>() {
            @Override
            public void onClick(Pokemon pokemon) {
                listener.openPokemonDetails(pokemon);
            }
        });
        getPokemonList();

        return view;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            listener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(USER, user);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
        adapter = null;
        user = null;
        interactor = null;
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.item_add_pokemon:
                listener.openAddPokemon();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onStop() {
        super.onStop();
        interactor.cancelAllCalls();
    }

    private void getPokemonList() {
        if (NetworkUtils.isNetworkAvailable() && user != null) {
            interactor.getAllPokemons(new Consumer<List<Pokemon>>() {
                @Override
                public void accept(List<Pokemon> pokemons) {
                    setPokemonList(pokemons);
                }
            });
        } else {
            SQLite.select()
                    .from(Pokemon.class)
                    .async()
                    .queryListResultCallback(new QueryTransaction.QueryResultListCallback<Pokemon>() {
                        @Override
                        public void onListQueryResult(QueryTransaction transaction, @Nullable List<Pokemon> tResult) {
                            setPokemonList(tResult);
                        }
                    }).execute();
        }
    }

    private void setPokemonList(List<Pokemon> list) {
        adapter = new PokemonListAdapter(getContext(), list, new EmptyRecyclerView.OnClickListener<Pokemon>() {
            @Override
            public void onClick(Pokemon pokemon) {
                listener.openPokemonDetails(pokemon);
            }
        });
        if (ervPokemons != null) {
            ervPokemons.setLayoutManager(new LinearLayoutManager(getActivity()));
            ervPokemons.setAdapter(adapter);
            ervPokemons.setEmptyView(llEmptyList);
        }
    }

    public interface OnFragmentInteractionListener {

        void openAddPokemon();

        void openPokemonDetails(Pokemon pokemon);

    }

}
