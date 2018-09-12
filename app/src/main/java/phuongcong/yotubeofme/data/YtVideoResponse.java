package phuongcong.yotubeofme.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ominext on 9/11/2018.
 */

public class YtVideoResponse {
    @SerializedName("nextPageToken")
    public String nextPageToken;
    @SerializedName("pageInfo")
    public PageInfo pageInfo;
    @SerializedName("items")
    public ArrayList<SnippetParent> items =null;



    public class SnippetParent {
        @SerializedName("snippet")
        public Snippet snippet;

    }


    public class PageInfo {
        @SerializedName("totalResults")
        public Integer totalResults;
        @SerializedName("resultsPerPage")
        public Integer resultsPerPage;


    }
    public class Snippet {
        @SerializedName("title")
        public String titlePl;
        @SerializedName("thumbnails")
        public Thumbnails thumbnails;
        @SerializedName("resourceId")
        public ResourceId resourceId;

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
    public class ResourceId {
        @SerializedName("videoId")
        public String videoId;

    }
}
