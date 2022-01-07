package com.binus.mp.wish.views.activities.menufragments;

import android.app.ProgressDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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


public class FeedFragment extends Fragment {
    List<Post> listPost;
    TextView tvLoading;
    ProgressDialog dialog;
    public FeedFragment() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_feed, container, false);

        dialog = new ProgressDialog(view.getContext());
        dialog.setMessage("Please wait for a moment...");
        dialog.setCancelable(false);
        dialog.setInverseBackgroundForced(false);
        dialog.show();

        listPost = new Vector<Post>();
        tvLoading = view.findViewById(R.id.welcomeTxt);
        Controller<PostApi> controller = new Controller<>(PostApi.class);
        Call<Result<List<Post>>> call = controller.getApi().readAll();
        call.enqueue(new Callback<Result<List<Post>>>() {
            @Override
            public void onResponse(Call<Result<List<Post>>> call, Response<Result<List<Post>>> response) {
                if(!response.isSuccessful()){
                    Log.i("FeedActivity","Error : " + response.code());

                }else{
                    Result<List<Post>> posts = response.body();
                    Log.i("FeedActivity","post0 count : " + posts.getContent().size());
                    setListPost(posts.getContent());
                    setRecyclerView(view);
//                    txtView.setText("Success");
                }
            }

            @Override
            public void onFailure(Call<Result<List<Post>>> call, Throwable t) {
                Log.e("FeedActivity",t.getMessage());
            }
        });
        return view;
    }

    private void setRecyclerView(View view){

        RecyclerView rv = view.findViewById(R.id.feedRV);
        Log.i("FeedActivity","post2 count : " + listPost.size());
        FeedAdapter adapter = new FeedAdapter(listPost);

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rv.setAdapter(adapter);
        tvLoading.setText("Welcome Users!");
        dialog.hide();
    }

    private void setListPost(List<Post> posts){
        this.listPost = posts;
        Post newPost = new Post(new UUID(100,100),new UUID(11111,1000)
                ,"title","content",new Timestamp(100),new Timestamp(100));
        this.listPost.add(newPost);
        newPost = new Post(new UUID(100,100),new UUID(11111,1000)
                ,"title","content",new Timestamp(100),new Timestamp(100));
        this.listPost.add(newPost);
        newPost = new Post(new UUID(100,100),new UUID(11111,1000)
                ,"title","content",new Timestamp(100),new Timestamp(100));
        this.listPost.add(newPost);
        Log.i("FeedActivity","post1 count : " + listPost.size());
    }
}