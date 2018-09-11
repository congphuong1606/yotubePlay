package com.thieumao.youtubeapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ominext on 9/11/2018.
 */

public class YtPlaylistResponse {
    @SerializedName("nextPageToken")
    public String nextPageToken;
    @SerializedName("pageInfo")
    public YtVideoResponse.PageInfo pageInfo;
    @SerializedName("items")
    public ArrayList<SnippetParent> items =null;


    public class SnippetParent {
        @SerializedName("id")
        public  String id;
        @SerializedName("snippet")
        public Snippet snippet;
        @SerializedName("ContentDetails")
        public ContentDetails ContentDetails;


    }

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

    public class PageInfo {
        @SerializedName("totalResults")
        public  Integer totalResults;
        @SerializedName("resultsPerPage")
        public Integer resultsPerPage;


    }

    public class Thumbnails {
        @SerializedName("default")
        public YtVideoResponse.Default defaultt;
        @SerializedName("high")
        public YtVideoResponse.High high;


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
