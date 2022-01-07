package com.binus.mp.wish.apis;

import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.models.Account;

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

public interface AccountApi {
    public static String ENDPOINT = "accounts";

    @GET(ENDPOINT + "/{id}")
    Call<Result<Account>> readOne(@Path("id") UUID id);

    @GET(ENDPOINT + "")
    Call<Result<List<Account>>> readAll();

    @GET(ENDPOINT + "")
    Call<Result<List<Account>>> readAll(@QueryMap Map<String, String> filter);

    @POST(ENDPOINT + "")
    Call<Result<Account>> createOne(@Body Account accountToCreate);

    @PUT(ENDPOINT + "/{id}")
    Call<Result<Account>> updateOne(@Path("id") UUID id, @Body Account accountToUpdate);

    @PATCH(ENDPOINT + "/{id}")
    Call<Result<Account>> patchOne(@Path("id") UUID id, @Body Account accountToPatch);

    @DELETE(ENDPOINT + "/{id}")
    Call<Result<Account>> deleteOne(@Path("id") UUID id);
}
