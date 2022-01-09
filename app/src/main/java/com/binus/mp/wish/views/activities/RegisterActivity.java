package com.binus.mp.wish.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.binus.mp.wish.R;
import com.binus.mp.wish.apis.AccountApi;
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

import java.sql.Timestamp;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity implements View.OnClickListener{
    Button registerBtn,goToLogin;
    EditText nameEt,emailEt,passwordEt,confirmEt;
    TextView errMsg;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        nameEt = findViewById(R.id.nameRegister);
        emailEt = findViewById(R.id.usernameRegister);
        passwordEt = findViewById(R.id.passwordRegister);
        confirmEt = findViewById(R.id.confirmRegister);

        registerBtn = findViewById(R.id.registerBtn);
        registerBtn.setOnClickListener(this);
        goToLogin = findViewById(R.id.toLoginBtn);
        goToLogin.setOnClickListener(this);
        errMsg = findViewById(R.id.errRegisterMsg);


    }

    private void validate(){
        if(nameEt.getText().equals("") || emailEt.getText().equals("")|| passwordEt.getText().equals("") || confirmEt.getText().equals("")){
            errMsg.setText("Fields must not be empty");
            return;
        }

        if(passwordEt.getText().toString().compareTo(confirmEt.getText().toString()) != 0){
            errMsg.setText("Password and Confirm Password must be same!");
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(emailEt.getText().toString()).matches()){
            errMsg.setText("Need to be in email format!");
            return;
        }

        register();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.registerBtn:
                validate();

                break;
            case R.id.toLoginBtn:
                Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
                startActivity(intent);
                break;
        }
    }
    private void register(){
        String name = nameEt.getText().toString();
        String password = passwordEt.getText().toString();
        String email = emailEt.getText().toString();

        Account accountToCreate = new Account(UUID.randomUUID(), "usernamez", "namez", "emailz", "passwordz", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        Controller<AccountApi> accountController = new Controller<>(AccountApi.class);
        Call<Result<Account>> accountCreateOne = accountController.getApi().createOne(accountToCreate);
        accountCreateOne.enqueue(new Callback<Result<Account>>() {
            @Override
            public void onResponse(Call<Result<Account>> call, Response<Result<Account>> response) {
                Result<Account> result = null;
                result = response.body();
                Log.d("xxx", String.valueOf(result));

                if(response.isSuccessful()){
                    registerSuccess();
                }
            }

            @Override
            public void onFailure(Call<Result<Account>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }


    private void registerSuccess(){
        Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
        startActivity(intent);
        Toast.makeText(this, "Register Success!", Toast.LENGTH_SHORT).show();
    }

}