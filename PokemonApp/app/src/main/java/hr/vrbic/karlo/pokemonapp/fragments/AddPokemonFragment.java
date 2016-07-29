package hr.vrbic.karlo.pokemonapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.activities.MainActivity;
import hr.vrbic.karlo.pokemonapp.checkboxlist.AbilityAdapter;
import hr.vrbic.karlo.pokemonapp.checkboxlist.CategoryAdapter;
import hr.vrbic.karlo.pokemonapp.checkboxlist.NonScrollListView;
import hr.vrbic.karlo.pokemonapp.model.Ability;
import hr.vrbic.karlo.pokemonapp.model.Category;
import hr.vrbic.karlo.pokemonapp.model.MoveListResponse;
import hr.vrbic.karlo.pokemonapp.model.Pokemon;
import hr.vrbic.karlo.pokemonapp.model.User;
import hr.vrbic.karlo.pokemonapp.network.ApiManager;
import hr.vrbic.karlo.pokemonapp.utilities.ApplicationUtils;
import hr.vrbic.karlo.pokemonapp.utilities.PermissionUtil;
import hr.vrbic.karlo.pokemonapp.utilities.StringUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPokemonFragment extends AbstractFragment {

    private static final String FRAGMENT_TAG = "add_pokemon";
    private static final int REQUEST_CODE_PERMISSION = 1;
    private static final int REQ_CODE_PICK_IMAGE = 2;
    private static final String USER = "user";
    private static final String IMAGE_URI = "image_uri";

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_height)
    EditText etHeight;
    @BindView(R.id.et_weight)
    EditText etWeight;
    @BindView(R.id.lv_categories)
    NonScrollListView lvCategories;
    @BindView(R.id.lv_abilities)
    NonScrollListView lvAbilities;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.iv_image)
    ImageView ivImage;

    private Uri imageUri;

    private Unbinder unbinder;

    private CategoryAdapter categoryAdapter;
    private AbilityAdapter abilityAdapter;
    private User user;
    private Call<MoveListResponse> movesCall;

    public AddPokemonFragment() {
        // Required empty public constructor
    }

    public static AddPokemonFragment newInstance(User user) {
        Objects.requireNonNull(user, "Null parameter: user");
        AddPokemonFragment fragment = new AddPokemonFragment();
        fragment.user = user;
        fragment.movesCall = ApiManager.getService().getAllMoves(user.getAuthorization());
        return fragment;
    }

    @Override
    public String getFragmentTag() {
        return FRAGMENT_TAG;
    }

    @Override
    public AbstractFragment copy() {
        AddPokemonFragment fragment = new AddPokemonFragment();
        fragment.user = this.user;
        fragment.movesCall = this.movesCall;
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if (savedInstanceState != null) {
            user = savedInstanceState.getParcelable(USER);
            movesCall = ApiManager.getService().getAllMoves(user.getAuthorization());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_pokemon, container, false);

        unbinder = ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getParcelable(IMAGE_URI);
            ivImage.setImageURI(imageUri);
            user = savedInstanceState.getParcelable(USER);
        }

        List<Category> categories = ((MainActivity) getActivity()).getCategoryList();
        categoryAdapter = new CategoryAdapter(getContext(), categories, new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int pos = lvCategories.getPositionForView(buttonView);
                if (pos != ListView.INVALID_POSITION) {
                    CategoryAdapter.CategoryItem category = categoryAdapter.getCategoryItem(pos);
                    category.setSelected(isChecked);
                }
            }
        });
        lvCategories.setAdapter(categoryAdapter);

        getAbilities();

        return view;
    }

    private void getAbilities() {
        movesCall.enqueue(new Callback<MoveListResponse>() {
            @Override
            public void onResponse(Call<MoveListResponse> call, Response<MoveListResponse> response) {
                if (response.isSuccessful()) {
                    List<Ability> abilities = new ArrayList<>();
                    abilities.addAll(response.body().getAbilities());
                    abilityAdapter = new AbilityAdapter(getContext(), abilities, new CompoundButton.OnCheckedChangeListener() {
                        @Override
                        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                            int pos = lvAbilities.getPositionForView(buttonView);
                            if (pos != ListView.INVALID_POSITION) {
                                AbilityAdapter.AbilityItem ability = abilityAdapter.getAbilityItem(pos);
                                ability.setSelected(isChecked);
                            }
                        }
                    });
                    lvAbilities.setAdapter(abilityAdapter);
                } else {
                    Toast.makeText(getActivity(), "failed", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<MoveListResponse> call, Throwable t) {
                if (!call.isCanceled()) {
                    Toast.makeText(getActivity(), "failed", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(IMAGE_URI, imageUri);
        outState.putParcelable(USER, user);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();

        if (unbinder != null) {
            unbinder.unbind();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_PICK_IMAGE:
                if (resultCode == Activity.RESULT_OK) {
                    imageUri = data.getData();
                    ivImage.setImageURI(imageUri);
                }
                break;
        }
    }

    @OnClick(R.id.fab_add_image)
    public void onAddImageClick() {
        PermissionUtil.doExternalStorageOperation(getContext(), getActivity(), new Runnable() {
                    @Override
                    public void run() {
                        startActivityForResult(getImagePickIntent(), REQ_CODE_PICK_IMAGE);
                    }
                }
        );
    }

    @OnClick(R.id.btn_save)
    public void onSaveClick() {
        String name = StringUtils.getString(etName);
        String strHeight = etHeight.getText().toString();
        strHeight = strHeight.isEmpty() ? "0" : strHeight.trim();
        String strWeight = etWeight.getText().toString();
        strWeight = strWeight.isEmpty() ? "0" : strWeight.trim();
        double height = Double.parseDouble(strHeight);
        double weight = Double.parseDouble(strWeight);
        List<Integer> categories = categoryAdapter.getSelected();
        List<Integer> abilities = abilityAdapter.getSelected();
        String description = etDescription.getText().toString();

        if (name != null) {
            try {
                Pokemon pokemon = new Pokemon(name, height, weight, categories, abilities, description, imageUri);

                RequestBody image = RequestBody.create(MediaType.parse("application/octet-stream"),
                        pokemon.getImageUri().toString());
                Call<Void> call = ApiManager.getService().createPokemon(user.getAuthorization(),
                        name,
                        pokemon.getHeight(),
                        pokemon.getWeight(),
                        1,
                        pokemon.getDescription(),
                        convertIntegers(pokemon.getCategories()),
                        convertIntegers(pokemon.getAbilities()),
                        image);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            Toast.makeText(getActivity(), "Pokemon successfully created", Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getActivity(), "Pokemon creation failed", Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        if (!call.isCanceled()) {
                            Toast.makeText(getActivity(), "Pokemon creation failed", Toast.LENGTH_LONG).show();
                        }
                    }
                });
                finish();
            } catch (NullPointerException | IllegalArgumentException e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
    }

    public void goBack() {
        if (!etName.getText().toString().isEmpty()
                || !etHeight.getText().toString().isEmpty()
                || !etWeight.getText().toString().isEmpty()
                || categoryAdapter.isChecked()
                || abilityAdapter.isChecked()
                || !etDescription.getText().toString().isEmpty()
                || imageUri != null) {
            showSaveChangesAlert();
        } else {
            finish();
        }
    }

    private void showExplanation() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(getContext());
        builder.setTitle(getString(R.string.app_name))
                .setMessage(R.string.read_external_permission_explanation)
                .setPositiveButton(getString(R.string.allow), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        ActivityCompat.requestPermissions(getActivity(),
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_CODE_PERMISSION);
                    }
                })
                .setNegativeButton(getString(R.string.deny), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

    private Intent getImagePickIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        return intent;
    }

    private void showSaveChangesAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        AlertDialog dialog = builder.setTitle("Save")
                .setMessage("Would you like to save the changes")
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        onSaveClick();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        finish();
                    }
                }).create();
        dialog.show();
    }

    private void finish() {
        MainActivity activity = (MainActivity) getActivity();
        View view = activity.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
        if (ApplicationUtils.isTabletAndLandscape()) {
            activity.replaceFirstFragmentWithList();
            activity.replaceFragment(AddPokemonFragment.newInstance(user),
                    R.id.container2, true);
        } else {
            activity.replaceFragment(PokemonListFragment.newInstance(user),
                    R.id.container);
        }
    }

    public static int[] convertIntegers(List<Integer> integers) {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = integers.get(i).intValue();
        }
        return ret;
    }
}
