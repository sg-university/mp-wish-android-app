package com.binus.mp.wish.controllers;

import static com.binus.mp.wish.configs.Environment.SERVER_URL;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class Controller<T> {
    Class<T> apiClass;

    public Controller(Class<T> apiClass) {
        this.apiClass = apiClass;
    }

    public T getApi() {

        HttpLoggingInterceptor loggingInterceptor =
                new HttpLoggingInterceptor()
                        .setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient.Builder httpClient = new OkHttpClient.Builder().
                addInterceptor(loggingInterceptor);
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(SERVER_URL)
                .addConverterFactory(JacksonConverterFactory.create())
                .client(httpClient.build())
                .build();

        return retrofit.create(apiClass);
    }
}
