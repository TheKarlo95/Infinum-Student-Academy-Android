package hr.vrbic.karlo.pokemonapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.model.Ability;
import hr.vrbic.karlo.pokemonapp.model.Category;
import hr.vrbic.karlo.pokemonapp.model.Pokemon;
import hr.vrbic.karlo.pokemonapp.model.User;

public class DetailsFragment extends AbstractFragment {

    private static final String FRAGMENT_TAG = "details_pokemon";
    private static final String POKEMON = "pokemon";
    private static final String USER = "user";

    @BindView(R.id.tv_details_name)
    TextView tvName;
    @BindView(R.id.tv_details_height)
    TextView tvHeight;
    @BindView(R.id.tv_details_weight)
    TextView tvWeight;
    @BindView(R.id.tv_details_category)
    TextView tvCategory;
    @BindView(R.id.tv_details_abilities)
    TextView tvAbilities;
    @BindView(R.id.tv_details_description)
    TextView tvDescription;
    @BindView(R.id.iv_details_image)
    ImageView ivImage;

    private Unbinder unbinder;

    public DetailsFragment() {
        // Required empty public constructor
        // remove user
    }

    public static DetailsFragment newInstance(User user, Pokemon pokemon) {
        Objects.requireNonNull(pokemon, "Null parameter: pokemon");
        Bundle arguments = new Bundle();
        arguments.putParcelable(POKEMON, pokemon);
        arguments.putParcelable(USER, user);

        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    @Override
    public AbstractFragment copy() {
        Pokemon pokemon = getArguments().getParcelable(POKEMON);
        User user = getArguments().getParcelable(USER);
        Bundle arguments = new Bundle();
        arguments.putParcelable(POKEMON, pokemon);
        arguments.putParcelable(USER, user);

        DetailsFragment fragment = new DetailsFragment();
        fragment.setArguments(arguments);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_details, container, false);
        unbinder = ButterKnife.bind(this, view);

        Pokemon pokemon = getArguments().getParcelable(POKEMON);

        if (pokemon != null) {
            tvName.setText(pokemon.getName());
            tvHeight.setText(getString(R.string.height_in_meters, pokemon.getHeight()));
            tvWeight.setText(getString(R.string.weight_in_kilos, pokemon.getWeight()));
            tvCategory.setText(StringUtils.join(pokemon.getCategories(), ", "));
            updateAbilitiesList(pokemon);
            updateCategoriesList(pokemon);
            tvDescription.setText(pokemon.getDescription());
            String imageUri = pokemon.getImageUriWithEndpoint();

            if (imageUri != null) {
                Glide.with(this).load(imageUri).into(ivImage);
            }
        }

        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    private void updateAbilitiesList(Pokemon pokemon) {
        List<String> abilityNames = new ArrayList<>();
        List<Ability> abilities = pokemon.getAbilities();

        if (abilities != null) {
            for (Ability ability : abilities) {
                abilityNames.add(ability.getName());
            }

            if (tvAbilities != null) {
                tvAbilities.setText(StringUtils.join(abilityNames, "\n"));
            }
        }
    }

    private void updateCategoriesList(Pokemon pokemon) {
        List<String> categoryNames = new ArrayList<>();
        List<Category> categories = pokemon.getCategories();

        if (categories != null) {
            for (Category category : categories) {
                categoryNames.add(category.getName());
            }
            if (tvCategory != null) {
                tvCategory.setText(StringUtils.join(categoryNames, "\n"));
            }
        }
    }

}
