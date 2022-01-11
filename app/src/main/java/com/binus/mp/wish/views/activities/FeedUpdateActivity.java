package com.binus.mp.wish.views.activities;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.binus.mp.wish.R;
import com.binus.mp.wish.apis.PostApi;
import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.controllers.Controller;
import com.binus.mp.wish.models.Account;
import com.binus.mp.wish.models.Auth;
import com.binus.mp.wish.models.Post;

import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedUpdateActivity extends AppCompatActivity implements View.OnClickListener {

    EditText editTextTitle, editTextContent;
    Button buttonDelete, buttonUpdate;
    UUID postId;
    Post post;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_update);

        editTextTitle = findViewById(R.id.activity_update_feed_edit_text_title);
        editTextContent = findViewById(R.id.activity_update_feed_edit_text_content);
        buttonDelete = findViewById(R.id.activity_update_feed_button_delete);
        buttonUpdate = findViewById(R.id.activity_update_feed_button_update);

        Intent intent = getIntent();
        postId = UUID.fromString(intent.getStringExtra("postId"));

        buttonUpdate.setOnClickListener(this);
        buttonDelete.setOnClickListener(this);

        readPost();
    }

    private void readPost() {
        Controller<PostApi> controller = new Controller<>(PostApi.class);
        Call<Result<Post>> call = controller.getApi().readOneById(postId);
        call.enqueue(new Callback<Result<Post>>() {
            @Override
            public void onResponse(Call<Result<Post>> call, Response<Result<Post>> response) {
                Result<Post> result = response.body();
                assert result != null;

                switch (result.getStatus()) {
                    case "read":
                        post = result.getContent();
                        editTextTitle.setText(post.getTitle());
                        editTextContent.setText(post.getContent());
                        Toast.makeText(FeedUpdateActivity.this, "Read Success!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(FeedUpdateActivity.this, "Read Failed", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Result<Post>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void updatePost() {
        Account acc = Auth.getInstance().getAccount();
        String title = editTextTitle.getText().toString();
        String content = editTextContent.getText().toString();
        Post postToUpdate = new Post(postId, acc.getId(), title, content, null, null);

        Controller<PostApi> controller = new Controller<>(PostApi.class);
        Call<Result<Post>> call = controller.getApi().patchOneById(postId, postToUpdate);
        call.enqueue(new Callback<Result<Post>>() {
            @Override
            public void onResponse(Call<Result<Post>> call, Response<Result<Post>> response) {
                Result<Post> result = response.body();
                assert result != null;
                switch (result.getStatus()) {
                    case "patched":
                        post = result.getContent();
                        editTextTitle.setText(post.getTitle());
                        editTextContent.setText(post.getContent());

                        Intent refreshIntent = new Intent();
                        refreshIntent.putExtra("postId", postId);
                        setResult(201, refreshIntent);

                        Toast.makeText(FeedUpdateActivity.this, "Update Success!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(FeedUpdateActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Result<Post>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void deletePost() {
        Controller<PostApi> controller = new Controller<>(PostApi.class);
        Call<Result<Post>> call = controller.getApi().deleteOneById(postId);
        call.enqueue(new Callback<Result<Post>>() {
            @Override
            public void onResponse(Call<Result<Post>> call, Response<Result<Post>> response) {
                Result<Post> result = response.body();
                assert result != null;
                switch (result.getStatus()) {
                    case "deleted":
                        Intent intent = new Intent(FeedUpdateActivity.this, HomeActivity.class);
                        FeedUpdateActivity.this.startActivity(intent);
                        Toast.makeText(FeedUpdateActivity.this, "Delete Success!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(FeedUpdateActivity.this, "Delete Failed", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Result<Post>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.activity_update_feed_button_update:
                if (editTextTitle.getText().toString().isEmpty() || editTextContent.getText().toString().isEmpty()) {
                    Toast.makeText(view.getContext(), "Title and Content must be inputted!", Toast.LENGTH_SHORT).show();
                } else {
                    updatePost();
                }
                break;
            case R.id.activity_update_feed_button_delete:
                deletePost();
                break;
        }

    }
}