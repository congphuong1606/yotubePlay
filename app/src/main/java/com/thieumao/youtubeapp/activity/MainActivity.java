package com.thieumao.youtubeapp.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.thieumao.youtubeapp.R;
import com.thieumao.youtubeapp.data.YtPlaylistResponse;
import com.thieumao.youtubeapp.data.YtVideoResponse;
import com.thieumao.youtubeapp.network.APIClient;
import com.thieumao.youtubeapp.utils.APIInterface;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    APIInterface apiInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        apiInterface = APIClient.getClient().create(APIInterface.class);
        getPlList();

    }

    private void getPlList() {
     /*   String playlistId="PLBcAa442MLAo9uhMRPazrSXlDNe2_mcO3";*/
        String channelId="UCYB4za4comHv4yYjgm6oMFA";
        Call<YtPlaylistResponse> call = apiInterface.doGetListPlaylist(channelId);
        call.enqueue(new Callback<YtPlaylistResponse>() {
            @Override
            public void onResponse(Call<YtPlaylistResponse> call, Response<YtPlaylistResponse> response) {
                YtPlaylistResponse ytPlaylistResponse = response.body();
            }

            @Override
            public void onFailure(Call<YtPlaylistResponse> call, Throwable t) {
                call.cancel();
            }
        });
    }
}
