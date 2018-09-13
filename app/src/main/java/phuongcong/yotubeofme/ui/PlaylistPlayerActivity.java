package phuongcong.yotubeofme.ui;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Window;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import java.util.ArrayList;
import phuongcong.yotubeofme.R;
import phuongcong.yotubeofme.adapter.YtVideoAdapter;
import phuongcong.yotubeofme.data.VideosResponse;
import phuongcong.yotubeofme.data.YtVideo;
import phuongcong.yotubeofme.event.EndlessRecyclerOnScrollListener;
import phuongcong.yotubeofme.event.OnClickIteml;
import phuongcong.yotubeofme.network.APIClient;
import phuongcong.yotubeofme.utils.APIInterface;
import phuongcong.yotubeofme.utils.Funs;
import phuongcong.yotubeofme.utils.Vari;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PlaylistPlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener, OnClickIteml {

    public static String title = "title";
    RecyclerView rcvPlaylist;
    YtVideoAdapter adapterPlaylist;
    String idPlaylist = "";
    private APIInterface apiInterface;
    private boolean loadmore=true;
    private ArrayList<VideosResponse.Item> items = new ArrayList<>();
    private String nextPage="";
    static private String VIDEO = "abcabc";

    private TextView videoTitle,videoLikeNumber,videocomentNumber;
    private YtVideo ytVideo=new YtVideo();
    private static final int RECOVERY_REQUEST = 1;
    private YouTubePlayerView youTubeView;
    private int currentMiPlay=0;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_playlist);

        apiInterface = APIClient.getClient().create(APIInterface.class);
        idPlaylist =getIntent().getStringExtra("id");
        VIDEO =getIntent().getStringExtra("videoId");
        findView();
        setupRecycleview();
        gitListvideo("");
        loadNextPage();

    }

    private void findView() {
        youTubeView =(YouTubePlayerView)findViewById(R.id.youtube_view1) ;
        videoTitle = (TextView) findViewById(R.id.tv_video_title);
        videoLikeNumber = (TextView) findViewById(R.id.tv_video_likeNumber);
        videocomentNumber = (TextView) findViewById(R.id.tv_video_commentNumber);
    }







    private void getVideoInfo() {
        if (Funs.isNetworkAvailable(this)) {
            Call<YtVideo> call = apiInterface.doGetVideoInfo(VIDEO);
            call.enqueue(new Callback<YtVideo>() {
                @Override
                public void onResponse(Call<YtVideo> call, Response<YtVideo> response) {
                    ytVideo = response.body();
                    setView();

                }
                @Override
                public void onFailure(Call<YtVideo> call, Throwable t)
                {
                    call.cancel();
                }
            });


        } else {
            Toast.makeText(this, "Please check Wifi/3G", Toast.LENGTH_SHORT)
                    .show();
        }



    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == RECOVERY_REQUEST) {
            getYouTubePlayerProvider().initialize(Vari.DEVELOPER_KEY, this);
        }
    }

    protected YouTubePlayer.Provider getYouTubePlayerProvider() {
        return youTubeView;
    }



    private void setView() {
        videoTitle.setText(ytVideo.getItems().get(0).getSnippet().getTitle());
        String cmtNum =ytVideo.getItems().get(0).getStatistics().getCommentCount() + " cmt";
        String likeNum =ytVideo.getItems().get(0).getStatistics().getLikeCount() + " like";
        videocomentNumber.setText(cmtNum);
        videoLikeNumber.setText(likeNum);
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer,boolean wasRestored) {
        ArrayList<String> listId= new ArrayList<>();
        listId.add(VIDEO);
        for(VideosResponse.Item item:items){
            listId.add(item.getSnippet().getResourceId().getVideoId());
        }
        youTubePlayer.loadVideos(listId);
        youTubePlayer.setPlaybackEventListener(new YouTubePlayer.PlaybackEventListener() {
            @Override
            public void onPlaying() {

            }

            @Override
            public void onPaused() {

            }

            @Override
            public void onStopped() {

            }

            @Override
            public void onBuffering(boolean b) {
                // Called when buffering starts or ends.
            }

            @Override
            public void onSeekTo(int i) {
            }
        });


    }



    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider,
                                        YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Error :" + youTubeInitializationResult.toString(), Toast.LENGTH_LONG)
                .show();
    }






    private void setupRecycleview() {
        rcvPlaylist = (RecyclerView) findViewById(R.id.listviewPlaylist);
        rcvPlaylist.setHasFixedSize(true);
        rcvPlaylist.setItemAnimator(new DefaultItemAnimator());
        rcvPlaylist
                .addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        rcvPlaylist.setLayoutManager(new LinearLayoutManager(this));
        adapterPlaylist = new YtVideoAdapter(PlaylistPlayerActivity.this, items,this);
        rcvPlaylist.setAdapter(adapterPlaylist);
    }


    private void loadNextPage() {
        rcvPlaylist.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(int next) {
                if(loadmore){
                    gitListvideo(nextPage);
                }
            }

        });

    }



    private void gitListvideo(String nextPageT) {
            Call<VideosResponse> call = apiInterface.doGetListVideo(idPlaylist,nextPageT);
            call.enqueue(new Callback<VideosResponse>() {
                @Override
                public void onResponse(Call<VideosResponse> call, Response<VideosResponse> response) {
                    VideosResponse videosResponse = response.body();
                    for(VideosResponse.Item item : videosResponse.getItems()){
                        if(!item.getSnippet().getTitle().equals("Private video")){
                            items.add(item);
                        }
                    }
                    if(VIDEO.equals("abcabc")&&videosResponse.getItems().size()>0){
                        VIDEO=items.get(0).getSnippet().getResourceId().getVideoId();
                        getVideoInfo();
                        youTubeView.initialize(Vari.DEVELOPER_KEY,PlaylistPlayerActivity.this);

                    }
                    int i=-1;
                    int index=0;
                    for(VideosResponse.Item item : items){
                        i++;
                        if(item.getSnippet().getResourceId().getVideoId().equals(VIDEO)){
                            index = i;
                        }
                    }
                    items.remove(index);
                    adapterPlaylist.notifyDataSetChanged();
                    if(videosResponse.getNextPageToken()==null){
                        loadmore = false;
                    }else if(videosResponse.getNextPageToken().equals(nextPage)){
                        loadmore = false;
                    }else {
                        nextPage =videosResponse.getNextPageToken();
                        loadmore = true;
                    }
                    getVideoInfo();
                    youTubeView.initialize(Vari.DEVELOPER_KEY,PlaylistPlayerActivity.this);

                }
                @Override
                public void onFailure(Call<VideosResponse> call, Throwable t) {
                    call.cancel();
                }
            });



    }



    @Override
    public void onClickItem(String id) {
        Intent data = new Intent();
        data.putExtra("videoId",id);
        setResult(Activity.RESULT_OK,data);
        finish();

    }
}
