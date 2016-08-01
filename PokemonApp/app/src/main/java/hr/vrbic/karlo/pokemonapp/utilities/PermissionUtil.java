package hr.vrbic.karlo.pokemonapp.utilities;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.support.v4.app.ActivityCompat;

import hr.vrbic.karlo.pokemonapp.R;

/**
 * Created by TheKarlo95 on 29.7.2016..
 */
public class PermissionUtil {

    private static final int REQUEST_EXTERNAL_STORAGE_PERMISSION = 1;

    public static void doExternalStorageOperation(Context context, Activity activity, Runnable operation) {
        if (ActivityCompat.checkSelfPermission(context, Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(activity,
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                showExternalStorageExplanation(context, activity);
            } else {
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        REQUEST_EXTERNAL_STORAGE_PERMISSION);
            }
        } else {
            operation.run();
        }
    }

    private static void showExternalStorageExplanation(final Context context, final Activity activity) {
        android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(context);
        builder.setTitle(context.getString(R.string.app_name))
                .setMessage(R.string.read_external_permission_explanation)
                .setPositiveButton(context.getString(R.string.allow), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                        ActivityCompat.requestPermissions(activity,
                                new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                                REQUEST_EXTERNAL_STORAGE_PERMISSION);
                    }
                })
                .setNegativeButton(context.getString(R.string.deny), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create().show();
    }

}
