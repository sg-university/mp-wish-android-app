package com.binus.mp.wish.views.activities;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.binus.mp.wish.R;
import com.binus.mp.wish.apis.AuthenticationApi;
import com.binus.mp.wish.contracts.CredentialsByEmail;
import com.binus.mp.wish.contracts.CredentialsByHuawei;
import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.controllers.Controller;
import com.binus.mp.wish.models.Account;
import com.huawei.hmf.tasks.OnFailureListener;
import com.huawei.hmf.tasks.OnSuccessListener;
import com.huawei.hmf.tasks.Task;
import com.huawei.hms.ads.AdParam;
import com.huawei.hms.ads.BannerAdSize;
import com.huawei.hms.ads.HwAds;
import com.huawei.hms.ads.banner.BannerView;
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
    Button normalRegisterBtn, goToRegister;
    TextView tvErrMsg;
    EditText username, password;
    ProgressDialog dialog;
    static final int REQUEST_CODE_SIGN_IN = 1000;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        buttonHuaweiAuth = findViewById(R.id.activity_register_button_huawei_sign_in);
        buttonHuaweiAuth.setOnClickListener(this);
        tvErrMsg = findViewById(R.id.activity_register_text_view_error_message);

        username = findViewById(R.id.activity_register_edit_text_email);
        password = findViewById(R.id.activity_register_edit_text_password);

        normalRegisterBtn = findViewById(R.id.activity_register_button_register);
        normalRegisterBtn.setOnClickListener(this);

        goToRegister = findViewById(R.id.activity_register_button_go_to_login);
        goToRegister.setOnClickListener(this);

        // Initialize the HUAWEI Ads SDK.
        HwAds.init(this);

        // Obtain BannerView based on the configuration in layout/ad_fragment.xml.
        BannerView bottomBannerView = findViewById(R.id.hw_banner_view);
        AdParam adParam = new AdParam.Builder().build();
        bottomBannerView.loadAd(adParam);

        // Call new BannerView(Context context) to create a BannerView class.
        BannerView topBannerView = new BannerView(this);
        topBannerView.setAdId("testw6vs28auh3");
        topBannerView.setBannerAdSize(BannerAdSize.BANNER_SIZE_SMART);
        topBannerView.loadAd(adParam);

        ConstraintLayout rootView = findViewById(R.id.root_view);
        rootView.addView(topBannerView);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_register_button_huawei_sign_in:
                silentSignInByHwId();
                break;
            case R.id.activity_register_button_register:

                Boolean isValidate = validateInput();
                if (isValidate) {
                    dialog = new ProgressDialog(this);
                    dialog.setMessage("Please wait for a moment...");
                    dialog.setCancelable(false);
                    dialog.setInverseBackgroundForced(false);
                    dialog.show();
                    handleRegisterByEmail();
                } else {
                    String error = "Register Failed! Please Check your Account again!";
                    tvErrMsg.setText(error);
                }

                break;
            case R.id.activity_register_button_go_to_login:
                Intent intent = new Intent(this, LoginActivity.class);
                startActivity(intent);
                break;
        }
    }


    //login normally without using huawei button
    private void handleRegisterByEmail() {
        Controller<AuthenticationApi> controller = new Controller<>(AuthenticationApi.class);
        String email = username.getText().toString();
        String pass = password.getText().toString();
        Call<Result<Account>> call = controller.getApi().registerByEmail(new CredentialsByEmail(email, pass));
        //belum ada validasi kalau misalkan akun tidak ditemukan didalam database.
        call.enqueue(new Callback<Result<Account>>() {
            @Override
            public void onResponse(Call<Result<Account>> call, Response<Result<Account>> response) {
                Result<Account> result = response.body();
                Log.d("RegisterActivity", String.valueOf(result));

                assert result != null;
                Account account = result.getContent();
                Intent intent = null;

                switch (result.getStatus()) {
                    case "registered":
                    case "exists":
                        intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        break;
                    default:
                        tvErrMsg.setText(String.valueOf(result.getStatus()));
                }

                if (intent != null) {
                    RegisterActivity.this.startActivity(intent);
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
    private Boolean validateInput() {
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
                handleRegisterByHuawei(authAccount);
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
            Log.i("RegisterActivity", "onActivitResult of sigInInIntent, request code: " + REQUEST_CODE_SIGN_IN);
            Task<AuthAccount> authAccountTask = AccountAuthManager.parseAuthResultFromIntent(data);
            if (authAccountTask.isSuccessful()) {
                // The login is successful, and the login account information object authAccount is obtained
                AuthAccount authAccount = authAccountTask.getResult();
                handleRegisterByHuawei(authAccount);
                Log.i("RegisterActivity", "onActivitResult of sigInInIntent, request code: " + REQUEST_CODE_SIGN_IN);
            } else {
                // Login failed. The status code identifies the reason for the failure. Please refer to the error
                // code reference in the API for detailed error reasons.
                Log.e("RegisterActivity", "sign in failed : " + ((ApiException) authAccountTask.getException()).getStatusCode());
            }
        }
    }

    private void handleRegisterByHuawei(AuthAccount authAccount) {
        CredentialsByHuawei credentials = new CredentialsByHuawei();
        credentials.setUnionId(authAccount.getUnionId());
        credentials.setOpenId(authAccount.getOpenId());
        credentials.setAuthorizationCode(authAccount.getAuthorizationCode());
        credentials.setAccessToken(authAccount.getAccessToken());
        Log.d("RegisterActivity", credentials.toString());

        Controller<AuthenticationApi> loginController = new Controller<>(AuthenticationApi.class);
        Call<Result<Account>> call = loginController.getApi().registerByHuaweiOpenId(credentials);
        call.enqueue(new Callback<Result<Account>>() {
            @Override
            public void onResponse(Call<Result<Account>> call, Response<Result<Account>> response) {
                Result<Account> result = response.body();
                Log.d("RegisterActivity", String.valueOf(result));

                assert result != null;
                Account account = result.getContent();
                Intent intent = null;

                switch (result.getStatus()) {
                    case "registered":
                    case "exists":
                        intent = new Intent(RegisterActivity.this, LoginActivity.class);
                        break;
                    default:
                        tvErrMsg.setText(String.valueOf(result.getStatus()));
                }

                if (intent != null) {
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