package e.mirzashafique.lib.model;

import android.os.Parcel;
import android.os.Parcelable;

public class Config implements Parcelable {
    private boolean files;
    private int maxFile;
    private boolean images;
    private int maxImages;
    private boolean videos;
    private int maxVideos;
    private boolean audios;
    private int maxAudios;

    public Config(boolean files, int maxFile, boolean images, int maxImages, boolean videos, int maxVideos, boolean audios, int maxAudios) {
        this.files = files;
        this.maxFile = maxFile;
        this.images = images;
        this.maxImages = maxImages;
        this.videos = videos;
        this.maxVideos = maxVideos;
        this.audios = audios;
        this.maxAudios = maxAudios;
    }

    public boolean isFiles() {
        return files;
    }

    public void setFiles(boolean files) {
        this.files = files;
    }

    public int getMaxFile() {
        return maxFile;
    }

    public void setMaxFile(int maxFile) {
        this.maxFile = maxFile;
    }

    public boolean isImages() {
        return images;
    }

    public void setImages(boolean images) {
        this.images = images;
    }

    public int getMaxImages() {
        return maxImages;
    }

    public void setMaxImages(int maxImages) {
        this.maxImages = maxImages;
    }

    public boolean isVideos() {
        return videos;
    }

    public void setVideos(boolean videos) {
        this.videos = videos;
    }

    public int getMaxVideos() {
        return maxVideos;
    }

    public void setMaxVideos(int maxVideos) {
        this.maxVideos = maxVideos;
    }

    public boolean isAudios() {
        return audios;
    }

    public void setAudios(boolean audios) {
        this.audios = audios;
    }

    public int getMaxAudios() {
        return maxAudios;
    }

    public void setMaxAudios(int maxAudios) {
        this.maxAudios = maxAudios;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }
}
