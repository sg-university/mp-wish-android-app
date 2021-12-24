package com.binus.mp.wish;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.binus.mp.wish.models.JSONPlaceHolderAPI;
import com.binus.mp.wish.models.Post;

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
        Retrofit retrofit = new Retrofit.Builder().
                baseUrl("https://mp-wish-web-service-one.herokuapp.com/api/v1/").
                addConverterFactory(GsonConverterFactory.create()).build();

        JSONPlaceHolderAPI placeHolderAPI = retrofit.create(JSONPlaceHolderAPI.class);
        Call<List<Post>> listCall = placeHolderAPI.getPosts();
        listCall.enqueue(new Callback<List<Post>>() {
            @Override
            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
                if(!response.isSuccessful()){
                    txtView.setText("Error : "+ response.code());
                }else{
                    List<Post> posts= response.body();
                    String pesan = "";
                    for(Post post: posts){

                        pesan = "Id : " + post.getID()+"\n";
                        pesan+= "Title : "+ post.getTitle()+"\n";
                        pesan+= "Content : " + post.getContent()+"\n";
                        pesan+= "Creator AccountID : " + post.getCreatorAccountID()+"\n\n";
                        txtView.append(pesan);
                    }
//                    Toast.makeText(FeedActivity.this, pesan, Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<Post>> call, Throwable t) {
                txtView.setText(t.getMessage());
            }
        });
    }
}