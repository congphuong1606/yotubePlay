package phuongcong.yotubeofme.data;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Ominext on 9/12/2018.
 */

public class VideoItem {
    @SerializedName("snippet")
    public Snippet snippet;
    public  boolean  isBrightness =false;

    public boolean isBrightness() {
        return isBrightness;
    }

    public void setBrightness(boolean brightness) {
        isBrightness = brightness;
    }


    public class Snippet {
        @SerializedName("title")
        public String titlePl;
        @SerializedName("thumbnails")
        public Thumbnails thumbnails;
        @SerializedName("resourceId")
        public ResourceId resourceId;

    }

    public class Thumbnails {
        @SerializedName("default")
        public Default defaultt;
        @SerializedName("high")
        public High high;


    }


    public class Default {
        @SerializedName("url")
        public String url;
        @SerializedName("width")
        public Integer widthIM;
        @SerializedName("height")
        public Integer heightIM;

    }


    public class High {
        @SerializedName("url")
        public String url;

    }
    public class ResourceId {
        @SerializedName("videoId")
        public String videoId;

    }
}
