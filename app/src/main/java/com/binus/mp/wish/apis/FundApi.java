package com.binus.mp.wish.apis;

import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.models.Fund;

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

public interface FundApi {
    public static String ENDPOINT = "funds";

    @GET(ENDPOINT + "/{id}")
    Call<Result<Fund>> readOne(@Path("id") UUID id);

    @GET(ENDPOINT + "")
    Call<Result<List<Fund>>> readAll();

    @GET(ENDPOINT + "")
    Call<Result<List<Fund>>> readAll(@QueryMap Map<String, String> filter);

    @POST(ENDPOINT + "")
    Call<Result<Fund>> createOne(@Body Fund fundToCreate);

    @PUT(ENDPOINT + "/{id}")
    Call<Result<Fund>> updateOneById(@Path("id") UUID id, @Body Fund fundToUpdate);

    @PATCH(ENDPOINT + "/{id}")
    Call<Result<Fund>> patchOneById(@Path("id") UUID id, @Body Fund fundToPatch);

    @DELETE(ENDPOINT + "/{id}")
    Call<Result<Fund>> deleteOneByIdById(@Path("id") UUID id);
}
