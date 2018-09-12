package phuongcong.yotubeofme.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by Ominext on 9/11/2018.
 */

public class YtPlaylistResponse {
    @SerializedName("nextPageToken")
    public String nextPageToken;
    @SerializedName("pageInfo")
    public PageInfo pageInfo;
    @SerializedName("items")
    public ArrayList<PlaylistItem> items =null;





    public class PageInfo {
        @SerializedName("totalResults")
        public Integer totalResults;
        @SerializedName("resultsPerPage")
        public Integer resultsPerPage;
    }

}
