package com.binus.mp.wish.views.activities;

import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.binus.mp.wish.R;
import com.binus.mp.wish.apis.PostApi;
import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.controllers.Controller;
import com.binus.mp.wish.models.Auth;
import com.binus.mp.wish.models.Post;
import com.binus.mp.wish.views.adapters.FeedAdapter;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.Vector;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class FeedActivity extends AppCompatActivity {
    List<Post> listPost;
    Auth auth;

    TextView textViewLocation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feed);
        textViewLocation = findViewById(R.id.activity_feed_text_view_location);
        auth = Auth.getInstance();
        listPost = new Vector<Post>();

        try {
            initLocation();
        } catch (IOException e) {
            e.printStackTrace();
        }

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

    public void initLocation() throws IOException {
        List<Location> locations = auth.getLocations();
        Location location = locations.get(0);
        Double latitude = location.getLatitude();
        Double longitude = location.getLongitude();
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
        String cityName = addresses.get(0).getAddressLine(0);
        String stateName = addresses.get(0).getAddressLine(1);
        String countryName = addresses.get(0).getAddressLine(2);
        String locationText = String.format("%s, %s, %s.", cityName, stateName, countryName);

        textViewLocation.setText(locationText);
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