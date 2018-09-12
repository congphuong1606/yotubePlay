package phuongcong.yotubeofme.ui;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;


import org.apache.http.NameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import phuongcong.yotubeofme.R;
import phuongcong.yotubeofme.adapter.YtVideoAdapter;
import phuongcong.yotubeofme.utils.Funs;
import phuongcong.yotubeofme.utils.JSONParser;
import phuongcong.yotubeofme.utils.Vari;

public class PlaylistActivity extends AppCompatActivity {

    private boolean loadOne = false;
    private boolean loadFinish = false;
    private ProgressBar loadmore;
    private int numberLoad = 0;
    private String nextPageToken = "";
    private String url = "";
    public static String title = "title";
    public static String thumbnails = "thumbnails";
    public static String videoId = "videoId";
    ListView lvPlaylist;
    YtVideoAdapter adapterPlaylist;
    ProgressDialog dialogPlaylist;
    ArrayList<HashMap<String, String>> arrPlaylist;
    String idPlaylist = "";
    String titlePlaylist = "Những Ca Khúc Mới Nhất Của Sơn Tùng M-TP 2017";

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_playlist);

        idPlaylist =getIntent().getStringExtra("id");


        arrPlaylist = new ArrayList<HashMap<String, String>>();
        lvPlaylist = (ListView) findViewById(R.id.listviewPlaylist);
        loadmore = new ProgressBar(this);
        lvPlaylist.addFooterView(loadmore);
        adapterPlaylist = new YtVideoAdapter(PlaylistActivity.this, arrPlaylist);
        lvPlaylist.setAdapter(adapterPlaylist);
        lvPlaylist.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScroll(AbsListView arg0, int arg1, int arg2, int arg3) {
                int lastitem = arg1 + arg2 + Integer.valueOf(Vari.maxResultsPlaylist);
                if (lastitem > arg3 && loadFinish == true) {
                    new AsynLoad().execute(idPlaylist);
                }
            }
            @Override
            public void onScrollStateChanged(AbsListView arg0, int arg1) {
            }
        });
        if (Funs.isNetworkAvailable(this)) {
            try {
                setTitle(titlePlaylist);

                try {
                    new AsynLoad().execute(idPlaylist);
                } catch (Exception e) {
                }
            } catch (Exception e) {
            }
        } else {
            Toast.makeText(this, "Please check Wifi/3G", Toast.LENGTH_SHORT).show();
        }
    }

    private class AsynLoad extends AsyncTask<String, Void, Boolean> {

        @Override
        protected void onPreExecute() {
            loadFinish = false;
            dialogPlaylist = new ProgressDialog(PlaylistActivity.this);
            dialogPlaylist.setMessage("Loading...");
            dialogPlaylist.setIndeterminate(false);
            if (loadOne == false) {
                dialogPlaylist.show();
                dialogPlaylist.dismiss();
            }
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... params) {
            url = String.format(Vari.linkPlaylist, Vari.maxResultsPlaylist,
                    params[0]);
            if (nextPageToken.length() > 0) {
                url = url + "&pageToken=" + nextPageToken;
            }
            JSONParser jParser = new JSONParser();
            List<NameValuePair> p = new ArrayList<NameValuePair>();
            JSONObject json = jParser.makeHttpRequest(url, "GET", p);
            try {
                nextPageToken = json.getString("nextPageToken");
            } catch (Exception e) {
            }
            try {
                JSONObject pageInfo = json.getJSONObject("pageInfo");
                int totalResults = pageInfo.getInt("totalResults");
                JSONArray items = json.getJSONArray("items");
                for (int i = 0; i < items.length(); i++) {
                    JSONObject c = items.getJSONObject(i);
                    JSONObject snippet = c.getJSONObject("snippet");
                    String title = snippet.getString("title");
                    JSONObject thumbnails = snippet.getJSONObject("thumbnails");
                    JSONObject high = thumbnails.getJSONObject("default");
                    String urlthumbnails = high.getString("url");
                    JSONObject resourceId = snippet.getJSONObject("resourceId");
                    String videoId = resourceId.getString("videoId");
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(PlaylistActivity.title, title);
                    map.put(PlaylistActivity.videoId, videoId);
                    map.put(PlaylistActivity.thumbnails, urlthumbnails);
                    arrPlaylist.add(map);
                    if (arrPlaylist.size() >= totalResults) {
                        return false;
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
                return false;
            }
            return true;
        }

        @Override
        protected void onPostExecute(Boolean result) {
            dialogPlaylist.dismiss();
            if (result) {
                numberLoad = numberLoad + 1;
                if (numberLoad == 1) {

                }
                loadOne = true;
                adapterPlaylist.notifyDataSetChanged();
                loadFinish = true;
            } else {
                loadFinish = false;
                lvPlaylist.removeFooterView(loadmore);
            }
            super.onPostExecute(result);
        }
    }
    
}
