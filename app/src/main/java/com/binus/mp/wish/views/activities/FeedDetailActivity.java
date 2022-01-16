package com.binus.mp.wish.views.activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.binus.mp.wish.R;
import com.binus.mp.wish.apis.AccountApi;
import com.binus.mp.wish.apis.CommentApi;
import com.binus.mp.wish.apis.PostApi;
import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.controllers.Controller;
import com.binus.mp.wish.models.Account;
import com.binus.mp.wish.models.Auth;
import com.binus.mp.wish.models.Comment;
import com.binus.mp.wish.models.Post;
import com.binus.mp.wish.views.adapters.CommentAdapter;

import java.sql.Timestamp;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedDetailActivity extends AppCompatActivity implements View.OnClickListener {
    TextView textViewTitle, textViewAuthor, textViewContent, textViewTimestamp;
    ImageButton buttonBack, buttonUpdate;
    RecyclerView recyclerViewComment;
    EditText editTextComment;
    Button buttonComment;
    ProgressDialog progressDialog;
    UUID postId;
    Post post;
    CommentAdapter adapterComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);
        Intent intent = getIntent();
        postId = UUID.fromString(intent.getStringExtra("id"));
        textViewTitle = findViewById(R.id.activity_feed_detail_text_view_title);
        textViewAuthor = findViewById(R.id.activity_feed_detail_text_view_author_name);
        textViewContent = findViewById(R.id.activity_feed_detail_text_view_content);
        textViewTimestamp = findViewById(R.id.activity_feed_detail_text_view_timestamp);
        buttonBack = findViewById(R.id.activity_feed_detail_button_back);
        buttonUpdate = findViewById(R.id.activity_feed_detail_button_update);
        editTextComment = findViewById(R.id.activity_feed_detail_edit_text_comment);
        buttonComment = findViewById(R.id.activity_feed_detail_button_comment);
        recyclerViewComment = findViewById(R.id.activity_feed_detail_recycler_view_comment);

        buttonBack.setOnClickListener(this);
        buttonUpdate.setOnClickListener(this);
        buttonComment.setOnClickListener(this);

        buttonUpdate.setVisibility(View.GONE);

        Log.i("Test", "UUID : " + postId);
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please wait for a moment...");
        progressDialog.setCancelable(false);
        progressDialog.setInverseBackgroundForced(false);
        progressDialog.show();

        adapterComment = new CommentAdapter();
        recyclerViewComment.setHasFixedSize(true);
        recyclerViewComment.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewComment.setAdapter(adapterComment);

        adapterComment.getItemData().observe(this, data -> {
            if (adapterComment.getItemCount() == 0) {
                Toast.makeText(FeedDetailActivity.this, "No comment now", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(FeedDetailActivity.this, "There is comment", Toast.LENGTH_SHORT).show();
            }
        });

        fetchComments();
        readPost();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

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
                        initPost(result.getContent());
                        Auth auth = Auth.getInstance();
                        Account account = auth.getAccount();
                        if (post.getCreatorAccountId().equals(account.getId())) {
                            buttonUpdate.setVisibility(View.VISIBLE);
                        }
                        Toast.makeText(FeedDetailActivity.this, "Read Success!", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        Toast.makeText(FeedDetailActivity.this, "Read Failed", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Result<Post>> call, Throwable t) {
                t.printStackTrace();
            }
        });
    }

    private void insertComment() {
        String comment = editTextComment.getText().toString();
        Auth auth = Auth.getInstance();
        Account acc = auth.getAccount();
        Controller<CommentApi> controller = new Controller<>(CommentApi.class);
        Comment comment1 = new Comment(UUID.randomUUID(), postId, acc.getId(), comment, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        Call<Result<Comment>> call = controller.getApi().createOne(comment1);
        call.enqueue(new Callback<Result<Comment>>() {
            @Override
            public void onResponse(Call<Result<Comment>> call, Response<Result<Comment>> response) {
                //success
                Result<Comment> result = response.body();
                assert result != null;
                switch (result.getStatus()) {
                    case "created":
                        Toast.makeText(FeedDetailActivity.this, "Create Success!", Toast.LENGTH_SHORT).show();
                        fetchComments();
                        break;
                    default:
                        Toast.makeText(FeedDetailActivity.this, "Create Failed", Toast.LENGTH_SHORT).show();
                        break;
                }
            }

            @Override
            public void onFailure(Call<Result<Comment>> call, Throwable t) {
                t.printStackTrace();
            }
        });

    }

    private void fetchComments() {
        Controller<CommentApi> controller = new Controller<>(CommentApi.class);
        HashMap<String, String> x = new HashMap<>();
        x.put("post_id", postId.toString());
        Call<Result<List<Comment>>> call = controller.getApi().readAll(x);
        call.enqueue(new Callback<Result<List<Comment>>>() {
            @Override
            public void onResponse(Call<Result<List<Comment>>> call, Response<Result<List<Comment>>> response) {
                if (response.isSuccessful()) {
                    progressDialog.hide();
                    Result<List<Comment>> result = response.body();
                    assert result != null;
                    if (result.getStatus().equals("read")) {

                        if (result.getContent() != null) {
                            initComment(result.getContent());
                            //
                        } else {
                            //
                        }
                    }
                } else {
                    Log.i("FeedDetailActivity", "error : " + response.code());
                }
            }

            @Override
            public void onFailure(Call<Result<List<Comment>>> call, Throwable t) {

            }
        });
    }

    private void initComment(List<Comment> comments) {
        adapterComment.setItemData(comments);
    }

    private void initPost(Post post) {
        textViewTitle.setText(post.getTitle());
        Controller<AccountApi> accountController = new Controller<>(AccountApi.class);
        Call<Result<Account>> accountCall = accountController.getApi().readOneById(post.getCreatorAccountId());
        accountCall.enqueue(new Callback<Result<Account>>() {
            @Override
            public void onResponse(Call<Result<Account>> call, Response<Result<Account>> response) {
                Result<Account> result = response.body();
                assert result != null;
                setTextViewAuthor(result.getContent());
            }

            @Override
            public void onFailure(Call<Result<Account>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        textViewContent.setText(post.getContent());
        textViewTimestamp.setText(post.getCreatedAt().toString());

    }

    private void setTextViewAuthor(Account acc) {
        textViewAuthor.setText(acc.getName());
    }


    @Override
    public void onClick(View view) {
        Intent intent = null;
        switch (view.getId()) {
            case R.id.activity_feed_detail_button_comment:
                insertComment();
                break;
            case R.id.activity_feed_detail_button_update:
                intent = new Intent(view.getContext(), FeedUpdateActivity.class);
                intent.putExtra("postId", postId.toString());
                break;
            case R.id.activity_feed_detail_button_back:
                intent = new Intent(view.getContext(), HomeActivity.class);
                break;
        }

        if (intent != null) {
            FeedDetailActivity.this.startActivity(intent);
        }
    }
}