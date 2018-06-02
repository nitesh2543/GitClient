package com.lbrand.githubclient.mvvm.viewmodel;

import android.util.Log;

import com.lbrand.githubclient.mvvm.model.User;

import org.json.JSONObject;

import java.net.HttpURLConnection;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class UserViewModel {

    private final String TAG = UserViewModel.class.getSimpleName();
    private UserListener userListener;
    private CompositeDisposable compositeDisposable;

    public interface UserListener {

        void onUserFetchSuccess(User user);
        void onUserFetchFailure(String errMsg);
        void onUserNotFound();
        void onAccessDenied();
    }

    public UserViewModel(UserListener userListener, CompositeDisposable compositeDisposable) {
        this.userListener = userListener;
        this.compositeDisposable = compositeDisposable;
    }

    public void getUserInfo(Observable<Response<User>> observable){
        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<User>>() {
                    @Override
                    public void onNext(Response<User> value) {
                        if (value.code() == HttpURLConnection.HTTP_OK) {
                            userListener.onUserFetchSuccess(value.body());
                        }
                        if (value.code() == HttpURLConnection.HTTP_NOT_FOUND) {
                            userListener.onUserNotFound();
                        }
                        if (value.code() == HttpURLConnection.HTTP_FORBIDDEN) {
                            userListener.onAccessDenied();
                        } else {
                            try {
                                JSONObject errorObj = new JSONObject(value.errorBody().string());
                                userListener.onUserFetchFailure(value.errorBody().string());
                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ");
                        userListener.onUserFetchFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("onComplete", "Taddaaaaaaaa......!!!!");
                    }
                }));
    }
}
