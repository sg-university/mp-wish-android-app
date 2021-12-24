package com.binus.mp.wish.models;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface JSONPlaceHolderAPI {

    @GET("posts")
    Call<List<Post>> getPosts();


}

