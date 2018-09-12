package phuongcong.yotubeofme.utils;


import phuongcong.yotubeofme.data.YtPlaylistResponse;
import phuongcong.yotubeofme.data.YtVideoResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ominext on 9/11/2018.
 */

public interface APIInterface {
    String DEVELOPERKEY ="AIzaSyB5Jgo4jTYPq5Nep-7k2KqQCHjV4wbWC-w";


    @GET("playlistItems?part=snippet&maxResults=10&fields=items(snippet(title,thumbnails(high(url),default),resourceId(videoId))),nextPageToken,pageInfo&key=" +DEVELOPERKEY)
    Call<YtVideoResponse> doGetListVideo(@Query("playlistId") String playlistId);



    @GET("playlists?part=snippet,contentDetails&maxResults=10&fields=items(contentDetails,id,snippet(title,thumbnails(high(url),default))),nextPageToken,pageInfo&key="+DEVELOPERKEY)
    Call<YtPlaylistResponse> doGetListPlaylist(@Query("channelId") String channelId, @Query("pageToken") String nextPage);
}
