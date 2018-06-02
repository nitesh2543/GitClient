package com.lbrand.githubclient.mvvm.view.fragment;


import android.app.Fragment;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.lbrand.githubclient.R;
import com.lbrand.githubclient.BR;
import com.lbrand.githubclient.common.Constants;
import com.lbrand.githubclient.databinding.FragmentRepoDetailBinding;
import com.lbrand.githubclient.mvvm.model.Repo;
import com.lbrand.githubclient.mvvm.model.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class RepoDetailFragment extends Fragment {

    private FragmentRepoDetailBinding binding;
    private Repo repo;
    private User user;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        repo = getArguments().getParcelable(Constants.BundleKeys.REPO);
        user = getArguments().getParcelable(Constants.BundleKeys.USER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater,R.layout.fragment_repo_detail, container, false);
        binding.setVariable(BR.repo,repo);
        binding.setVariable(BR.user,user);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }
}
