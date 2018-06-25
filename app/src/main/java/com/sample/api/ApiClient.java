package com.sample.api;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by cisner-1 on 9/3/18.
 */

public class ApiClient {

    public static final String URL_ENDPOINT = "http://api.giphy.com/";
    public static final String SEARCH_URL = URL_ENDPOINT + "v1/gifs/search";

    private static Retrofit retrofit = null;
    private static OkHttpClient client = null;
    private static HttpLoggingInterceptor interceptor = null;
    private static ApiInterface service;

    public static Retrofit getClient() {
        if (retrofit == null) {
            if (interceptor == null) {
                interceptor = new HttpLoggingInterceptor();
                interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
            }
            if (client == null) {
                client = new OkHttpClient.Builder()
                        .connectTimeout(10, TimeUnit.MINUTES)
                        .readTimeout(10, TimeUnit.MINUTES)
                        .writeTimeout(10, TimeUnit.MINUTES)
                        .retryOnConnectionFailure(true)
                        .followSslRedirects(true)
                        .build();
            }
            retrofit = new Retrofit.Builder()
                    .baseUrl(URL_ENDPOINT)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }

    public static ApiInterface getService() {
        if (service == null) {
            service = getClient().create(ApiInterface.class);
        }
        return service;
    }
}
