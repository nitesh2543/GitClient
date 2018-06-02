package com.lbrand.githubclient.mvvm.view.activity;

import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.lbrand.githubclient.R;
import com.lbrand.githubclient.common.Constants;
import com.lbrand.githubclient.common.FragmentHelper;
import com.lbrand.githubclient.databinding.ActivityMainBinding;
import com.lbrand.githubclient.mvvm.model.Repo;
import com.lbrand.githubclient.mvvm.model.User;
import com.lbrand.githubclient.mvvm.view.fragment.RepoDetailFragment;
import com.lbrand.githubclient.mvvm.view.fragment.RepoListingFragment;
import com.lbrand.githubclient.mvvm.view.fragment.UserNameFragment;

public class MainActivity extends AppCompatActivity implements Constants.RepoListener {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        DataBindingUtil.setContentView(this, R.layout.activity_main);
        FragmentHelper.addFragment(this, R.id.main_container, new UserNameFragment());
    }

    @Override
    public void onUserFetchSuccess(User user) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BundleKeys.USER, user);
        Fragment fragment = new RepoListingFragment();
        fragment.setArguments(bundle);
        FragmentHelper.replaceAndAddFragment(this, R.id.main_container, fragment);
    }

    @Override
    public void onRepoItemClicked(Repo repo ,User user) {
        Bundle bundle = new Bundle();
        bundle.putParcelable(Constants.BundleKeys.REPO, repo);
        bundle.putParcelable(Constants.BundleKeys.USER, user);
        Fragment fragment = new RepoDetailFragment();
        fragment.setArguments(bundle);
        FragmentHelper.replaceAndAddFragment(this, R.id.main_container, fragment);
    }
}
