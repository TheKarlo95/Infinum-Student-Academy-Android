package hr.vrbic.karlo.homework2;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.KeyEvent;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Button;
import android.widget.EditText;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * {@code WebviewActivity} is an activity class that uses {@code WebView} to display web content to user and gives
 * user the toolbar for navigation.
 *
 * @author Karlo Vrbić
 * @version 1.0
 * @see AppCompatActivity
 */
public class WebviewActivity extends AppCompatActivity {

    /**
     * Key used to get URL from intent extras.
     */
    public static final String EXTRA_URL = "url";

    /**
     * Web view used to display web pages.
     */
    @BindView(R.id.wv_browser)
    WebView webView;
    /**
     * Button used for navigating back in web browser.
     */
    @BindView(R.id.btn_back)
    Button btnBack;
    /**
     * Button used for navigating forward in web browser.
     */
    @BindView(R.id.btn_forward)
    Button btnForward;
    /**
     * Text field for URL.
     */
    @BindView(R.id.et_toolbar_url)
    EditText etUrl;
    /**
     * Button used for confirming current URL written in {@code etUrl}.
     */
    @BindView(R.id.btn_go)
    Button btnGo;

    @SuppressLint("SetJavaScriptEnabled")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        ButterKnife.bind(this);

        setUpToolbar();
        webView.getSettings().setJavaScriptEnabled(true);
        webView.setWebViewClient(new CustomWebViewClient(btnBack, btnForward, etUrl));

        if(webView.getUrl() != null)
            return;

        Bundle extras = getIntent().getExtras();
        String url = null;
        if (extras != null) {
            url = extras.getString(EXTRA_URL);
        } else {
            url = getIntent().getData().toString();
        }

        webView.loadUrl(url);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && webView.canGoBack()) {
            webView.goBack();
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

    /**
     * Initializes toolbar, adds on click listeners on the toolbar components.
     */
    private void setUpToolbar() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoBack()) {
                    webView.goBack();
                }
            }
        });

        btnForward.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (webView.canGoForward()) {
                    webView.goForward();
                }
            }
        });

        btnGo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = ((EditText) findViewById(R.id.et_toolbar_url)).getText().toString();
                if (!url.startsWith("http://") && !url.startsWith("https://")) {
                    url = "http://" + url;
                }
                webView.loadUrl(url);
            }
        });

        etUrl.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                etUrl.selectAll();
                return true;
            }
        });
    }

    /**
     * {@code CustomWebViewClient} is a {@linkplain WebViewClient} class that updates the toolbar whenever new URL is
     * browsed.
     *
     * @author Karlo Vrbić
     * @version 1.0
     * @see WebViewClient
     */
    private static class CustomWebViewClient extends WebViewClient {

        /**
         * Button used for navigating back in web browser.
         */
        Button btnBack;
        /**
         * Button used for navigating forward in web browser.
         */
        Button btnForward;
        /**
         * Text field for URL.
         */
        EditText etUrl;

        /**
         * Constructs a new {@code CustomWebViewClient} with specified toolbar components.
         *
         * @param btnBack    the button used for navigating back in web browser
         * @param btnForward the button used for navigating forward in web browser
         * @param etUrl      the text field for URL
         */
        public CustomWebViewClient(Button btnBack, Button btnForward, EditText etUrl) {
            super();

            this.btnBack = Objects.requireNonNull(btnBack, "Parameter btnBack cannot be null.");
            this.btnForward = Objects.requireNonNull(btnForward, "Parameter btnForward cannot be null.");
            this.etUrl = Objects.requireNonNull(etUrl, "Parameter etUrl cannot be null.");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            updateToolbar(view, url);
            super.onPageStarted(view, url, favicon);
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            updateToolbar(view, url);
            super.onPageFinished(view, url);
        }

        /**
         * Updates the toolbar with new url, and sets the visibility to back nad forward buttons.
         *
         * @param view the {@linkplain WebView} that is initiating the callback
         * @param url  the url to be loaded
         */
        private void updateToolbar(WebView view, String url) {
            etUrl.setText(url);
            updateBackButton(view);
            updateForwardButton(view);
        }

        /**
         * Updates the visibility of the {@code btnBack}. If
         * {@linkplain WebView#canGoBack()} returns {@code true} {@code btnBack} is set to {@linkplain View#VISIBLE};
         * otherwise it's set to {@linkplain View#GONE}.
         *
         * @param view the {@linkplain WebView} that is initiating the callback
         */
        private void updateBackButton(WebView view) {
            if (view.canGoBack()) {
                btnBack.setVisibility(View.VISIBLE);
            } else {
                btnBack.setVisibility(View.GONE);
            }
        }

        /**
         * Updates the visibility of the {@code btnForward}. If
         * {@linkplain WebView#canGoForward()} returns {@code true} {@code btnForward} is set to {@linkplain View#VISIBLE};
         * otherwise it's set to {@linkplain View#GONE}.
         *
         * @param view the {@linkplain WebView} that is initiating the callback
         */
        private void updateForwardButton(WebView view) {
            if (view.canGoForward()) {
                btnForward.setVisibility(View.VISIBLE);
            } else {
                btnForward.setVisibility(View.GONE);
            }
        }

    }

}
