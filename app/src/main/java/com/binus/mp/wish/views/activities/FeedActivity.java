package com.binus.mp.wish.views.activities;

import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.binus.mp.wish.R;
import com.binus.mp.wish.apis.PostApi;
import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.controllers.Controller;
import com.binus.mp.wish.models.Post;
import com.binus.mp.wish.views.adapters.FeedAdapter;

import java.sql.Timestamp;
import java.util.List;
import java.util.UUID;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedActivity extends AppCompatActivity {
    List<Post> listPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        listPost = new Vector<Post>();

        Controller<PostApi> controller = new Controller<>(PostApi.class);
        Call<Result<List<Post>>> call = controller.getApi().readAll();
        call.enqueue(new Callback<Result<List<Post>>>() {
            @Override
            public void onResponse(Call<Result<List<Post>>> call, Response<Result<List<Post>>> response) {
                if (response.isSuccessful()) {
                    Result<List<Post>> posts = response.body();
                    Log.i("FeedActivity", "post0 count : " + posts.getContent().size());
                    setListPost(posts.getContent());
                    setRecyclerView();
                } else {
                    Log.i("FeedActivity", "Error : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Result<List<Post>>> call, Throwable t) {
                Log.e("FeedActivity", t.getMessage());
            }
        });


    }

    private void setRecyclerView() {
        RecyclerView rv = findViewById(R.id.feedRV);
        Log.i("FeedActivity", "post2 count : " + listPost.size());
        FeedAdapter adapter = new FeedAdapter(listPost);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);
    }

    //UUID id, UUID creatorAccountId, String title, String content, Timestamp createdAt, Timestamp updatedAt
    private void setListPost(List<Post> posts) {
        this.listPost = posts;
        Log.i("FeedActivity", "postcount : " + listPost.size());
    }
}