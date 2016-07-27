package hr.vrbic.karlo.pokemonapp.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.model.Pokemon;
import hr.vrbic.karlo.pokemonapp.model.PokemonsList;
import hr.vrbic.karlo.pokemonapp.list.EmptyRecyclerView;
import hr.vrbic.karlo.pokemonapp.list.PokemonListAdapter;

public class PokemonListFragment extends AbstractFragment {

    public static final String FRAGMENT_TAG = "pokemon_list";
    private static final String POKEMONS = "pokemons";

    @BindView(R.id.erv_pokemons)
    EmptyRecyclerView ervPokemons;
    @BindView(R.id.ll_empty_list)
    LinearLayout llEmptyList;

    private PokemonListAdapter adapter;

    private Unbinder unbinder;
    private OnFragmentInteractionListener listener;

    public PokemonListFragment() {
        // Required empty public constructor
    }

    public static PokemonListFragment newInstance() {
        return new PokemonListFragment();
    }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    @Override
    public AbstractFragment copy() {
        return new PokemonListFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
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
        adapter.addAll(PokemonsList.getAll());

        ervPokemons.setAdapter(adapter);
        ervPokemons.setEmptyView(llEmptyList);

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

        if(adapter != null) {
            List<Pokemon> pokemonList = adapter.getAllPokemons();

            if (pokemonList != null && !pokemonList.isEmpty()) {
                Pokemon[] pokemons = adapter.getAllPokemons().toArray(new Pokemon[1]);
                outState.putParcelableArray(POKEMONS, pokemons);
            }
        }
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

    public interface OnFragmentInteractionListener {

        void openAddPokemon();

        void openPokemonDetails(Pokemon pokemon);

    }
}
