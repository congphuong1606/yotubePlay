package phuongcong.yotubeofme.adapter;

import android.graphics.Bitmap;

import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;



import android.content.Context;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;
import com.squareup.picasso.Target;

import java.util.ArrayList;


import phuongcong.yotubeofme.R;
import phuongcong.yotubeofme.data.VideosResponse;
import phuongcong.yotubeofme.event.OnClickIteml;
import phuongcong.yotubeofme.image.ImageLoader;


public class YtVideoAdapter extends RecyclerView.Adapter<YtVideoAdapter.VideosViewHolder> {
    Context context;
    ImageLoader imageLoader;
    ArrayList<VideosResponse.Item> data;
    private VideosResponse.Item resultp;
    OnClickIteml onClickIteml;

    public YtVideoAdapter(Context context, ArrayList<VideosResponse.Item> arraylist,OnClickIteml onClickIteml) {
        this.context = context;
        this.data = arraylist;
        imageLoader = new ImageLoader(context);
        this.onClickIteml =onClickIteml;
    }

    @Override
    public VideosViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        final View itemView = layoutInflater.inflate(R.layout.video_item, parent, false);
        VideosViewHolder ret = new VideosViewHolder(itemView);
        return ret;
    }

    @Override
    public void onBindViewHolder(final VideosViewHolder holder, int position) {
        if (data != null) {
            final VideosResponse.Item item = data.get(position);
            if (item != null) {
                String title = item.getSnippet().getTitle();
                title = title.substring(0, 1).toUpperCase() + title.substring(1).toLowerCase();
                holder.textView.setText(title);

             /* if (item.isBrightness()) {
                    holder.textView.setTextColor(context.getResources().getColor(R.color.ccc));
                    holder.textViewC.setTextColor(context.getResources().getColor(R.color.ccc));
                } else {
                    holder.textView.setTextColor(context.getResources().getColor(R.color.fff));
                    holder.textViewC.setTextColor(context.getResources().getColor(R.color.fff));
                }*/
               if(title.equals("Private video")){
                   holder.imageView.setImageResource(R.mipmap.ic_launcher);
               }else {
                   final int h = 240;

                   Target target = new Target() {
                       @Override
                       public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
                           Bitmap top1 = Bitmap.createBitmap(bitmap, 0, 60, 360, 240);


                           holder.imageView.setImageBitmap(top1);
                       }

                       @Override
                       public void onBitmapFailed(Drawable errorDrawable) {
                           holder.imageView.setImageResource(R.mipmap.ic_launcher);

                       }

                       @Override
                       public void onPrepareLoad(Drawable placeHolderDrawable) {

                       }
                   };

                   holder.imageView.setTag(target);
                   String url =item.getSnippet().getThumbnails().getHigh().getUrl();
                   Picasso.with(context)
                           .load(url)
                           .error(R.mipmap.ic_launcher)
                           .placeholder(R.mipmap.ic_launcher)
                           .into(target);
               }




            }
        }
    }


    @Override
    public int getItemCount() {
        int ret = 0;
        if (data != null) {
            ret = data.size();
        }
        return ret;
    }


    public class VideosViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView = null;
        TextView textView = null;


        public VideosViewHolder(View itemView) {
            super(itemView);

            if (itemView != null) {
                imageView = (ImageView) itemView.findViewById(R.id.video_thumbnail);
                textView = (TextView) itemView.findViewById(R.id.video_title);
               itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        resultp = data.get(getAdapterPosition());
                        String idVideo = resultp.getSnippet().getResourceId().getVideoId();
                        onClickIteml.onClickItem(idVideo);
                    }
                });
            }
        }

    }

}
