 package com.binus.mp.wish.views.activities;

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
    Button normalLoginBtn,goToRegister;
    TextView tvErrMsg;
    EditText username,password;
    ProgressDialog dialog;
    static final int REQUEST_CODE_SIGN_IN = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        buttonHuaweiAuth = findViewById(R.id.activity_login_button_huawei_auth);
        buttonHuaweiAuth.setOnClickListener(this);
        tvErrMsg = findViewById(R.id.errLoginMsg);

        username = findViewById(R.id.usernameLogin);
        password = findViewById(R.id.passwordLogin);


        normalLoginBtn = findViewById(R.id.loginBtn);
        normalLoginBtn.setOnClickListener(this);

        goToRegister = findViewById(R.id.goToRegisterBtn);
        goToRegister.setOnClickListener(this);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_login_button_huawei_auth:
                silentSignInByHwId();
                break;
            case R.id.loginBtn:

                boolean isValidate = validateInput();
                if(isValidate){
                    dialog = new ProgressDialog(this);
                    dialog.setMessage("Please wait for a moment...");
                    dialog.setCancelable(false);
                    dialog.setInverseBackgroundForced(false);
                    dialog.show();
                    normalLogin();
                }else{
                    String error = "Login Failed! Please Check your Account again!";
                    tvErrMsg.setText(error);
                }

                break;
            case R.id.goToRegisterBtn:
                Intent intent = new Intent(this,RegisterActivity.class);
                startActivity(intent);
                break;
        }
    }


    //login normally without using huawei button
    private void normalLogin(){
        Controller<AuthenticationApi> controller = new Controller<>(AuthenticationApi.class);
        String email = username.getText().toString();
        String pass = password.getText().toString();
        Call<Result<Account>> call = controller.getApi().loginByEmail(new CredentialsByEmail(email,pass));
        //belum ada validasi kalau misalkan akun tidak ditemukan didalam database.
        call.enqueue(new Callback<Result<Account>>() {
            @Override
            public void onResponse(Call<Result<Account>> call, Response<Result<Account>> response) {
                Result<Account> result = response.body();
                Log.d("xxx", String.valueOf(result));

                assert result != null;
                Account account = result.getContent();
                //accountnya ga masuk

                if(account == null){
                    String error = "Login Failed! Please Check your Account again!";
                    tvErrMsg.setText(error);
                    dialog.hide();

                }else{
                    Intent intent = new Intent(LoginActivity.this, HomeActivity.class);

                    setAccountLogin(account);
                    LoginActivity.this.startActivity(intent);
                }

            }

            @Override
            public void onFailure(Call<Result<Account>> call, Throwable t) {

            }
        });
    }
    //validasi input
    private boolean validateInput(){
        if(username.getText().toString().equals("") || password.getText().toString().equals("")){
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
                signInHandler(authAccount);
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
            Log.i("xxx", "onActivitResult of sigInInIntent, request code: " + REQUEST_CODE_SIGN_IN);
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                // The login is successful, and the login account information object authAccount is obtained
                AuthAccount authAccount = authAccountTask.getResult();
                signInHandler(authAccount);
                Log.i("xxx", "onActivitResult of sigInInIntent, request code: " + REQUEST_CODE_SIGN_IN);
            } else {
                // Login failed. The status code identifies the reason for the failure. Please refer to the error
                // code reference in the API for detailed error reasons.
                Log.e("xxx", "sign in failed : " + ((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }
    }

    private void signInHandler(AuthAccount authAccount) {
        CredentialsByHuawei credentials = new CredentialsByHuawei();
        credentials.setUnionId(authAccount.getUnionId());
        credentials.setOpenId(authAccount.getOpenId());
        credentials.setAuthorizationCode(authAccount.getAuthorizationCode());
        credentials.setAccessToken(authAccount.getAccessToken());
        Log.d("xxx", credentials.toString());

        Controller<AuthenticationApi> loginController = new Controller<>(AuthenticationApi.class);
        Call<Result<Account>> call = loginController.getApi().loginByHuaweiOpenId(credentials);
        call.enqueue(new Callback<Result<Account>>() {
            @Override
            public void onResponse(Call<Result<Account>> call, Response<Result<Account>> response) {
                Result<Account> result = response.body();
                Log.d("xxx", String.valueOf(result));

                assert result != null;
                Account account = result.getContent();
                Intent intent = null;

                if(result.getStatus().equals("logged_in")){
                    if (account.getEmail() == null || account.getPassword() == null) {
                        intent = new Intent(LoginActivity.this, UpdateAccountActivity.class);
                    } else {
                        setAccountLogin(account);
                        intent = new Intent(LoginActivity.this, HomeActivity.class);
                    }
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
    private void setAccountLogin(Account account){
        Auth.getSession().setAcc(account);
        dialog.hide();
    }
}