package com.thieumao.youtubeapp.activity;

import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.thieumao.youtubeapp.R;
import com.thieumao.youtubeapp.adapter.YtPlaylistAdapter;
import com.thieumao.youtubeapp.data.CollapsingRecyclerViewItem;
import com.thieumao.youtubeapp.data.PlaylistItem;
import com.thieumao.youtubeapp.data.YtPlaylistResponse;
import com.thieumao.youtubeapp.event.EndlessRecyclerOnScrollListener;
import com.thieumao.youtubeapp.network.APIClient;
import com.thieumao.youtubeapp.utils.APIInterface;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    APIInterface apiInterface;
    private CollapsingToolbarLayout collapsingToolbarLayout;
    private YtPlaylistResponse ytPlaylistResponse;
    ArrayList<PlaylistItem> playlistItems = new ArrayList<>();
    private YtPlaylistAdapter dataAdapter;
    private RecyclerView recyclerView;
    String nextPage ="";
    private boolean loadmore=true;
    private int overallXScroll=0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiInterface = APIClient.getClient().create(APIInterface.class);


        Toolbar toolbar = (Toolbar) findViewById(R.id.collapsing_toolbar);
        setSupportActionBar(toolbar);

        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_layout);
        collapsingToolbarLayout.setTitle("chanel name ");
        ImageView collapsingToolbarImageView = (ImageView) findViewById(R.id.collapsing_toolbar_image_view);
        collapsingToolbarImageView.setImageResource(R.drawable.anhbia);
        recyclerView = (RecyclerView) findViewById(R.id.collapsing_toolbar_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        dataAdapter = new YtPlaylistAdapter(MainActivity.this, playlistItems);
        recyclerView.setAdapter(dataAdapter);
        getPlList("");
        loadNextPage();

    }


    private void loadNextPage() {
        recyclerView.addOnScrollListener(new EndlessRecyclerOnScrollListener() {
            @Override
            public void onLoadMore(int next) {
                    if(loadmore ){
                      getPlList(nextPage);
                    }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                overallXScroll = overallXScroll + dy;
            }

        });

    }


    private void getPlList(final String nextPageT) {
     /*   String playlistId="PLBcAa442MLAo9uhMRPazrSXlDNe2_mcO3";*/
        String channelId = "UCA3GtNtuKvcj6VrwZ5Gpx9g";
        Call<YtPlaylistResponse> call = apiInterface.doGetListPlaylist(channelId,nextPageT);
        call.enqueue(new Callback<YtPlaylistResponse>() {
            @Override
            public void onResponse(Call<YtPlaylistResponse> call, Response<YtPlaylistResponse> response) {
                ytPlaylistResponse = response.body();
                if(ytPlaylistResponse.nextPageToken==null){
                    loadmore = false;
                }else if(ytPlaylistResponse.nextPageToken.equals(nextPage)){
                    loadmore = false;
                }else {
                    nextPage =ytPlaylistResponse.nextPageToken;
                    loadmore = true;
                }
                playlistItems.addAll(ytPlaylistResponse.items);
                dataAdapter.notifyDataSetChanged();

            }

            @Override
            public void onFailure(Call<YtPlaylistResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
