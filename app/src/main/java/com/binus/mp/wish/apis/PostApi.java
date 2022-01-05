package com.binus.mp.wish.apis;

import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.models.Post;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface PostApi {
    public static String ENDPOINT = "posts";

    @GET(ENDPOINT + "/{id}")
    Call<Result<Post>> readOne(@Query("id") UUID id);

    @GET(ENDPOINT + "")
    Call<Result<List<Post>>> readAll();

    @GET(ENDPOINT + "")
    Call<Result<List<Post>>> readAll(@QueryMap Map<String, String> filter);

    @POST(ENDPOINT + "")
    Call<Result<Post>> createOne(@Body Post postToCreate);

    @PUT(ENDPOINT + "/{id}")
    Call<Result<Post>> updateOne(@Query("id") UUID id, @Body Post postToUpdate);

    @PATCH(ENDPOINT + "/{id}")
    Call<Result<Post>> patchOne(@Query("id") UUID id, @Body Post postToPatch);

    @DELETE(ENDPOINT + "/{id}")
    Call<Result<Post>> deleteOne(@Query("id") UUID id);
}
