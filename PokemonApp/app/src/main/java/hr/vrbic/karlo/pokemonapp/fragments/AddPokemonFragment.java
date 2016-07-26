package hr.vrbic.karlo.pokemonapp.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.activities.MainActivity;
import hr.vrbic.karlo.pokemonapp.beans.Pokemon;
import hr.vrbic.karlo.pokemonapp.beans.PokemonsList;

public class AddPokemonFragment extends AbstractFragment {

    private static final int REQUEST_CODE_PERMISSION = 1;
    private static final int REQ_CODE_PICK_IMAGE = 2;

    private static final String IMAGE_URI = "image_uri";

    @BindView(R.id.et_name)
    EditText etName;
    @BindView(R.id.et_height)
    EditText etHeight;
    @BindView(R.id.et_weight)
    EditText etWeight;
    @BindView(R.id.et_category)
    EditText etCategory;
    @BindView(R.id.et_abilities)
    EditText etAbilities;
    @BindView(R.id.et_gender)
    EditText etGender;
    @BindView(R.id.et_description)
    EditText etDescription;
    @BindView(R.id.iv_image)
    ImageView ivImage;

    private Uri imageUri;

    private Unbinder unbinder;

    public AddPokemonFragment() {
        // Required empty public constructor
    }

    public static AddPokemonFragment newInstance() {
        return new AddPokemonFragment();
    }

    @Override
    public AbstractFragment copy() {
        return new AddPokemonFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_pokemon, container, false);
        unbinder = ButterKnife.bind(this, view);

        if (savedInstanceState != null) {
            imageUri = savedInstanceState.getParcelable(IMAGE_URI);
            ivImage.setImageURI(imageUri);
        }

        return view;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(IMAGE_URI, imageUri);
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
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showExplanation();
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSION);
            }
        } else {
            startActivityForResult(getImagePickIntent(), REQ_CODE_PICK_IMAGE);
        }
    }

    @OnClick(R.id.btn_save)
    public void onSaveClick() {
        String name = etName.getText().toString();
        String strHeight = etHeight.getText().toString();
        strHeight = strHeight.isEmpty() ? "0" : strHeight.trim();
        String strWeight = etWeight.getText().toString();
        strWeight = strWeight.isEmpty() ? "0" : strWeight.trim();
        double height = Double.parseDouble(strHeight);
        double weight = Double.parseDouble(strWeight);
        String category = etCategory.getText().toString();
        String abilities = etAbilities.getText().toString();
        String gender = etGender.getText().toString();
        String description = etDescription.getText().toString();

        try {
            Pokemon pokemon = new Pokemon(getContext(), name, height, weight, category, abilities, gender, description,
                    imageUri);

            PokemonsList.add(pokemon);
            finish();
        } catch (NullPointerException | IllegalArgumentException e) {
            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public void goBack() {
        if (!etName.getText().toString().isEmpty()
                || !etHeight.getText().toString().isEmpty()
                || !etWeight.getText().toString().isEmpty()
                || !etCategory.getText().toString().isEmpty()
                || !etAbilities.getText().toString().isEmpty()
                || !etGender.getText().toString().isEmpty()
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
        if (getResources().getBoolean(R.bool.is_tablet_landscape)) {
            ((MainActivity) getActivity()).replaceFragmentTabletLandscape(PokemonListFragment.newInstance(),
                    MainActivity.POKEMON_LIST_TAG, true);
            ((MainActivity) getActivity()).replaceFragmentTabletLandscape(AddPokemonFragment.newInstance(),
                    MainActivity.ADD_POKEMON_TAG, false);
        } else {
            ((MainActivity) getActivity()).replaceFragment(new PokemonListFragment(), MainActivity.POKEMON_LIST_TAG);
        }

        View view = getActivity().getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

}
