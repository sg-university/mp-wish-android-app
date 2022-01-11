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

public class FeedDetailActivity extends AppCompatActivity {
    TextView title, author, content, date;
    ImageButton backBtn;
    RecyclerView rv;
    EditText et;
    Button commentBtn;
    ProgressDialog dialog;
    UUID id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);
        Intent intent = getIntent();
        id = UUID.fromString(intent.getStringExtra("id"));
        title = findViewById(R.id.detailTitle);
        author = findViewById(R.id.detailAuthor);
        content = findViewById(R.id.detailContent);
        date = findViewById(R.id.detailPostDate);
        backBtn = findViewById(R.id.detailBackBtn);
        et = findViewById(R.id.detailET);
        commentBtn = findViewById(R.id.detailBtn);
        rv = findViewById(R.id.detailRv);

        et.getText();
        commentBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                insertComment();
            }
        });

        Log.i("Test", "UUID : " + id);
        dialog = new ProgressDialog(this);
        dialog.setMessage("Please wait for a moment...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

        Controller<PostApi> controller = new Controller<>(PostApi.class);
        Call<Result<Post>> call = controller.getApi().readOne(id);
        call.enqueue(new Callback<Result<Post>>() {
            @Override
            public void onResponse(Call<Result<Post>> call, Response<Result<Post>> response) {
                if (response.isSuccessful()) {
                    Result<Post> result = response.body();
                    assert result != null;
                    if (result.getStatus().equals("read")) {
                        initPost(result.getContent());
                    }
                } else {
                    Log.i("FeedDetailActivity", "error : " + response.code());
                }

            }

            @Override
            public void onFailure(Call<Result<Post>> call, Throwable t) {
                t.printStackTrace();

            }
        });
        generateComment();
    }

    private void insertComment() {
        String comment = et.getText().toString();
        Auth auth = Auth.getInstance();
        Account acc = auth.getAccount();
        Controller<CommentApi> controller = new Controller<>(CommentApi.class);
        Comment comment1 = new Comment(UUID.randomUUID(), id, acc.getId(), comment, new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
        Call<Result<Comment>> call = controller.getApi().createOne(comment1);
        call.enqueue(new Callback<Result<Comment>>() {
            @Override
            public void onResponse(Call<Result<Comment>> call, Response<Result<Comment>> response) {
                //success
                refresh();
            }

            @Override
            public void onFailure(Call<Result<Comment>> call, Throwable t) {

            }
        });

    }


    private void generateComment() {
        Controller<CommentApi> controller = new Controller<>(CommentApi.class);
        HashMap<String, String> x = new HashMap<>();
        x.put("post_id", id.toString());
        Call<Result<List<Comment>>> call = controller.getApi().readAll(x);
        call.enqueue(new Callback<Result<List<Comment>>>() {
            @Override
            public void onResponse(Call<Result<List<Comment>>> call, Response<Result<List<Comment>>> response) {
                if (response.isSuccessful()) {
                    dialog.hide();
                    Result<List<Comment>> result = response.body();
                    assert result != null;
                    if (result.getStatus().equals("read")) {

                        if (result.getContent() != null) {
                            initComment(result.getContent());
                            refresh();
                            Toast.makeText(FeedDetailActivity.this, "There is comment", Toast.LENGTH_SHORT).show();
//                            TextView tv = findViewById(R.id.commentMsg);
//                            tv.setText("There is comment");
                        } else {
//                            TextView tv = findViewById(R.id.commentMsg);
//                            tv.setText("No Comment now");
                            Toast.makeText(FeedDetailActivity.this, "No comment now", Toast.LENGTH_SHORT).show();
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

    private void refresh() {
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }

    private void initComment(List<Comment> comments) {
        CommentAdapter adapter = new CommentAdapter(comments);
        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(this));
        rv.setAdapter(adapter);

    }

    private void initPost(Post post) {
        title.setText(post.getTitle());
        Controller<AccountApi> accountController = new Controller<>(AccountApi.class);
        Call<Result<Account>> accountCall = accountController.getApi().readOne(post.getCreatorAccountId());
        accountCall.enqueue(new Callback<Result<Account>>() {
            @Override
            public void onResponse(Call<Result<Account>> call, Response<Result<Account>> response) {
                Result<Account> result = response.body();
                assert result != null;
                setAuthor(result.getContent());
            }

            @Override
            public void onFailure(Call<Result<Account>> call, Throwable t) {
                t.printStackTrace();
            }
        });
        content.setText(post.getContent());
        date.setText(post.getCreatedAt().toString());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), HomeActivity.class);
                view.getContext().startActivity(intent);
            }
        });


    }

    private void setAuthor(Account acc) {
        author.setText(acc.getName());
    }


}