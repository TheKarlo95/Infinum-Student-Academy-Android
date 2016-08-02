package hr.vrbic.karlo.pokemonapp.fragments;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hr.vrbic.karlo.pokemonapp.Consumer;
import hr.vrbic.karlo.pokemonapp.PokemonInteractor;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.activities.MainActivity;
import hr.vrbic.karlo.pokemonapp.checkboxlist.CheckBoxAdapter;
import hr.vrbic.karlo.pokemonapp.checkboxlist.CheckBoxItem;
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
import hr.vrbic.karlo.pokemonapp.utilities.ToastUtils;
import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPokemonFragment extends AbstractFragment {

    private static final String FRAGMENT_TAG = "add_pokemon";
    private static final int REQ_CODE_PICK_IMAGE = 100;
    private static final String USER = "user";
    private static final String IMAGE_URI = "image_uri";
    private static final String ABILITIES = "abilities";
    private static final String CATEGORIES = "categories";

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

    private String imageUri;

    private Unbinder unbinder;

    private CheckBoxAdapter<Category> categoryAdapter;
    private CheckBoxAdapter<Ability> abilityAdapter;
    private User user;
    private Call<MoveListResponse> movesCall;

    public AddPokemonFragment() {
        // Required empty public constructor
    }

    public static AddPokemonFragment newInstance(User user) {
        AddPokemonFragment fragment = new AddPokemonFragment();
        if (user != null) {
            fragment.user = user;
            fragment.movesCall = ApiManager.getService().getAllMoves(user.getAuthorization());
        }
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
            if (imageUri != null) {
                ivImage.setImageURI(Uri.parse(imageUri));
            }

            user = savedInstanceState.getParcelable(USER);

            List<Ability> abilities = savedInstanceState.getParcelableArrayList(ABILITIES);
            List<Category> categories = savedInstanceState.getParcelableArrayList(CATEGORIES);

            abilityAdapter = new CheckBoxAdapter<>(getContext(), abilities, new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = lvAbilities.getPositionForView(buttonView);
                    if (pos != ListView.INVALID_POSITION) {
                        Ability ability = abilityAdapter.getCheckBoxItem(pos);
                        ability.setSelected(isChecked);
                    }
                }
            });
            lvAbilities.setAdapter(abilityAdapter);

            categoryAdapter = new CheckBoxAdapter<>(getContext(), categories, new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int pos = lvCategories.getPositionForView(buttonView);
                    if (pos != ListView.INVALID_POSITION) {
                        Category category = categoryAdapter.getCheckBoxItem(pos);
                        category.setSelected(isChecked);
                    }
                }
            });
            lvCategories.setAdapter(categoryAdapter);
        }

        initAbilitiesList();
        initCategoriesList();

        return view;
    }

    private void initAbilitiesList() {
        PokemonInteractor pokemonInteractor = new PokemonInteractor(getContext(), user);
        pokemonInteractor.getAllAbilities(new Consumer<List<Ability>>() {
            @Override
            public void accept(List<Ability> abilities) {
                abilityAdapter = new CheckBoxAdapter<>(getContext(), abilities, new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int pos = lvAbilities.getPositionForView(buttonView);
                        if (pos != ListView.INVALID_POSITION) {
                            Ability ability = abilityAdapter.getCheckBoxItem(pos);
                            ability.setSelected(isChecked);
                        }
                    }
                });
                lvAbilities.setAdapter(abilityAdapter);
            }
        });
    }

    private void initCategoriesList() {
        PokemonInteractor pokemonInteractor = new PokemonInteractor(getContext(), user);
        pokemonInteractor.getAllCategories(new Consumer<List<Category>>() {
            @Override
            public void accept(List<Category> abilities) {
                categoryAdapter = new CheckBoxAdapter<>(getContext(), abilities, new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                        int pos = lvCategories.getPositionForView(buttonView);
                        if (pos != ListView.INVALID_POSITION) {
                            Category category = categoryAdapter.getCheckBoxItem(pos);
                            category.setSelected(isChecked);
                        }
                    }
                });
                lvCategories.setAdapter(categoryAdapter);
            }
        });
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putString(IMAGE_URI, imageUri);
        outState.putParcelable(USER, user);
        outState.putParcelableArrayList(ABILITIES, abilityAdapter.getAll());
        outState.putParcelableArrayList(CATEGORIES, categoryAdapter.getAll());
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
                    imageUri = data.getData().toString();
                    ivImage.setImageURI(Uri.parse(imageUri));
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
        String name = StringUtils.requireNonEmpty(etName);
        String strHeight = StringUtils.getString(etHeight);
        strHeight = strHeight.isEmpty() ? "0" : strHeight.trim();
        String strWeight = StringUtils.getString(etWeight);
        strWeight = strWeight.isEmpty() ? "0" : strWeight.trim();
        double height = Double.parseDouble(strHeight);
        double weight = Double.parseDouble(strWeight);
        List<Category> categories = categoryAdapter.getSelected();
        List<Ability> abilities = abilityAdapter.getSelected();
        String description = StringUtils.getString(etDescription);

        if (name != null) {
            try {
                Pokemon pokemon = new Pokemon(name, height, weight, categories, abilities, description, imageUri);

                RequestBody image = null;
                if (imageUri != null) {
                    image = RequestBody.create(MediaType.parse("image/*"), new File(getRealPathFromUri(getContext(),
                            pokemon.getImageUri())));
                } else {
                    Drawable d = getContext().getDrawable(Pokemon.DEFAULT_IMAGE);
                    if (d != null) {
                        Bitmap bitmap = ((BitmapDrawable) d).getBitmap();
                        ByteArrayOutputStream stream = new ByteArrayOutputStream();
                        bitmap.compress(Bitmap.CompressFormat.PNG, 100, stream);

                        image = RequestBody.create(MediaType.parse("image/*"), stream.toByteArray());
                    }
                }
                Call<Void> call = ApiManager.getService().createPokemon(user.getAuthorization(),
                        name,
                        pokemon.getHeight(),
                        pokemon.getWeight(),
                        1,
                        pokemon.getDescription(),
                        getIdArray(pokemon.getCategories()),
                        getIdArray(pokemon.getAbilities()),
                        image);
                call.enqueue(new Callback<Void>() {
                    @Override
                    public void onResponse(Call<Void> call, Response<Void> response) {
                        if (response.isSuccessful()) {
                            finish();
                        } else {
                            ToastUtils.showToast(getActivity(), R.string.pokemon_create_fail);
                        }
                    }

                    @Override
                    public void onFailure(Call<Void> call, Throwable t) {
                        if (!call.isCanceled()) {
                            ToastUtils.showToast(getActivity(), R.string.pokemon_create_fail);
                        }
                    }
                });
            } catch (NullPointerException | IllegalArgumentException e) {
                ToastUtils.showToast(getContext(), e.getMessage());
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

    public static int[] getIdArray(List<? extends CheckBoxItem> integers) {
        int[] ret = new int[integers.size()];
        for (int i = 0; i < ret.length; i++) {
            ret[i] = integers.get(i).getId();
        }
        return ret;
    }

    private static String getRealPathFromUri(Context context, String contentUri) {
        return getRealPathFromUri(context, Uri.parse(contentUri));
    }

    private static String getRealPathFromUri(Context context, Uri contentUri) {
        String result = null;

        Cursor cursor = context.getContentResolver().query(contentUri, null, null, null, null);

        if (cursor == null) {
            result = contentUri.toString();
        } else {
            if (cursor.moveToFirst()) {
                int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                result = cursor.getString(idx);
            }
            cursor.close();
        }
        return result;
    }
}
