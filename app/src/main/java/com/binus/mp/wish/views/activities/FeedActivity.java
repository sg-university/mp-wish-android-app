package com.binus.mp.wish.views.activities;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.binus.mp.wish.R;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        TextView txtView = findViewById(R.id.textView);

//      Call<PostAPI> listCall = placeHolderAPI.getPosts();
//
//
//      listCall.enqueue(new Callback<PostAPI>() {
//            @Override
//            public void onResponse(Call<PostAPI> call, Response<PostAPI> response) {
//                if (!response.isSuccessful()) {
//                    txtView.setText("Error : " + response.code());
//                } else {
//                    PostAPI posts = response.body();
//                    String pesan = "";
////                    txtView.append("Success");
//                    txtView.setText("");
//                    List<Post> listPost = posts.content;
//
//                    for (Post post : listPost) {
//                        pesan = "Id : " + post.getId() + "\n";
//                        pesan += "Title : " + post.getTitle() + "\n";
//                        pesan += "Content : " + post.getContent() + "\n";
//                        pesan += "Creator AccountID : " + post.getCreator_account_id() + "\n\n";
//                        txtView.append(pesan);
//                    }
//
////                    Toast.makeText(FeedActivity.this, pesan, Toast.LENGTH_SHORT).show();
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<PostAPI> call, Throwable t) {
//                Log.e("FeedActivity", t.getMessage());
//                txtView.setText(t.getMessage());
//            }
//        });
    }
}