package com.lbrand.githubclient.network;

import android.content.Context;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import com.lbrand.githubclient.common.Constants;
import com.lbrand.githubclient.common.SharedPrefsHelper;

import java.io.File;
import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkService {

    private NetworkService() {}
    public static String BASE_URL =
            "https://api.github.com/";
    private static HttpLoggingInterceptor httpLoggingInterceptor;
    private static AuthenticationInterceptor authenticationInterceptor;
    private static File cacheFile;
    //private static Cache cache;
    private static OkHttpClient okHttpClient;

    public static GitClientAPI create(Context context, String authToken) {
        if(authToken == null)
            authToken = SharedPrefsHelper.getSharedPreferenceString(context, Constants.Keys
                .AUTH_TOKEN, null);
        Retrofit.Builder builder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient(context.getApplicationContext(), authToken))
                .baseUrl(BASE_URL);
        return builder.build().create(GitClientAPI.class);
    }

    public static OkHttpClient getOkHttpClient(Context context, String authToken) {
                    OkHttpClient.Builder builder = new OkHttpClient.Builder()
                            .addInterceptor(getHttpLoggingInterceptor());
                      if(authToken != null)
                            builder.addInterceptor(getAuthInterceptor(authToken));
                    okHttpClient = builder.build();
        return okHttpClient;
    }

    public static HttpLoggingInterceptor getHttpLoggingInterceptor() {
        if (httpLoggingInterceptor == null)
            synchronized (NetworkService.class) {
                if (httpLoggingInterceptor == null) {
                    httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            Log.e("Network",message);
                        }
                    });
                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                }
            }
        return httpLoggingInterceptor;
    }

    public static File getCacheFile(Context context) {
        if(cacheFile == null)
            synchronized (NetworkService.class) {
                if(cacheFile == null) {
                    cacheFile = new File(context.getApplicationContext().getCacheDir(), "okhttp_cache");
                }
            }
        return cacheFile;
    }

    public static AuthenticationInterceptor getAuthInterceptor(String authToken) {
        return new AuthenticationInterceptor(authToken);
    }

    private static class AuthenticationInterceptor implements Interceptor {
        private String authToken;
        private AuthenticationInterceptor(String token) {
            this.authToken = token;
        }
        @Override
        public okhttp3.Response intercept(Chain chain) throws IOException {
            Request original = chain.request();
            Request.Builder builder = original.newBuilder().header("Authorization", authToken);
            Request request = builder.build();
            return chain.proceed(request);
        }
    }
}
