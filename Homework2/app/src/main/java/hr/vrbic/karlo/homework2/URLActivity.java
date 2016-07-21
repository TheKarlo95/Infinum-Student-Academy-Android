package hr.vrbic.karlo.homework2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * {@code URLActivity} is an activity class that provides user with an
 * {@linkplain EditText} and two {@linkplain android.widget.Button Buttons} so user can set first URL and choose browser.
 *
 * @author Karlo Vrbić
 * @version 1.0
 * @see AppCompatActivity
 */
public class URLActivity extends AppCompatActivity {

    /**
     * Text field for URL.
     */
    @BindView(R.id.et_url)
    EditText etUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_url);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_open_webview)
    public void onOpenWebViewClick(View v) {
        String url = getUrl();
        Intent intent = new Intent(this, WebviewActivity.class);
        intent.putExtra(WebviewActivity.EXTRA_URL, url);
        startActivity(intent);
    }

    @OnClick(R.id.btn_open_browser)
    public void onOpenBrowserClick(View v) {
        String url = getUrl();
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        String title = getResources().getText(R.string.chooser_title).toString();
        Intent chooser = Intent.createChooser(intent, title);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(chooser);
        }
    }

    private String getUrl() {
        String url = ((EditText) findViewById(R.id.et_url)).getText().toString();
        if (!url.startsWith("http://") && !url.startsWith("https://")) {
            url = "http://" + url;
        }
        return url;
    }
}
