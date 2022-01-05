package com.binus.mp.wish.apis;

import com.binus.mp.wish.contracts.CredentialsByEmail;
import com.binus.mp.wish.contracts.CredentialsByHuawei;
import com.binus.mp.wish.contracts.Result;
import com.binus.mp.wish.models.Account;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface AuthenticationApi {
    public static String ENDPOINT = "authentication";

    @POST(ENDPOINT + "/login/email")
    Call<Result<Account>> loginByEmail(@Body CredentialsByEmail credentials);

    @POST(ENDPOINT + "/login/huawei/open-id")
    Call<Result<Account>> loginByHuaweiOpenId(@Body CredentialsByHuawei credentials);

    @POST(ENDPOINT + "/register/email")
    Call<Result<Account>> registerByEmail(@Body CredentialsByEmail credentials);

    @POST(ENDPOINT + "/register/huawei/open-id")
    Call<Result<Account>> registerByHuaweiOpenId(@Body CredentialsByHuawei credentials);
}
