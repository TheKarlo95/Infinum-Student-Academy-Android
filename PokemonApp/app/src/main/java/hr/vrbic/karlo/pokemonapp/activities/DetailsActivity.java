package hr.vrbic.karlo.pokemonapp.activities;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import butterknife.BindView;
import butterknife.ButterKnife;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.model.Pokemon;

/**
 * {@code DetailsActivity} is an activity that displays the Pokemon's name and description.
 *
 * @author Karlo Vrbić
 * @version 1.0
 * @see AppCompatActivity
 */
public class DetailsActivity extends AppCompatActivity {

    /**
     * {@linkplain TextView} for displaying name of the Pokemon.
     */
    @BindView(R.id.tv_details_name)
    TextView tvName;
    /**
     * {@linkplain TextView} for displaying height of the Pokemon.
     */
    @BindView(R.id.tv_details_height)
    TextView tvHeight;
    /**
     * {@linkplain TextView} for displaying weight of the Pokemon.
     */
    @BindView(R.id.tv_details_weight)
    TextView tvWeight;
    /**
     * {@linkplain TextView} for displaying category of the Pokemon.
     */
    @BindView(R.id.tv_details_category)
    TextView tvCategory;
    /**
     * {@linkplain TextView} for displaying abilities of the Pokemon.
     */
    @BindView(R.id.tv_details_abilities)
    TextView tvAbilities;
    /**
     * {@linkplain TextView} for displaying description of the Pokemon.
     */
    @BindView(R.id.tv_details_gender)
    TextView tvGender;
    /**
     * {@linkplain TextView} for displaying description of the Pokemon.
     */
    @BindView(R.id.tv_details_description)
    TextView tvDescription;
    /**
     * {@linkplain ImageView} for displaying image of the Pokemon.
     */
    @BindView(R.id.iv_details_image)
    ImageView ivImage;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.details_title);

        ButterKnife.bind(this);

        Pokemon pokemon = getIntent().getExtras().getParcelable(PokemonListActivity.POKEMON);

        tvName.setText(pokemon.getName());
        tvHeight.setText(getString(R.string.height_in_meters, pokemon.getHeight()));
        tvWeight.setText(getString(R.string.weight_in_kilos, pokemon.getWeight()));
        tvCategory.setText(pokemon.getCategory());
        tvAbilities.setText(pokemon.getAbilities());
        tvGender.setText(pokemon.getGender());
        tvDescription.setText(pokemon.getDescription());
        ivImage.setImageURI(pokemon.getImageUri());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(Activity.RESULT_OK, null);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
