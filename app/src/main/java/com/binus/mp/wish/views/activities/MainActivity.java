package com.binus.mp.wish.views.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.binus.mp.wish.R;
import com.huawei.hms.ads.InterstitialAd;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    Button buttonLogin, buttonRegister, buttonBanner;
    private InterstitialAd interstitialAd = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        buttonLogin = findViewById(R.id.activity_main_button_login);
        buttonRegister = findViewById(R.id.activity_main_button_register);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);

        buttonBanner = findViewById(R.id.activity_main_button_banner);
        buttonBanner.setOnClickListener(this);
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.activity_main_button_login:
                intent = new Intent(this, LoginActivity.class);
                break;
            case R.id.activity_main_button_register:
                intent = new Intent(this, RegisterActivity.class);
                break;
            case R.id.activity_main_button_banner:
                intent = new Intent(this, BannerActivity.class);
                break;
        }

        if (intent != null) {
            this.startActivity(intent);
        }
    }
}