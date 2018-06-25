package com.sample;

import com.google.gson.annotations.SerializedName;

/**
 * Created by HTISPL on 22-Jun-18.
 */
public class VideoData {
    @SerializedName("id")
    private String id;

    @SerializedName("type")
    private String type;

    @SerializedName("title")
    private String title;

    @SerializedName("images")
    private ImageData imageData;

    public class ImageData {
        @SerializedName("original_still")
        private ThumbnailData thumbnailData;

        @SerializedName("original")
        private VideoUrl videoUrl;

        public class ThumbnailData {
            @SerializedName("url")
            private String thumbnail;

            public String getThumbnail() {
                return thumbnail;
            }

            public void setThumbnail(String thumbnail) {
                this.thumbnail = thumbnail;
            }
        }

        public class VideoUrl {
            @SerializedName("mp4")
            private String url;

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }

        public ThumbnailData getThumbnailData() {
            return thumbnailData;
        }

        public void setThumbnailData(ThumbnailData thumbnailData) {
            this.thumbnailData = thumbnailData;
        }

        public VideoUrl getVideoUrl() {
            return videoUrl;
        }

        public void setVideoUrl(VideoUrl videoUrl) {
            this.videoUrl = videoUrl;
        }
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public ImageData getImageData() {
        return imageData;
    }

    public void setImageData(ImageData imageData) {
        this.imageData = imageData;
    }
}