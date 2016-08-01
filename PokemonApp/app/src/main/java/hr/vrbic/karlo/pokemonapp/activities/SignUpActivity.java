package hr.vrbic.karlo.pokemonapp.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.EditText;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import hr.vrbic.karlo.pokemonapp.R;
import hr.vrbic.karlo.pokemonapp.model.User;
import hr.vrbic.karlo.pokemonapp.model.UserCreateRequest;
import hr.vrbic.karlo.pokemonapp.model.UserCreateResponse;
import hr.vrbic.karlo.pokemonapp.network.ApiManager;
import hr.vrbic.karlo.pokemonapp.utilities.StringUtils;
import hr.vrbic.karlo.pokemonapp.utilities.ToastUtils;
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

    private Call<UserCreateResponse> userCreateCall;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setHomeButtonEnabled(true);
        }

        ButterKnife.bind(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (userCreateCall != null) {
            userCreateCall.cancel();
        }
    }

    @OnClick(R.id.btn_finish_sign_up)
    public void onSignUpClick() {
        String email = StringUtils.requireNonEmptyAndClear(etMail);
        String nickname = StringUtils.requireNonEmptyAndClear(etNickname);
        String password = StringUtils.requireNonEmptyAndClear(etPassword);
        String passwordConfirmation = StringUtils.requireNonEmptyAndClear(etConfirmPassword);

        if (!(TextUtils.isEmpty(email)
                || TextUtils.isEmpty(nickname)
                || TextUtils.isEmpty(password)
                || TextUtils.isEmpty(passwordConfirmation))) {
            UserCreateRequest userCreateRequest = new UserCreateRequest(email, nickname, password,
                    passwordConfirmation);

            userCreateCall = ApiManager.getService().userCreate(userCreateRequest);
            userCreateCall.enqueue(new Callback<UserCreateResponse>() {
                @Override
                public void onResponse(Call<UserCreateResponse> call, Response<UserCreateResponse> response) {
                    if (response.isSuccessful()) {
                        User user = response.body().getUser();

                        Intent intent = new Intent(SignUpActivity.this, MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable(MainActivity.USER, user);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    } else {
                        ToastUtils.showToast(SignUpActivity.this, R.string.sign_up_fail);
                    }
                }

                @Override
                public void onFailure(Call<UserCreateResponse> call, Throwable t) {
                    if (!call.isCanceled()) {
                        ToastUtils.showToast(SignUpActivity.this, R.string.sign_up_fail);
                    }
                }
            });
            finish();
        }
    }

}
