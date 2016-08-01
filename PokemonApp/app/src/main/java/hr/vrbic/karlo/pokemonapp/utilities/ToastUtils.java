package hr.vrbic.karlo.pokemonapp.utilities;

import android.content.Context;
import android.view.Gravity;
import android.widget.TextView;
import android.widget.Toast;

/**
 * Created by TheKarlo95 on 1.8.2016..
 */
public class ToastUtils {

    private ToastUtils() {
    }

    public static void showToast(Context context, String message, int duration) {
        Toast toast = Toast.makeText(context, message, duration);
        TextView view = (TextView) toast.getView().findViewById(android.R.id.message);
        if (view != null) {
            view.setGravity(Gravity.CENTER);
        }
        toast.show();
    }

    public static void showToast(Context context, int resId, int duration) {
        showToast(context, context.getString(resId), duration);
    }

    public static void showToast(Context context, String message) {
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_LONG);
        TextView view = (TextView) toast.getView().findViewById(android.R.id.message);
        if (view != null) {
            view.setGravity(Gravity.CENTER);
        }
        toast.show();
    }

    public static void showToast(Context context, int resId) {
        showToast(context, context.getString(resId));
    }

}
