package hr.vrbic.karlo.pokemonapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;

import java.net.HttpURLConnection;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.model.User;
import hr.vrbic.karlo.pokemonapp.model.UserLoginRequest;
import hr.vrbic.karlo.pokemonapp.model.UserLoginResponse;
import hr.vrbic.karlo.pokemonapp.network.ApiManager;
import hr.vrbic.karlo.pokemonapp.utilities.NetworkUtils;
import hr.vrbic.karlo.pokemonapp.utilities.StringUtils;
import hr.vrbic.karlo.pokemonapp.utilities.ToastUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.et_mail)
    EditText etMail;
    @BindView(R.id.et_password)
    EditText etPassword;

    private Call<UserLoginResponse> userLoginCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);

        if (!NetworkUtils.isNetworkAvailable()) {
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (userLoginCall != null) {
            userLoginCall.cancel();
        }
    }

    @OnClick(R.id.btn_sign_up)
    public void onSignUpClick() {
        if (NetworkUtils.isNetworkAvailable()) {
            Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
            startActivity(intent);
        }else {
            ToastUtils.showToast(LoginActivity.this, R.string.sign_up_no_internet);
        }
    }

    @OnClick(R.id.btn_login)
    public void onLoginClick() {
        String email = StringUtils.requireNonEmptyAndClear(etMail);
        String password = StringUtils.requireNonEmptyAndClear(etPassword);

        if (NetworkUtils.isNetworkAvailable()) {
            if (!(TextUtils.isEmpty(email) || TextUtils.isEmpty(password))) {
                UserLoginRequest request = new UserLoginRequest(email, password);

                userLoginCall = ApiManager.getService().userLogin(request);
                userLoginCall.enqueue(new Callback<UserLoginResponse>() {
                    @Override
                    public void onResponse(Call<UserLoginResponse> call, Response<UserLoginResponse> response) {
                        if (response.isSuccessful()) {
                            User user = response.body().getUser();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            Bundle bundle = new Bundle();
                            bundle.putParcelable(MainActivity.USER, user);
                            intent.putExtras(bundle);
                            startActivity(intent);
                        } else if (response.code() == HttpURLConnection.HTTP_UNAUTHORIZED) {
                            ToastUtils.showToast(LoginActivity.this, R.string.login_invalid_email_password);
                        } else {
                            ToastUtils.showToast(LoginActivity.this, R.string.login_fail);
                        }
                    }

                    @Override
                    public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                        if (!call.isCanceled()) {
                            ToastUtils.showToast(LoginActivity.this, R.string.login_fail);
                        }
                    }
                });

            }
        } else {
            ToastUtils.showToast(this, R.string.login_fail_no_internet);
        }
    }

}
