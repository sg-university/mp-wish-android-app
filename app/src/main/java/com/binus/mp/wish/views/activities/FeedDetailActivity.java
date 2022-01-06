package com.binus.mp.wish.views.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.binus.mp.wish.R;
import com.binus.mp.wish.apis.PostApi;
import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.controllers.Controller;
import com.binus.mp.wish.models.Post;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);
        Intent intent = getIntent();
        UUID id = UUID.fromString(intent.getStringExtra("id"));



        Controller<PostApi> controller = new Controller(PostApi.class);
        Call<Result<Post>> call = controller.getApi().readOne(id);
        call.enqueue(new Callback<Result<Post>>() {
            @Override
            public void onResponse(Call<Result<Post>> call, Response<Result<Post>> response) {

            }

            @Override
            public void onFailure(Call<Result<Post>> call, Throwable t) {

            }
        });

    }



}