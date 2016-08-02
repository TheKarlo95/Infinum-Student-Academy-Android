package hr.vrbic.karlo.pokemonapp.list;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import butterknife.BindView;
import butterknife.ButterKnife;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.model.Pokemon;

/**
 * {@code PokemonListAdapter} is an adapter that handles all of the data about Pokemons.
 *
 * @author Karlo Vrbić
 * @version 1.0
 * @see EmptyRecyclerView.Adapter
 */
public class PokemonListAdapter extends EmptyRecyclerView.Adapter<PokemonListAdapter.ViewHolder> {

    /**
     * Context of the {@linkplain RecyclerView}.
     */
    private Context context;
    /**
     * List of Pokemons displayed in {@linkplain RecyclerView}.
     */
    private List<Pokemon> pokemons;
    /**
     * Click listener that performs some operation when user clicks on one of the items displayed in {@linkplain RecyclerView}.
     */
    private EmptyRecyclerView.OnClickListener<Pokemon> clickListener;
    /**
     * Set of all change listeners.
     */
    private Set<EmptyRecyclerView.OnChangeListener> changeListeners;

    private int previousPosition;

    /**
     * Constructs a new {@code PokemonListAdapter} with specified parameters.
     *
     * @param context       context of the {@linkplain RecyclerView}
     * @param pokemons      list of Pokemons displayed in {@linkplain RecyclerView}
     * @param clickListener click listener
     */
    public PokemonListAdapter(Context context,
                              Collection<Pokemon> pokemons,
                              EmptyRecyclerView.OnClickListener<Pokemon> clickListener) {
        this.context = Objects.requireNonNull(context, "Parameter context cannot be null.");
        this.clickListener = clickListener;
        this.changeListeners = new HashSet<>();
        if (pokemons != null && !pokemons.isEmpty()) {
            this.pokemons = new ArrayList<>(pokemons);
            Collections.sort(this.pokemons);
            notifyDataSetChanged();
        }
    }

    /**
     * Constructs a new {@code PokemonListAdapter} with specified parameters.
     *
     * @param context  context of the {@linkplain RecyclerView}
     * @param pokemons list of Pokemons displayed in {@linkplain RecyclerView}
     */
    public PokemonListAdapter(Context context, Collection<Pokemon> pokemons) {
        this(context, pokemons, null);
    }

    /**
     * Constructs a new {@code PokemonListAdapter} with specified parameters.
     *
     * @param context context of the {@linkplain RecyclerView}
     */
    public PokemonListAdapter(Context context) {
        this(context, null);
    }

    @Override
    public PokemonListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.pokemon_list_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(PokemonListAdapter.ViewHolder holder, int position) {
        animate(holder, position);

        Pokemon pokemon = pokemons.get(position);

        String name = pokemon.getName();
        String imageUri = pokemon.getImageUriWithEndpoint();

        holder.tvPokemonName.setText(name);
        if (imageUri != null) {
            Glide.with(context).load(imageUri).into(holder.ivPokemonImage);
        }
    }

    private void animate(PokemonListAdapter.ViewHolder holder, int position) {
        ObjectAnimator animator = null;
        if (position > previousPosition) {
            animator = ObjectAnimator.ofFloat(holder, "translationY", 100, 0);
        } else {
            animator = ObjectAnimator.ofFloat(holder, "translationY", -100, 0);
        }
        animator.setDuration(1000);
        animator.start();
        previousPosition = position;
    }


    @Override
    public int getItemCount() {
        if (pokemons == null) {
            return 0;
        }
        return pokemons.size();
    }

    /**
     * Adds the specified change listener to the set of all change listeners.
     *
     * @param changeListener the change listener that is going to observe chnages on this adapter
     * @return {@code true} if change listener set is modified; {@code false} otherwise.
     */
    public boolean addChangeListener(EmptyRecyclerView.OnChangeListener changeListener) {
        return changeListeners.add(changeListener);
    }

    /**
     * Removes the specified change listener from the set of change listeners.
     *
     * @param changeListener the change listener that is going to be removed
     * @return {@code true} if change listener set is modified; {@code false} otherwise.
     */
    public boolean removeChangeListener(EmptyRecyclerView.OnChangeListener changeListener) {
        return changeListeners.remove(changeListener);
    }

    /**
     * Adds the specified PokemonInteractor to the adapter(if this PokemonInteractor haven't previously existed in the adapter).
     *
     * @param pokemon the PokemonInteractor to be added
     * @return {@code true} if pokemon adapter is modified; {@code false} otherwise.
     */
    public boolean add(Pokemon pokemon) {
        if (pokemons == null) {
            pokemons = new ArrayList<>();
        }

        if (pokemons.indexOf(pokemon) != -1) {
            return false;
        }

        pokemons.add(pokemon);
        Collections.sort(pokemons);

        int index = pokemons.indexOf(pokemon);
        fireRoundResultsAdded(index, index);
        notifyItemInserted(index);
        return true;
    }

    /**
     * Adds all Pokemons from the array to this adapter.
     *
     * @param pokemons array of new Pokemons to be added to this adapter
     */
    public void addAll(Pokemon[] pokemons) {
        if (pokemons == null)
            return;

        for (Pokemon pokemon : pokemons) {
            add(pokemon);
        }
    }

    public void addAll(Collection<Pokemon> pokemons) {
        if (pokemons == null || pokemons.isEmpty())
            return;

        for (Pokemon pokemon : pokemons) {
            add(pokemon);
        }
    }

    /**
     * Removes the specified PokemonInteractor from this adapter.
     *
     * @param pokemon PokemonInteractor to be removed
     * @return {@code true} if pokemon adapter is modified; {@code false} otherwise.
     */
    public boolean remove(Pokemon pokemon) {
        if (pokemons == null) {
            return false;
        }

        int index = pokemons.indexOf(pokemon);
        if (index != -1) {
            fireRoundResultsRemoved(index, index);
        }
        return pokemons.remove(pokemon);
    }

    /**
     * Returns the unmodifiable list of all Pokemons from this Adapter.
     *
     * @return the unmodifiable list of all Pokemons from this Adapter or {@code null} if there is no Pokemons in
     * this adapter.
     */
    public List<Pokemon> getAllPokemons() {
        return pokemons != null ? Collections.unmodifiableList(pokemons) : null;
    }

    /**
     * Notifies all change listeners about the added Pokemons.
     *
     * @param index0 index of the first added PokemonInteractor
     * @param index1 index of the last added PokemonInteractor
     */
    private void fireRoundResultsAdded(int index0, int index1) {
        for (EmptyRecyclerView.OnChangeListener changeListener : changeListeners) {
            changeListener.objectsAdded(this, index0, index1);
        }
    }

    /**
     * Notifies all change listeners about the removed Pokemons.
     *
     * @param index0 index of the first removed PokemonInteractor
     * @param index1 index of the last removed PokemonInteractor
     */
    private void fireRoundResultsRemoved(int index0, int index1) {
        for (EmptyRecyclerView.OnChangeListener changeListener : changeListeners) {
            changeListener.objectsRemoved(this, index0, index1);
        }
    }

    /**
     * {@code ViewHolder} is a describes an item view and metadata about its place within the
     * {@linkplain EmptyRecyclerView}.
     *
     * @author Karlo Vrbić
     * @version 1.0
     * @see EmptyRecyclerView.ViewHolder
     */
    protected class ViewHolder extends EmptyRecyclerView.ViewHolder {

        @BindView(R.id.tv_details_name)
        protected TextView tvPokemonName;
        @BindView(R.id.iv_pokemon_image)
        protected ImageView ivPokemonImage;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            if (clickListener != null) {
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        clickListener.onClick(pokemons.get(getAdapterPosition()));
                    }
                });
            }
        }
    }

}
