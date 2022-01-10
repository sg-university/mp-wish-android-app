package com.binus.mp.wish.views.activities;

import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

import com.binus.mp.wish.R;
import com.binus.mp.wish.models.Account;
import com.binus.mp.wish.models.Auth;
import com.binus.mp.wish.views.activities.menufragments.CreatePostFragment;
import com.binus.mp.wish.views.activities.menufragments.FeedFragment;
import com.binus.mp.wish.views.activities.menufragments.ProfileFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.sql.Timestamp;
import java.util.UUID;


public class HomeActivity extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {
    BottomNavigationView nav;
    FeedFragment homeFragment;
    ProfileFragment profileFragment;
    CreatePostFragment createPostFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        getSupportActionBar().hide();
        homeFragment = new FeedFragment();
        profileFragment = new ProfileFragment();
        createPostFragment = new CreatePostFragment();


        //dummy login
        ((Auth) getApplication()).setAcc(
                new Account(UUID.randomUUID(), "usernamez", "namez", "emailz", "passwordz", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis())));

        nav = findViewById(R.id.bottomNav);
        nav.setOnNavigationItemSelectedListener(this);
        nav.setSelectedItemId(R.id.home);
    }


    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.home) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, homeFragment).commit();
            return true;
        } else if (item.getItemId() == R.id.profile) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, profileFragment).commit();
            return true;
        } else if (item.getItemId() == R.id.add) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, createPostFragment).commit();
            return true;
        }

        return false;
    }
}