package phuongcong.yotubeofme.utils;


import phuongcong.yotubeofme.data.YtPlaylistResponse;
import phuongcong.yotubeofme.data.YtVideo;
import phuongcong.yotubeofme.data.VideosResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by Ominext on 9/11/2018.
 */

public interface APIInterface {
    String DEVELOPERKEY ="AIzaSyB5Jgo4jTYPq5Nep-7k2KqQCHjV4wbWC-w";


    @GET("playlistItems?part=snippet&maxResults=10&fields=items(snippet(title,description,thumbnails(high(url),default),resourceId(videoId))),nextPageToken,pageInfo&key=" +DEVELOPERKEY)
    Call<VideosResponse> doGetListVideo(@Query("playlistId") String playlistId, @Query("pageToken") String nextPage);



    @GET("playlists?part=snippet,contentDetails&maxResults=10&fields=items(contentDetails,id,snippet(title,description,thumbnails(high(url),default))),nextPageToken,pageInfo&key="+DEVELOPERKEY)
    Call<YtPlaylistResponse> doGetListPlaylist(@Query("channelId") String channelId, @Query("pageToken") String nextPage);

    @GET("videos?&part=snippet,contentDetails,statistics&key="+DEVELOPERKEY)
    Call<YtVideo> doGetVideoInfo(@Query("id") String videoId);
}
