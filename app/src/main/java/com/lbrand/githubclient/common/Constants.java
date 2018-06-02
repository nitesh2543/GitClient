package com.lbrand.githubclient.common;


import com.lbrand.githubclient.mvvm.model.Repo;
import com.lbrand.githubclient.mvvm.model.User;

public class Constants {

    public interface Keys {
        String AUTH_TOKEN = "";
    }

    public interface QueryKeys {
        String PAGE_SIZE = "per_page";
    }


    public interface ViewType {
        int TYPE_HEADER = 1;
        int TYPE_ITEM = 0;
    }

    public interface BundleKeys {
        String URL = "url";
        String ACTION_BAR_TITLE = "action_bar_title";
        String USER = "user";
        String REPO = "repo";
    }

    public interface RepoListener{
        void onUserFetchSuccess(User user);
        void onRepoItemClicked(Repo repo , User user);
    }
}
