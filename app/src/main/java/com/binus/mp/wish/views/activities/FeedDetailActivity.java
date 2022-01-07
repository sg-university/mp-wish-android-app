package com.binus.mp.wish.views.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import com.binus.mp.wish.R;
import com.binus.mp.wish.apis.PostApi;
import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.controllers.Controller;
import com.binus.mp.wish.models.Post;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedDetailActivity extends AppCompatActivity {
    TextView title,author,content,date;
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
        Log.i("Test","UUID : " +id);
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
                if(response.isSuccessful()){
                    Result<Post> post = response.body();
                    initPost(post.getContent());
                }else{
                    Log.i("FeedDetailActivity","error : "+response.code());

                }
            }

            @Override
            public void onFailure(Call<Result<Post>> call, Throwable t) {

            }
        });

    }

    private void initPost(Post post){
        title.setText(post.getTitle());
//        author.setText(post.);
        content.setText(post.getContent());
        date.setText(post.getCreatedAt().getDate());

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(),HomeActivity.class);
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
        dialog.hide();
    }


}