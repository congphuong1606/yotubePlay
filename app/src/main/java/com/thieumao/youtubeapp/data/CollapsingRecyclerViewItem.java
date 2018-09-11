package com.thieumao.youtubeapp.data;

/**
 * Created by Ominext on 9/11/2018.
 */

public class CollapsingRecyclerViewItem {
    // car image resource id.
    private int imageId;

    public CollapsingRecyclerViewItem(int imageId) {
        this.imageId = imageId;
    }

    public int getImageId() {
        return imageId;
    }

    public void setImageId(int imageId) {
        this.imageId = imageId;
    }
}
