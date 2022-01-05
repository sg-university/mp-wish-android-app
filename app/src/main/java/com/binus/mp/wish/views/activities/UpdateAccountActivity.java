package com.binus.mp.wish.views.activities;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.binus.mp.wish.R;

public class UpdateAccountActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_account);

        // After authentication of register and not yet fill email & password to the account,
        // the user must be redirected to update account activity to fill it first.
    }
}