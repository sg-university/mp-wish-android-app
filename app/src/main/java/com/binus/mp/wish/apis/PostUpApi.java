package com.binus.mp.wish.apis;

import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.models.PostUp;

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

public interface PostUpApi {
    public static String ENDPOINT = "post-ups";

    @GET(ENDPOINT + "/{id}")
    Call<Result<PostUp>> readOneById(@Path("id") UUID id);

    @GET(ENDPOINT + "")
    Call<Result<List<PostUp>>> readAll();

    @GET(ENDPOINT + "")
    Call<Result<List<PostUp>>> readAll(@QueryMap Map<String, String> filter);

    @POST(ENDPOINT + "")
    Call<Result<PostUp>> createOne(@Body PostUp postUpToCreate);

    @PUT(ENDPOINT + "/{id}")
    Call<Result<PostUp>> updateOneById(@Path("id") UUID id, @Body PostUp postUpToUpdate);

    @PATCH(ENDPOINT + "/{id}")
    Call<Result<PostUp>> patchOneById(@Path("id") UUID id, @Body PostUp postUpToPatch);

    @DELETE(ENDPOINT + "/{id}")
    Call<Result<PostUp>> deleteOneById(@Path("id") UUID id);
}
