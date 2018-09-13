package phuongcong.yotubeofme.adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;
import java.util.ArrayList;

import phuongcong.yotubeofme.R;
import phuongcong.yotubeofme.data.PlaylistItem;
import phuongcong.yotubeofme.event.OnClickIteml;
import phuongcong.yotubeofme.ui.PlaylistPlayerActivity;
import phuongcong.yotubeofme.utils.BlurImage;


/**
 * Created by Ominext on 9/11/2018.
 */

public class YtPlaylistAdapter extends RecyclerView.Adapter<YtPlaylistAdapter.PlaylistViewHolder> {
    Context context;
    private ArrayList<PlaylistItem> viewItemList;
    YtPlaylistAdapter.OnClickIteml onClickIteml;
    public YtPlaylistAdapter(Context context, ArrayList<PlaylistItem> viewItemList,OnClickIteml onClickIteml) {
        this.context =context;
        this.onClickIteml =  onClickIteml;
        this.viewItemList = viewItemList;

    }

    @Override
    public PlaylistViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get LayoutInflater object.
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        // Inflate the RecyclerView item layout xml.
        final View itemView = layoutInflater.inflate(R.layout.playlist_item, parent, false);

        // Create and return collapsing Recycler View Holder object.
        PlaylistViewHolder ret = new PlaylistViewHolder(itemView);
        return ret;
    }

    @Override
    public void onBindViewHolder(final PlaylistViewHolder holder, int position) {
        if(viewItemList!=null) {
            // Get item dto in list.
            final PlaylistItem item = viewItemList.get(position);

            if(item != null) {



                String title =item.snippet.titlePl;
                title = title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase();
                holder.textView.setText(title);
                if(item.contentDetails!=null){
                    String count = item.contentDetails.itemCount.toString() + " video";
                    holder.textViewC.setText(count);
                }
                if(item.isBrightness()){
                    holder.textView.setTextColor(context.getResources().getColor(R.color.ccc));
                    holder.textViewC.setTextColor(context.getResources().getColor(R.color.ccc));
                }else {
                    holder.textView.setTextColor(context.getResources().getColor(R.color.fff));
                    holder.textViewC.setTextColor(context.getResources().getColor(R.color.fff));
                }


                final int h = item.snippet.thumbnails.defaultt.heightIM-25;

                Target target = new Target() {
                    @Override
                    public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                        Bitmap top1 = Bitmap.createBitmap(bitmap, 0, 12, h, h);
                        if(!item.isBrightness()){
                            holder.imageView.setImageBitmap(BlurImage.fastblur(bitmap, 0.1f, 20));
                        }else {
                            holder.imageView.setImageResource(0);
                        }

                        holder.imageViewAva.setImageBitmap(getRoundedCornerBitmap(top1,6));
                    }

                    @Override
                    public void onBitmapFailed(Drawable errorDrawable) {
                        holder.imageView.setImageResource(R.mipmap.ic_launcher);
                        holder.imageViewAva.setImageResource(R.mipmap.ic_launcher);

                    }

                    @Override
                    public void onPrepareLoad(Drawable placeHolderDrawable) {

                    }
                };

                holder.imageView.setTag(target);

                Picasso.with(context)
                        .load(item.snippet.thumbnails.defaultt.url)
                        .error(R.mipmap.ic_launcher)
                        .placeholder(R.mipmap.ic_launcher)
                        .into(target);



            }
        }
    }
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, int pixels) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap
                .getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final int color = 0xff424242;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);
        final float roundPx = pixels;

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF, roundPx, roundPx, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        return output;
    }
    @Override
    public int getItemCount() {
        int ret = 0;
        if(viewItemList!=null)
        {
            ret = viewItemList.size();
        }
        return ret;
    }

    public interface OnClickIteml {
        void onClickItem(String id);
    }


    public class PlaylistViewHolder extends RecyclerView.ViewHolder {

         ImageView imageView = null;
         ImageView imageViewAva = null;
         TextView textView = null;
         TextView textViewC = null;

        public PlaylistViewHolder(View itemView) {
            super(itemView);

            if(itemView != null)
            {
                imageView = (ImageView)itemView.findViewById(R.id.collapsing_toolbar_recycler_view_item_image);
                imageViewAva = (ImageView)itemView.findViewById(R.id.imv_playlist_avatar);
                textView = (TextView)itemView.findViewById(R.id.tvPlaylist);
                textViewC = (TextView)itemView.findViewById(R.id.tvItemCount);
                itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onClickIteml.onClickItem(viewItemList.get(getAdapterPosition()).id);

                    }
                });
            }
        }

    }

}