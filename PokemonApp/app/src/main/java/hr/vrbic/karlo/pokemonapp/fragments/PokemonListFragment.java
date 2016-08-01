package hr.vrbic.karlo.pokemonapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.list.EmptyRecyclerView;
import hr.vrbic.karlo.pokemonapp.list.PokemonListAdapter;
import hr.vrbic.karlo.pokemonapp.model.Pokemon;
import hr.vrbic.karlo.pokemonapp.model.PokemonListResponse;
import hr.vrbic.karlo.pokemonapp.model.User;
import hr.vrbic.karlo.pokemonapp.network.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PokemonListFragment extends AbstractFragment {

    public static final String FRAGMENT_TAG = "pokemon_list";
    private static final String USER = "user";
    private static final String POKEMONS = "pokemons";

    @BindView(R.id.erv_pokemons)
    EmptyRecyclerView ervPokemons;
    @BindView(R.id.ll_empty_list)
    LinearLayout llEmptyList;

    Call<PokemonListResponse> pokedexCall = null;

    private PokemonListAdapter adapter;
    private User user;
    private Unbinder unbinder;
    private OnFragmentInteractionListener listener;


    public PokemonListFragment() {
        // Required empty public constructor
    }

    public static PokemonListFragment newInstance(User user) {
        Objects.requireNonNull(user, "Null parameter: user");
        PokemonListFragment fragment = new PokemonListFragment();
        fragment.user = user;
        fragment.pokedexCall = ApiManager.getService().getAllPokemons(user.getAuthorization());
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
            pokedexCall = ApiManager.getService().getAllPokemons(user.getAuthorization());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pokemon_list, container, false);
        unbinder = ButterKnife.bind(this, view);

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

    private void getPokemonList() {
        pokedexCall.enqueue(new Callback<PokemonListResponse>() {
            @Override
            public void onResponse(Call<PokemonListResponse> call, Response<PokemonListResponse> response) {
                if (response.isSuccessful()) {
                    setPokemonList(response.body().getPokemonList());
                } else {
                    setPokemonList(null);
                    Toast.makeText(getActivity(), "Cannot get pokemon list. You are probably offline",
                            Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<PokemonListResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    setPokemonList(null);
                    Toast.makeText(getActivity(), "Cannot get pokemon list. You are probably offline",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
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
