package hr.vrbic.karlo.pokemonapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.model.User;
import hr.vrbic.karlo.pokemonapp.model.UserLoginRequest;
import hr.vrbic.karlo.pokemonapp.model.UserLoginResponse;
import hr.vrbic.karlo.pokemonapp.network.ApiManager;
import hr.vrbic.karlo.pokemonapp.utilities.PermissionUtil;
import hr.vrbic.karlo.pokemonapp.utilities.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {


    @BindView(R.id.et_mail)
    EditText etMail;
    @BindView(R.id.et_password)
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_sign_up)
    public void onSignUpClick() {
        Intent intent = new Intent(LoginActivity.this, SignUpActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.btn_login)
    public void onLoginClick() {
        PermissionUtil.doInternetOperation(this, this, new Runnable() {
            @Override
            public void run() {
                String email = StringUtils.getString(etMail);
                String password = StringUtils.getString(etPassword);

                if (email != null && password != null) {
                    etMail.setText("");
                    etPassword.setText("");

                    try {
                        UserLoginRequest request = new UserLoginRequest(email, password);

                        Call<UserLoginResponse> userLoginCall = ApiManager.getService().userLogin(request);
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
                                } else if (response.code() == 401) {
                                    Toast.makeText(LoginActivity.this, R.string.login_invalid_email_password,
                                            Toast.LENGTH_LONG).show();
                                } else {
                                    Toast.makeText(LoginActivity.this, R.string.login_fail, Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<UserLoginResponse> call, Throwable t) {
                                if (!call.isCanceled()) {
                                    Toast.makeText(LoginActivity.this, R.string.login_fail, Toast.LENGTH_LONG).show();
                                }
                            }
                        });
                    } catch (NullPointerException | IllegalArgumentException e) {
                        Toast.makeText(LoginActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                    }
                }
            }
        });
    }

}
