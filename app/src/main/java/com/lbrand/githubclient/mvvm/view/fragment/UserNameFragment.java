package com.lbrand.githubclient.mvvm.view.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.lbrand.githubclient.R;
import com.lbrand.githubclient.Util.Utility;
import com.lbrand.githubclient.common.Constants;
import com.lbrand.githubclient.common.Network;
import com.lbrand.githubclient.databinding.FragmentUserNameBinding;
import com.lbrand.githubclient.mvvm.model.User;
import com.lbrand.githubclient.mvvm.viewmodel.UserViewModel;
import com.lbrand.githubclient.network.NetworkService;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 */
public class UserNameFragment extends Fragment implements UserViewModel.UserListener, View.OnClickListener {

    private final String TAG = UserNameFragment.class.getSimpleName();
    private FragmentUserNameBinding binding;
    private UserViewModel userViewModel;
    private CompositeDisposable compositeDisposable;
    private Constants.RepoListener repoListener;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        repoListener = (Constants.RepoListener) context;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        repoListener = (Constants.RepoListener) activity;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_user_name, container, false);
        binding.btnLogin.setOnClickListener(this);
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        compositeDisposable = new CompositeDisposable();
        userViewModel = new UserViewModel(this, compositeDisposable);
    }

    private void login() {
        Utility.hideKeyboard(getActivity());
        Observable<Response<User>> observable = NetworkService.create(getActivity(),
                null).getUserInfo(binding.userName.getText().toString());
        if (TextUtils.isEmpty(binding.userName.getText().toString().trim())) {
            Toast.makeText(getActivity(), R.string.err_empty, Toast.LENGTH_SHORT).show();
            return;
        }
        if (!Network.isConnected(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        binding.progressBar.setVisibility(View.VISIBLE);
        userViewModel.getUserInfo(observable);
    }

    @Override
    public void onUserFetchSuccess(User user) {
        binding.progressBar.setVisibility(View.GONE);
        binding.userName.setText("");
        repoListener.onUserFetchSuccess(user);
    }

    @Override
    public void onUserFetchFailure(String errMsg) {
        binding.progressBar.setVisibility(View.GONE);
        Log.e(TAG, "onUserFetchFailure: " + errMsg);
    }

    @Override
    public void onUserNotFound() {
        binding.progressBar.setVisibility(View.GONE);
        Utility.showAlert(getActivity(), getActivity().getResources().getString(R.string.err_not_found));
    }

    @Override
    public void onAccessDenied() {
        binding.progressBar.setVisibility(View.GONE);
        Toast.makeText(getActivity(), "You don't have access to this repo!!", Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                login();
                break;
        }
    }
}
