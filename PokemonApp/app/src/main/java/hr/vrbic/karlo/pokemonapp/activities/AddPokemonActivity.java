package hr.vrbic.karlo.pokemonapp.activities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Rect;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.model.Pokemon;

/**
 * {@code AddPokemonActivity} is an activity that gives user the ability to add new Pokemons to the list.
 *
 * @author Karlo Vrbić
 * @version 1.0
 * @see AppCompatActivity
 */
public class AddPokemonActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_PERMISSION = 1;

    /**
     * Request code for picking an image.
     */
    private static final int REQ_CODE_PICK_IMAGE = 2;

    private static final String IMAGE_URI = "image_uri";

    /**
     * {@linkplain EditText} for name.
     */
    @BindView(R.id.et_name)
    EditText etName;
    /**
     * {@linkplain EditText} for height.
     */
    @BindView(R.id.et_height)
    EditText etHeight;
    /**
     * {@linkplain EditText} for weight.
     */
    @BindView(R.id.et_weight)
    EditText etWeight;
    /**
     * {@linkplain EditText} for category.
     */
    @BindView(R.id.et_category)
    EditText etCategory;
    /**
     * {@linkplain EditText} for abilities.
     */
    @BindView(R.id.et_abilities)
    EditText etAbilities;
    /**
     * {@linkplain EditText} for image.
     */
    @BindView(R.id.et_gender)
    EditText etGender;
    /**
     * {@linkplain EditText} for description.
     */
    @BindView(R.id.et_description)
    EditText etDescription;
    /**
     * {@linkplain ImageView} for image.
     */
    @BindView(R.id.iv_image)
    ImageView ivImage;

    private Uri imageUri;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_pokemon);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(R.string.add_pokemon_title);

        ButterKnife.bind(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelable(IMAGE_URI, imageUri);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        imageUri = savedInstanceState.getParcelable(IMAGE_URI);
        ivImage.setImageURI(imageUri);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                goBack();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch (requestCode) {
            case REQ_CODE_PICK_IMAGE:
                if (resultCode == RESULT_OK) {
                    imageUri = data.getData();
                    ivImage.setImageURI(imageUri);
                }
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_CODE_PERMISSION:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startActivityForResult(getImagePickIntent(), REQ_CODE_PICK_IMAGE);
                } else {
                    showMessage("Permission request denied");
                }
                break;
        }

    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            goBack();
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            View v = getCurrentFocus();
            if (v instanceof EditText) {
                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int) event.getRawX(), (int) event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent(event);
    }

    /**
     * Saves the Pokemon with name and description from {@code etName} and {@code etDescription} and finishes this
     * activity. If any of those fields are empty {@linkplain Toast} is displayed and nothing is saved. Invoked when
     * button {@linkplain R.id#btn_save} is clicked.
     */
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
            Pokemon pokemon = new Pokemon(this, name, height, weight, category, abilities, gender, description,
                    imageUri);

            Intent intent = new Intent();
            setResult(Activity.RESULT_OK, intent);
            Bundle extras = new Bundle();
            extras.putParcelable(PokemonListActivity.POKEMON, pokemon);

            intent.putExtras(extras);
            finish();
        } catch (NullPointerException | IllegalArgumentException e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    /**
     * Starts implicit image chooser activity.
     */
    @OnClick(R.id.fab_add_image)
    public void onAddImageClick() {
        if (ActivityCompat.checkSelfPermission(AddPokemonActivity.this, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(AddPokemonActivity.this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showExplanation();
            } else {
                ActivityCompat.requestPermissions(AddPokemonActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_CODE_PERMISSION);
            }
        } else {
            startActivityForResult(getImagePickIntent(), REQ_CODE_PICK_IMAGE);
        }
    }

    private Intent getImagePickIntent() {
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType("image/*");

        return intent;
    }

    private void showExplanation() {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name))
                .setMessage(R.string.read_external_permission_explanation)
                .setPositiveButton(getString(R.string.allow), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        ActivityCompat.requestPermissions(AddPokemonActivity.this, new String[]{Manifest.permission
                                        .READ_EXTERNAL_STORAGE},
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

    /**
     * Finishes this activity. If needed dialog is displayed that asks the user if saving the Pokeon is needed.
     * Invoked when return home button from menu is clicked or back button is clicked
     */
    private void goBack() {
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
            setResult(Activity.RESULT_CANCELED, null);
            finish();
        }
    }

    /**
     * Displays the dialog that asks the user whether he/she wants to save Pokemon.
     */
    private void showSaveChangesAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
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
                        setResult(Activity.RESULT_CANCELED, null);
                        finish();
                    }
                }).create();
        dialog.show();
    }

    private void showMessage(String message) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
        builder.setTitle(getString(R.string.app_name))
                .setMessage(message)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
    }

}
