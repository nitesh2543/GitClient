package com.lbrand.githubclient.mvvm.view.adapter;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.lbrand.githubclient.R;
import com.lbrand.githubclient.BR;
import com.lbrand.githubclient.common.FooterViewHolder;
import com.lbrand.githubclient.common.RecyclerViewEvents;
import com.lbrand.githubclient.common.RecyclerViewHolder;
import com.lbrand.githubclient.common.RecyclerViewOnItemClickHandler;
import com.lbrand.githubclient.databinding.AdapterRepoListItemBinding;
import com.lbrand.githubclient.mvvm.model.Repo;

import java.util.List;

public class RepoListingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private final LayoutInflater layoutInflater;
    private final List<Repo> list;
    private final RecyclerViewEvents.Listener<Repo> listener;

    public RepoListingAdapter(LayoutInflater layoutInflater, List<Repo> list, RecyclerViewEvents.Listener<Repo> listener) {
        this.layoutInflater = layoutInflater;
        this.list = list;
        this.listener = listener;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {

            case RecyclerViewEvents.FOOTER:
                return new FooterViewHolder(layoutInflater.inflate(R.layout.footer_progress_bar, parent, false));
            default:
                AdapterRepoListItemBinding binding = DataBindingUtil.inflate(layoutInflater,
                        R.layout.adapter_repo_list_item, parent, false);
                return new RecyclerViewHolder<>(binding);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (holder instanceof RecyclerViewHolder) {
            final Repo item = list.get(position);
            final RecyclerViewHolder repoViewHolder = (RecyclerViewHolder) holder;
            repoViewHolder.getBinding().setVariable(BR.clickListener,
                    new RecyclerViewOnItemClickHandler<>(item, position, listener));
            repoViewHolder.getBinding().setVariable(BR.repo, item);
            repoViewHolder.getBinding().executePendingBindings();
        }

    }

    @Override
    public int getItemViewType(int position) {
        Repo item = list.get(position);
        return item.viewType;
    }

}
