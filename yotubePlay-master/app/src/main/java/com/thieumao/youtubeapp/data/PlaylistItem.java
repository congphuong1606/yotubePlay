package com.thieumao.youtubeapp.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ominext on 9/11/2018.
 */

public class PlaylistItem {
    @SerializedName("id")
    public  String id;
    @SerializedName("snippet")
    public Snippet snippet;
    @SerializedName("contentDetails")
    public ContentDetails contentDetails;

    public class ContentDetails {
        @SerializedName("itemCount")
        public Integer itemCount;


    }


    public class Snippet {
        @SerializedName("title")
        public String titlePl;
        @SerializedName("thumbnails")
        public Thumbnails thumbnails;

    }
    public class Thumbnails {
        @SerializedName("default")
        public Default defaultt;
        @SerializedName("high")
        public High high;


    }


    public class Default {
        @SerializedName("url")
        public String url;
        @SerializedName("width")
        public Integer widthIM;
        @SerializedName("height")
        public Integer heightIM;

    }


    public class High {
        @SerializedName("url")
        public String url;

    }
}
