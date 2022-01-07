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

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.binus.mp.wish.R;
import com.binus.mp.wish.apis.AccountApi;
import com.binus.mp.wish.apis.PostApi;
import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.controllers.Controller;
import com.binus.mp.wish.models.Account;
import com.binus.mp.wish.models.Post;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed_detail);
        Intent intent = getIntent();
        UUID id = UUID.fromString(intent.getStringExtra("id"));
        title = findViewById(R.id.detailTitle);
        author = findViewById(R.id.detailAuthor);
        content = findViewById(R.id.detailContent);
        date = findViewById(R.id.detailPostDate);
        backBtn = findViewById(R.id.detailBackBtn);
        et = findViewById(R.id.detailET);
        commentBtn = findViewById(R.id.detailBtn);
        rv = findViewById(R.id.detailRv);
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
                    if(result.getStatus().equals("read")){
                        initPost(result.getContent());
                    }
                } else {
                    Log.i("FeedDetailActivity", "error : " + response.code());
                }
                dialog.hide();
            }

            @Override
            public void onFailure(Call<Result<Post>> call, Throwable t) {
                t.printStackTrace();
                dialog.hide();
            }
        });

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
                author.setText(result.getContent().getName());
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

        title = findViewById(R.id.detailTitle);
        author = findViewById(R.id.detailAuthor);
        content = findViewById(R.id.detailContent);
        date = findViewById(R.id.detailPostDate);
        backBtn = findViewById(R.id.detailBackBtn);

        et = findViewById(R.id.detailET);
        commentBtn = findViewById(R.id.detailBtn);
        rv = findViewById(R.id.detailRv);
    }


}