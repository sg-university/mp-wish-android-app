package com.binus.mp.wish;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.binus.mp.wish.deserializer.Deserializer;
import com.binus.mp.wish.models.JSONPlaceHolderAPI;
import com.binus.mp.wish.models.Post;
import com.binus.mp.wish.models.PostAPI;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FeedActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        TextView txtView = findViewById(R.id.textView);
        Gson gson = new GsonBuilder().registerTypeAdapter(Post.class,new Deserializer<List<Post>>()).create();
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("https://mp-wish-web-service-one.herokuapp.com/api/v1/").
                addConverterFactory(GsonConverterFactory.create()).build();

        JSONPlaceHolderAPI placeHolderAPI = retrofit.create(JSONPlaceHolderAPI.class);

        Call<PostAPI> listCall = placeHolderAPI.getPosts();


        listCall.enqueue(new Callback<PostAPI>() {
            @Override
            public void onResponse(Call<PostAPI> call, Response<PostAPI> response) {
                if(!response.isSuccessful()){
                    txtView.setText("Error : "+ response.code());
                }else{
                    PostAPI posts= response.body();
                    String pesan = "";
//                    txtView.append("Success");
                    txtView.setText("");
                    List<Post> listPost = posts.content;

                    for (Post post: listPost) {
                        pesan = "Id : " + post.getId()+"\n";
                        pesan+= "Title : "+ post.getTitle()+"\n";
                        pesan+= "Content : " + post.getContent()+"\n";
                        pesan+= "Creator AccountID : " + post.getCreator_account_id()+"\n\n";
                        txtView.append(pesan);
                    }

//                    Toast.makeText(FeedActivity.this, pesan, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<PostAPI> call, Throwable t) {
                Log.e("FeedActivity",t.getMessage());
                txtView.setText(t.getMessage());
            }
        });
    }
}