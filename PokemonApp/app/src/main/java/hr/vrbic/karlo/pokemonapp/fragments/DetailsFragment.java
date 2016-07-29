package hr.vrbic.karlo.pokemonapp.fragments;

import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.activities.MainActivity;
import hr.vrbic.karlo.pokemonapp.model.MoveResponse;
import hr.vrbic.karlo.pokemonapp.model.Pokemon;
import hr.vrbic.karlo.pokemonapp.model.User;
import hr.vrbic.karlo.pokemonapp.network.ApiManager;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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
        User user = getArguments().getParcelable(USER);

        if (pokemon != null) {
            tvName.setText(pokemon.getName());
            tvHeight.setText(getString(R.string.height_in_meters, pokemon.getHeight()));
            tvWeight.setText(getString(R.string.weight_in_kilos, pokemon.getWeight()));
            tvCategory.setText(StringUtils.join(pokemon.getCategories(), ", "));
            getAbilities(user, pokemon);
            updateCategories(pokemon);
            tvDescription.setText(pokemon.getDescription());
            Uri imageUri = pokemon.getImageUri();
            if (imageUri != null) {
                Glide.with(this).load(imageUri).into(ivImage);
            } else {
                ivImage.setImageDrawable(null);
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

    private void getAbilities(User user, Pokemon pokemon) {
        final List<Integer> abilitiesIds = pokemon.getAbilities();
        final List<String> moveNames = new ArrayList<>();

        if (abilitiesIds != null) {
            for (Integer id : abilitiesIds) {
                final Call<MoveResponse> moveCall = ApiManager.getService().getMove(user.getAuthorization(), id);
                moveCall.enqueue(new Callback<MoveResponse>() {
                    @Override
                    public void onResponse(Call<MoveResponse> call, Response<MoveResponse> response) {
                        if (response.isSuccessful()) {
                            moveNames.add(response.body().getMoveName());
                            updateAbilities(moveNames);
                        } else {
                            Toast.makeText(getActivity(), "Failed to load move", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<MoveResponse> call, Throwable t) {
                        if (!call.isCanceled()) {
                            Toast.makeText(getActivity(), "Failed to load move", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        }
    }

    private void updateAbilities(List<String> abilities) {
        if (tvAbilities != null) {
            tvAbilities.setText(StringUtils.join(abilities, "\n"));
        }
    }

    private void updateCategories(Pokemon pokemon) {
        Map<Integer, String> allCategories = ((MainActivity) getActivity()).getCategories();

        List<String> categories = new ArrayList<>();
        List<Integer> categoryIds = pokemon.getCategories();

        if (categoryIds != null) {
            for (Integer id : categoryIds) {
                if (allCategories.keySet().contains(id)) {
                    categories.add(allCategories.get(id));
                }
            }
            if (tvCategory != null) {
                tvCategory.setText(StringUtils.join(categories, "\n"));
            }
        }
    }

}
