package com.lbrand.githubclient.mvvm.view.fragment;


import android.app.Activity;
import android.app.Fragment;
import android.content.Context;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.databinding.library.baseAdapters.BR;
import com.lbrand.githubclient.R;
import com.lbrand.githubclient.common.Constants;
import com.lbrand.githubclient.common.Network;
import com.lbrand.githubclient.common.RecyclerViewEvents;
import com.lbrand.githubclient.databinding.FragmentRepoListingBinding;
import com.lbrand.githubclient.mvvm.model.Repo;
import com.lbrand.githubclient.mvvm.model.User;
import com.lbrand.githubclient.mvvm.view.adapter.RepoListingAdapter;
import com.lbrand.githubclient.mvvm.viewmodel.RepoViewModel;
import com.lbrand.githubclient.network.NetworkService;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.disposables.CompositeDisposable;
import retrofit2.Response;


public class RepoListingFragment extends Fragment implements View.OnClickListener,
        RepoViewModel.RepoListener, RecyclerViewEvents.Listener<Repo>, SwipeRefreshLayout.OnRefreshListener {

    private FragmentRepoListingBinding binding;
    private User user;
    private List<Repo> repos;
    private RepoViewModel viewModel;
    private CompositeDisposable compositeDisposable;
    private Integer page = 20;
    private Repo footerView;
    private int previousTotal = 0; // The total number of items in the dataset after the last load
    private boolean loading = true; // True if we are still waiting for the last set of data to load.
    private int visibleThreshold = 20; // The minimum amount of items to have below your current scroll position before loading more.
    int firstVisibleItem, visibleItemCount, totalItemCount;
    private Map<String, Integer> queryMap;
    private Constants.RepoListener repoListener;

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        repoListener = (Constants.RepoListener) activity;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        repoListener = (Constants.RepoListener) context;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        user = getArguments().getParcelable(Constants.BundleKeys.USER);
        repos = new ArrayList<>();
        compositeDisposable = new CompositeDisposable();
        queryMap = new HashMap<>();
        footerView = new Repo(RecyclerViewEvents.FOOTER);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_repo_listing, container, false);
        binding.btnBack.setOnClickListener(this);
        binding.setVariable(BR.user, user);
        binding.recyclerView.setAdapter(new RepoListingAdapter(inflater, repos, this));
        binding.swipeRefreshLayout.setColorSchemeResources(R.color.colorAccent, R.color.green, R.color.colorPrimaryDark);
        binding.swipeRefreshLayout.setOnRefreshListener(this);
        return binding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = new RepoViewModel(this, compositeDisposable);
        getRepoList(page);
        binding.recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                RecyclerView.LayoutManager layoutManager = binding.recyclerView.getLayoutManager();
                visibleItemCount = recyclerView.getChildCount();
                totalItemCount = layoutManager.getItemCount();
                firstVisibleItem = ((LinearLayoutManager) layoutManager).findFirstVisibleItemPosition();

                if (loading) {
                    if (totalItemCount > previousTotal) {
                        loading = false;
                        previousTotal = totalItemCount;
                    }
                }
                if (!loading && (totalItemCount - visibleItemCount)
                        <= (firstVisibleItem + visibleThreshold)) {
                    showFooterView();
                    page += 20;
                    getRepoList(page);
                    loading = true;
                }
            }
        });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_back:
                getActivity().onBackPressed();
                break;
        }
    }

    private void getRepoList(Integer page) {
        binding.swipeRefreshLayout.setRefreshing(true);
        queryMap.put(Constants.QueryKeys.PAGE_SIZE, page);
        Observable<Response<List<Repo>>> observable = NetworkService.create(getActivity(),
                null).getRepoList(user.getUserName(), queryMap);
        if (!Network.isConnected(getActivity())) {
            Toast.makeText(getActivity(), R.string.no_internet, Toast.LENGTH_SHORT).show();
            return;
        }
        viewModel.getRepoList(observable);
    }

    @Override
    public void onRepoFetchSuccess(List<Repo> repos) {
        hideFooterView();
        if (binding.swipeRefreshLayout.isRefreshing())
            binding.swipeRefreshLayout.setRefreshing(false);
        this.repos.clear();
        this.repos.addAll(repos);
        binding.recyclerView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void onRepoFetchFailure(String errMsg) {
        if (binding.swipeRefreshLayout.isRefreshing())
            binding.swipeRefreshLayout.setRefreshing(false);
    }

    @Override
    public void onItemClick(Repo item, View v, int position) {
        repoListener.onRepoItemClicked(item ,user);
    }

    private void showFooterView() {
        if (repos.add(footerView))
            binding.recyclerView.getAdapter().notifyItemInserted(repos.size() - 1);
    }

    private void hideFooterView() {
        if (repos.remove(footerView))
            binding.recyclerView.getAdapter().notifyItemRemoved(repos.size());
    }

    @Override
    public void onRefresh() {
        getRepoList(page);
    }
}
