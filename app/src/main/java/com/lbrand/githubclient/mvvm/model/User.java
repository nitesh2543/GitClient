package com.lbrand.githubclient.mvvm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class User implements Parcelable{

    @SerializedName("login")
    private String userName;

    @SerializedName("url")
    private String repoUrl;

    @SerializedName("avatar_url")
    private String avatarUrl;

    protected User(Parcel in) {
        userName = in.readString();
        repoUrl = in.readString();
        avatarUrl = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    public String getRepoUrl() {
        return repoUrl;
    }

    public String getUserName() {
        return userName;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(userName);
        parcel.writeString(repoUrl);
        parcel.writeString(avatarUrl);
    }
}
