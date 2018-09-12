package phuongcong.yotubeofme.ui;

import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.design.widget.AppBarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Space;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import de.hdodenhof.circleimageview.CircleImageView;
import phuongcong.yotubeofme.R;
import phuongcong.yotubeofme.adapter.YtPlaylistAdapter;
import phuongcong.yotubeofme.callbacks.AvatarCallback;
import phuongcong.yotubeofme.data.PlaylistItem;
import phuongcong.yotubeofme.data.YtPlaylistResponse;
import phuongcong.yotubeofme.event.EndlessRecyclerOnScrollListener;
import phuongcong.yotubeofme.libs.AppBarStateChangeListener;
import phuongcong.yotubeofme.libs.NetworkHelper;
import phuongcong.yotubeofme.libs.Utils;
import phuongcong.yotubeofme.models.AvatarModel;
import phuongcong.yotubeofme.network.APIClient;
import phuongcong.yotubeofme.utils.APIInterface;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

	private final static float EXPAND_AVATAR_SIZE_DP = 80f;
	private final static float COLLAPSED_AVATAR_SIZE_DP = 32f;
	private View mContainerView;
	private AppBarLayout mAppBarLayout;
	private CircleImageView mAvatarImageView;
	private TextView mToolbarTextView, mTitleTextView;
	private Space mSpace;
	private Toolbar mToolBar;
	private RecyclerView recyclerView;
	private AppBarStateChangeListener mAppBarStateChangeListener;
	private float[] mAvatarPoint = new float[2], mSpacePoint = new float[2], mToolbarTextPoint =
			new float[2], mTitleTextViewPoint = new float[2];
	private float mTitleTextSize;

    boolean isBrightness = false;



	APIInterface apiInterface;
	private YtPlaylistResponse ytPlaylistResponse;
	ArrayList<PlaylistItem> playlistItems = new ArrayList<>();
	private YtPlaylistAdapter dataAdapter;
	String nextPage ="";
	private boolean loadmore=true;





	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		apiInterface = APIClient.getClient().create(APIInterface.class);

		findViews();
		setUpViews();
		fetchAvatar();
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

		});

	}



















	private void findViews() {
		mContainerView = findViewById(R.id.view_container);
		mAppBarLayout = findViewById(R.id.app_bar);
		mAvatarImageView = findViewById(R.id.imageView_avatar);
		mToolbarTextView = findViewById(R.id.toolbar_title);
		mTitleTextView = findViewById(R.id.textView_title);
		mSpace = findViewById(R.id.space);
		mToolBar = findViewById(R.id.toolbar);
		recyclerView = findViewById(R.id.rcv_yt_playlist);
	}

	private void setUpViews() {
		mTitleTextSize = mTitleTextView.getTextSize();
		setUpToolbar();
		setUpRecyclerView();
		setUpAmazingAvatar();
	}

	private void setUpRecyclerView() {
		recyclerView.setHasFixedSize(true);
		recyclerView.setItemAnimator(new DefaultItemAnimator());
		recyclerView
				.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
		recyclerView.setLayoutManager(new LinearLayoutManager(this));
		dataAdapter = new YtPlaylistAdapter(MainActivity.this, playlistItems);
		recyclerView.setAdapter(dataAdapter);
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
				ArrayList<PlaylistItem>  playlistItems1= ytPlaylistResponse.items;
				for(PlaylistItem item :playlistItems1){
				      item.setBrightness(isBrightness);
				}
				playlistItems.addAll(playlistItems1);
				dataAdapter.notifyDataSetChanged();

			}

			@Override
			public void onFailure(Call<YtPlaylistResponse> call, Throwable t) {
				call.cancel();
			}
		});


	}
















































	private void setUpToolbar() {
		mAppBarLayout.getLayoutParams().height = Utils.getDisplayMetrics(this).widthPixels * 9 / 16;
		mAppBarLayout.requestLayout();

		setSupportActionBar(mToolBar);
		if (getSupportActionBar() != null) {
			getSupportActionBar().setDisplayShowTitleEnabled(false);
			getSupportActionBar().setDisplayHomeAsUpEnabled(true);
		}
		mToolBar.setNavigationOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				isBrightness = !isBrightness;
				if(isBrightness){
					mToolBar.setNavigationIcon(R.drawable.ic_brightness_2_black_24dp);
				}else {
					mToolBar.setNavigationIcon(R.drawable.ic_brightness_high_black_24dp);
				}
				for(PlaylistItem item :playlistItems){
					item.setBrightness(isBrightness);
				}
				dataAdapter.notifyDataSetChanged();
			}
		});
	}

	private void setUpAmazingAvatar() {
		mAppBarStateChangeListener = new AppBarStateChangeListener() {

			@Override
			public void onStateChanged(AppBarLayout appBarLayout,
			                           AppBarStateChangeListener.State state) {
			}

			@Override
			public void onOffsetChanged(AppBarStateChangeListener.State state, float offset) {
				translationView(offset);
			}
		};
		mAppBarLayout.addOnOffsetChangedListener(mAppBarStateChangeListener);
	}

	private void translationView(float offset) {
		float newAvatarSize = Utils.convertDpToPixel(
				EXPAND_AVATAR_SIZE_DP - (EXPAND_AVATAR_SIZE_DP - COLLAPSED_AVATAR_SIZE_DP) * offset,
				this);
		float expandAvatarSize = Utils.convertDpToPixel(EXPAND_AVATAR_SIZE_DP, this);
		float xAvatarOffset =
				(mSpacePoint[0] - mAvatarPoint[0] - (expandAvatarSize - newAvatarSize) / 2f) *
						offset;
		// If avatar center in vertical, just half `(expandAvatarSize - newAvatarSize)`
		float yAvatarOffset =
				(mSpacePoint[1] - mAvatarPoint[1] - (expandAvatarSize - newAvatarSize)) * offset;
		mAvatarImageView.getLayoutParams().width = Math.round(newAvatarSize);
		mAvatarImageView.getLayoutParams().height = Math.round(newAvatarSize);
		mAvatarImageView.setTranslationX(xAvatarOffset);
		mAvatarImageView.setTranslationY(yAvatarOffset);

		float newTextSize =
				mTitleTextSize - (mTitleTextSize - mToolbarTextView.getTextSize()) * offset;
		Paint paint = new Paint(mTitleTextView.getPaint());
		paint.setTextSize(newTextSize);
		float newTextWidth = Utils.getTextWidth(paint, mTitleTextView.getText().toString());
		paint.setTextSize(mTitleTextSize);
		float originTextWidth = Utils.getTextWidth(paint, mTitleTextView.getText().toString());
		// If rtl should move title view to end of view.
		boolean isRTL = TextUtils.getLayoutDirectionFromLocale(Locale.getDefault()) ==
				View.LAYOUT_DIRECTION_RTL ||
				mContainerView.getLayoutDirection() == View.LAYOUT_DIRECTION_RTL;
		float xTitleOffset = ((mToolbarTextPoint[0] + (isRTL ? mToolbarTextView.getWidth() : 0)) -
				(mTitleTextViewPoint[0] + (isRTL ? mTitleTextView.getWidth() : 0)) -
				(mToolbarTextView.getWidth() > newTextWidth ?
						(originTextWidth - newTextWidth) / 2f : 0)) * offset;
		float yTitleOffset = (mToolbarTextPoint[1] - mTitleTextViewPoint[1]) * offset;
		mTitleTextView.setTextSize(TypedValue.COMPLEX_UNIT_PX, newTextSize);
		mTitleTextView.setTranslationX(xTitleOffset);
		mTitleTextView.setTranslationY(yTitleOffset);
	}

	/**
	 * Avatar from TinyFaces (https://github.com/maximedegreve/TinyFaces)
	 */
	private void fetchAvatar() {
		NetworkHelper.getAvatar(new AvatarCallback() {

			@Override
			public void onSuccess(final AvatarModel avatarModel) {
				super.onSuccess(avatarModel);
				if (isFinishing()) {
					return;
				}
				runOnUiThread(new Runnable() {

					@Override
					public void run() {
						Glide.with(MainActivity.this).load(avatarModel.url).into(mAvatarImageView);
						String name =
								String.format(Locale.getDefault(), "%s %s", avatarModel.firstName,
										avatarModel.lastName);
						mTitleTextView.setText(name);
						mTitleTextView.post(new Runnable() {

							@Override
							public void run() {
								resetPoints(true);
							}
						});
					}
				});
			}
		});
	}

	private void resetPoints(boolean isTextChanged) {
		final float offset = mAppBarStateChangeListener.getCurrentOffset();

		float newAvatarSize = Utils.convertDpToPixel(
				EXPAND_AVATAR_SIZE_DP - (EXPAND_AVATAR_SIZE_DP - COLLAPSED_AVATAR_SIZE_DP) * offset,
				this);
		float expandAvatarSize = Utils.convertDpToPixel(EXPAND_AVATAR_SIZE_DP, this);

		int[] avatarPoint = new int[2];
		mAvatarImageView.getLocationOnScreen(avatarPoint);
		mAvatarPoint[0] = avatarPoint[0] - mAvatarImageView.getTranslationX() -
				(expandAvatarSize - newAvatarSize) / 2f;
		// If avatar center in vertical, just half `(expandAvatarSize - newAvatarSize)`
		mAvatarPoint[1] = avatarPoint[1] - mAvatarImageView.getTranslationY() -
				(expandAvatarSize - newAvatarSize);

		int[] spacePoint = new int[2];
		mSpace.getLocationOnScreen(spacePoint);
		mSpacePoint[0] = spacePoint[0];
		mSpacePoint[1] = spacePoint[1];

		int[] toolbarTextPoint = new int[2];
		mToolbarTextView.getLocationOnScreen(toolbarTextPoint);
		mToolbarTextPoint[0] = toolbarTextPoint[0];
		mToolbarTextPoint[1] = toolbarTextPoint[1];

		Paint paint = new Paint(mTitleTextView.getPaint());
		float newTextWidth = Utils.getTextWidth(paint, mTitleTextView.getText().toString());
		paint.setTextSize(mTitleTextSize);
		float originTextWidth = Utils.getTextWidth(paint, mTitleTextView.getText().toString());
		int[] titleTextViewPoint = new int[2];
		mTitleTextView.getLocationOnScreen(titleTextViewPoint);
		mTitleTextViewPoint[0] = titleTextViewPoint[0] - mTitleTextView.getTranslationX() -
				(mToolbarTextView.getWidth() > newTextWidth ?
						(originTextWidth - newTextWidth) / 2f : 0);
		mTitleTextViewPoint[1] = titleTextViewPoint[1] - mTitleTextView.getTranslationY();

		if (isTextChanged) {
			new Handler().post(new Runnable() {

				@Override
				public void run() {
					translationView(offset);
				}
			});
		}
	}

	@Override
	public void onWindowFocusChanged(boolean hasFocus) {
		super.onWindowFocusChanged(hasFocus);
		if (!hasFocus) {
			return;
		}
		resetPoints(false);
	}


}
