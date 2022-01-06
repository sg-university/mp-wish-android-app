package com.binus.mp.wish.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.binus.mp.wish.R;
import com.binus.mp.wish.apis.AuthenticationApi;
import com.binus.mp.wish.contracts.CredentialsByHuawei;
import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.controllers.Controller;
import com.binus.mp.wish.models.Account;
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

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener {

    AccountAuthService mAuthService;
    AccountAuthParams mAuthParam;

    HuaweiIdAuthButton buttonHuaweiAuth;

    static final int REQUEST_CODE_SIGN_IN = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonHuaweiAuth = findViewById(R.id.activity_register_button_huawei_auth);
        buttonHuaweiAuth.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_register_button_huawei_auth:
                silentSignInByHwId();
                break;
        }
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

        Controller<AuthenticationApi> registerController = new Controller<>(AuthenticationApi.class);
        Call<Result<Account>> call = registerController.getApi().registerByHuaweiOpenId(credentials);
        call.enqueue(new Callback<Result<Account>>() {
            @Override
            public void onResponse(Call<Result<Account>> call, Response<Result<Account>> response) {
                Result<Account> result = response.body();
                Log.d("xxx", String.valueOf(result));

                assert result != null;
                Account account = result.getContent();
                Intent intent = null;

                if(result.getStatus().equals("registered")){
                    if (account.getEmail() == null || account.getPassword() == null) {
                        intent = new Intent(RegisterActivity.this, UpdateAccountActivity.class);
                    } else {
                        intent = new Intent(RegisterActivity.this, FeedActivity.class);
                    }
                } else if(result.getStatus().equals("exists")){
                    intent = new Intent(RegisterActivity.this, LoginActivity.class);
                }

                if(intent != null) {
                    RegisterActivity.this.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Result<Account>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}