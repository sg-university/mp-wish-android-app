package com.binus.mp.wish.apis;

import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.models.CommentUp;

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
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface CommentUpApi {
    public static String ENDPOINT = "comment-ups";

    @GET(ENDPOINT + "/{id}")
    Call<Result<CommentUp>> readOne(@Path("id") UUID id);

    @GET(ENDPOINT + "")
    Call<Result<List<CommentUp>>> readAll();

    @GET(ENDPOINT + "")
    Call<Result<List<CommentUp>>> readAll(@QueryMap Map<String, String> filter);

    @POST(ENDPOINT + "")
    Call<Result<CommentUp>> createOne(@Body CommentUp commentUpToCreate);

    @PUT(ENDPOINT + "/{id}")
    Call<Result<CommentUp>> updateOne(@Path("id") UUID id, @Body CommentUp commentUpToUpdate);

    @PATCH(ENDPOINT + "/{id}")
    Call<Result<CommentUp>> patchOne(@Path("id") UUID id, @Body CommentUp commentUpToPatch);

    @DELETE(ENDPOINT + "/{id}")
    Call<Result<CommentUp>> deleteOne(@Path("id") UUID id);
}
