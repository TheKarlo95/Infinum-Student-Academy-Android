package hr.vrbic.karlo.pokemonapp.activities;

import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.widget.EditText;
import android.widget.Toast;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.model.UserCreateRequest;
import hr.vrbic.karlo.pokemonapp.model.UserCreateResponse;
import hr.vrbic.karlo.pokemonapp.network.ApiManager;
import hr.vrbic.karlo.pokemonapp.utilities.PermissionUtil;
import hr.vrbic.karlo.pokemonapp.utilities.StringUtils;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignUpActivity extends AppCompatActivity {

    @BindView(R.id.et_sign_up_mail)
    EditText etMail;
    @BindView(R.id.et_sign_up_nickname)
    EditText etNickname;
    @BindView(R.id.et_sign_up_password)
    EditText etPassword;
    @BindView(R.id.et_sign_up_confirm_password)
    EditText etConfirmPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
        }

        ButterKnife.bind(this);
    }

    @OnClick(R.id.btn_finish_sign_up)
    public void onSignUpClick() {
        PermissionUtil.doInternetOperation(this, this, new Runnable() {
                    @Override
                    public void run() {
                        String email = StringUtils.getString(etMail);
                        String nickname = StringUtils.getString(etNickname);
                        String password = StringUtils.getString(etPassword);
                        String passwordConfirmation = StringUtils.getString(etConfirmPassword);

                        if (email != null && nickname != null && password != null && passwordConfirmation != null) {
                            try {
                                UserCreateRequest userCreateRequest = new UserCreateRequest(email, nickname, password,
                                        passwordConfirmation);

                                Call<UserCreateResponse> userCreateCall = ApiManager.getService().userCreate(userCreateRequest);
                                userCreateCall.enqueue(new Callback<UserCreateResponse>() {
                                    @Override
                                    public void onResponse(Call<UserCreateResponse> call, Response<UserCreateResponse> response) {
                                        if (!response.isSuccessful()) {
                                            Toast.makeText(SignUpActivity.this, R.string.sign_up_fail, Toast.LENGTH_LONG).show();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<UserCreateResponse> call, Throwable t) {
                                        if (!call.isCanceled()) {
                                            Toast.makeText(SignUpActivity.this, R.string.sign_up_fail, Toast.LENGTH_LONG).show();
                                        }
                                    }
                                });
                            } catch (NullPointerException | IllegalArgumentException e) {
                                Toast.makeText(SignUpActivity.this, e.getMessage(), Toast.LENGTH_LONG).show();
                            }
                            finish();
                        }
                    }
                }
        );
    }

}
