package com.thieumao.youtubeapp.utils;

/**
 * Created by thieumao on 1/22/17.
 */

public class Vari {

    public static String DEVELOPER_KEY = "AIzaSyB5Jgo4jTYPq5Nep-7k2KqQCHjV4wbWC-w";
    public static String maxResultsPlaylist = "10";
    public static String linkPlaylist = "https://www.googleapis.com/youtube/v3/playlistItems?part=snippet&maxResults=%s&playlistId=%s&fields=items(snippet(title,thumbnails(high(url),default),resourceId(videoId))),nextPageToken,pageInfo&key=" + DEVELOPER_KEY;

}
