package hr.vrbic.karlo.pokemonapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.model.Pokemon;

public class DetailsFragment extends AbstractFragment {

    private static final String FRAGMENT_TAG = "details_pokemon";
    private static final String POKEMON = "pokemon";

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
    @BindView(R.id.tv_details_gender)
    TextView tvGender;
    @BindView(R.id.tv_details_description)
    TextView tvDescription;
    @BindView(R.id.iv_details_image)
    ImageView ivImage;

    private Unbinder unbinder;

    public DetailsFragment() {
        // Required empty public constructor
    }

    public static DetailsFragment newInstance(Pokemon pokemon) {
        Objects.requireNonNull(pokemon, "Null parameter: pokemon");
        Bundle arguments = new Bundle();
        arguments.putParcelable(POKEMON, pokemon);

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
        Bundle arguments = new Bundle();
        arguments.putParcelable(POKEMON, pokemon);

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
            tvCategory.setText(pokemon.getCategory());
            tvAbilities.setText(pokemon.getAbilities());
            tvGender.setText(pokemon.getGender());
            tvDescription.setText(pokemon.getDescription());
            ivImage.setImageURI(pokemon.getImageUri());
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

}
