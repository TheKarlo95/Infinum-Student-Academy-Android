package hr.vrbic.karlo.homework1;

import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {

    /**
     * Text view that is used to display current number of clicks on the increment button.
     */
    private TextView textView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        textView = (TextView) findViewById(R.id.textView);
    }

    /**
     * Increments the number shown in {@code textView} and sets the color of text to green, if number is even, or
     * blue, if number is odd.
     *
     * @param v the view that was clicked
     */
    public void onIncrementClick(View v) {
        int number = Integer.parseInt(textView.getText().toString()) + 1;
        textView.setText(String.valueOf(number));
        if ((number & 1) == 0) {
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.green));
        } else {
            textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.blue));
        }
    }

    /**
     * Resets the number shown in {@code textView} to default value {@code "0"} and sets the text color to black.
     *
     * @param v the view that was clicked
     */
    public void onResetClick(View v) {
        textView.setText("0");
        textView.setTextColor(ContextCompat.getColor(getApplicationContext(), R.color.componentForeground));
    }

}