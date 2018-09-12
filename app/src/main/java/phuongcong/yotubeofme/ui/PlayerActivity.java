package phuongcong.yotubeofme.ui;

import android.os.Bundle;
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
import phuongcong.yotubeofme.data.PlaylistItem;
import phuongcong.yotubeofme.data.VideosResponse;
import phuongcong.yotubeofme.data.YtPlaylistResponse;
import phuongcong.yotubeofme.data.YtVideo;
import phuongcong.yotubeofme.network.APIClient;
import phuongcong.yotubeofme.utils.APIInterface;
import phuongcong.yotubeofme.utils.Funs;
import phuongcong.yotubeofme.utils.Vari;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class PlayerActivity extends YouTubeBaseActivity implements YouTubePlayer.OnInitializedListener {


     YtVideo ytVideo = new YtVideo();
    private APIInterface apiInterface;
    static private String VIDEO = "";//"SR6iYWJxHqs";
    private YouTubePlayerView youTubeView;
    private TextView videoTitle,videoLikeNumber,videocomentNumber;
    private RecyclerView videoRcv;
    private ArrayList<VideosResponse.Item> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_player);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        findView();
        Bundle bundle = getIntent().getExtras();
        if (bundle != null){
            VIDEO = bundle.getString("idVideo");
            items = (ArrayList<VideosResponse.Item>) bundle.getSerializable("list");
        }

        getVideoInfo();


    }

    private void findView() {
        youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
        videoTitle = (TextView) findViewById(R.id.tv_video_title);
        videoLikeNumber = (TextView) findViewById(R.id.tv_video_likeNumber);
        videocomentNumber = (TextView) findViewById(R.id.tv_video_commentNumber);
        videoRcv = (RecyclerView) findViewById(R.id.rcv_video);
    }

    private void getVideoInfo() {
        if (Funs.isNetworkAvailable(this)) {

            youTubeView.initialize(Vari.DEVELOPER_KEY, this);
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

    private void setView() {
        videoTitle.setText(ytVideo.getItems().get(0).getSnippet().getTitle());
        String cmtNum =ytVideo.getItems().get(0).getStatistics().getCommentCount() + " cmt";
        String likeNum =ytVideo.getItems().get(0).getStatistics().getLikeCount() + " like";
        videocomentNumber.setText(cmtNum);
        videoLikeNumber.setText(likeNum);

    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        youTubePlayer.loadVideo(VIDEO);
        /*youTubePlayer.setFullscreen(true);*/
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        Toast.makeText(this, "Error :" + youTubeInitializationResult.toString(), Toast.LENGTH_LONG)
                .show();
    }
}
