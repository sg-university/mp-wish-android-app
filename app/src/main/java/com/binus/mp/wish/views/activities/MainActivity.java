package com.binus.mp.wish.views.activities;


import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.binus.mp.wish.R;
import com.binus.mp.wish.models.Auth;
import com.binus.mp.wish.services.LocationService;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Auth auth;
    Button buttonLogin, buttonRegister, buttonBanner, buttonLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = Auth.getInstance();

        buttonLogin = findViewById(R.id.activity_main_button_login);
        buttonRegister = findViewById(R.id.activity_main_button_register);

        buttonLogin.setOnClickListener(this);
        buttonRegister.setOnClickListener(this);

        buttonBanner = findViewById(R.id.activity_main_button_banner);
        buttonBanner.setOnClickListener(this);

        buttonLocation = findViewById(R.id.activity_main_button_location);
        buttonLocation.setOnClickListener(this);

//        Intent serviceIntent = new Intent(this, LocationService.class);
//        startService(serviceIntent);
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
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
                intent = new Intent(this, HuaweiBannerActivity.class);
                break;
            case R.id.activity_main_button_location:
                intent = new Intent(this, HuaweiRequestLocationUpdatesWithCallbackActivity.class);
                break;
        }

        if (intent != null) {
            this.startActivity(intent);
        }
    }
}