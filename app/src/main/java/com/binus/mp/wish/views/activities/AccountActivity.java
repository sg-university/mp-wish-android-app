package com.binus.mp.wish.views.activities;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.binus.mp.wish.R;
import com.binus.mp.wish.apis.AccountApi;
import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.controllers.Controller;
import com.binus.mp.wish.models.Account;
import com.binus.mp.wish.models.Auth;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AccountActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextName, editTextEmail, editTextPassword, editTextUsername;
    Button buttonUpdate;

    TextView textViewErrorMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);

        // After authentication of register and not yet fill email & password to the account,
        // the user must be redirected to update account activity to fill it first.

        editTextName = findViewById(R.id.activity_account_edit_text_name);
        editTextUsername = findViewById(R.id.activity_account_edit_text_username);
        editTextEmail = findViewById(R.id.activity_account_edit_text_email);
        editTextPassword = findViewById(R.id.activity_account_edit_text_password);
        buttonUpdate = findViewById(R.id.activity_account_button_update);
        textViewErrorMessage = findViewById(R.id.activity_account_text_view_error_message);

        Auth auth = Auth.getInstance();
        Account account = auth.getAccount();
        editTextName.setText(account.getName());
        editTextUsername.setText(account.getUsername());
        editTextEmail.setText(account.getEmail());

        buttonUpdate.setOnClickListener(this);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.activity_account_button_update:
                Log.d("xxx", "Account Update onclick");
                Boolean isValidate = validateInput();
                if (isValidate) {
                    Log.d("xxx", "Account Update validated");
                    handleAccountUpdate();
                } else {
                    Log.d("xxx", "Account error");
                    String errorMessage = "Update Failed! Please Check your Account again!";
                    textViewErrorMessage.setText(errorMessage);
                }
                break;
        }
    }

    private Boolean validateInput() {
        String name = editTextName.getText().toString();
        String username = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();
        if (username.isEmpty() || password.isEmpty()
                || email.isEmpty() || name.isEmpty()
        ) {
            return false;
        }
        return true;
    }

    private void handleAccountUpdate() {
        String name = editTextName.getText().toString();
        String username = editTextUsername.getText().toString();
        String email = editTextEmail.getText().toString();
        String password = editTextPassword.getText().toString();

//      Auth auth = (Auth) this.getApplication();
        Auth auth = Auth.getInstance();
        Account accountToUpdate = auth.getAccount();
        accountToUpdate.setName(name);
        accountToUpdate.setUsername(username);
        accountToUpdate.setEmail(email);
        accountToUpdate.setPassword(password);
        Controller<AccountApi> accountController = new Controller<>(AccountApi.class);
        Call<Result<Account>> accountCall = accountController.getApi().updateOneById(accountToUpdate.getId(), accountToUpdate);

        accountCall.enqueue(new Callback<Result<Account>>() {

            @Override
            public void onResponse(Call<Result<Account>> call, Response<Result<Account>> response) {
                Result<Account> result = response.body();
                assert result != null;
                Account account = result.getContent();
                Intent intent = null;
                switch (result.getStatus()) {
                    case "updated":
                        auth.setAccount(account);
                        intent = new Intent(AccountActivity.this, HomeActivity.class);
                        break;

                    default:
                        textViewErrorMessage.setText(result.getStatus());
                        break;
                }

                if (intent != null) {
                    AccountActivity.this.startActivity(intent);
                }
            }

            @Override
            public void onFailure(Call<Result<Account>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }
}