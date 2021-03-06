package com.binus.mp.wish.apis;

import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.models.Comment;

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
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface CommentApi {
    public static String ENDPOINT = "comments";

    @GET(ENDPOINT + "/{id}")
    Call<Result<Comment>> readOneById(@Path("id") UUID id);

    @GET(ENDPOINT + "")
    Call<Result<List<Comment>>> readAll();

    @GET(ENDPOINT + "")
    Call<Result<List<Comment>>> readAll(@QueryMap Map<String, String> filter);

    @POST(ENDPOINT + "")
    Call<Result<Comment>> createOne(@Body Comment commentToCreate);

    @PUT(ENDPOINT + "/{id}")
    Call<Result<Comment>> updateOneById(@Path("id") UUID id, @Body Comment commentToUpdate);

    @PATCH(ENDPOINT + "/{id}")
    Call<Result<Comment>> patchOneById(@Path("id") UUID id, @Body Comment commentToPatch);

    @DELETE(ENDPOINT + "/{id}")
    Call<Result<Comment>> deleteOneById(@Path("id") UUID id);
}
