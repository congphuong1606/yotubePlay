package phuongcong.yotubeofme.ui;

import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import phuongcong.yotubeofme.R;
import phuongcong.yotubeofme.utils.Funs;
import phuongcong.yotubeofme.utils.Vari;


public class PlayerActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    static private String VIDEO = "sAdNmVEHZvQ";//"SR6iYWJxHqs";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_player);
        if (Funs.isNetworkAvailable(this)) {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null)
                VIDEO = bundle.getString("idVideo");

            YouTubePlayerView youTubeView = (YouTubePlayerView) findViewById(R.id.youtube_view);
            youTubeView.initialize(Vari.DEVELOPER_KEY, this);

        } else {
            Toast.makeText(this, "Please check Wifi/3G", Toast.LENGTH_SHORT)
                    .show();
        }
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
