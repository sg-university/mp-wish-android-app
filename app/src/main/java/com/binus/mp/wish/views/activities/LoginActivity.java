package com.binus.mp.wish.views.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.binus.mp.wish.R;
import com.binus.mp.wish.apis.AuthenticationApi;
import com.binus.mp.wish.contracts.CredentialsByEmail;
import com.binus.mp.wish.contracts.CredentialsByHuawei;
import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.controllers.Controller;
import com.binus.mp.wish.models.Account;
import com.binus.mp.wish.models.Auth;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.common.ApiException;
import com.huawei.hms.support.account.AccountAuthManager;
import com.huawei.hms.support.account.request.AccountAuthParams;
import com.huawei.hms.support.account.request.AccountAuthParamsHelper;
import com.huawei.hms.support.account.result.AuthAccount;
import com.huawei.hms.support.account.service.AccountAuthService;
import com.huawei.hms.support.hwid.ui.HuaweiIdAuthButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    AccountAuthService mAuthService;
    AccountAuthParams mAuthParam;

    HuaweiIdAuthButton buttonHuaweiAuth;
    Button normalLoginBtn, goToRegister;
    TextView tvErrMsg;
    EditText username, password;
    ProgressDialog dialog;
    static final int REQUEST_CODE_SIGN_IN = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonHuaweiAuth = findViewById(R.id.activity_login_button_huawei_sign_in);
        buttonHuaweiAuth.setOnClickListener(this);
        tvErrMsg = findViewById(R.id.activity_login_text_view_error_message);

        username = findViewById(R.id.activity_login_edit_text_email);
        password = findViewById(R.id.activity_login_edit_text_password);

        normalLoginBtn = findViewById(R.id.activity_login_button_login);
        normalLoginBtn.setOnClickListener(this);

        goToRegister = findViewById(R.id.activity_login_button_go_to_register);
        goToRegister.setOnClickListener(this);

    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_login_button_huawei_sign_in:
                silentSignInByHwId();
                break;
            case R.id.activity_login_button_login:
                boolean isValidate = validateInput();
                if (isValidate) {
                    dialog = new ProgressDialog(this);
                    dialog.setMessage("Please wait for a moment...");
                    dialog.setCancelable(false);
                    dialog.setInverseBackgroundForced(false);
                    dialog.show();
                    handleLoginByEmail();
                } else {
                    String error = "Login Failed! Please Check your Account again!";
                    tvErrMsg.setText(error);
                }

                break;
            case R.id.activity_login_button_go_to_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }


    //login normally without using huawei button
    private void handleLoginByEmail() {
        Controller<AuthenticationApi> controller = new Controller<>(AuthenticationApi.class);
        String email = username.getText().toString();
        String pass = password.getText().toString();
        Call<Result<Account>> call = controller.getApi().loginByEmail(new CredentialsByEmail(email, pass));
        //belum ada validasi kalau misalkan akun tidak ditemukan didalam database.
        call.enqueue(new Callback<Result<Account>>() {
            @Override
            public void onResponse(Call<Result<Account>> call, Response<Result<Account>> response) {
                Result<Account> result = response.body();
                Log.d("LoginActivity", String.valueOf(result));

                assert result != null;
                Account account = result.getContent();
                Intent intent = null;

                switch (result.getStatus()) {
                    case "logged_in":
                        if (account.getEmail() == null || account.getPassword() == null) {
                            intent = new Intent(LoginActivity.this, AccountActivity.class);
                        } else {
                            setAccountLogin(account);
                            intent = new Intent(LoginActivity.this, HomeActivity.class);
                        }
                        break;
                    default:
                        tvErrMsg.setText(result.getStatus());
                        break;
                }

                if (intent != null) {
                    LoginActivity.this.startActivity(intent);
                }

                dialog.hide();
            }

            @Override
            public void onFailure(Call<Result<Account>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //validasi input
    private boolean validateInput() {
        if (username.getText().toString().isEmpty() || password.getText().toString().isEmpty()) {
            return false;
        }
        return true;
    }

    private void silentSignInByHwId() {
        mAuthParam = new AccountAuthParamsHelper(AccountAuthParams.DEFAULT_AUTH_REQUEST_PARAM)
                .setEmail()
                .setAuthorizationCode()
                .createParams();

        // Use request parameters to construct a Huawei account login authorization service AccountAuthService
        mAuthService = AccountAuthManager.getService(this, mAuthParam);

        // Use silent sign in for HUAWEI ID login
        Task<AuthAccount> task = mAuthService.silentSignIn();
        task.addOnSuccessListener(new OnSuccessListener<AuthAccount>() {
            @Override
            public void onSuccess(AuthAccount authAccount) {
                // Silent sign in is successful, the returned account object AuthAccount is processed,account information is obtained and processed
                handleLoginByHuawei(authAccount);
            }
        });
        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(Exception e) {
                // Silent sign in fails, use the getSignInIntent() method to log in from the foreground
                if (e instanceof ApiException) {
                    ApiException apiException = (ApiException) e;
                    Intent signInIntent = mAuthService.getSignInIntent();
                    startActivityForResult(signInIntent, REQUEST_CODE_SIGN_IN);
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_SIGN_IN) {
            Log.i("LoginActivity", "onActivitResult of sigInInIntent, request code: " + REQUEST_CODE_SIGN_IN);
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                // The login is successful, and the login account information object authAccount is obtained
                AuthAccount authAccount = authAccountTask.getResult();
                handleLoginByHuawei(authAccount);
                Log.i("LoginActivity", "onActivitResult of sigInInIntent, request code: " + REQUEST_CODE_SIGN_IN);
            } else {
                // Login failed. The status code identifies the reason for the failure. Please refer to the error
                // code reference in the API for detailed error reasons.
                Log.e("LoginActivity", "sign in failed : " + ((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }
    }

    private void handleLoginByHuawei(AuthAccount authAccount) {
        CredentialsByHuawei credentials = new CredentialsByHuawei();
        credentials.setUnionId(authAccount.getUnionId());
        credentials.setOpenId(authAccount.getOpenId());
        credentials.setAuthorizationCode(authAccount.getAuthorizationCode());
        credentials.setAccessToken(authAccount.getAccessToken());
        Log.d("LoginActivity", credentials.toString());

        Controller<AuthenticationApi> loginController = new Controller<>(AuthenticationApi.class);
        Call<Result<Account>> call = loginController.getApi().loginByHuaweiOpenId(credentials);
        call.enqueue(new Callback<Result<Account>>() {
            @Override
            public void onResponse(Call<Result<Account>> call, Response<Result<Account>> response) {
                Result<Account> result = response.body();
                Log.d("LoginActivity", String.valueOf(result));

                assert result != null;
                Account account = result.getContent();
                Intent intent = null;

                switch (result.getStatus()) {
                    case "logged_in":
                        if (account.getEmail() == null || account.getPassword() == null) {
                            intent = new Intent(LoginActivity.this, AccountActivity.class);
                        } else {
                            if (account.getEmail().isEmpty() || account.getPassword().isEmpty()) {
                                intent = new Intent(LoginActivity.this, AccountActivity.class);
                            }
                            setAccountLogin(account);
                            intent = new Intent(LoginActivity.this, HomeActivity.class);
                        }
                        break;
                    default:
                        tvErrMsg.setText(result.getStatus());
                        break;
                }

                if (intent != null) {
                    LoginActivity.this.startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<Result<Account>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    //session setelah login akan diletakan di Class Auth yang singleton supaya bisa diakses semua class
    private void setAccountLogin(Account account) {
        Auth auth = Auth.getInstance();
        auth.setAccount(account);
    }
}