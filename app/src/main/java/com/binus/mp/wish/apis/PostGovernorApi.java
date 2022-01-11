package com.binus.mp.wish.apis;

import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.models.PostGovernor;

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

public interface PostGovernorApi {
    public static String ENDPOINT = "postGovernors";

    @GET(ENDPOINT + "/{id}")
    Call<Result<PostGovernor>> readOneById(@Path("id") UUID id);

    @GET(ENDPOINT + "")
    Call<Result<List<PostGovernor>>> readAll();

    @GET(ENDPOINT + "")
    Call<Result<List<PostGovernor>>> readAll(@QueryMap Map<String, String> filter);

    @POST(ENDPOINT + "")
    Call<Result<PostGovernor>> createOne(@Body PostGovernor postGovernorToCreate);

    @PUT(ENDPOINT + "/{id}")
    Call<Result<PostGovernor>> updateOneById(@Path("id") UUID id, @Body PostGovernor postGovernorToUpdate);

    @PATCH(ENDPOINT + "/{id}")
    Call<Result<PostGovernor>> patchOneById(@Path("id") UUID id, @Body PostGovernor postGovernorToPatch);

    @DELETE(ENDPOINT + "/{id}")
    Call<Result<PostGovernor>> deleteOneById(@Path("id") UUID id);
}
