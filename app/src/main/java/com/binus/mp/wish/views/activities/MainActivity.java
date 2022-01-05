package com.binus.mp.wish.views.activities;


import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.binus.mp.wish.R;
import com.binus.mp.wish.apis.AccountApi;
import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.controllers.Controller;
import com.binus.mp.wish.models.Account;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonLogin, buttonRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin = findViewById(R.id.activity_main_button_login);
        buttonRegister = findViewById(R.id.activity_main_button_register);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);

        Controller<AccountApi> accountController = new Controller<>(AccountApi.class);
        Call<Result<List<Account>>> accountReadAll = accountController.getApi().readAll();
        accountReadAll.enqueue(new Callback<Result<List<Account>>>() {
            @Override
            public void onResponse(Call<Result<List<Account>>> call, Response<Result<List<Account>>> response) {
                Result<List<Account>> result = response.body();
                Log.d("xxx", String.valueOf(result));
            }

            @Override
            public void onFailure(Call<Result<List<Account>>> call, Throwable t) {
                t.printStackTrace();
            }
        });

        Account accountToCreate = new Account(UUID.randomUUID(), "usernamez", "namez", "emailz", "passwordz", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));

        Call<Result<Account>> accountCreateOne = accountController.getApi().createOne(accountToCreate);
        accountCreateOne.enqueue(new Callback<Result<Account>>() {
            @Override
            public void onResponse(Call<Result<Account>> call, Response<Result<Account>> response) {
                Result<Account> result = null;
                result = response.body();
                Log.d("xxx", String.valueOf(result));
            }

            @Override
            public void onFailure(Call<Result<Account>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View v) {
        Intent intent = null;
        switch (v.getId()) {
            case R.id.activity_main_button_register:
                intent = new Intent(this, RegisterActivity.class);
                break;
            case R.id.activity_main_button_login:
                intent = new Intent(this, LoginActivity.class);
                break;
        }
        if (intent != null) {
            this.startActivity(intent);
        }
    }
}