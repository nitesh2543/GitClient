package com.lbrand.githubclient.mvvm.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.SerializedName;

public class Repo implements Parcelable {

    public int viewType;

    @SerializedName("name")
    private String name;

    @SerializedName("description")
    private String description;

    @SerializedName("watchers_count")
    private Integer watchersCount;

    @SerializedName("size")
    private Integer size;

    @SerializedName("open_issues")
    private Integer openIssues;

    @SerializedName("html_url")
    private String htmlUrl;

    protected Repo(Parcel in) {
        viewType = in.readInt();
        name = in.readString();
        description = in.readString();
        if (in.readByte() == 0) {
            watchersCount = null;
        } else {
            watchersCount = in.readInt();
        }
        if (in.readByte() == 0) {
            size = null;
        } else {
            size = in.readInt();
        }
        if (in.readByte() == 0) {
            openIssues = null;
        } else {
            openIssues = in.readInt();
        }
        htmlUrl = in.readString();
    }

    public static final Creator<Repo> CREATOR = new Creator<Repo>() {
        @Override
        public Repo createFromParcel(Parcel in) {
            return new Repo(in);
        }

        @Override
        public Repo[] newArray(int size) {
            return new Repo[size];
        }
    };

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public Integer getWatchersCount() {
        return watchersCount;
    }

    public Integer getSize() {
        return size;
    }

    public Integer getOpenIssues() {
        return openIssues;
    }

    public String getHtmlUrl() {
        return htmlUrl;
    }

    public Repo(int viewType) {
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(viewType);
        parcel.writeString(name);
        parcel.writeString(description);
        if (watchersCount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(watchersCount);
        }
        if (size == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(size);
        }
        if (openIssues == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeInt(openIssues);
        }
        parcel.writeString(htmlUrl);
    }
}
