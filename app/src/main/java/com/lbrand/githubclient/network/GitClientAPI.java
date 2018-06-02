package com.lbrand.githubclient.network;


import com.lbrand.githubclient.mvvm.model.Repo;
import com.lbrand.githubclient.mvvm.model.User;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface GitClientAPI {


    @GET("users/{username}")
    Observable<Response<User>> getUserInfo(@Path(value = "username", encoded = true) String userName);

    @GET("users/{username}/repos")
    Observable<Response<List<Repo>>> getRepoList(@Path(value = "username", encoded = true) String userName,
                                                 @QueryMap Map<String, Integer> page);
}
