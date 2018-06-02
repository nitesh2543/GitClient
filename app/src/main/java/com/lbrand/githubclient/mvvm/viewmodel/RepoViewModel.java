package com.lbrand.githubclient.mvvm.viewmodel;

import android.util.Log;

import com.lbrand.githubclient.mvvm.model.Repo;
import com.lbrand.githubclient.mvvm.model.User;

import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.observers.DisposableObserver;
import io.reactivex.schedulers.Schedulers;
import retrofit2.Response;

public class RepoViewModel {

    private final String TAG = RepoViewModel.class.getSimpleName();
    private RepoViewModel.RepoListener repoListener;
    private CompositeDisposable compositeDisposable;

    public RepoViewModel(RepoViewModel.RepoListener fileUploadListener, CompositeDisposable compositeDisposable) {
        this.repoListener = fileUploadListener;
        this.compositeDisposable = compositeDisposable;
    }

    public interface RepoListener {
        void onRepoFetchSuccess(List<Repo> user);
        void onRepoFetchFailure(String errMsg);
    }


    public void getRepoList(Observable<Response<List<Repo>>> observable) {
        compositeDisposable.add(observable.subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeWith(new DisposableObserver<Response<List<Repo>>>() {
                    @Override
                    public void onNext(Response<List<Repo>> value) {
                        if (value.code() == HttpURLConnection.HTTP_OK) {
                            repoListener.onRepoFetchSuccess(value.body());
                        } else {
                            try {
                                JSONObject errorObj = new JSONObject(value.errorBody().string());
                                repoListener.onRepoFetchFailure(value.errorBody().string());
                            } catch (Exception e) {
                            }
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        Log.e(TAG, "onError: ");
                        repoListener.onRepoFetchFailure(e.getMessage());
                    }

                    @Override
                    public void onComplete() {
                        Log.e("onComplete", "Taddaaaaaaaa......!!!!");
                    }
                }));
    }
}
